# Copyright (C) 2021 Kontron Europe GmbH
# SPDX-License-Identifier: MIT

SUMMARY = "Fixup the partition table on first startup of live images with partition table"
DESCRIPTION = "${SUMMARY}"

PROVIDES = "fixup-live-img"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

RDEPENDS:${PN}="gptfdisk"

SRC_URI = "\
    file://COPYING.MIT \
    file://fixup-live-img \
    "
S = "${WORKDIR}"

FILES:${PN} += "${sbindir}/init.d/fixup-live-img \
        "

do_install () {
    install -d ${D}/${sbindir}
    install -m 0755 ${WORKDIR}/fixup-live-img ${D}${sbindir}/fixup-live-img
}

pkg_postinst_ontarget:${PN} () {
    $D/usr/sbin/fixup-live-img
}
