SUMMARY = "munin-autoconf"
LICENSE = "GPLv2+"
LIC_FILES_CHKSUM = "file://munin-autoconf;beginline=2;endline=2;md5=25b2fc1727a5d34a28ef0cb3fa994eb6"

S = "${WORKDIR}"

SRC_URI = "file://munin-autoconf \
           "
INITSCRIPT_NAME = "munin-autoconf"
INITSCRIPT_PARAMS = "defaults 98"

inherit update-rc.d

do_install () {
    install -d "${D}${sysconfdir}/init.d"
    install -m 0755 ${WORKDIR}/munin-autoconf ${D}${sysconfdir}/init.d/munin-autoconf
}
