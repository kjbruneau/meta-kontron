SUMMARY = "munin-node-c"
LICENSE = "GPLv3"
LIC_FILES_CHKSUM = "file://gpl-3.0.txt;md5=d32239bcb673463ab874e80d47fae504"

SRC_URI = "git://github.com/munin-monitoring/munin-c.git;protocol=git;branch=master;name=munin-c;destsuffix=munin-c \
           file://munin-node-c \
           "
SRCREV = "4e4715959388c3326f03041597f836ee8a879921"

S = "${WORKDIR}/munin-c"

SRC_URI[md5sum] = "a56352fcf4f9f6f424ad44876ddaf660"
SRC_URI[sha256sum] = "9a87e8a3dec62d3cf74a4deef81d5bc8c4f1f838aba85507546e075b36783034"

INITSCRIPT_NAME = "munin-node-c"
INITSCRIPT_PARAMS = "defaults"

RCONFLICTS:${PN} = "munin-node"

inherit autotools update-rc.d pkgconfig

do_install:append () {
    install -d "${D}${sysconfdir}/init.d"
    install -d "${D}${sysconfdir}/munin/plugins"
    install -d "${D}${sysconfdir}/munin/plugin-conf.d"
    install -m 0755 ${WORKDIR}/munin-node-c ${D}${sysconfdir}/init.d/munin-node-c
    ln -s ${libexecdir}/munin-c/munin-plugins-c ${D}${sysconfdir}/munin/plugins/cpu
    ln -s ${libexecdir}/munin-c/munin-plugins-c ${D}${sysconfdir}/munin/plugins/entropy
    ln -s ${libexecdir}/munin-c/munin-plugins-c ${D}${sysconfdir}/munin/plugins/forks
    ln -s ${libexecdir}/munin-c/munin-plugins-c ${D}${sysconfdir}/munin/plugins/fw_packets
    ln -s ${libexecdir}/munin-c/munin-plugins-c ${D}${sysconfdir}/munin/plugins/interrupts
    ln -s ${libexecdir}/munin-c/munin-plugins-c ${D}${sysconfdir}/munin/plugins/load
    ln -s ${libexecdir}/munin-c/munin-plugins-c ${D}${sysconfdir}/munin/plugins/open_files
    ln -s ${libexecdir}/munin-c/munin-plugins-c ${D}${sysconfdir}/munin/plugins/open_inodes
    ln -s ${libexecdir}/munin-c/munin-plugins-c ${D}${sysconfdir}/munin/plugins/processes
    ln -s ${libexecdir}/munin-c/munin-plugins-c ${D}${sysconfdir}/munin/plugins/swap
    ln -s ${libexecdir}/munin-c/munin-plugins-c ${D}${sysconfdir}/munin/plugins/uptime
}
