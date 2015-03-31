package com.api.exception;

import java.lang.Thread.UncaughtExceptionHandler;

import android.content.Context;

public class APIUncaughtExeptionHandler implements UncaughtExceptionHandler {

    private static Context sContext = null;
    private static final String BUG_FILE = "BugReport";
    private static final UncaughtExceptionHandler sDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();

    /**
     * コンストラクタ
     * 
     * @param context
     */
    public APIUncaughtExeptionHandler(Context context) {
        sContext = context;
    }

    /**
     * キャッチされない例外によって指定されたスレッドが終了したときに呼び出されます
     */
    public void uncaughtException(Thread thread, Throwable ex) {

        // try {
        // if (ex instanceof RuntimeException) {
        // // TODO
        // Log.e("RuntimeException", ex.getMessage());
        // Log.e("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa",
        // ex.toString());
        // }
        //
        // } catch (Exception e) {
        // e.printStackTrace();
        // }
        // android.os.Process.killProcess(android.os.Process.myPid());
        sDefaultHandler.uncaughtException(thread, ex);
    }
}
