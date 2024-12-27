FILESEXTRAPATHS:prepend := "${THISDIR}/files:"

# Set environment to use tpm2-abrmd

SRC_URI += "\
    file://tpm2-abrmd.sh \
    "

do_install:append() {
    install -d "${D}${sysconfdir}/profile.d"
    install -m 0644 "${WORKDIR}/tpm2-abrmd.sh" "${D}${sysconfdir}/profile.d/tpm2-abrmd.sh"
}
