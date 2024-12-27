# beep: a console utility to "and allow a little more granularity
# than you get with the default terminal bell"
DESCRIPTION = "A console utility to make a beep noise"
SECTION = "console/utils"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=0636e73ff0215e8d672dc4c32c317bb3"

SRC_URI  = "git://github.com/johnath/beep.git;protocol=git;branch=master;name=beep-git;destsuffix=beep-git \
            file://0001-beep-Makefile-clean.patch \
            "
SRCREV = "0d790fa45777896749a885c3b93b2c1476d59f20"

EXTRA_OEMAKE += 'CC="${CC}"'
EXTRA_OEMAKE += 'FLAGS="${CFLAGS} ${LDFLAGS}"'

do_install() {
	install -d "${D}${bindir}"
	install -c -m 755 beep "${D}${bindir}/beep"
	install -d "${D}${mandir}/man1"
	install -c -m 644 beep.1.gz "${D}${mandir}/man1/beep.1.gz"
}
