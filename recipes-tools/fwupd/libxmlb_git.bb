SUMMARY = "libxmlb - XML binary converter"
LICENSE = "LGPLv2.1"
LIC_FILES_CHKSUM = "file://${S}/LICENSE;md5=1803fa9c2c3ce8cb06b4861d75310742"

SRC_URI = "git://github.com/hughsie/libxmlb.git;protocol=https \
           "
SRCREV="3101baad938d63f26cda7e3296875adcde282f60"

EXTRA_OEMESON += "-Dintrospection=false -Dgtkdoc=false -Dtests=false"

S = "${WORKDIR}/git"

DEPENDS = "glib-2.0 json-glib \
          "

inherit meson
