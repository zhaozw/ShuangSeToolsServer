package com.shuangsetoolsserver.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.shuangsetoolsserver.base.BaseAction;
import com.shuangsetoolsserver.base.Log;
import com.shuangsetoolsserver.logic.SuggestionLogic;

public class SuggestionAction extends BaseAction {

    private static final long serialVersionUID = 1L;

    @Override
    protected void execute(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        
        response.setCharacterEncoding("utf-8");
        PrintWriter out = response.getWriter();
        String action = request.getParameter("Action");
        String contact = request.getParameter("ContactBack");
        String suggestion = request.getParameter("Suggestion");
        Log.i("SuggestionAction", "Suggestion Got:" + suggestion);

        if (("add".equalsIgnoreCase(action)) && (suggestion != null && suggestion.length() > 0)) {
                try {
                    SuggestionLogic suggestLogic = new SuggestionLogic();
                    String encodedStr = new String(suggestion.getBytes("ISO-8859-1"), "UTF-8");
                    Log.i("SuggestionAction", "Suggestion After Encode:" + encodedStr);
                    if (suggestLogic.insertSuggestion(contact, encodedStr)) {
                        Log.i("SuggestionAction", "insert suggestion successfully.");
                        out.print("OK");
                    } else {
                        response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                    }
                } catch (Exception e) {
                    Log.i("SuggestionAction", "Got a exception when insert suggestion into DB." + e.toString());
                    response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                }
            } else if("query".equalsIgnoreCase(action)) {
              try {
                SuggestionLogic suggestLogic = new SuggestionLogic();
                Vector<String> suggestionSet = suggestLogic.querySuggestion();
                ObjectMapper mapper = new ObjectMapper();
                
                 out.print(mapper.writeValueAsString(suggestionSet));
                
                 out.flush();
                 out.close();
              } catch (Exception e) {
                  Log.i("SuggestionAction", "Got a exception when query suggestion from database." + e.toString());
                  response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
               }
            } else {
                response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            }
    }
}
