SUMMARY = "munin node and plugins"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=30d656d1b43d7e2baaf16cb784e8a1c3"

SRC_URI = "git://github.com/munin-monitoring/munin.git;protocol=git;branch=stable-2.0;name=munin;destsuffix=munin \
           file://smistat \
           file://smistat.conf \
           file://sensors.conf \
           file://diskstat.conf \
           file://Makefile.config \
           file://munin-node \
           file://munin-node.conf \
           "
SRCREV = "9006d3b4c3aac53cb8fcd0c37e1f1aeff5fb39c6"

FILES:${PN} = " \
  /usr/share/munin \
  /usr/share/munin/plugins \
  /usr/share/munin/plugins/amavis \
  /usr/share/munin/plugins/snmp__memory \
  /usr/share/munin/plugins/meminfo \
  /usr/share/munin/plugins/psu_ \
  /usr/share/munin/plugins/multips \
  /usr/share/munin/plugins/apc_envunit_ \
  /usr/share/munin/plugins/postfix_mailstats \
  /usr/share/munin/plugins/plugin.sh \
  /usr/share/munin/plugins/cps_ \
  /usr/share/munin/plugins/port_ \
  /usr/share/munin/plugins/snmp__print_supplies \
  /usr/share/munin/plugins/plugins.history \
  /usr/share/munin/plugins/snmp__sensors_fsc_bx_temp \
  /usr/share/munin/plugins/postgres_bgwriter \
  /usr/share/munin/plugins/haproxy_ng \
  /usr/share/munin/plugins/snmp__winload \
  /usr/share/munin/plugins/snmp__fc_if_err_ \
  /usr/share/munin/plugins/mysql_bytes \
  /usr/share/munin/plugins/quota_usage_ \
  /usr/share/munin/plugins/vserver_cpu_ \
  /usr/share/munin/plugins/nfs4_client \
  /usr/share/munin/plugins/ping_ \
  /usr/share/munin/plugins/snmp__netapp_inodeusage_ \
  /usr/share/munin/plugins/fw_conntrack \
  /usr/share/munin/plugins/mysql_slowqueries \
  /usr/share/munin/plugins/snmp__netapp_diskusage_ \
  /usr/share/munin/plugins/netstat \
  /usr/share/munin/plugins/postgres_querylength_ \
  /usr/share/munin/plugins/postgres_checkpoints \
  /usr/share/munin/plugins/dhcpd3 \
  /usr/share/munin/plugins/snmp__sensors_fsc_fan \
  /usr/share/munin/plugins/courier_mta_mailvolume \
  /usr/share/munin/plugins/asterisk_sippeers \
  /usr/share/munin/plugins/qmailqstat \
  /usr/share/munin/plugins/courier_mta_mailstats \
  /usr/share/munin/plugins/slony_lag_ \
  /usr/share/munin/plugins/postgres_cache_ \
  /usr/share/munin/plugins/freeradius_proxy_acct \
  /usr/share/munin/plugins/squid_cache \
  /usr/share/munin/plugins/snmp__if_err_ \
  /usr/share/munin/plugins/snmp__df \
  /usr/share/munin/plugins/nginx_status \
  /usr/share/munin/plugins/apache_processes \
  /usr/share/munin/plugins/mysql_threads \
  /usr/share/munin/plugins/bind9 \
  /usr/share/munin/plugins/haproxy_ \
  /usr/share/munin/plugins/ntp_kernel_pll_off \
  /usr/share/munin/plugins/hddtemp2 \
  /usr/share/munin/plugins/postgres_users \
  /usr/share/munin/plugins/ntp_ \
  /usr/share/munin/plugins/snmp__sensors_mbm_volt \
  /usr/share/munin/plugins/hddtemp_smartctl \
  /usr/share/munin/plugins/snmp__swap \
  /usr/share/munin/plugins/freeradius_proxy_auth \
  /usr/share/munin/plugins/multips_memory \
  /usr/share/munin/plugins/selinux_avcstat \
  /usr/share/munin/plugins/asterisk_codecs \
  /usr/share/munin/plugins/vlan_linkuse_ \
  /usr/share/munin/plugins/tomcat_ \
  /usr/share/munin/plugins/snmp__df_ram \
  /usr/share/munin/plugins/snmp__load \
  /usr/share/munin/plugins/fw_forwarded_local \
  /usr/share/munin/plugins/nut_misc \
  /usr/share/munin/plugins/ntp_offset \
  /usr/share/munin/plugins/iostat_ios \
  /usr/share/munin/plugins/snmp__winmem \
  /usr/share/munin/plugins/squid_requests \
  /usr/share/munin/plugins/hddtemp \
  /usr/share/munin/plugins/apt_all \
  /usr/share/munin/plugins/snort_bytes_pkt \
  /usr/share/munin/plugins/if_err_ \
  /usr/share/munin/plugins/netstat_multi \
  /usr/share/munin/plugins/processes \
  /usr/share/munin/plugins/openvpn \
  /usr/share/munin/plugins/interrupts \
  /usr/share/munin/plugins/ip_ \
  /usr/share/munin/plugins/asterisk_meetmeusers \
  /usr/share/munin/plugins/cupsys_pages \
  /usr/share/munin/plugins/ipmi_ \
  /usr/share/munin/plugins/ntp_states \
  /usr/share/munin/plugins/asterisk_channelstypes \
  /usr/share/munin/plugins/spamstats \
  /usr/share/munin/plugins/nutups_ \
  /usr/share/munin/plugins/sensors_ \
  /usr/share/munin/plugins/snmp__cpuload \
  /usr/share/munin/plugins/postgres_autovacuum \
  /usr/share/munin/plugins/sendmail_mailqueue \
  /usr/share/munin/plugins/squid_objectsize \
  /usr/share/munin/plugins/ejabberd_ \
  /usr/share/munin/plugins/cmc_tc_sensor_ \
  /usr/share/munin/plugins/squid_icp \
  /usr/share/munin/plugins/bind9_rndc \
  /usr/share/munin/plugins/foldingathome_rank \
  /usr/share/munin/plugins/asterisk_sipchannels \
  /usr/share/munin/plugins/threads \
  /usr/share/munin/plugins/asterisk_channels \
  /usr/share/munin/plugins/proc_pri \
  /usr/share/munin/plugins/buddyinfo \
  /usr/share/munin/plugins/postgres_streaming_ \
  /usr/share/munin/plugins/snort_traffic \
  /usr/share/munin/plugins/mysql_innodb \
  /usr/share/munin/plugins/snmp__if_multi \
  /usr/share/munin/plugins/nfs_client \
  /usr/share/munin/plugins/load \
  /usr/share/munin/plugins/acpi \
  /usr/share/munin/plugins/snmp__uptime \
  /usr/share/munin/plugins/postgres_tuples_ \
  /usr/share/munin/plugins/nut_volts \
  /usr/share/munin/plugins/memcached_ \
  /usr/share/munin/plugins/ipac-ng \
  /usr/share/munin/plugins/smart_ \
  /usr/share/munin/plugins/snmp__netstat \
  /usr/share/munin/plugins/munin_update \
  /usr/share/munin/plugins/postgres_xlog \
  /usr/share/munin/plugins/fail2ban \
  /usr/share/munin/plugins/vlan_ \
  /usr/share/munin/plugins/nginx_request \
  /usr/share/munin/plugins/http_loadtime \
  /usr/share/munin/plugins/snmp__if_ \
  /usr/share/munin/plugins/uptime \
  /usr/share/munin/plugins/vlan_inetuse_ \
  /usr/share/munin/plugins/postfix_mailvolume \
  /usr/share/munin/plugins/varnish_ \
  /usr/share/munin/plugins/postgres_connections_db \
  /usr/share/munin/plugins/mailscanner \
  /usr/share/munin/plugins/ntp_kernel_pll_freq \
  /usr/share/munin/plugins/postgres_prepared_xacts_ \
  /usr/share/munin/plugins/squid_traffic \
  /usr/share/munin/plugins/cpu \
  /usr/share/munin/plugins/vserver_loadavg \
  /usr/share/munin/plugins/asterisk_meetme \
  /usr/share/munin/plugins/snmp__rdp_users \
  /usr/share/munin/plugins/postgres_size_ \
  /usr/share/munin/plugins/courier_mta_mailqueue \
  /usr/share/munin/plugins/tomcat_threads \
  /usr/share/munin/plugins/samba \
  /usr/share/munin/plugins/postgres_oldest_prepared_xact_ \
  /usr/share/munin/plugins/slapd_ \
  /usr/share/munin/plugins/courier_ \
  /usr/share/munin/plugins/loggrep \
  /usr/share/munin/plugins/tomcat_access \
  /usr/share/munin/plugins/freeradius_acct \
  /usr/share/munin/plugins/proc \
  /usr/share/munin/plugins/mbmon_ \
  /usr/share/munin/plugins/proxy_plugin \
  /usr/share/munin/plugins/lpstat \
  /usr/share/munin/plugins/bonding_err_ \
  /usr/share/munin/plugins/hp2000_ \
  /usr/share/munin/plugins/lpar_cpu \
  /usr/share/munin/plugins/df_abs \
  /usr/share/munin/plugins/surfboard \
  /usr/share/munin/plugins/exim_mailstats \
  /usr/share/munin/plugins/mailman \
  /usr/share/munin/plugins/entropy \
  /usr/share/munin/plugins/asterisk_voicemail \
  /usr/share/munin/plugins/mhttping \
  /usr/share/munin/plugins/zimbra_ \
  /usr/share/munin/plugins/ps_ \
  /usr/share/munin/plugins/mysql_isam_space_ \
  /usr/share/munin/plugins/postfix_mailqueue \
  /usr/share/munin/plugins/forks \
  /usr/share/munin/plugins/qmailscan \
  /usr/share/munin/plugins/snmp__sensors_mbm_fan \
  /usr/share/munin/plugins/digitemp_ \
  /usr/share/munin/plugins/snort_pattern_match \
  /usr/share/munin/plugins/foldingathome_wu \
  /usr/share/munin/plugins/postgres_locks_ \
  /usr/share/munin/plugins/qmailscan-simple \
  /usr/share/munin/plugins/sendmail_mailstats \
  /usr/share/munin/plugins/exim_mailqueue \
  /usr/share/munin/plugins/pgbouncer_connections \
  /usr/share/munin/plugins/snort_alerts \
  /usr/share/munin/plugins/snmp__sensors_fsc_temp \
  /usr/share/munin/plugins/apc_nis \
  /usr/share/munin/plugins/hddtempd \
  /usr/share/munin/plugins/df \
  /usr/share/munin/plugins/tcp \
  /usr/share/munin/plugins/exim_mailqueue_alt \
  /usr/share/munin/plugins/snmp__print_pages \
  /usr/share/munin/plugins/vserver_resources \
  /usr/share/munin/plugins/foldingathome \
  /usr/share/munin/plugins/snmp__sensors_mbm_temp \
  /usr/share/munin/plugins/apache_accesses \
  /usr/share/munin/plugins/diskstats \
  /usr/share/munin/plugins/ipmi_sensor_ \
  /usr/share/munin/plugins/pop_stats \
  /usr/share/munin/plugins/ifx_concurrent_sessions_ \
  /usr/share/munin/plugins/iostat \
  /usr/share/munin/plugins/yum \
  /usr/share/munin/plugins/freeradius_auth \
  /usr/share/munin/plugins/snmp__sensors_fsc_bx_fan \
  /usr/share/munin/plugins/postgres_connections_ \
  /usr/share/munin/plugins/nfsd \
  /usr/share/munin/plugins/netopia \
  /usr/share/munin/plugins/swap \
  /usr/share/munin/plugins/apt \
  /usr/share/munin/plugins/postgres_transactions_ \
  /usr/share/munin/plugins/df_inode \
  /usr/share/munin/plugins/slapd_bdb_cache_ \
  /usr/share/munin/plugins/named \
  /usr/share/munin/plugins/snmp__users \
  /usr/share/munin/plugins/snort_drop_rate \
  /usr/share/munin/plugins/open_files \
  /usr/share/munin/plugins/multiping \
  /usr/share/munin/plugins/snmp__fc_if_ \
  /usr/share/munin/plugins/postgres_scans_ \
  /usr/share/munin/plugins/memory \
  /usr/share/munin/plugins/pm3users_ \
  /usr/share/munin/plugins/snort_pkts \
  /usr/share/munin/plugins/snmp__processes \
  /usr/share/munin/plugins/diskstat_ \
  /usr/share/munin/plugins/squeezebox_ \
  /usr/share/munin/plugins/munin_stats \
  /usr/share/munin/plugins/ircu \
  /usr/share/munin/plugins/sendmail_mailtraffic \
  /usr/share/munin/plugins/users \
  /usr/share/munin/plugins/tomcat_jvm \
  /usr/share/munin/plugins/mysql_queries \
  /usr/share/munin/plugins/mysql_ \
  /usr/share/munin/plugins/nvidia_ \
  /usr/share/munin/plugins/fw_packets \
  /usr/share/munin/plugins/perdition \
  /usr/share/munin/plugins/vmstat \
  /usr/share/munin/plugins/open_inodes \
  /usr/share/munin/plugins/sybase_space \
  /usr/share/munin/plugins/nfsd4 \
  /usr/share/munin/plugins/irqstats \
  /usr/share/munin/plugins/ntp_kernel_err \
  /usr/share/munin/plugins/pgbouncer_requests \
  /usr/share/munin/plugins/cpuspeed \
  /usr/share/munin/plugins/if_ \
  /usr/share/munin/plugins/nomadix_users_ \
  /usr/share/munin/plugins/tomcat_volume \
  /usr/share/munin/plugins/apache_volume \
  /etc/munin \
  /etc/munin/plugin-conf.d \
  /etc/munin/plugins \
  /etc/munin/plugins/smistat \
  /etc/munin/plugins/hddtemp_smartctl \
  /etc/munin/plugins/iostat_ios \
  /etc/munin/plugins/acpi \
  /etc/munin/plugins/sensors_volt \
  /etc/munin/plugins/sensors_fan \
  /etc/munin/plugins/sensors_temp \
  /etc/munin/plugins/iostat \
  /etc/munin/plugins/memory \
  /etc/munin/plugins/irqstats \
  /var/lib/munin-node \
  /var/lib/munin-node/plugin-state \
  /usr/sbin/munin-node-configure \
  /usr/sbin/munin-node \
  /usr/sbin/munin-run \
  /usr/bin/munindoc \
  /run/munin \
  /etc/perl \
  /etc/perl/Munin \
  /etc/perl/Munin/Plugin.pm \
  /etc/perl/Munin/Node \
  /etc/perl/Munin/Plugin \
  /etc/perl/Munin/Common \
  /etc/perl/Munin/Node/Server.pm \
  /etc/perl/Munin/Node/Config.pm \
  /etc/perl/Munin/Node/OS.pm \
  /etc/perl/Munin/Node/SpoolReader.pm \
  /etc/perl/Munin/Node/Session.pm \
  /etc/perl/Munin/Node/SNMPConfig.pm \
  /etc/perl/Munin/Node/Service.pm \
  /etc/perl/Munin/Node/Logger.pm \
  /etc/perl/Munin/Node/SpoolWriter.pm \
  /etc/perl/Munin/Node/Utils.pm \
  /etc/perl/Munin/Node/Configure \
  /etc/perl/Munin/Node/Configure/Debug.pm \
  /etc/perl/Munin/Node/Configure/Plugin.pm \
  /etc/perl/Munin/Node/Configure/HostEnumeration.pm \
  /etc/perl/Munin/Node/Configure/PluginList.pm \
  /etc/perl/Munin/Node/Configure/History.pm \
  /etc/perl/Munin/Plugin/Pgsql.pm \
  /etc/perl/Munin/Plugin/SNMP.pm \
  /etc/perl/Munin/Common/Defaults.pm \
  /etc/perl/Munin/Common/DictFile.pm \
  /etc/perl/Munin/Common/Config.pm \
  /etc/perl/Munin/Common/TLSClient.pm \
  /etc/perl/Munin/Common/Timeout.pm \
  /etc/perl/Munin/Common/SyncDictFile.pm \
  /etc/perl/Munin/Common/TLSServer.pm \
  /etc/perl/Munin/Common/TLS.pm \
  /var/volatile \
  /var/volatile/log \
  /var/volatile/log/munin \
  /var/lib/munin \
  /var/lib/munin/var \
  /var/lib/munin/var/spool \
  /etc/init.d \
  /etc/init.d/munin-node \
  "

S = "${WORKDIR}/munin"

DEPENDS = "perl"
RDEPENDS:${PN} = "python3-core ruby bash perl net-server-perl"
RCONFLICTS:${PN} = "munin-node-c"

inherit perlnative

INITSCRIPT_NAME = "munin-node"
INITSCRIPT_PARAMS = "defaults"

inherit useradd update-rc.d

USERADD_PACKAGES = "${PN}"
GROUPADD_PARAM:${PN} = "--system munin"
USERADD_PARAM:${PN} = "--system -M -d /var/lib/munin-node -s /bin/false -g munin munin"

do_configure () {
    cp ${WORKDIR}/Makefile.config ${S}
}

do_compile () {
    make
}

do_install () {
    export DESTDIR=${D}
    make install-common-prime
    make install-node-prime
    make install-node-plugins

    install -m 0644 ${WORKDIR}/diskstat.conf ${D}${sysconfdir}/munin/plugin-conf.d/
    install -m 0644 ${WORKDIR}/sensors.conf ${D}${sysconfdir}/munin/plugin-conf.d/
    install -m 0644 ${WORKDIR}/smistat.conf ${D}${sysconfdir}/munin/plugin-conf.d/
    install -m 0755 ${WORKDIR}/smistat ${D}${datadir}/munin/plugins/
    install -m 0644 ${WORKDIR}/munin-node.conf ${D}${sysconfdir}/munin/
    install -d "${D}${sysconfdir}/init.d"
    install -m 0755 ${WORKDIR}/munin-node ${D}${sysconfdir}/init.d/munin-node

    # Don't create system dirs, as they will conflict with base-files
    rm -rf ${D}/var/log
    rm -rf ${D}/var/run

    # Plugins enabled by default
    ln -s ${datadir}/munin/plugins/sensors_ ${D}${sysconfdir}/munin/plugins/sensors_fan
    ln -s ${datadir}/munin/plugins/sensors_ ${D}${sysconfdir}/munin/plugins/sensors_temp
    ln -s ${datadir}/munin/plugins/sensors_ ${D}${sysconfdir}/munin/plugins/sensors_volt
    ln -s ${datadir}/munin/plugins/acpi ${D}${sysconfdir}/munin/plugins/acpi
    ln -s ${datadir}/munin/plugins/hddtemp_smartctl ${D}${sysconfdir}/munin/plugins/hddtemp_smartctl
    ln -s ${datadir}/munin/plugins/memory ${D}${sysconfdir}/munin/plugins/memory
    ln -s ${datadir}/munin/plugins/iostat ${D}${sysconfdir}/munin/plugins/iostat
    ln -s ${datadir}/munin/plugins/iostat_ios ${D}${sysconfdir}/munin/plugins/iostat_ios
    ln -s ${datadir}/munin/plugins/irqstats ${D}${sysconfdir}/munin/plugins/irqstats
    ln -s ${datadir}/munin/plugins/smistat ${D}${sysconfdir}/munin/plugins/smistat
    ln -s ${datadir}/munin/plugins/cpuspeed ${D}${sysconfdir}/munin/plugins/cpuspeed
    ln -s ${datadir}/munin/plugins/cpu ${D}${sysconfdir}/munin/plugins/cpu
    ln -s ${datadir}/munin/plugins/uptime ${D}${sysconfdir}/munin/plugins/uptime
}
