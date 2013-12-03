#!/usr/bin/python
import urllib
import os
import MySQLdb
import time

def download():
    logfile = open("/var/log/downloaddata.log","a")

    try:
        print time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
        logfile.write("\n" + time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())) + "\n")

        db = MySQLdb.connect(host="localhost",user="java",passwd="100200",db="shuangse")
        cursor = db.cursor()
        cursor.execute("SELECT max(itemid) from historydata")

        curmaxitemid = 2003001
        for row in cursor.fetchone():
            curmaxitemid = row

        print "current latest itemid in mysql is " + str(curmaxitemid)
        logfile.write("current latest itemid in mysql is " + str(curmaxitemid) + "\n")

        print "start to read remote data......"
        logfile.write("start to read remote data......\n")

        url = "http://www.17500.cn/getData/ssq.TXT"
        data = urllib.urlopen(url).read()

        lines=data.split("\n");

        cnt = 0
        for line in lines:
            #2013035 2013-03-28 01 14 15 17 26 30 02
            tmpitemid = line[0:7]
            if len(tmpitemid) > 0 and long(tmpitemid) > curmaxitemid:
                shortline = line[0:39]

                print "get one new item " + shortline + " that needs to be inserted"
                logfile.write("get one new item " + shortline + " that needs to be inserted\n")
                numbers = []
                numbers.append(int(tmpitemid))
                numbers.append(int(line[19:21]))
                numbers.append(int(line[22:24]))
                numbers.append(int(line[25:27]))
                numbers.append(int(line[28:30]))
                numbers.append(int(line[31:33]))
                numbers.append(int(line[34:36]))
                numbers.append(int(line[37:39]))
                print str(numbers) + " will be inserted"
                logfile.write(str(numbers) + " will be inserted\n")

                cursor.execute('insert into historydata values(%s,%s,%s,%s,%s,%s,%s,%s)', numbers)
                cnt += 1

        db.commit()
        cursor.close()
        db.close()

        if cnt > 0:
            print "Download and insert into mysql successfully " + str(cnt) + " records."
            logfile.write("Download and insert into mysql successfully" + str(cnt) + " records.\n")
        else:
            print "no data is updated"
            logfile.write("no data is updated\n")

        print time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
        logfile.write(time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())) + "\n")

    except Exception,data:
        print "Exception happens"
        logfile.write("Exception happends \n")
        print Exception,":",data
        logfile.write(str(Exception) + ":" + str(data) + "\n")

    logfile.close()
    exit(0)

if __name__ == '__main__' :
    download()
