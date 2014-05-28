#!/bin/sh
export JAVA_HOME=/opt/jdk1.7.0_05
export JRE_HOME=/opt/jdk1.7.0_05/jre
export TOMCAT_HOME=/opt/apache_tomcat_7.0.28
export CATALINA_HOME=$TOMCAT_HOME

log_file=/var/log/tomcat_state.log
flag=1

nowtime=`date`
echo "start to observer the tomcat at $nowtime" >> $log_file
while [ $flag -gt 0 ]; do
    count=`/bin/ps -ef|grep Bootstrap|grep -v "grep" | wc -l`
    nowtime=`date`

    if [ $count -gt 0 ]
    then
        echo -n "." >> $log_file
    else
       echo "$nowtime" >> $log_file
       echo "[observer]:tomcat is not started yet, start it now" >> $log_file
       /opt/apache_tomcat_7.0.28/bin/startup.sh >> $log_file
    fi

    sleep 10m
done

