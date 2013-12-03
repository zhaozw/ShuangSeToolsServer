package com.shuangsetoolsserver.logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import com.shuangsetoolsserver.base.DateUtil;
import com.shuangsetoolsserver.db.DBUtil;
import com.shuangsetoolsserver.meta.UserCounterObj;

public class UserCounterLogic {

  public UserCounterObj getUserCounterObj() {
    UserCounterObj userCounterObj = null;

    String curDate = DateUtil.parseShortStringFromDate(new Date());
    String startTime = curDate + " 00:00:00";
    String endTime = curDate + " 23:59:59";

    String sql1 = "select count(*) from userlist where lastLoginTime > '"
        + startTime + "' and lastLoginTime < '" + endTime + "'";
    String sql2 = "select count(*) from userlist where regTime > '" + startTime
        + "' and regTime < '" + endTime + "'";
    String sql3 = "select count(*) from userlist";
    
    DBUtil db;
    try {
      db = new DBUtil();
      userCounterObj = new UserCounterObj();

      ResultSet result = db.read(sql1);
      if (result != null && result.next()) {
        userCounterObj.setTodayAccess(result.getInt(1));
      }
      
      ResultSet result2 = db.read(sql2);
      if (result2 != null && result2.next()) {
        userCounterObj.setTodayReg(result2.getInt(1));
      }
      
      ResultSet result3 = db.read(sql3);
      if (result3 != null && result3.next()) {
        userCounterObj.setTotalNumber(result3.getInt(1));
      }
      
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }

    if(db != null) {
      db.release();
    }

    return userCounterObj;
  }

}
