package com.shuangsetoolsserver.logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Vector;

import com.shuangsetoolsserver.base.DateUtil;
import com.shuangsetoolsserver.db.DBUtil;

public class SuggestionLogic {

    public boolean insertSuggestion(String contact, String suggestion) throws SQLException {
    
        String curTime = DateUtil.parseStringFromDate(new Date());
        StringBuffer insertSQL = new StringBuffer();
        insertSQL.append("insert suggestion (contactnum, suggestion, posttime, processed, valid)")
            .append("values('") 
            .append(contact).append("','")
            .append(suggestion).append("','")
            .append(curTime).append("', 0, 1)");
        
        DBUtil db = new DBUtil();
        db.execute(insertSQL.toString());
        db.release();

        return true;
    }
    
    public Vector<String> querySuggestion() throws SQLException {
      String querySQL = "select * from suggestion where valid = 1";
      DBUtil db = new DBUtil();
      ResultSet resultSet = db.read(querySQL);
      Vector<String> suggestionSet = new Vector<String>();
      
      while(resultSet != null && resultSet.next()) {
        StringBuffer sb = new StringBuffer();
        sb.append(resultSet.getString("contactnum") );
        sb.append(resultSet.getString("suggestion") );
        sb.append(resultSet.getString("posttime") );
        sb.append(resultSet.getInt("processed"));
        suggestionSet.add(sb.toString());
      }
      db.release();
      
      return suggestionSet;
    }
}
