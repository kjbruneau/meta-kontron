SUMMARY = "ioport - direct access to I/O ports from the command line"
HOMEPAGE = "https://people.redhat.com/rjones/ioport/"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=18810669f13b87348459e611d31ab760"

SRC_URI = "https://people.redhat.com/rjones/ioport/files/ioport-1.2.tar.gz"

COMPATIBLE_HOST = "(i.86|x86_64).*-linux"

SRC_URI[md5sum] = "cb5491eb1b08de5372bbbfbff8c3eada"
SRC_URI[sha256sum] = "7fac1c4b61eb9411275de0e1e7d7a8c3f34166f64f16413f50741e8fce2b8dc0"

inherit autotools

DEPENDS="perl-native"

do_configure() {
    cd ${S}
    ./configure ${CONFIGUREOPTS} ${EXTRA_OECONF}
}

do_compile() {
    cd ${S}
    make ${OEMAKE_EXTRA}
}

do_install () {
    cd ${S}
    make DESTDIR=${D} ${OEMAKE_EXTRA} install
}
