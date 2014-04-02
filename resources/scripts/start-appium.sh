#!/bin/sh
export ANDROID_HOME=$1
export ADB_PATH=$2
export ADB_TOOLS=$3
export ADB_BUILD=$4
export PATH=$PATH:$ANDROID_HOME
export PATH=$PATH:$ADB_PATH
export PATH=$PATH:$ADB_TOOLS
export PATH=$PATH:$ADB_BUILD
$5 -a $6 -p $7 --avd $8