# use more updated version of mmc-utils
SRCREV = "43282e80e174cc73b09b81a4d17cb3a7b4dc5cfc"
# remove unneeded patch from Yocto base
SRC_URI:remove = "file://0001-mmc_cmd.c-Use-extra-braces-when-initializing-subobje.patch"
