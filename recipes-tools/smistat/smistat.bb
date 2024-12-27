FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

SUMMARY = "smistat tool to check Intel processors for occurring SMIs"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://smistat.c;endline=18;md5=205283de8b0bc2012f91f397b81099ce"

SRC_URI = "git://github.com/ColinIanKing/debug-code.git;protocol=https \
           file://0001-smistat-Added-raw-SMI-count-output-with-r-option.patch;striplevel=2"
SRCREV = "ee37b3b2bf5925d76f7e74fbcb823985ffae3ec4"

S = "${WORKDIR}/git/smistat"

do_install() {
    install -d ${D}${bindir}
    install -m 0755 ${S}/smistat ${D}${bindir}/smistat
}
