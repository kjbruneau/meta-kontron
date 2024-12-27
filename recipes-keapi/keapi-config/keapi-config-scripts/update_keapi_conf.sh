#!/bin/bash
# Tool to autogenerate KEAPI config for Kontron platforms
#
# Copyright (c) 2016-2017 Kontron Europe
#
# Permission is hereby granted, free of charge, to any person obtaining a 
# copy of this software and associated documentation files (the
# "Software"), to deal in the Software without restriction, including
# without limitation the rights to use, copy, modify, merge, publish,
# distribute, distribute with modifications, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included
# in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS  
# OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
# IN NO EVENT SHALL THE ABOVE COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,   
# DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR  
# OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR 
# THE USE OR OTHER DEALINGS IN THE SOFTWARE.
#
# Except as contained in this notice, the name(s) of the above copyright  
# holders shall not be used in advertising or otherwise to promote the 
# sale, use or other dealings in this Software without prior written 
# authorization. 


KEAPI_CONF_DIR=/etc/keapi/
BACKLIGHT_CONF=${KEAPI_CONF_DIR}backlight.conf
GPIO_CONF=${KEAPI_CONF_DIR}gpio.conf
GPIO_CONF_LEGACY=${KEAPI_CONF_DIR}gpio.conf.legacy
GPIO_CONF_KEMLIKE=${KEAPI_CONF_DIR}gpio.conf.kemlike
GPIO_CONF_FULL=${KEAPI_CONF_DIR}gpio.conf.full
I2C_CONF=${KEAPI_CONF_DIR}i2c.conf
SMBUS_CONF=${KEAPI_CONF_DIR}smbus.conf
SPI_CONF=${KEAPI_CONF_DIR}spi.conf
STORAGE_CONF=${KEAPI_CONF_DIR}storage.conf
SYSTEMSTATES_CONF=${KEAPI_CONF_DIR}systemstates.conf
WATCHDOG_CONF=${KEAPI_CONF_DIR}watchdog.conf

#### Load variables from file
source /etc/keapi/update_keapi.conf

#### Make sure all necessary modules are really loaded ####
cd /etc/modules-load.d/
for MOD_CONFIG in $MODULE_LOAD_D
do
    if [ -e "$MOD_CONFIG" ] ; then
        for MODULE in `cat $MOD_CONFIG | sed -e "s/#.*//"`
        do
          modprobe $MODULE
        done
    else
        echo "Warning: $MOD_CONFIG does not exist"
    fi
done

mkdir -p $KEAPI_CONF_DIR

#### Helper functions ####
function create_backup {
    local ORIG=$1
    local BACKUP=$1.orig

    if [ -e "$ORIG" ] ; then
        if ! [ -e "$BACKUP" ] ; then
            cp -a $ORIG $BACKUP
        fi
    fi
}

#### Backlight ####
function update_backlight_conf {
    local BASEDIR=/sys/class/backlight/
    local I=0

    create_backup $BACKLIGHT_CONF

    # Start with an empty file
    echo -n > $BACKLIGHT_CONF

    cd ${BASEDIR} 2> /dev/null || return

    for ENTRY in *
    do
        # Kontron backlight is prefered, therefore it should be first
        # also added ptn3460 here as it used on SMARC-sXAL4
        if [ "$ENTRY" = "ku_backlight" ] || [ "$ENTRY" = "ptn3460_backlight" ]
        then
            echo ${BASEDIR}$ENTRY >> ${BACKLIGHT_CONF}
        else
            if ! [ -e "$ENTRY" ] 
            then
                continue
            fi
            BACKLIGHT[$I]="$ENTRY"
            I=$((I+1))
        fi
    done

    # Fill in additional backlights, if available
    for ENTRY in ${BACKLIGHT[*]}
    do
        echo ${BASEDIR}$ENTRY >> ${BACKLIGHT_CONF}
    done
}


#### GPIO ####
function update_gpio_conf_legacy {
    local BASEDIR=/sys/class/gpio

    create_backup $GPIO_CONF_LEGACY

    # Start with the header
    echo '{' > $GPIO_CONF_LEGACY
    echo '    "gpioStyle": "linux-like",' >> $GPIO_CONF_LEGACY
    echo '    "gpioPort": [' >> $GPIO_CONF_LEGACY

    local GPIO=0
    for GPIOCHIP in ${BASEDIR}/gpiochip* ; do
        if [ ! -e "$GPIOCHIP/label" -o ! -e "$GPIOCHIP/ngpio" ]
        then
            continue
        fi
        # We are only interested in KEMPLD GPIOs
        local LABEL=`cat $GPIOCHIP/label`
        if (! [ "$LABEL" = "kempld-gpio" ]) && (! [ "$LABEL" = "gpio-kempld" ])
        then
            continue
        fi
        local BASE=`echo $GPIOCHIP | sed -e 's/.*gpiochip\([0-9]*\)$/\1/'`
        local NGPIO=`cat $GPIOCHIP/ngpio | sed -e 's/^\([0-9]*\)$/\1/'`
        if [ "$BASE" = "$GPIOCHIP" -o "$NGPIO" != "`cat $GPIOCHIP/ngpio`" ] ;
        then
            echo "Error processing $GPIOCHIP"
            continue
        fi

        while [ $GPIO -lt $NGPIO ]
        do
            echo '        {' >> $GPIO_CONF_LEGACY
            echo "            \"portNumber\": $GPIO," >> $GPIO_CONF_LEGACY
            echo '            "gpio": [' >> $GPIO_CONF_LEGACY
            echo '                {' >> $GPIO_CONF_LEGACY
            echo "                    \"gpioIdx\": 0," >> $GPIO_CONF_LEGACY
            echo "                    \"gpioNum\": $((BASE + GPIO))," >> $GPIO_CONF_LEGACY
            echo "                    \"directionCaps\": \"A\"" >> $GPIO_CONF_LEGACY
            echo '                }' >> $GPIO_CONF_LEGACY
            echo '            ]' >> $GPIO_CONF_LEGACY
            if [ $GPIO -eq $((NGPIO-1)) ]
            then
                echo '        }' >> $GPIO_CONF_LEGACY
            else
                echo '        },' >> $GPIO_CONF_LEGACY
            fi

            let GPIO=GPIO+1
        done
    done

    echo '    ]' >> $GPIO_CONF_LEGACY
    echo '}' >> $GPIO_CONF_LEGACY
}

function update_gpio_conf_kemlike {
    local BASEDIR=/sys/class/gpio

    create_backup $GPIO_CONF_KEMLIKE

    # Start with the header
    echo '{' > $GPIO_CONF_KEMLIKE
    echo '    "gpioStyle": "kontron-like-kem",' >> $GPIO_CONF_KEMLIKE
    echo '    "gpioPort": [' >> $GPIO_CONF_KEMLIKE

    local PORTNUMBER=0;
    for GPIOCHIP in ${BASEDIR}/gpiochip* ; do
        if [ ! -e "$GPIOCHIP/label" -o ! -e "$GPIOCHIP/ngpio" ]
        then
            continue
        fi
        # We are only interested in KEMPLD GPIOs
        local LABEL=`cat $GPIOCHIP/label`
        if (! [ "$LABEL" = "kempld-gpio" ]) && (! [ "$LABEL" = "gpio-kempld" ])
        then
            continue
        fi
        local BASE=`echo $GPIOCHIP | sed -e 's/.*gpiochip\([0-9]*\)$/\1/'`
        local NGPIO=`cat $GPIOCHIP/ngpio | sed -e 's/^\([0-9]*\)$/\1/'`
        if [ "$BASE" = "$GPIOCHIP" -o "$NGPIO" != "`cat $GPIOCHIP/ngpio`" ] ;
        then
            echo "Error processing $GPIOCHIP"
            continue
        fi

        echo '        {' >> $GPIO_CONF_KEMLIKE
        echo "            \"portNumber\": $PORTNUMBER," >> $GPIO_CONF_KEMLIKE
        echo '            "gpio": [' >> $GPIO_CONF_KEMLIKE
        local GPIO=0
        while [ $GPIO -lt $NGPIO ]
        do
            echo '                {' >> $GPIO_CONF_KEMLIKE
            echo "                    \"gpioIdx\": $GPIO," >> $GPIO_CONF_KEMLIKE
            echo "                    \"gpioNum\": $((BASE + GPIO))," >> $GPIO_CONF_KEMLIKE
            echo "                    \"directionCaps\": \"A\"" >> $GPIO_CONF_KEMLIKE
            if [ $GPIO -eq $((NGPIO-1)) ]
            then
                echo '                }' >> $GPIO_CONF_KEMLIKE
            else
                echo '                },' >> $GPIO_CONF_KEMLIKE
            fi

            let GPIO=GPIO+1
        done
        echo '            ]' >> $GPIO_CONF_KEMLIKE
        echo '        }' >> $GPIO_CONF_KEMLIKE

        let PORTNUMBER=PORTNUMBER+1
    done

    echo '    ]' >> $GPIO_CONF_KEMLIKE
    echo '}' >> $GPIO_CONF_KEMLIKE
}

function update_gpio_conf_full {
    local BASEDIR=/sys/class/gpio
    local GPIO_LABEL="gpio-kempld"

    create_backup $GPIO_CONF_FULL

    # Start with the header
    echo '{' > $GPIO_CONF_FULL
    echo '    "gpioStyle": "keapi-full",' >> $GPIO_CONF_FULL
    echo '    "gpioPort": [' >> $GPIO_CONF_FULL

    local PORTNUMBER=0
    for GPIOCHIP in ${BASEDIR}/gpiochip* ; do
        if [ ! -e "$GPIOCHIP/label" -o ! -e "$GPIOCHIP/ngpio" ]
        then
            continue
        fi
        # We are only interested in KEMPLD GPIOs
        local LABEL=`cat $GPIOCHIP/label`
        if (! [ "$LABEL" = "kempld-gpio" ]) && (! [ "$LABEL" = "gpio-kempld" ])
        then
            continue
        fi
        local NGPIO=`cat $GPIOCHIP/ngpio | sed -e 's/^\([0-9]*\)$/\1/'`
        echo '        {' >> $GPIO_CONF_FULL
        echo "            \"portNumber\": $PORTNUMBER," >> $GPIO_CONF_FULL
        echo "            \"label\": \"$GPIO_LABEL\"," >> $GPIO_CONF_FULL
        echo '            "gpio": [' >> $GPIO_CONF_FULL
        local GPIO=0
        while [ $GPIO -lt $NGPIO ]
        do
            echo '                {' >> $GPIO_CONF_FULL
            echo "                    \"gpioIdx\": $GPIO," >> $GPIO_CONF_FULL
            echo "                    \"directionCaps\": \"A\"" >> $GPIO_CONF_FULL
            if [ $GPIO -eq $((NGPIO-1)) ]
            then
                echo '                }' >> $GPIO_CONF_FULL
            else
                echo '                },' >> $GPIO_CONF_FULL
            fi
                let GPIO=GPIO+1
        done
        echo '            ]' >> $GPIO_CONF_FULL
        echo '        }' >> $GPIO_CONF_FULL
        let PORTNUMBER=PORTNUMBER+1
    done

    echo '    ]' >> $GPIO_CONF_FULL
    echo '}' >> $GPIO_CONF_FULL
}

function update_gpio_conf {
    if [ -f $GPIO_CONF ] ; then
        create_backup $GPIO_CONF
        rm $GPIO_CONF
    fi

    ln -s $GPIO_CONF.$GPIO_SYMLINK_CONFIG $GPIO_CONF
}

#### STORAGE ####
function update_storage_conf {
    local BASEDIR=/sys/bus/i2c/devices

    create_backup $STORAGE_CONF

    # Start with the header
    echo '{' > $STORAGE_CONF
    echo '    "storage": [' >> $STORAGE_CONF
    local STORAGES="`find -L $BASEDIR -maxdepth 2 -type f -name kontron_eeep`"
    local NUMSTORAGES=`echo "$STORAGES" | wc -w`
    local NUMSTORAGE=0

    for STORAGE in $STORAGES ; do
        local SIZE=`cat $STORAGE | wc -c`
        echo '        {' >> $STORAGE_CONF
        echo "            \"path\": \"$STORAGE\"," >> $STORAGE_CONF
        echo "            \"start\": \"0\"," >> $STORAGE_CONF
        printf "            \"size\": \"0x%x\"\n" $SIZE >> $STORAGE_CONF
        let NUMSTORAGE=$NUMSTORAGE+1
        if [ $NUMSTORAGE -ge $NUMSTORAGES ]
        then
            echo '        }' >> $STORAGE_CONF
        else
            echo '        },' >> $STORAGE_CONF
        fi
    done

    echo '    ]' >> $STORAGE_CONF
    echo '}' >> $STORAGE_CONF
}

#### WATCHDOG ####
function update_watchdog_conf {
    local WDTFILE=/dev/watchdog

    create_backup $WATCHDOG_CONF

    # We create this config in any case as it works with basically any watchdog
    # device
    echo '{' > $WATCHDOG_CONF
    echo '    "minTimeout": 1000,' >> $WATCHDOG_CONF
    echo '    "maxTimeout": 4123170,' >> $WATCHDOG_CONF
    echo '    "maxPath": "/dev/watchdog",' >> $WATCHDOG_CONF
    echo '    "wdtStyle": "linux-like"' >> $WATCHDOG_CONF
    echo '}' >> $WATCHDOG_CONF
}

function update_systemstates_conf {
    # We asume available settings are equal for all cores
    local BASEDIR=/sys/devices/system/cpu/cpufreq/policy0/
    local PSTATE=$(find /sys/ -name intel_pstate)

    if [ ! -z "$PSTATE" ]
    then
        update_systemstates_intel_conf
        return
    fi

    create_backup $SYSTEMSTATES_CONF

    # Start with the header
    echo '{' > $SYSTEMSTATES_CONF
    echo '    "state": [' >> $SYSTEMSTATES_CONF

    # KEAPI support up to 16 performance states, 0 state always means max performance
    # and not described in config
    local MAX_STATES_AMOUNT=15
    local GOVERNORS_USABLE=""
    local GOVERNORS=`cat $BASEDIR/scaling_available_governors 2> /dev/null`
    local CPU_MAX_FREQ=$(cat $BASEDIR/cpuinfo_max_freq)
    local CPU_MIN_FREQ=$(cat $BASEDIR/cpuinfo_min_freq)
    local CPU_FREQ_STEP=$((($CPU_MAX_FREQ-$CPU_MIN_FREQ)/($CPU_STATES_AMOUNT-1)))
    # List of CPU ACPI states
    IFS=' ' read -r -a cpu_states <<< $(cat $BASEDIR/scaling_available_frequencies)
    local CPU_STATES_AMOUNT=${#cpu_states[@]}
    # Determine amount of states: available CPU frequency states + ondemand + powersave
    local STATES=0
    local CPU_FREQ_STEP=1

    if [[ $GOVERNORS = *"userspace"* ]]; then
        let STATES=$STATES+$CPU_STATES_AMOUNT
        GOVERNORS_USABLE="$GOVERNORS_USABLE userspace"
    fi
    if [[ $GOVERNORS = *"ondemand"* ]] && [[ $SYSTEMSTATES_ADD_ONDEMAND = 1 ]]; then
        let STATES=$STATES+1
        GOVERNORS_USABLE="$GOVERNORS_USABLE ondemand"
    fi
    if [[ $GOVERNORS = *"powersave"* ]]; then
        let STATES=$STATES+1
        GOVERNORS_USABLE="$GOVERNORS_USABLE powersave"
    fi

    # Try to reduce amount of frequencies if needs
    if [[ $STATES -gt $MAX_STATES_AMOUNT ]] ; then
        CPU_FREQ_STEP=2
        let STATES=$STATES-$CPU_STATES_AMOUNT+$CPU_STATES_AMOUNT/2+1
    fi

    local CPU_CURR_FREQ=$CPU_MAX_FREQ
    local STATENUM=1

    for GOVERNOR in $GOVERNORS_USABLE; do
        if [[ $GOVERNOR = "userspace" ]] ; then
            local STATE_ID=0
            while [ "$STATE_ID" -lt "$CPU_STATES_AMOUNT" ] ; do
                CPU_CURR_FREQ=${cpu_states[$STATE_ID]}
                echo '        {' >> $SYSTEMSTATES_CONF
                echo "            \"stateNum\": $STATENUM," >> $SYSTEMSTATES_CONF
                echo "            \"description\": \"CPUfreq policy set to '$GOVERNOR' and frequency set to $(($CPU_CURR_FREQ/1000))Mhz for all cores\"," >> $SYSTEMSTATES_CONF
                echo '            "condition": [' >> $SYSTEMSTATES_CONF
                echo '                {' >> $SYSTEMSTATES_CONF
                echo "                    \"key\": \"cpugov\"," >> $SYSTEMSTATES_CONF
                echo "                    \"value\": \"$GOVERNOR\"" >> $SYSTEMSTATES_CONF
                echo '                },' >> $SYSTEMSTATES_CONF
                echo '                {' >> $SYSTEMSTATES_CONF
                echo "                    \"key\": \"cpufreq\"," >> $SYSTEMSTATES_CONF
                echo "                    \"value\": \"$CPU_CURR_FREQ\"" >> $SYSTEMSTATES_CONF
                echo '                }' >> $SYSTEMSTATES_CONF
                echo '            ]' >> $SYSTEMSTATES_CONF
                if [ $STATENUM -eq $STATES ]
                then
                    echo '        }' >> $SYSTEMSTATES_CONF
                else
                    echo '        },' >> $SYSTEMSTATES_CONF
                fi
                let STATENUM=$STATENUM+1
                let STATE_ID=$STATE_ID+$CPU_FREQ_STEP
            done
        elif [ $GOVERNOR = "ondemand" ] || [ $GOVERNOR = "powersave" ] ; then
            echo '        {' >> $SYSTEMSTATES_CONF
            echo "            \"stateNum\": $STATENUM," >> $SYSTEMSTATES_CONF
            echo "            \"description\": \"CPUfreq policy set to '$GOVERNOR' for all cores\"," >> $SYSTEMSTATES_CONF
            echo '            "condition": [' >> $SYSTEMSTATES_CONF
            echo '                {' >> $SYSTEMSTATES_CONF
            echo "                    \"key\": \"cpugov\"," >> $SYSTEMSTATES_CONF
            echo "                    \"value\": \"$GOVERNOR\"" >> $SYSTEMSTATES_CONF
            echo '                }' >> $SYSTEMSTATES_CONF
            echo '            ]' >> $SYSTEMSTATES_CONF
            if [ $STATENUM -eq $STATES ]
            then
                echo '        }' >> $SYSTEMSTATES_CONF
            else
                echo '        },' >> $SYSTEMSTATES_CONF
            fi
            let STATENUM=$STATENUM+1
        fi
    done
    echo '    ]' >> $SYSTEMSTATES_CONF
    echo '}' >> $SYSTEMSTATES_CONF
}

function update_systemstates_intel_conf {
    local STATES_AMOUNT=8

    create_backup $SYSTEMSTATES_CONF

    # Start with the header
    echo '{' > $SYSTEMSTATES_CONF
    echo '   "state": "auto",' >> $SYSTEMSTATES_CONF
    echo "   \"howmany\": $STATES_AMOUNT" >> $SYSTEMSTATES_CONF
    echo '}' >> $SYSTEMSTATES_CONF
}

#### SMBUS ####
function update_smbus_conf {
    local SMBUS_CMI_NAME="SMBus CMI adapter cmi"
    local SMBUS_LEGACY_NAME="SMBus I801 adapter at"
    local SMBUS_NAME=""

    create_backup $SMBUS_CONF

    if [ $SMBUS_USE_LEGACY -eq 1 ]
    then
        SMBUS_NAME=$SMBUS_LEGACY_NAME
    else
        SMBUS_NAME=$SMBUS_CMI_NAME
    fi
    # Only list the SCMI watchdog interface, even if there should be other
    # detected
    echo '{' > $SMBUS_CONF
    echo '    "type": "smbus",' >> $SMBUS_CONF
    echo '    "bus": [' >> $SMBUS_CONF
    echo '        {' >> $SMBUS_CONF
    echo '            "unit": 0,' >> $SMBUS_CONF
    echo "            \"name\": \"$SMBUS_NAME\"" >> $SMBUS_CONF
    echo '        }' >> $SMBUS_CONF
    echo '    ]' >> $SMBUS_CONF
    echo '}' >> $SMBUS_CONF
}

function update_i2c_conf {
    local BASEDIR=/sys/class/i2c-dev/

    create_backup $I2C_CONF

    local ADAPTERS=(`find $BASEDIR -name i2c-[0-9]* | sort -V`)
    local KEMPLD=''
    local JIDA=''
    local JILI=''
    local SMBUS=()
    local I2C=()

    local I=0

    for ((I=0; I < ${#ADAPTERS[@]}; I++)); do
        local BUS=${ADAPTERS[$I]}

        local NAME=`cat $BUS/name`

        if [[ "$NAME" == SMBus* ]] ; then
            SMBUS[${#SMBUS[@]}]=$NAME
        elif [[ "$NAME" == "i2c-kempld" ]] ; then
            # Assuming the mux adapters will always follow the master
            KEMPLD=${BUS##*i2c-}
            JIDA=$NAME
        elif [[ "$NAME" == "i2c-${KEMPLD}-mux (chan_id 0)" ]] ; then
            JIDA=$NAME
        elif [[ "$NAME" == "i2c-${KEMPLD}-mux (chan_id 1)" ]] ; then
            JILI=$NAME
        else
            I2C[${#I2C[@]}]=$NAME
        fi
    done

    # Start with the header
    echo '{' > $I2C_CONF
    echo '    "type": "i2c",' >> $I2C_CONF
    echo '    "bus": [' >> $I2C_CONF

    # JIDA I2C Bus comes first
    if ! [ -z "$JIDA" ] ; then
        echo '        {'  >> $I2C_CONF
        echo '            "unit": 0,' >> $I2C_CONF
        echo "            \"name\": \"$JIDA\"" >> $I2C_CONF
    fi

    if  [ ! $I2C_ADD_ALL_BUSES -eq 1 ]
    then
        echo '        }'  >> $I2C_CONF
        echo '    ]' >> $I2C_CONF
        echo '}' >> $I2C_CONF
        return
    fi

    if [ ${#I2C[@]} -eq 0 -a -z "$JILI" ] ; then
        echo '        }'  >> $I2C_CONF
    else
        echo '        },'  >> $I2C_CONF
    fi

    # Other I2C busses just come as 
    for ((I=0; I < ${#I2C[@]}; I++)); do
        local BUS=${I2C[$I]}

        echo '        {'  >> $I2C_CONF
        echo '            "unit": 0,' >> $I2C_CONF
        echo "            \"name\": \"$BUS\"" >> $I2C_CONF

        if [ $I -lt $((${#I2C[@]}-1)) ] ; then
            echo '        },'  >> $I2C_CONF
        else
            if [ -z "$JILI" ] ; then
                echo '        }'  >> $I2C_CONF
            else
                echo '        },'  >> $I2C_CONF
            fi
        fi
    done

    # JILI I2C comes last
    if ! [ -z "$JILI" ] ; then
        echo '        {'  >> $I2C_CONF
        echo '            "unit": 0,' >> $I2C_CONF
        echo "            \"name\": \"$JILI\"" >> $I2C_CONF
        echo '        }'  >> $I2C_CONF
    fi

    echo '    ]' >> $I2C_CONF
    echo '}' >> $I2C_CONF
}

#### SPI ####
function update_spi_conf {
    local BASEDIR=/dev

    create_backup $SPI_CONF

    local SPIDEVS=(`find $BASEDIR -name "spidev*" | grep -E /dev/spidev[0-9]+ -o | sort -uV`)

    # Start with an empty file
    echo -n > $SPI_CONF

    for SPIDEV in ${SPIDEVS[@]}
    do
        echo $SPIDEV >> ${SPI_CONF}
    done
}

if [ $UPDATE_BACKLIGHT_CONF -eq 1 ]
then
    update_backlight_conf
fi

if [ $UPDATE_GPIO_CONF_KEMLIKE -eq 1 ]
then
    update_gpio_conf_kemlike
fi

if [ $UPDATE_GPIO_CONF_LEGACY -eq 1 ]
then
    update_gpio_conf_legacy
fi

if [ $UPDATE_GPIO_CONF_FULL -eq 1 ]
then
    update_gpio_conf_full
fi

if [ -n "$GPIO_SYMLINK_CONFIG" ]
then
    update_gpio_conf
fi

if [ $UPDATE_STORAGE_CONF -eq 1 ]
then
    update_storage_conf
fi

if [ $UPDATE_WATCHDOG_CONF -eq 1 ]
then
    update_watchdog_conf
fi

if [ $UPDATE_SYSTEMSTATES_CONF -eq 1 ]
then
    update_systemstates_conf
fi

if [ $UPDATE_SMBUS_CONF -eq 1 ]
then
    update_smbus_conf
fi

if [ $UPDATE_I2C_CONF -eq 1 ]
then
    update_i2c_conf
fi

if [ $UPDATE_SPI_CONF -eq 1 ]
then
    update_spi_conf
fi
