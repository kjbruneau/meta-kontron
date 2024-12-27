#!/bin/sh
MESSAGE_A="The kontron-test-image is for product evaluation and testing!\n\n \
It does not incorporate any security features and is accessible through \
network, graphical interface and serial console as root without password."
MESSAGE_B="Never use this image in an untrusted environment!"
PLAIN_IMAGE=backgrounds/kontron-background_raw.jpg
MOD_IMAGE=backgrounds/kontron-background_warn.jpg

echo "Updating wallpapers..."

if command -v convert > /dev/null ; then
    convert -background '#0001' -gravity center -fill black -font Helvetica -size 600x130 \
        caption:"$MESSAGE_A" $PLAIN_IMAGE +swap -gravity north -geometry +0+140 -composite \
        $MOD_IMAGE
    convert -background '#0001' -gravity center -fill red -font Helvetica -size 600x40 \
        caption:"$MESSAGE_B" $MOD_IMAGE +swap -gravity north -geometry +0+270 -composite \
        $MOD_IMAGE
else
    echo "Imagemagick is needed to update the wallpapers"
    exit -1
fi

echo "done"
