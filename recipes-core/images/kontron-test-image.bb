SUMMARY = "Kontron test image with basic X11 support"
inherit image-live
inherit core-image
inherit kontron-image

IMAGE_INSTALL:append = ' \
                       kernel-modules \
                       kontron-ci \
                       packagegroup-core-x11 \
                       packagegroup-xfce-base \
                       fixup-live-img \
                       '

COPY_LIC_MANIFEST = "1"

IMAGE_FSTYPES += "ext4 wic wic.bz2 wic.bmap"

IMAGE_FEATURES += "ssh-server-openssh splash x11-base tools-testapps x11"
IMAGE_FEATURES += "hwcodecs package-management"
IMAGE_FEATURES += "basetools testtools benchmarks systemtools xfce-addons tpm tpm2"
IMAGE_FEATURES += "verification"

# Packages needed for SW and kernel development/testing
EXTRA_IMAGE_FEATURES += "dev-pkgs tools-sdk tools-testapps development"
TOOLCHAIN_TARGET_TASK += "kernel-devsrc"

BAD_RECOMMENDATIONS="tpm2-abrmd"

IMAGE_ROOTFS_EXTRA_SPACE = "262144"

WKS_FILE = "${@bb.utils.contains('DEFAULTTUNE', 'amd64', 'image-installer-kontron-amd.wks.in', 'image-installer-kontron.wks.in', d)}"

IMAGE_TYPEDEP:wic = "ext4"
INITRD_IMAGE_LIVE="core-image-minimal-initramfs"
do_image_wic[depends] += "${INITRD_IMAGE_LIVE}:do_image_complete"
do_rootfs[depends] += "virtual/kernel:do_deploy"
IMAGE_BOOT_FILES:append = "\
      ${KERNEL_IMAGETYPE} \
      ${@bb.utils.contains('DEFAULTTUNE', 'amd64', '', 'microcode.cpio', d)} \
      ${IMGDEPLOYDIR}/${IMAGE_BASENAME}-${MACHINE}.ext4;rootfs.img \
      ${@bb.utils.contains('EFI_PROVIDER', 'grub-efi', 'grub-efi-bootx64.efi;EFI/BOOT/bootx64.efi', '', d)} \
      ${@bb.utils.contains('EFI_PROVIDER', 'grub-efi', '${IMAGE_ROOTFS}/boot/EFI/BOOT/grub.cfg;EFI/BOOT/grub.cfg', '', d)} \
      ${@bb.utils.contains('EFI_PROVIDER', 'systemd-boot', 'systemd-bootx64.efi;EFI/BOOT/bootx64.efi', '', d)} \
      ${@bb.utils.contains('EFI_PROVIDER', 'systemd-boot', '${IMAGE_ROOTFS}/boot/loader/loader.conf;loader/loader.conf ', '', d)} \
      ${@bb.utils.contains('EFI_PROVIDER', 'systemd-boot', '${IMAGE_ROOTFS}/boot/loader/entries/boot.conf;loader/entries/boot.conf', '', d)} "
