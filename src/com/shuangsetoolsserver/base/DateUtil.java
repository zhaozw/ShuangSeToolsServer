package com.shuangsetoolsserver.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtil {

    /**translate 2011-01-12 16:41:12 to Date*/
    public static Date parseDateFromStr(String str) throws ParseException {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt=sdf.parse(str);
        return dt;
    }
    /**translate Date object to String format: 2011-01-12 16:41:12*/
    public static String parseStringFromDate(Date date) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdf.format(date);
        return dateStr;        
    }

    /**translate Date object to String format: 2011-01-12*/
    public static String parseShortStringFromDate(Date date) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String dateStr = sdf.format(date);
        return dateStr;
    }

    
    /**translate Date object to String format: 20110112164112 */
    public static String parseCompactStringFromDate(Date date) {
        SimpleDateFormat sdf=new SimpleDateFormat("yyyyMMddHHmmss");
        String dateStr = sdf.format(date);
        return dateStr;        
    }    
}
