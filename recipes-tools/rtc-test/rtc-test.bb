DESCRIPTION = "Real Time Clock Driver Test/Example Program"
PRIORITY = "optional"
LICENSE = "GPLv2"
LIC_FILES_CHKSUM = "file://COPYING;md5=12f884d2ae1ff87c09e5b7ccc2c4ca7e"

PR = "r1"
PV = "1.0"

SRC_URI = "file://COPYING \
           file://rtctest.c"

S = "${WORKDIR}"

TARGET_CC_ARCH += "${LDFLAGS}"

#EXTRA_OEMAKE = "'CC=${CC}' LFLAGS='${LDFLAGS} -lpthread' CROSS_COMPILE=arm-poky-linux-gnueabi-"

#do_compile() {
#	${CC} -s -Wall -Wstrict-prototypes rtctest.c -o rtc-test
#}
do_compile() {
	${CC} -Wall -Wstrict-prototypes rtctest.c -o rtc-test
}

do_install() {
	install -d ${D}${bindir}
	install -m 0755 rtc-test ${D}${bindir}
}
