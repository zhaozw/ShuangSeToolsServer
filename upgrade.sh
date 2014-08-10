#!/bin/bash
###############################################################
#   Usage: upgrade SQLFile WarFilename TargetDir
#       - SQLFile: MySQL DB Script for upgrade (shuangse.sql)
#       - WarFilename: Tomcat WAR filename
#       - TargetDir:   TargetDir that for installation
#                      (e.g. /opt/apache-tomcat7.0.4/webapps/)
#
#   
##############################################################
if [ ! -n "$1" ] ; then
  echo "Usage: upgrade.sh [SQLFile] [WarFilename] [TargetDir]"
  exit 1
fi

if [ ! -n "$2" ] ; then
  echo "Usage: upgrade.sh [SQLFile] [WarFilename] [TargetDir]"
  exit 1
fi

if [ ! -n "$3" ] ; then
  echo "Usage: upgrade.sh [SQLFile] [WarFilename] [TargetDir]"
  exit 1
fi

SQLFILE=$1
WARFILE=$2
WARDIR=$3
BACKUPDIR=/opt/backup
SHUT_TOMCAT="service tomcat stop"
START_TOMCAT="service tomcat start"

echo "upgrade database by executing $SQLFILE"
mysql shuangse -h localhost -u java -p -v < $SQLFILE

if [ ! -d $BACKUPDIR ]
then
  mkdir -p $BACKUPDIR > /dev/null 2>&1 && echo "Directory $BACKUPDIR created." ||  echo "Error: Failed to create $BACKUPDIR directory."
fi

currenttime=`date +%Y-%m-%d_%H-%M-%S`
$SHUT_TOMCAT

warslist=($(ls $WARDIR/*.war))
for war in ${warslist[@]};do
    echo "backup $war"
    basename=$(basename $war)
    realpath=$(readlink -f $war)
    echo "cp $realpath $BACKUPDIR/$basename.bak.$currenttime"
    cp $realpath $BACKUPDIR/$basename.bak.$currenttime
    dirname=$(basename $war | awk -F "." '{print$1}')
    echo "rm -rf $realpath"
    rm -rf $realpath
    echo "rm -rf $WARDIR$dirname"
    rm -rf $WARDIR$dirname
done
echo "copy $WARFILE to $WARDIR ..."
cp $WARFILE $WARDIR

$START_TOMCAT

