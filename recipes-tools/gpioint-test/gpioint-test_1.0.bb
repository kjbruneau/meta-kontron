SUMMARY = "GPIO interrupt test application"
LICENSE = "Kontron-AS-IS"
SECTION = "devel"

SRC_URI = "file://COPYING \
           file://gpioint.c \
           file://Makefile"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d8ca7aaa6e8987629cb803a3b5a398"

S = "${WORKDIR}"

do_install () {
    install -d ${D}${base_bindir}
    install -D -m 0755 ${S}/gpioint ${D}${base_bindir}
}
