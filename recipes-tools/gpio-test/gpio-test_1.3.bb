SUMMARY = "A small GPIO test application"
LICENSE = "Kontron-AS-IS"
SECTION = "devel"

SRC_URI = "file://COPYING \
           file://gpio-test.c \
           file://Makefile"

LIC_FILES_CHKSUM = "file://COPYING;md5=94d8ca7aaa6e8987629cb803a3b5a398"

DEPENDS = " \
    virtual/${MLPREFIX}libc \
    ${MLPREFIX}elfutils \
    ${MLPREFIX}binutils \
"

inherit linux-kernel-base kernel-arch

do_configure[depends] += "virtual/kernel:do_shared_workdir"

S = "${WORKDIR}"

do_install () {
    install -d ${D}${base_bindir}
    install -D -m 0755 ${S}/gpio-test ${D}${base_bindir}
}
