LICENSE = "Kontron-public-license"

PV = "0.1"
PR = "0"

SRC_URI = "file://keapi-update-configs \
           file://update_keapi_conf.sh \
           file://COPYING \
"

S = "${WORKDIR}"
LIC_FILES_CHKSUM = "file://COPYING;md5=e76f26ff7a5930b6f95009fddcaf270a"

inherit allarch update-rc.d

RDEPENDS:${PN} = "bash"

INITSCRIPT_NAME = "keapi-update-configs"
INITSCRIPT_PARAMS = "start 99 2 3 4 5 ."

PACKAGES = "${PN}"

do_install () {
    install -d ${D}/${sysconfdir}/keapi/
    install -d ${D}/${sysconfdir}/init.d/

    install -m 0755 ${S}/keapi-update-configs ${D}/${sysconfdir}/init.d/keapi-update-configs
    install -m 0755 ${S}/update_keapi_conf.sh ${D}/etc/keapi/update_keapi_conf.sh
}
