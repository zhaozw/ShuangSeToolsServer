package com.shuangsetoolsserver.logic;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.shuangsetoolsserver.db.DBUtil;
import com.shuangsetoolsserver.meta.ExperienceItem;

public class SharedExperienceLogic {

  public List<ExperienceItem> getSharedExperience(int pageSize, int pageNum) throws SQLException {
    int offset = pageSize * (pageNum - 1);
    
    String querySQL = "select Id, title from experienceshare limit " + offset + "," + pageSize;
    
    DBUtil db = new DBUtil();
    ResultSet resultSet = db.read(querySQL);
    List<ExperienceItem> expList = new ArrayList<ExperienceItem>();

    while (resultSet != null && resultSet.next()) {
      
      ExperienceItem e = new ExperienceItem();
      e.setId(resultSet.getInt("Id"));
      e.setTitle(resultSet.getString("title"));

      expList.add(e);
    }
    db.release();

    return expList;
  }

  public ExperienceItem getExperienceDetail(int itemId) throws SQLException {
    String querySQL = "select * from experienceshare where Id = " + itemId;
    
    DBUtil db = new DBUtil();
    ResultSet resultSet = db.read(querySQL);
    ExperienceItem e = null;
    
    if (resultSet != null && resultSet.next()) {
      e = new ExperienceItem();
      e.setId(resultSet.getInt("Id"));
      e.setTitle(resultSet.getString("title"));
      e.setHtmlText(resultSet.getString("htmlText"));
    }
    
    db.release();

    return e;
  }
}
