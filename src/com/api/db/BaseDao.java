package com.api.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import android.content.Context;
import android.content.res.AssetManager;

import com.api.API.log.APILog;
import com.api.exception.ApplicationException;
import com.api.exception.DBException;

/**
 *
 */
public abstract class BaseDao {

    /**
     * コンテキスト
     */
    private Context context;
    /**
     * DBインスタンス
     */
    protected DBAdapter db;

    /**
     * コンストラクタ<br>
     * 
     * @param context Activity<br>
     * @param db DBAdapter<br>
     */
    public BaseDao(Context context) {

        APILog.d(this, "Create");

        this.context = context;
        this.db = new DBAdapter(context);
    }

    /**
     * SQLを取得<br>
     * 
     * @param sqlFileName SQLファイルパス sql/fileName.sql<br>
     * @return String SQL <br>
     * @throws DBException 取得できない場合<br>
     */
    public String getSql(String sqlFileName) {

        APILog.d(this, "getSql()", String.format("Start SqlFileName 【%s】", sqlFileName));

        InputStream is = null;
        BufferedReader br = null;
        StringBuilder sb = new StringBuilder();

        AssetManager as = context.getResources().getAssets();
        try {
            is = as.open(sqlFileName);
            br = new BufferedReader(new InputStreamReader(is));

            String str;
            while ((str = br.readLine()) != null) {
                str = str.replaceAll("\n", "");
                sb.append(str);
            }
        } catch (IOException e) {
            throw new ApplicationException("SQL取得に失敗しました");
        }

        APILog.d(this, "getSql()", "End");

        return sb.toString();
    }

    /**
     * DBオープン
     */
    public void dbOpen() {

        APILog.d(this, "dbOpen()");

        db.open();
    }

    /**
     * DBクローズ
     */
    public void dbClose() {

        APILog.d(this, "dbClose()");

        db.close();
    }

    public void beginTransaction() {

        APILog.d(this, "beginTransaction()");

        db.beginTransaction();
    }

    public void endTransaction() {

        APILog.d(this, "endTransaction()");

        db.endTransaction();
    }

    public void commit() {

        APILog.d(this, "setTransactionSuccessful()");

        db.commit();
    }

    /**************************************************************************************
     * Select モジュール<br>
     * 
     **************************************************************************************/
    /**
     * マスタ取得系<br>
     * 条件なし<br>
     */
    public ArrayList<String[]> selectAll(String sql) {
        // SQL文の実行
        return db.selectRecord(sql);
    }

}
