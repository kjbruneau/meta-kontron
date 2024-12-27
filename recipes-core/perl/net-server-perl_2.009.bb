SUMMARY = "Net::Server - Extensible, general Perl server engine"
SECTION = "libs"
LICENSE = "Artistic-1.0 | GPL-1.0+"
LIC_FILES_CHKSUM = "file://LICENSE;md5=b3e3f36aab8aa5cda4fa39ced4cefd2f"

SRC_URI = "https://cpan.metacpan.org/authors/id/R/RH/RHANDOM/Net-Server-2.009.tar.gz"

SRC_URI[md5sum] = "d45a0700d820b3eebd1e9e14611b1613"
SRC_URI[sha256sum] = "8267c6560355e2e0f483d3cc16195f342f32fe13cae9dde75a0a1ece5e9a813f"

S = "${WORKDIR}/Net-Server-${PV}"

inherit cpan
