inherit allarch

PACKAGES = "${PN} ${PN}-doc"
FILES:${PN}-doc += "${docdir}/keapi_config/*"

RPROVIDES:${PN} = "keapi-config"
PROVIDES += "virtual/keapi-config"

do_install () {
    install -d ${D}/${sysconfdir}/keapi
    install -d ${D}/${docdir}/keapi_config
    install -d ${D}/${sysconfdir}/sensors.d

    # Install doc files
    install -m 0644 ${S}/usr/share/doc/keapi-config/AUTHORS ${D}/${docdir}/keapi_config/AUTHORS
    install -m 0644 ${S}/usr/share/doc/keapi-config/COPYING ${D}/${docdir}/keapi_config/COPYING
    install -m 0644 ${S}/usr/share/doc/keapi-config/CONFIGURATION_FILES_FORMAT ${D}/${docdir}/keapi_config/CONFIGURATION_FILES_FORMAT

    # Install keapi configuration files
    install -m 0644 ${S}/etc/keapi/sensors.conf ${D}/${sysconfdir}/keapi/sensors.conf
    install -m 0644 ${S}/etc/keapi/backlight.conf ${D}/${sysconfdir}/keapi/backlight.conf
    install -m 0644 ${S}/etc/keapi/gpio.conf ${D}/${sysconfdir}/keapi/gpio.conf
    install -m 0644 ${S}/etc/keapi/i2c.conf ${D}/${sysconfdir}/keapi/i2c.conf
    install -m 0644 ${S}/etc/keapi/smbus.conf ${D}/${sysconfdir}/keapi/smbus.conf
    install -m 0644 ${S}/etc/keapi/systemstates.conf ${D}/${sysconfdir}/keapi/systemstates.conf
    install -m 0644 ${S}/etc/keapi/watchdog.conf ${D}/${sysconfdir}/keapi/watchdog.conf
    install -m 0644 ${S}/etc/keapi/storage.conf ${D}/${sysconfdir}/keapi/storage.conf

    # Create link for lm-sensors
    ln -sf /etc/keapi/sensors.conf ${D}/${sysconfdir}/sensors.d/kontron-keapi.conf
}
