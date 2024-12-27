FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

PR = "r1"

SRC_URI += " file://tpmtoken_init.patch \
            file://syslog-lib.patch \
            file://fs_stress \
           "

do_install:append() {
	install -m 0644 ${WORKDIR}/fs_stress ${D}/opt/ltp/runtest/fs_stress
}
