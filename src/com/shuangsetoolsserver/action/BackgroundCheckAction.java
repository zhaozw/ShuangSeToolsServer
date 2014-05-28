package com.shuangsetoolsserver.action;

import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shuangsetoolsserver.base.BaseAction;
import com.shuangsetoolsserver.base.Log;
import com.shuangsetoolsserver.logic.BackgroundCheckLogic;
import com.shuangsetoolsserver.meta.ControlMsg;

public class BackgroundCheckAction extends BaseAction {
  private static final long serialVersionUID = 18888888L;
  private static final String TAG = "BackgroundCheckAction";

  @Override
  protected void execute(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    response.setCharacterEncoding("utf-8");
    PrintWriter out = response.getWriter();
    
    try {
      BackgroundCheckLogic backgroundCheckLogic = new BackgroundCheckLogic();
      ControlMsg controlMsg = backgroundCheckLogic.getNewControlInfo();
      
      if (controlMsg != null) {
        Log.i(TAG, "Response ControlMsg:" + controlMsg.toString());
        
        ObjectMapper mapper = new ObjectMapper();
        out.print(mapper.writeValueAsString(controlMsg));
      } else {
        response.sendError(HttpServletResponse.SC_NO_CONTENT);
      }
      
      out.flush();
    } catch (Exception e) {
      Log.w(TAG, e.toString());
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

}
