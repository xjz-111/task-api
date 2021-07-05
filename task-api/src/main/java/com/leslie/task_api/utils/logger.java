package com.leslie.task_api.utils;

/**
 * 作者：xjzhao
 * 时间：2021-07-01 19:26
 */
public class logger {

    private static ILogger instance = new DefaultLogger();

    public static void showLog(boolean isShowLog) {
        instance.showLog(isShowLog);
    }

    public static  void showStackTrace(boolean isShowStackTrace) {
        instance.showStackTrace(isShowStackTrace);
    }

    public static  void debug(String message) {
        instance.debug(null, message);
    }

    public static  void info(String message) {
        instance.info(null, message);
    }

    public static  void warning(String message) {
        instance.warning(null, message);
    }

    public static  void error(String message) {
        instance.error(null, message);
    }

    public static  void monitor(String message) {
        instance.monitor(message);
    }

    public static  boolean isMonitorMode() {
        return instance.isMonitorMode();
    }

    public static  String getDefaultTag() {
        return instance.getDefaultTag();
    }

}
