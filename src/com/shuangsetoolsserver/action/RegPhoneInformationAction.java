package com.shuangsetoolsserver.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.shuangsetoolsserver.base.BaseAction;
import com.shuangsetoolsserver.base.Log;
import com.shuangsetoolsserver.logic.RegPhoneInfoLogic;
import com.shuangsetoolsserver.meta.AndroidPhoneInfo;

public class RegPhoneInformationAction extends BaseAction {
  private static final long serialVersionUID = 1153440341608471250L;
  private final static String TAG = "RegPhoneInformationAction";

  @Override
  protected void execute(HttpServletRequest request,
      HttpServletResponse response) throws ServletException, IOException {
    try {
      PrintWriter out = response.getWriter();
      String action = request.getParameter("Action");
      
      String deviceId = request.getParameter(AndroidPhoneInfo.DEVICEID);
      Log.i(TAG, "deviceId:" + deviceId);
      
      String msisdnMdn = request.getParameter(AndroidPhoneInfo.MSISDNMDN);
      Log.i(TAG, "msisdnMdn:" + msisdnMdn);
      String networkOperator = request.getParameter(AndroidPhoneInfo.NETWORKOPERATOR);
      Log.i(TAG, "networkOperator:" + networkOperator);
      
      String origNetworkOperatorName = request.getParameter(AndroidPhoneInfo.NETWORKOPERATORNAME);
      Log.i(TAG, "origNetworkOperatorName:" + origNetworkOperatorName);
      String networkOperatorName = new String(origNetworkOperatorName.getBytes("ISO-8859-1"), "UTF-8");
      Log.i(TAG, "networkOperatorName:" + networkOperatorName);
      
      String networkType = request.getParameter(AndroidPhoneInfo.NETWORKTYPE);
      Log.i(TAG, "networkType:" + networkType);
      
      String phoneType = request.getParameter(AndroidPhoneInfo.PHONETYPE);
      Log.i(TAG, "phoneType:" + phoneType);
      
      String simOperator = request.getParameter(AndroidPhoneInfo.SIMOPERATOR);
      Log.i(TAG, "simOperator:" + simOperator);
      String origSimOperatorName = request.getParameter(AndroidPhoneInfo.SIMOPERATORNAME);
      Log.i(TAG, "simOperatorName:" + origSimOperatorName);
      String simOperatorName = new String(origSimOperatorName.getBytes("ISO-8859-1"), "UTF-8");
      Log.i(TAG, "simOperatorName:" + simOperatorName);
      
      String simSerialNumber = request.getParameter(AndroidPhoneInfo.SIMSERIALNUMBER);
      Log.i(TAG, "simSerialNumber:" + simSerialNumber);
      String softVersion = request.getParameter(AndroidPhoneInfo.SOFTVERSION);
      Log.i(TAG, "softVersion:" + softVersion);
      String subscriberId = request.getParameter(AndroidPhoneInfo.SUBSCRIBERID);
      Log.i(TAG, "subscriberId:" + subscriberId);
      
      String apkVersion = request.getParameter(AndroidPhoneInfo.APKVERSION);
      Log.i(TAG, "apkVersion:" + apkVersion);
      
      AndroidPhoneInfo phoneInfo = new AndroidPhoneInfo();
      
      phoneInfo.setDeviceId(deviceId);//String  
      phoneInfo.setSoftVersion(softVersion);//String 
      phoneInfo.setMsisdnMdn(msisdnMdn);//String  
      phoneInfo.setNetworkOperator(networkOperator);//String   
      phoneInfo.setNetworkOperatorName(networkOperatorName);//String
      phoneInfo.setNetworkType(Integer.parseInt(networkType));//int
      phoneInfo.setPhoneType(Integer.parseInt(phoneType));//int
      phoneInfo.setSimOperator(simOperator);//String 
      phoneInfo.setSimOperatorName(simOperatorName);//String
      phoneInfo.setSimSerialNumber(simSerialNumber);//String
      phoneInfo.setSubscriberId(subscriberId);//String
      phoneInfo.setApkVersion(apkVersion);
      
      RegPhoneInfoLogic regLogic = new RegPhoneInfoLogic();
      
      if(action != null && action.equals("Update")) {
        
        regLogic.updateUseCntByPhoneInfo(phoneInfo);
        
      } else {//action is null for older version APK or action is Reg
        if(regLogic.insertPhoneInfo(phoneInfo)) {
          out.write("RegSuccess");
        } else {
          Log.w(TAG, "insertPhoneInfo failed.");
          response.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }

      }
            
      out.flush();
    } catch (Exception e) {
      Log.w(TAG, e.toString());
      response.sendError(HttpServletResponse.SC_BAD_REQUEST);
    }
  }
  
}
