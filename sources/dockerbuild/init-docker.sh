#!/bin/bash
###########################################################################
# Copyright (C) 2017-2018 Kontron Europe GmbH
#
# Permission is hereby granted, free of charge, to any person obtaining a
# copy of this software and associated documentation files (the
# "Software"), to deal in the Software without restriction, including
# without limitation the rights to use, copy, modify, merge, publish,
# distribute, distribute with modifications, sublicense, and/or sell
# copies of the Software, and to permit persons to whom the Software is
# furnished to do so, subject to the following conditions:
#
# The above copyright notice and this permission notice shall be included
# in all copies or substantial portions of the Software.
#
# THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS
# OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
# MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
# IN NO EVENT SHALL THE ABOVE COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
# DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR
# OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR
# THE USE OR OTHER DEALINGS IN THE SOFTWARE.
#
# Except as contained in this notice, the name(s) of the above copyright
# holders shall not be used in advertising or otherwise to promote the
# sale, use or other dealings in this Software without prior written
# authorization.
###########################################################################


. project.conf

if [ -z $PROJECT ] ; then
    PROJECT=default
    echo "No project name configured - using default: $PROJECT"
    echo "Please add a line PROJECT=\$PROJECTNAME\$ to project.conf"
    echo
fi

IMAGE_TAG=$PROJECT

MOUNTS=

if [ -z $LOCAL_BUILD_DIR ] ; then
    echo "No local build directory is configured - not using feature"
    echo "Please add a line LOCAL_BUILD_DIR=\$DIRECTORY\$ to project.conf"
    echo
else
    MOUNTS="$MOUNTS -v $LOCAL_BUILD_DIR:/home/$DEVUSER/builddirs:delegated"
fi

if [ -z $LOCAL_PROJECT_DIR ] ; then
    echo "No local project storage is configured! Are you sure?"
    echo "Please add a line LOCAL_PROJECT_DIR=\$DIRECTORY\$ to project.conf"
    echo
else
    MOUNTS="$MOUNTS -v $LOCAL_PROJECT_DIR:/home/$DEVUSER/project:consistent"
fi

if [ -z $LOCAL_CCACHE_DIR ] ; then
    echo "No local ccache storage is configured!"
    echo "If available, please add a line LOCAL_CCACHE_DIR=\$DIRECTORY\$ to project.conf"
    echo
else
    CCACHE_DIR=/ccache
    MOUNTS="$MOUNTS -v $LOCAL_CCACHE_DIR:$CCACHE_DIR:cached -e CCACHE_DIR=$CCACHE_DIR"
fi

mkdir -p dockerfiles/
if [ -z $SSH_AUTHORIZED_KEYS ] ; then
    echo "Key based SSH authentication will be disabled!"
    echo "Please add a line SSH_AUTHORIZED_KEYS=\$KEYFILE\$ to project.conf if"
    echo "you want to enable it."
    echo

    rm -f dockerfiles/authorized_keys
else
    if [ -e $SSH_AUTHORIZED_KEYS ] ; then
        cp $SSH_AUTHORIZED_KEYS dockerfiles/authorized_keys
    else
        echo "Authorized SSH keys file not found, no keys will be added!"
        echo
    fi
fi
touch dockerfiles/authorized_keys

echo "Stopping existing container $PROJECT"
docker stop $PROJECT
echo "Removing existing container $PROJECT"
docker rm $PROJECT
echo "Building image with tag $IMAGE_TAG"
docker build -t $IMAGE_TAG .
echo "Removing dangling images"
UNTAGGED="`docker images -f "dangling=true" -q`"
if [ -n "$UNTAGGED" ] ; then
    # Cleanup a bit...
    docker rmi $UNTAGGED
fi
echo "Running docker container $IMAGE_TAG"
docker run -t -i --name $PROJECT $MOUNTS $IMAGE_TAG
