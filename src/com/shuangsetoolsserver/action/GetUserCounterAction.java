package com.shuangsetoolsserver.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shuangsetoolsserver.base.BaseAction;
import com.shuangsetoolsserver.logic.UserCounterLogic;
import com.shuangsetoolsserver.meta.UserCounterObj;

public class GetUserCounterAction extends BaseAction {
  private static final long serialVersionUID = 8970809235317459674L;

  @Override
  protected void execute(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    response.setCharacterEncoding("utf-8");
    PrintWriter out = response.getWriter();
    
    UserCounterLogic userLogic = new UserCounterLogic();
    UserCounterObj userObj = userLogic.getUserCounterObj();
    
    if (userObj != null) {
      ObjectMapper mapper = new ObjectMapper();
      mapper.writeValue(out, userObj);
    } else {
      response.sendError(HttpServletResponse.SC_NO_CONTENT);
    }
    
  }

}
