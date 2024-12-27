SUMMARY = "Kontron CI adaptions"
HOMEPAGE = "http://www.kontron.com/"

PROVIDES = "kontron-ci"

DEFAULT_WALLPAPER ??= "kontron-background_warn.jpg"

LICENSE = "KONTRON-LOGO"
LIC_FILES_CHKSUM="file://LICENSE.KONTRON-LOGO;md5=8f34ef125aa25b4009d8d70d6af2a57f \
                "
SRC_URI = "file://backgrounds \
            file://skel \
            file://LICENSE.KONTRON-LOGO \
            "

S = "${WORKDIR}"

FILES:${PN} = "${datadir}/backgrounds/* \
        ${sysconfdir}/skel/.config \
        ${sysconfdir}/skel/.config/xfce4 \
        ${sysconfdir}/skel/.config/xfce4/helpers.rc \
        ${sysconfdir}/skel/.config/xfce4/xfconf \
        ${sysconfdir}/skel/.config/xfce4/xfconf/xfce-perchannel-xml \
        ${sysconfdir}/skel/.config/xfce4/xfconf/xfce-perchannel-xml/xfce4-desktop.xml \
        ${sysconfdir}/skel/.config/xfce4/xfconf/xfce-perchannel-xml/xfce4-keyboard-shortcuts.xml \
        ${sysconfdir}/skel/.config/xfce4/xfconf/xfce-perchannel-xml/xfce4-panel.xml \
        ${sysconfdir}/skel/.config/xfce4/panel \
        ${sysconfdir}/skel/.config/xfce4/panel/launcher-9 \
        ${sysconfdir}/skel/.config/xfce4/panel/launcher-9/terminal-emulator.desktop \
        ${sysconfdir}/skel/.config/xfce4/panel/launcher-10 \
        ${sysconfdir}/skel/.config/xfce4/panel/launcher-10/file-manager.desktop \
        ${sysconfdir}/skel/.config/xfce4/panel/launcher-11 \
        ${sysconfdir}/skel/.config/xfce4/panel/launcher-11/web-browser.desktop \
        ${sysconfdir}/skel/.config/xfce4/panel/cpugraph-18.rc \
        /home/root/.config \
        /home/root/.config/xfce4 \
        /home/root/.config/xfce4/helpers.rc \
        /home/root/.config/xfce4/xfconf \
        /home/root/.config/xfce4/xfconf/xfce-perchannel-xml \
        /home/root/.config/xfce4/xfconf/xfce-perchannel-xml/xfce4-desktop.xml \
        /home/root/.config/xfce4/xfconf/xfce-perchannel-xml/xfce4-keyboard-shortcuts.xml \
        /home/root/.config/xfce4/xfconf/xfce-perchannel-xml/xfce4-panel.xml \
        /home/root/.config/xfce4/panel \
        /home/root/.config/xfce4/panel/launcher-9 \
        /home/root/.config/xfce4/panel/launcher-9/terminal-emulator.desktop \
        /home/root/.config/xfce4/panel/launcher-10 \
        /home/root/.config/xfce4/panel/launcher-10/file-manager.desktop \
        /home/root/.config/xfce4/panel/launcher-11 \
        /home/root/.config/xfce4/panel/launcher-11/web-browser.desktop \
        /home/root/.config/xfce4/panel/cpugraph-18.rc \
        "

do_install () {
    install -d ${D}${datadir}/backgrounds
    install -m 0644 ${WORKDIR}/backgrounds/*.jpg ${D}/${datadir}/backgrounds/
    ln -sf ${DEFAULT_WALLPAPER} ${D}/${datadir}/backgrounds/kontron-background.jpg

    install -d ${D}${sysconfdir}/skel/.config/xfce4/
    install -m 0644 ${WORKDIR}/skel/helpers.rc ${D}${sysconfdir}/skel/.config/xfce4
    install -d ${D}${sysconfdir}/skel/.config/xfce4/panel/
    install -m 0644 ${WORKDIR}/skel/cpugraph-18.rc ${D}${sysconfdir}/skel/.config/xfce4/panel

    install -d ${D}/home/root/.config/xfce4/
    install -m 0644 ${WORKDIR}/skel/helpers.rc ${D}/home/root/.config/xfce4
    install -d ${D}/home/root/.config/xfce4/panel/
    install -m 0644 ${WORKDIR}/skel/cpugraph-18.rc ${D}/home/root/.config/xfce4/panel

    install -d ${D}${sysconfdir}/skel/.config/xfce4/panel/launcher-9
    install -m 0644 ${WORKDIR}/skel/terminal-emulator.desktop ${D}${sysconfdir}/skel/.config/xfce4/panel/launcher-9
    install -d ${D}${sysconfdir}/skel/.config/xfce4/panel/launcher-10
    install -m 0644 ${WORKDIR}/skel/file-manager.desktop ${D}${sysconfdir}/skel/.config/xfce4/panel/launcher-10
    install -d ${D}${sysconfdir}/skel/.config/xfce4/panel/launcher-11
    install -m 0644 ${WORKDIR}/skel/web-browser.desktop ${D}${sysconfdir}/skel/.config/xfce4/panel/launcher-11

    install -d ${D}/home/root/.config/xfce4/panel/launcher-9
    install -m 0644 ${WORKDIR}/skel/terminal-emulator.desktop ${D}/home/root/.config/xfce4/panel/launcher-9
    install -d ${D}/home/root/.config/xfce4/panel/launcher-10
    install -m 0644 ${WORKDIR}/skel/file-manager.desktop ${D}/home/root/.config/xfce4/panel/launcher-10
    install -d ${D}/home/root/.config/xfce4/panel/launcher-11
    install -m 0644 ${WORKDIR}/skel/web-browser.desktop ${D}/home/root/.config/xfce4/panel/launcher-11

    install -d ${D}${sysconfdir}/skel/.config/xfce4/xfconf/xfce-perchannel-xml
    install -m 0644 ${WORKDIR}/skel/xfce4-desktop.xml ${D}${sysconfdir}/skel/.config/xfce4/xfconf/xfce-perchannel-xml
    install -m 0644 ${WORKDIR}/skel/xfce4-keyboard-shortcuts.xml ${D}${sysconfdir}/skel/.config/xfce4/xfconf/xfce-perchannel-xml
    install -m 0644 ${WORKDIR}/skel/xfce4-panel.xml ${D}${sysconfdir}/skel/.config/xfce4/xfconf/xfce-perchannel-xml

    install -d ${D}/home/root/.config/xfce4/xfconf/xfce-perchannel-xml
    install -m 0644 ${WORKDIR}/skel/xfce4-desktop.xml ${D}/home/root/.config/xfce4/xfconf/xfce-perchannel-xml
    install -m 0644 ${WORKDIR}/skel/xfce4-keyboard-shortcuts.xml ${D}/home/root/.config/xfce4/xfconf/xfce-perchannel-xml
    install -m 0644 ${WORKDIR}/skel/xfce4-panel.xml ${D}/home/root/.config/xfce4/xfconf/xfce-perchannel-xml
}
