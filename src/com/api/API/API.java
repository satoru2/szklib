package com.api.API;

import android.content.Context;

import com.api.API.log.APILog;
import com.api.application.font.Fonts;
import com.api.exception.APIUncaughtExeptionHandler;
import com.api.util.properties.AssetsPropertyReader;

/**
 * このAPIの情報を持ったクラス
 */
public class API {

    private static APIBean _bean = new APIBean();

    private API() {
    }

    public static APIBean getBean() {
        return _bean;
    }

    /**
     * APIを初期化する<br>
     * アプリケーションを利用する際に必ず１度だけ呼ぶ<br>
     * 
     * @param context
     */
    public static void Initialize(Context context) {
        _bean.setApplicationContext(context);
        _bean.setProperties(new AssetsPropertyReader(context.getApplicationContext()).getProperties("API.properties"));
        _bean.setDebug(Boolean.parseBoolean(_bean.getProperties().getProperty("DEBUG", "False")));
        _bean.setFonts(Fonts.parse(_bean.getProperties().getProperty("API_FONT", Fonts.NONE.toString())));
        if (Boolean.parseBoolean(_bean.getProperties().getProperty("EXCEPTION_HANDLE"))) {
            Thread.setDefaultUncaughtExceptionHandler(new APIUncaughtExeptionHandler(context));
        }

        APILog.d(API.class, "初期化完了");
    }
}
