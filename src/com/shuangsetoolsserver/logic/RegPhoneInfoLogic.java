package com.shuangsetoolsserver.logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.shuangsetoolsserver.base.DateUtil;
import com.shuangsetoolsserver.base.Log;
import com.shuangsetoolsserver.db.DBUtil;
import com.shuangsetoolsserver.meta.AndroidPhoneInfo;

public class RegPhoneInfoLogic {
  public boolean insertPhoneInfo(AndroidPhoneInfo phoneInfo) throws SQLException {
    
    String curTime = DateUtil.parseStringFromDate(new Date());

    StringBuffer insertSQL = new StringBuffer();
    insertSQL.append("insert userlist (deviceId, softVersion, msisdnMdn, ")
                      .append("networkOperator,networkOperatorName,networkType,phoneType,")
                      .append("simOperator,simOperatorName,simSerialNumber,subscriberId,regTime,loginCnt,lastLoginTime,apkVersion) ")
                      .append("values('")
                      .append(phoneInfo.getDeviceId()).append("','")
                      .append(phoneInfo.getSoftVersion()).append("','")
                      .append(phoneInfo.getMsisdnMdn()).append("','")
                      .append(phoneInfo.getNetworkOperator()).append("','")
                      .append(phoneInfo.getNetworkOperatorName()).append("','")
                      .append(phoneInfo.getNetworkType()).append("','")
                      .append(phoneInfo.getPhoneType()).append("','")
                      .append(phoneInfo.getSimOperator()).append("','")
                      .append(phoneInfo.getSimOperatorName()).append("','")
                      .append(phoneInfo.getSimSerialNumber()).append("','")
                      .append(phoneInfo.getSubscriberId()).append("','")
                      .append(curTime).append("','")
                      .append(1).append("','")
                      .append(curTime).append("','").append(phoneInfo.getApkVersion()).append("')");
    
    DBUtil db = new DBUtil();
    db.execute(insertSQL.toString());
    db.release();

    return true;
}

  public void updateUseCntByPhoneInfo(AndroidPhoneInfo phoneInfo) throws SQLException {
    StringBuffer querySQL = new StringBuffer();
    querySQL.append("select Id, loginCnt from userlist where deviceId='").append(phoneInfo.getDeviceId()).append("' and ")
                      .append("simSerialNumber='").append(phoneInfo.getSimSerialNumber()).append("' and ")
                      .append("subscriberId='").append(phoneInfo.getSubscriberId()).append("'");
    DBUtil db = new DBUtil();
    ResultSet rs = db.read(querySQL.toString());
    
    if (rs != null && rs.next()) {
      
      int itemId = rs.getInt("Id");
      int oldCnt = rs.getInt("loginCnt");
      int newCnt = oldCnt + 1;
      String curTime = DateUtil.parseStringFromDate(new Date());
      String updateStr = "update userlist set loginCnt=" + newCnt + ",lastLoginTime='" + curTime + "',apkVersion='" + phoneInfo.getApkVersion() + "' where Id=" + itemId;
      
      db.beginTransaction();
      
      db.execute(updateStr);

      db.commitTransaction();
    } else {
      Log.w("updateUseCntByPhoneInfo", "\n" + phoneInfo.toString() + " \n doesn't exist while to update its login cnt. insert it now\n");
      this.insertPhoneInfo(phoneInfo);
    }
    
    db.release();
  }
}
