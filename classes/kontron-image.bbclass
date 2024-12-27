inherit core-image

FEATURE_PACKAGES_basetools = ' \
                            acpid \
                            bash \
                            beep \
                            cifs-utils \
                            coreutils \
                            file \
                            findutils \
                            grep \
                            grub \
                            less \
                            mc \
                            nano \
                            opkg \
                            procps \
                            psmisc \
                            pv \
                            rsync \
                            tmux \
                            util-linux \
                            util-linux-hwclock \
                            util-linux-lscpu \
                            vim \
                            wget \
                            whiptail \
                            '

FEATURE_PACKAGES_testtools = ' \
                            alsa-utils \
                            alsa-utils-speakertest \
                            analyze-suspend \
                            can-utils \
                            dmidecode \
                            i2c-tools \
                            ${@bb.utils.contains('XSERVER', \
                              '${XSERVER_X86_I915}', 'igt-gpu-tools', '', d)} \
                            ${@bb.utils.contains('MACHINE_EXTRA_RRECOMMENDS', \
                              'libkeapi3', 'ktool', '', d)} \
                            memtester \
                            perf \
                            powertop \
                            rt-tests \
                            stress \
                            stress-ng \
                            usbutils \
                            watchdog-test \
                            gpio-kernel-tools \
                            gpio-test \
                            ltp \
                            fwts \
                            latencytop \
                            opensc \
                            pcsc-lite \
                            tshark \
                            phytool \
                            strace \
                            setserial \
                            smistat \
                            spi-kernel-tools \
                            spitools \
                            tcpdump \
                            turbostat \
                            cpupower \
                            gpioint-test \
                            rtc-test \
                            evtest \
                            trace-cmd \
                            libva-intel-utils \
                            '

FEATURE_PACKAGES_benchmarks = ' \
                              dbench \
                              fio \
                              glmark2 \
                              iozone3 \
                              iperf2 \
                              iperf3 \
                              phoronix-test-suite \
                              sysbench \
                              libsdl2-dev \
                              '

FEATURE_PACKAGES_systemtools = ' \
                               acpica \
                               acpitool \
                               ddrescue \
                               ethtool \
                               flashrom \
                               fwupd \
                               gparted \
                               gptfdisk \
                               htop \
                               ifenslave \
                               libgpiod \
                               libgpiod-tools \
                               linuxptp \
                               lmsensors-config-libsensors \
                               lmsensors-fancontrol \
                               lmsensors-pwmconfig \
                               lmsensors-sensors \
                               lmsensors-sensorsdetect \
                               lsof \
                               minicom \
                               mmc-utils \
                               msr-tools \
                               mtd-utils \
                               mtools \
                               nvme-cli \
                               pmtools \
                               picocom \
                               pps-tools \
                               efibootmgr \
                               efivar \
                               smartmontools \
                               ntp \
                               ntpdate \
                               read-edid \
                               bc \
                               lsscsi \
                               iproute2-tc \
                               ntfs-3g \
                               '

FEATURE_PACKAGES_tpm = ' \
                            tpm-tools \
                            trousers \
                         '

FEATURE_PACKAGES_tpm2 = ' \
                            tpm2-tools \
                            tpm2-tss \
                            libtss2-tcti-device \
                            iproute2-ss \
                            uthash-dev \
                         '

FEATURE_PACKAGES_xfce-addons  = ' \
                                epiphany \
                                gigolo \
                                ristretto \
                                thunar-archive-plugin \
                                thunar-media-tags-plugin \
                                xfce4-closebutton-plugin \
                                xfce4-cpufreq-plugin \
                                xfce4-cpugraph-plugin \
                                xfce4-datetime-plugin \
                                xfce4-diskperf-plugin \
                                xfce4-genmon-plugin \
                                ${@bb.utils.contains('DISTRO_FEATURES', 'pulseaudio', 'xfce4-pulseaudio-plugin', '', d)} \
                                ${@bb.utils.contains("DISTRO_FEATURES", "bluetooth", "blueman", "", d)} \
                                xfce4-mount-plugin \
                                xfce4-netload-plugin \
                                xfce4-screenshooter \
                                xfce4-sensors-plugin \
                                xfce4-systemload-plugin \
                                xfce4-taskmanager \
                                xfce4-verve-plugin \
                                xfce4-xkb-plugin \
                                '

FEATURE_PACKAGES_development = ' \
                               kernel-devsrc \
                               git \
                               unzip \
                               patchelf \
                               devmem2 \
                               ioport \
                               '

FEATURE_PACKAGES_verification = ' \
                               munin-autoconf \
                               munin-node \
                               python3-misc \
                               python3-pip \
                               python3-robotframework \
                               '

IMAGE_INSTALL:append = " ${@bb.utils.contains('TUNE_FEATURES', 'neon', 'cpuburn-neon', '', d)} \
                       "
