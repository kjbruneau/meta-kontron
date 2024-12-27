SUMMARY = "libjcat - handle gzipped JSON catalog files"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=1803fa9c2c3ce8cb06b4861d75310742"

SRC_URI = "git://github.com/hughsie/libjcat.git;protocol=https \
           "
SRCREV="2278866c5ccd9f7d37290af92c04a4b6b2bd7065"

EXTRA_OEMESON += "-Dintrospection=false -Dgtkdoc=false -Dtests=false \
                  -Dman=false \
                 "

S = "${WORKDIR}/git"

DEPENDS = "glib-2.0 glib-2.0-native json-glib cmake-native prelink-native \
           gnutls gpgme \
          "

inherit meson
