package com.api.API.log;

import android.util.Log;

import com.api.API.API;

public class APILog {
    private APILog() {
    }

    /**
     * Info
     */
    /**
     * メソッド名＋メッセージ
     * 
     * @param method
     * @param msg
     */
    public static void i(Object clazz, String method, String msg) {
        Log.i(clazz.getClass().getSimpleName(), method + " " + msg);
    }

    /**
     * メソッド名のみの表示
     * 
     * @param msg
     */
    public static void i(Object clazz, String msg) {
        Log.i(clazz.getClass().getSimpleName(), msg);
    }

    /**
     * Debug
     */
    /**
     * メソッド名＋メッセージ
     * 
     * @param method
     * @param msg
     */
    public static void d(Object clazz, String method, String msg) {
        if (API.getBean().isDebug()) {
            Log.d(clazz.getClass().getSimpleName(), method + " " + msg);
        }

    }

    /**
     * メッセージのみの表示
     * 
     * @param msg
     */
    public static void d(Object clazz, String msg) {
        if (API.getBean().isDebug()) {
            Log.d(clazz.getClass().getSimpleName(), msg);
        }
    }

}
