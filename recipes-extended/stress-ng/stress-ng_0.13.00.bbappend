FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

HOMEPAGE = "https://github.com/ColinIanKing/stress-ng#readme"

SRC_URI += "git://github.com/ColinIanKing/stress-ng.git;protocol=https;branch=master \
           file://0001-Makefile-do-not-write-the-timestamp-into-compressed-.patch \
           "

SRCREV = "61b454b4a3a9d052e63c78a9574ccf8a650575dc"
S = "${WORKDIR}/git"
