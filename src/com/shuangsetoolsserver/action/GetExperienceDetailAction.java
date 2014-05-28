package com.shuangsetoolsserver.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shuangsetoolsserver.base.BaseAction;
import com.shuangsetoolsserver.base.Log;
import com.shuangsetoolsserver.logic.SharedExperienceLogic;
import com.shuangsetoolsserver.meta.ExperienceItem;

public class GetExperienceDetailAction extends BaseAction {

  private static final long serialVersionUID = 6163374054154778727L;
  private final static String TAG = "GetExperienceDetailAction";

  @Override
  protected void execute(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    response.setCharacterEncoding("utf-8");
    PrintWriter out = response.getWriter();
    
    try {
      int itemId = Integer.parseInt(request.getParameter("itemId"));
      Log.i(TAG, "itemID:" + itemId);
      SharedExperienceLogic expLogic = new SharedExperienceLogic();
      ExperienceItem expItem= expLogic.getExperienceDetail(itemId);
      
      if (expItem != null) {
        
        Log.i(TAG, "Response experience details: :" + expItem.getHtmlText());
        
        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(out, expItem);
        
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
