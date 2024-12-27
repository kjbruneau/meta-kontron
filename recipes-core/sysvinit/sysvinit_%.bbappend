# This file adds a dummy rc.local script, that can be used to easily start
# scripts at startup and disables the bootlog daemon by default

FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SRC_URI += "file://rc.local \
            file://rc.local.init \
            file://bootlogd.default \
           "

FILES:${PN} += "${SYSCONFDIR}/init.d/rc.local \
               ${SYSCONFDIR}/rc.local \
               ${SYSCONFDIR}/rc2.d/S99rc.local \
               ${SYSCONFDIR}/rc3.d/S99rc.local \
               ${SYSCONFDIR}/rc4.d/S99rc.local \
               ${SYSCONFDIR}/rc5.d/S99rc.local \
               ${SYSCONFDIR}/default/bootlogd \
              "

do_install:append () {
    install -m 0755 ${WORKDIR}/rc.local.init ${D}/${sysconfdir}/init.d/rc.local
    install -m 0755 ${WORKDIR}/rc.local ${D}/${sysconfdir}/rc.local
    ln -s ../init.d/rc.local ${D}/${sysconfdir}/rc2.d/S99rc.local
    ln -s ../init.d/rc.local ${D}/${sysconfdir}/rc3.d/S99rc.local
    ln -s ../init.d/rc.local ${D}/${sysconfdir}/rc4.d/S99rc.local
    ln -s ../init.d/rc.local ${D}/${sysconfdir}/rc5.d/S99rc.local

    install -d ${sysconfdir}/default/
    install -m 0644 ${WORKDIR}/bootlogd.default ${D}/${sysconfdir}/default/bootlogd
}
