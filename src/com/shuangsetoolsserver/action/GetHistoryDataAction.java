package com.shuangsetoolsserver.action;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Vector;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shuangsetoolsserver.base.BaseAction;
import com.shuangsetoolsserver.base.Log;
import com.shuangsetoolsserver.logic.HisDataLogic;
import com.shuangsetoolsserver.meta.CodeItem;

public class GetHistoryDataAction extends BaseAction {
    private static final long serialVersionUID = 16897979797L;
    private static final String TAG = "GetHistoryDataAction";

    @Override
    protected void execute(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        PrintWriter out = response.getWriter();
        
        int startItemID = Integer.parseInt(request.getParameter("StartItemID"));
        String endItemIDStr = request.getParameter("EndItemID");
        int endItemID = 0;
        
        try {
            HisDataLogic hisDataLogic = new HisDataLogic();
            if (endItemIDStr == null || endItemIDStr.equalsIgnoreCase("now")) {
                // 获取最新的数据                
                endItemID = hisDataLogic.getLatestHisItemID();
            } else {
                endItemID = Integer.parseInt(endItemIDStr);
            }
            //如果要获取的数据已经是当前数据库中最新的数据了，表示目前无新数据更新
            if(startItemID >= endItemID) {
                StringBuffer sb = new StringBuffer();
                sb.append("GetHistoryDataAction: startItemID:")
                    .append(startItemID)
                    .append(" endItemID:")
                    .append(endItemID)
                    .append(" return NOMOREDATA");
                Log.i(TAG, sb.toString());
                
                out.write("NOMOREDATA");
                
            } else {
                Vector<CodeItem> hisData = hisDataLogic.getHisDataList(startItemID, endItemID);
                String hisDataStr = hisDataLogic.getXMLStr(hisData);
                
                //Log.i(TAG, "GetHistoryDataAction: return hisDataStr:" + hisDataStr);
                Log.i(TAG, "GetHistoryDataAction: returned " + hisData.size() + " items.");
                out.write(hisDataStr);
            }    
            
            out.flush();
        } catch (Exception e) {
            Log.w(TAG, e.toString());
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

    }

}
