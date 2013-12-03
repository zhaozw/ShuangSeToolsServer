package com.shuangsetoolsserver.logic;

import java.sql.ResultSet;
import java.sql.SQLException;

import com.shuangsetoolsserver.base.Log;
import com.shuangsetoolsserver.db.DBUtil;
import com.shuangsetoolsserver.meta.ControlMsg;

public class BackgroundCheckLogic {
  private final static String TAG = "BackgroundCheckLogic";

  public ControlMsg getNewControlInfo() throws SQLException {
    ControlMsg controlMsg = null;

    String sql = "select * from controlmsg where validflag = 1";
    DBUtil db = new DBUtil();

    ResultSet result = db.read(sql);
    if (result != null && result.next()) {
      int id = result.getInt("Id");
      String infoType = result.getString("infoType");
      String text = result.getString("text");
      String url = result.getString("url");
      String version = result.getString("version");
      int validFlag = result.getInt("validflag");

      controlMsg = new ControlMsg();

      controlMsg.setId(id);
      controlMsg.setInfoType(infoType);
      controlMsg.setText(text);
      controlMsg.setUrl(url);
      controlMsg.setVersion(version);
      controlMsg.setValidFlag(validFlag);
    }
    db.release();

    return controlMsg;
  }

  public String getSMSText() {

    String smsText = null;
    try {
      String sql = "select * from smstext where validflag = '1'";
      DBUtil db = new DBUtil();
      ResultSet result = db.read(sql);
      if (result != null && result.next()) {
        //int id = result.getInt("Id");
        smsText = result.getString("smsText");
        //int validFlag = result.getInt("validflag");
      }
      db.release();
    } catch (Exception e) {
      Log.e(TAG, "Got exception when select * from smstext where validflag = '1'" + e.toString());
      return null;
    }

    return smsText;

  }

}
