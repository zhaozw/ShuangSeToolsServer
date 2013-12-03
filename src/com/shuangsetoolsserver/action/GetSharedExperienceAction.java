package com.shuangsetoolsserver.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.jdbc.StringUtils;
import com.shuangsetoolsserver.base.BaseAction;
import com.shuangsetoolsserver.base.Log;
import com.shuangsetoolsserver.logic.SharedExperienceLogic;
import com.shuangsetoolsserver.meta.ExperienceItem;

public class GetSharedExperienceAction extends BaseAction {

  private static final long serialVersionUID = -7721512798238284500L;

  @Override
  protected void execute(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    response.setCharacterEncoding("utf-8");
    PrintWriter out = response.getWriter();
    
    try {
      String pageSizeStr = request.getParameter("pageSize");
      String pageNumStr = request.getParameter("pageNum");
      if(StringUtils.isNullOrEmpty(pageSizeStr) || StringUtils.isNullOrEmpty(pageNumStr)) {
        response.sendError(HttpServletResponse.SC_BAD_REQUEST);
      } else {
        int pageSize = Integer.parseInt(pageSizeStr);
        int pageNum = Integer.parseInt(pageNumStr);
        
        SharedExperienceLogic expLogic = new SharedExperienceLogic();
        List<ExperienceItem> expList = expLogic.getSharedExperience(pageSize, pageNum);
        
        if (expList != null && expList.size() > 0) {
          Log.i("GetSharedExperience","List Size:" + expList.size());
          ObjectMapper mapper = new ObjectMapper();
          mapper.writeValue(out, expList);
          
        } else {
          response.sendError(HttpServletResponse.SC_NO_CONTENT);
        }
        
        out.flush();
      }
      
    } catch (Exception e) {
      Log.w("GetSharedExperience", e.toString());
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
  }

}
