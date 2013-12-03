package com.shuangsetoolsserver.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.shuangsetoolsserver.base.BaseAction;
import com.shuangsetoolsserver.base.Log;
import com.shuangsetoolsserver.logic.BackgroundCheckLogic;

public class GetSmsTextAction extends BaseAction {

  private static final long serialVersionUID = 767802952067769204L;
  private final static String TAG = "GetSmsTextAction";

  @Override
  protected void execute(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    response.setCharacterEncoding("utf-8");
    PrintWriter out = response.getWriter();
    
    try {
      BackgroundCheckLogic backgroundCheckLogic = new BackgroundCheckLogic();
      String smsText = backgroundCheckLogic.getSMSText();
      
      if (smsText != null) {
        
        Log.i(TAG, "Response SmsText:" + smsText);
        out.print(smsText);
        
      } else {
        response.sendError(HttpServletResponse.SC_NO_CONTENT);
      }
      out.flush();
    } catch (Exception e) {
      Log.i(TAG, e.toString());
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }

  }

}
