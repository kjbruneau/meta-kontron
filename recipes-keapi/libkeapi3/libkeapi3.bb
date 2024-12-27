SUMMARY = "Kontron Embedded API Library"
DESCRIPTION = "Kontron Embedded API - a software library that enables \
               programmers to easily create their applications for \
               monitoring and control hardware resources of Kontron boards, \
               modules, systems and platforms."
AUTHOR = "Kontron AG"
HOMEPAGE = "https://www.kontron.com/products/software-and-solutions/keapi-3.0"
SECTION = "libs"
LICENSE = "BSD-3-Clause"
LIC_FILES_CHKSUM = "file://LICENSE;md5=9229ab014dc6888ac2b5ce6c23151cb6"

PV = "3.0.6+git${SRCPV}"
PR = "r1"

DEPENDS = "bc-native"
DEPENDS += "libpcre libatasmart jansson e2fsprogs"
RDEPENDS:${PN} = "keapi-config coreutils"
PROVIDES = "libkeapi3"

PACKAGES = "${PN} ${PN}-dbg ${PN}-dev ${PN}-staticdev"

SRCBRANCH = "master"
SRC_URI = "git://github.com/kontron/keapi.git;protocol=https;branch=${SRCBRANCH}"
SRCREV = "5ec2809ce485491fac7cfeccacc637e93edeca0f"

S = "${WORKDIR}/git"

inherit autotools

# The autotools configuration seems to have a problem with a race condition when parallel make is enabled
PARALLEL_MAKE = ""

# Static library generation, for most cases, is now disabled by default in the Poky distribution
DISABLE_STATIC = ""

# Parameter for configure script
prefix="/usr"

LEAD_SONAME = "libkeapi.so"

FILES:${PN} += "${libdir}/libkeapi.so.* \
		${libdir}/libEApi.so.* \
		${libdir}/libjida.so.* \
                ${libdir}/pkgconfig/*.pc \
                ${includedir}/* \
                ${docdir}/keapi/*"
