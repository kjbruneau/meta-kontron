LICENSE = "Kontron-public-license"
DESCRIPTION = "Default config for COMe modules"

PV = "0.1"
PR = "0"

RDEPENDS:${PN} = "keapi-config-scripts"

SRC_URI = "file://keapi-config"

S = "${WORKDIR}/keapi-config"
LIC_FILES_CHKSUM = "file://${S}/usr/share/doc/keapi-config/COPYING;md5=e76f26ff7a5930b6f95009fddcaf270a"

inherit keapi-config

do_install:append () {
    install -m 0755 ${S}/etc/keapi/update_keapi.conf ${D}/etc/keapi/update_keapi.conf
    install -m 0755 ${S}/etc/keapi/FIRST_STARTUP ${D}/etc/keapi/FIRST_STARTUP

    ln -sf /sys/devices/platform/kontron-bootcounter/count ${D}/etc/keapi/bootcount0
}
