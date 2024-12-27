SUMMARY = "SPI tools for Linux"
DESCRIPTION = "SPI tools comming with the linux kernel"

LICENSE = "GPLv2"
# Starting with Linux 5.10 Linux uses the SPDX-License-Identifier
KERNEL_LIC_CHKSUM = "${@ "6bc538ed5bd9a7fc9398086aedcd7e46" \
                         if bb.utils.vercmp_string_op( \
                             d.getVar('KERNEL_VERSION'), '5.10','>=') \
                         else "bbea815ee2795b2f4230826c0c6b8814" }"
LIC_FILES_CHKSUM = "file://COPYING;md5=${KERNEL_LIC_CHKSUM}"

DEPENDS = " \
    virtual/${MLPREFIX}libc \
    ${MLPREFIX}elfutils \
    ${MLPREFIX}binutils \
"

do_configure[depends] += "virtual/kernel:do_shared_workdir"

PROVIDES = "virtual/spi-kernel-tools"

inherit linux-kernel-base kernel-arch

#kernel 3.1+ supports WERROR to disable warnings as errors
export WERROR = "0"

do_populate_lic[depends] += "virtual/kernel:do_patch"

inherit kernelsrc

B = "${WORKDIR}/${BPN}-${PV}"
SPDX_S = "${S}/tools/spi"

# The LDFLAGS is required or some old kernels fails due missing
# symbols and this is preferred than requiring patches to every old
# supported kernel.
LDFLAGS="-ldl -lutil"

EXTRA_OEMAKE = '\
    -C ${S}/tools/spi \
    O=${B} \
    CROSS_COMPILE=${TARGET_PREFIX} \
    ARCH=${ARCH} \
    CC="${CC}" \
    AR="${AR}" \
    LD="${LD}" \
'

EXTRA_OEMAKE += "\
    'DESTDIR=${D}' \
    'prefix=${prefix}' \
    'bindir=${bindir}' \
    'sharedir=${datadir}' \
    'sysconfdir=${sysconfdir}' \
"

do_compile() {
	# Linux kernel build system is expected to do the right thing
	unset CFLAGS
	oe_runmake all
}

do_install() {
	# Linux kernel build system is expected to do the right thing
	unset CFLAGS
	oe_runmake install
}

do_configure:prepend () {
    # Fix for rebuilding
    rm -rf ${B}/
    mkdir -p ${B}/
}

PACKAGE_ARCH = "${MACHINE_ARCH}"

INHIBIT_PACKAGE_DEBUG_SPLIT="1"
