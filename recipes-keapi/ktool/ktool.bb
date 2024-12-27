SUMMARY = "Kontron Embedded API shell tools"
DESCRIPTION = "This package contains KEAPI shell tools."
AUTHOR = "Kontron AG"
HOMEPAGE = "https://www.kontron.com/products/software-and-solutions/keapi-3.0"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9229ab014dc6888ac2b5ce6c23151cb6"

PV = "3.0.0.7"
PR = "r1"

inherit pkgconfig

DEPENDS = "libkeapi3"

SRCBRANCH = "master"
SRC_URI = "git://github.com/kontron/keapi-ktool.git;protocol=https;branch=${SRCBRANCH}"
SRCREV = "6e3e5ca0f618f36492cdbc204de9080ebba8ec6d"

S = "${WORKDIR}/git"

# The autotools configuration seems to have a problem with a race condition when parallel make is enabled
PARALLEL_MAKE = ""

EXTRA_OEMAKE = "'CC=${CC}' 'CFLAGS=${CFLAGS} -I${S}/include -I${STAGING_INCDIR}/keapi -Wl,--hash-style=gnu'"

do_install() {
        install -d ${D}${bindir}/
        install -m 0755 ${S}/bin/ktool ${D}${bindir}/
}
