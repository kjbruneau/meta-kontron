SUMMARY = "gcab - A GObject library to create cabinet files"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://${S}/COPYING;md5=4fbd65380cdd255951079008b364516c"

SRC_URI = "git://gitlab.gnome.org/GNOME/gcab.git;protocol=https \
           "
SRCREV="e3f0c240eb1a1961f0ff61a83a2c7fafba95abb4"

EXTRA_OEMESON += "-Dintrospection=false -Ddocs=false -Dtests=false \
                  -Dinstalled_tests=false \
                 "

S = "${WORKDIR}/git"

DEPENDS = "glib-2.0 glib-2.0-native intltool-native \
          "
BBCLASSEXTEND = "native nativesdk"

inherit meson
