#!/bin/bash
export WORKSPACEPATH=../../..
export PATHTOPROJECT=$WORKSPACEPATH/bos-web
export MAVENREPOPATH=/home/colin/.m2/repository
export BONITA_HOME="$PATHTOPROJECT/bonita-home/target/bonita-home-6.0.0-beta-021-SNAPSHOT/bonita"
#export BONITA_HOME="/home/colin/workspace/svn/bonita-web/bos-web/build/bonita-home/target/bonita-home-6.0-Beta-SNAPSHOT/bonita"
export GWT_MODULE=devBonitaConsoleFF

source $PATHTOPROJECT/console/console-war/common.sh
