SUMMARY = "Firmware update daemon"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=4fbd65380cdd255951079008b364516c"

SRC_URI = "git://github.com/hughsie/fwupd.git;branch=1_4_X \
           file://0001-Disable-po-tests.patch \
           file://0001-Only-gzip-if-bmp-has-actually-been-created.patch \
           file://remotes.d/ \
          "

SRCREV = "8778905e6f4e37f16a9ab6188e71d3c269d77ab6"
PV = "1.4.6+git${SRCPV}"

S = "${WORKDIR}/git"

FILES:${PN} += " \
        ${datadir}/metainfo/org.freedesktop.fwupd.metainfo.xml \
        ${datadir}/bash-completion/completions/fwupdtool \
        ${datadir}/icons/hicolor/scalable/apps/org.freedesktop.fwupd.svg \
        ${datadir}/fish/vendor_completions.d/fwupdmgr.fish \
        ${datadir}/dbus-1/interfaces/org.freedesktop.fwupd.xml \
        ${datadir}/dbus-1/system.d/org.freedesktop.fwupd.conf \
        ${datadir}/polkit-1/rules.d/org.freedesktop.fwupd.rules \
        ${datadir}/polkit-1/actions/org.freedesktop.fwupd.policy \
        ${libdir}/fwupd-plugins-3 \
        "

EXTRA_OEMESON += "-Dbuild=standalone \
                  -Dagent=false \
                  -Dintrospection=false \
                  -Dman=false \
                  -Dtests=false \
                  -Defi-ldsdir=${WORKDIR}/recipe-sysroot-native/usr/lib/ \
                  -Defi-includedir=${WORKDIR}/recipe-sysroot-native/usr/include/efi/ \
                 "

PACKAGECONFIG ?= "${@ bb.utils.filter('DISTRO_FEATURES', 'systemd', d) } \
                  ${@ bb.utils.filter('MACHINE_FEATURES', 'efi', d) } \
                  ${@ bb.utils.filter('MACHINE_FEATURES', 'tpm2', d)} \
                  lvfs \
                  "
PACKAGECONFIG[systemd] = "-Dsystemd=true,-Dsystemd=false,systemd"
PACKAGECONFIG[elogind] = "-Delogind=true,-Delogind=false,libelogind"
PACKAGECONFIG[dell] = "-Dplugin_dell=true,-Dplugin_dell=false,libsmbios_c"
PACKAGECONFIG[synaptics] = "-Dplugin_synaptics=true,-Dplugin_synaptics=false"
PACKAGECONFIG[consolekit] = "-Dconsolekit=true,-Dconsolekit=false,consolekit"
PACKAGECONFIG[gtkdoc] = "-Dgtkdoc=true,-Dgtkdoc=false,gtkdoc-native"
PACKAGECONFIG[lvfs] = "-Dlvfs=true,-Dlvfs=false,"
PACKAGECONFIG[tpm2] = "-Dtpm=true,-Dtpm=false,tpm2-tss"
PACKAGECONFIG[efi] = "-Dplugin_uefi=true,-Dplugin_uefi=false,gnu-efi-native"

DEPENDS = "glib-2.0 libgudev libgusb libarchive efivar \
           libxmlb libjcat gcab-native gcab udisks2 cmake-native \
           libsoup-2.4 elfutils cairo cairo-native python3-pygobject-native \
            "
RDEPENDS:${PN} = "gnupg python3-core"

do_install:append() {
    if ${@bb.utils.contains('DISTRO_FEATURES', 'polkit', 'true', 'false', d)}; then
        if test -d ${D}/${datadir}/polkit-1/rules.d ; then
            # Fix up permissions on polkit rules.d to work with rpm4 constraints
            chmod 700 ${D}/${datadir}/polkit-1/rules.d
            chown polkitd:root ${D}/${datadir}/polkit-1/rules.d
        fi
    fi

    install -m 0644 ${WORKDIR}/remotes.d/* ${D}${sysconfdir}/fwupd/remotes.d/
}

USERADD_PACKAGES = "${PN}"
USERADD_PARAM:${PN} = "--system --no-create-home --user-group --home-dir ${sysconfdir}/${BPN}-1 polkitd"

inherit meson pkgconfig gettext python3native gobject-introspection
