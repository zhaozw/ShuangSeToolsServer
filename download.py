#!/usr/bin/python
#  execute every 5 minutes to update
#  crontab -e 
#  */5 * * * * python /root/download.py
#
import urllib
import os
import MySQLdb
import time

def download():
    #logfile = open("/var/log/downloaddata.log","a")

    try:
        print time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
        #logfile.write("\n" + time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())) + "\n")

        db = MySQLdb.connect(host="localhost",user="java",passwd="100200",db="shuangse")
        cursor = db.cursor()
        cursor.execute("SELECT max(itemid) from historydata")

        curmaxitemid = 2003001
        for row in cursor.fetchone():
            curmaxitemid = row

        print "current latest itemid in mysql is " + str(curmaxitemid)
        #logfile.write("current latest itemid in mysql is " + str(curmaxitemid) + "\n")

        print "start to read remote data......"
        #logfile.write("start to read remote data......\n")

        url = "http://www.17500.cn/getData/ssq.TXT"
        data = urllib.urlopen(url).read()

        lines=data.split("\n");

        cnt = 0
        updateCnt = 0;
        for line in lines:
            #2014089 2014-08-05 04 06 14 17 27 30 09 27 14 04 30 17 06 349996988 327508140 15 5545146 145 70493 2737 3000 117179 200 1813485 10 16166172 5
            tmpitemid = line[0:7]
            if len(tmpitemid) > 0 and long(tmpitemid) > curmaxitemid:
                items = line.split();

                print "get one new item " + tmpitemid + " that needs to be inserted"
                #logfile.write("get one new item " + tmpitemid + " that needs to be inserted\n")
                numbers = []
                numbers.append(int(tmpitemid))
                numbers.append(int(items[2]))
                numbers.append(int(items[3]))
                numbers.append(int(items[4]))
                numbers.append(int(items[5]))
                numbers.append(int(items[6]))
                numbers.append(int(items[7]))
                numbers.append(int(items[8]))
                numbers.append(items[1]);
                numbers.append(int(items[15]));
                numbers.append(int(items[16]));
                numbers.append(int(items[17]));
                numbers.append(int(items[18]));
                numbers.append(int(items[19]));
                numbers.append(int(items[20]));
                print str(numbers) + " will be inserted"
                #logfile.write(str(numbers) + " will be inserted\n")

                cursor.execute('insert into historydata values(%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s,%s)', numbers)
                cnt += 1
            elif (len(tmpitemid) > 0 and long(tmpitemid) == curmaxitemid):
                items = line.split();

                print "update one item " + tmpitemid + " that needs to be updated"
                #logfile.write("update one item " + tmpitemid + " that needs to be updated\n")
                numbers = []
                numbers.append(items[1]);
                numbers.append(int(items[15]));
                numbers.append(int(items[16]));
                numbers.append(int(items[17]));
                numbers.append(int(items[18]));
                numbers.append(int(items[19]));
                numbers.append(int(items[20]));
                numbers.append(int(tmpitemid))
                print str(numbers) + " will be updated"
                #logfile.write(str(numbers) + " will be updated\n")
                cursor.execute("update historydata set openDate=%s, totalSale=%s, poolTotal=%s, firstPrizeCnt=%s, firstPrizeValue=%s, secondPrizeCnt=%s, secondPrizeValue=%s where itemid=%s", numbers)
                updateCnt += 1

        db.commit()
        cursor.close()
        db.close()

        if cnt > 0:
            print "Download and insert into mysql successfully " + str(cnt) + " records."
            #logfile.write("Download and insert into mysql successfully" + str(cnt) + " records.\n")
        elif updateCnt > 0:
            print "update mysql successfully " + str(updateCnt) + " record(s)."
            #logfile.write("update mysql successfully" + str(updateCnt) + " record(s).\n")
        else:
            print "no data is updated"
            #logfile.write("no data is updated\n")

        print time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time()))
        #logfile.write(time.strftime('%Y-%m-%d %H:%M:%S',time.localtime(time.time())) + "\n")

    except Exception,data:
        print "Exception happens"
        #logfile.write("Exception happens \n")
        print Exception,":",data
        #logfile.write(str(Exception) + ":" + str(data) + "\n")

    #logfile.close()
    exit(0)

if __name__ == '__main__' :
    download()
