package com.shuangsetoolsserver.base;

public class Log {

    public final static int  DEFAULT_DEBUG_LVL      = 0x00011111;
    private final static int DEBUG_LEVEL_INFO       = 0x00000001;
    private final static int DEBUG_LEVEL_DEBUG      = 0x00000010;
    private final static int DEBUG_LEVEL_VERBOSE    = 0x00000100;
    private final static int DEBUG_LEVEL_WARN       = 0x00001000;
    private final static int DEBUG_LEVEL_ERROR      = 0x00010000;
    
    //Information level 1
    public static void i(String tag, String info) {
        if((getDebugLevel() & DEBUG_LEVEL_INFO)  > 0) {
            System.out.println(tag + "::" + info);
        }
    }
    //Debug level 2
    public static void d(String tag, String info) {
        if((getDebugLevel() & DEBUG_LEVEL_DEBUG) > 0) {
            System.out.println(tag + "::" + info);
        }        
    }
    //Verbose level 3
    public static void v(String tag, String info) {
        if((getDebugLevel() & DEBUG_LEVEL_VERBOSE) > 0) {
            System.out.println(tag + "::" + info);
        }        
    }
    
    //Warning level 4
    public static void w(String tag, String info) {
        if((getDebugLevel() & DEBUG_LEVEL_WARN) > 0) {
            System.out.println(tag + "::" + info);
        }        
    }
    //Error level 5
    public static void e(String tag, String info) {
        if((getDebugLevel() & DEBUG_LEVEL_ERROR) > 0) {
            System.out.println(tag + "::" + info);
        }
    }
    
    public static int getDebugLevel() {
      return DEFAULT_DEBUG_LVL;
    }
}
