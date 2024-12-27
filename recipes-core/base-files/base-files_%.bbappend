do_install:append () {
    echo "############################################################################" >> ${D}${sysconfdir}/motd
    echo "# WARNING: The kontron-test-image is for product evaluation and testing!   #" >> ${D}${sysconfdir}/motd
    echo "#          It does not incorporate any security features and is accessible #" >> ${D}${sysconfdir}/motd
    echo "#          through network, graphical interface and serial console as root #" >> ${D}${sysconfdir}/motd
    echo "#          without password.                                               #" >> ${D}${sysconfdir}/motd
    echo "#          Never use this image in an untrusted environment!               #" >> ${D}${sysconfdir}/motd
    echo "############################################################################" >> ${D}${sysconfdir}/motd
}
