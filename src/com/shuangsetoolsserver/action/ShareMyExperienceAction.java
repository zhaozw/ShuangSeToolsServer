package com.shuangsetoolsserver.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shuangsetoolsserver.base.BaseAction;
import com.shuangsetoolsserver.base.Log;
import com.shuangsetoolsserver.logic.SharedExperienceLogic;

public class ShareMyExperienceAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Override
    protected void execute(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("Action");
        String exprTitle = request.getParameter("exprTitle");
        String exprText = request.getParameter("exprText");
        String exprAuthor = request.getParameter("exprAuthor");
        String exprContact = request.getParameter("exprContact");
        
        Log.i("ShareMyExperienceAction", "ShareMyExperienceAction Got:" + exprTitle + exprText + exprAuthor + exprContact);

        if (("add".equalsIgnoreCase(action)) &&
            (exprTitle != null && exprTitle.length() > 0) && 
            (exprText != null && exprText.length() > 0) ) {
                try {
                    SharedExperienceLogic exprLogic = new SharedExperienceLogic();
                    String encodedExprTitleStr = new String(exprTitle.getBytes("ISO-8859-1"), "UTF-8");
                    String encodedExprTextStr = new String(exprText.getBytes("ISO-8859-1"), "UTF-8");
                    String encodedExprAuthorStr = new String(exprAuthor.getBytes("ISO-8859-1"), "UTF-8");
                    String encodedExprContactStr = new String(exprContact.getBytes("ISO-8859-1"), "UTF-8");
                    
                    if (exprLogic.insertExperience(encodedExprTitleStr, encodedExprTextStr,
                            encodedExprAuthorStr, encodedExprContactStr)) {
                        Log.i("ShareMyExperienceAction", "insert experience successfully.");
                        out.print("OK");
                    } else {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                } catch (Exception e) {
                    Log.i("ShareMyExperienceAction", "Got a exception when insert experience into DB." + e.toString());
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
    }
}
