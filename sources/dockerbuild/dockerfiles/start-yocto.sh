#!/bin/bash

. /project.conf

if [ -d /ccache ] ; then 
    export CCACHE_DIR=/ccache
fi

cd /home/$DEVUSER/project/$POKYDIR

if [ ! -d "$BUILDDIR/conf" ] && [ -n "$TEMPLATE" ] ; then
    echo "Creating new configuration from template ($TEMPLATE)"
    TEMPLATECONF=$TEMPLATE . oe-init-build-env $BUILDDIR
else
    echo "Using existing/default configuration"
    . oe-init-build-env $BUILDDIR
fi

if [ "$START_TOASTER" -eq "1" ] ; then
    echo "Starting yocto Toaster"
    source toaster start webport=0.0.0.0:8000
fi
