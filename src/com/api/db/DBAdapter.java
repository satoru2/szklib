package com.api.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;

import com.api.API.API;
import com.api.API.log.APILog;
import com.api.exception.ApplicationException;
import com.api.exception.DBException;

public class DBAdapter {

    /**
     * Activity
     */
    protected Context context;

    /**
     * DB本体
     */
    protected DatabaseHelper dbHelper;

    /**
     * DB作成用
     */
    protected SQLiteDatabase db;

    /**
     * コンストラクタ<br>
     * 
     * @param context Activity<br>
     */
    public DBAdapter(Context context) {

        APILog.d(this, "Create");

        this.context = context;

        if (!DBInfo.isInitialized) {
            APILog.i(this, "DBInfo Initialized START");

            Properties prop = API.getBean().getProperties();
            DBInfo.DB_FILE_NAME = prop.getProperty("DB_FILE_NAME");
            DBInfo.DB_NAME = prop.getProperty("DB_NAME");
            DBInfo.DB_VERSION = Integer.parseInt(prop.getProperty("DB_VERSION"));
            DBInfo.isInitialized = true;

            APILog.i(this, String.format("DB_FILE_NAME = %s", DBInfo.DB_FILE_NAME));
            APILog.i(this, String.format("DB_NAME = %s", DBInfo.DB_NAME));
            APILog.i(this, String.format("DB_VERSION = %d", DBInfo.DB_VERSION));
            APILog.i(this, "DBInfo Initialized END");
        }
        dbHelper = new DatabaseHelper(this.context);
    }

    /**
     * DB本体
     * 
     */
    private static class DatabaseHelper extends SQLiteOpenHelper {
        /**
         * Context
         */
        private Context context;

        /**
         * DBPath
         */
        private File dbPath;

        /**
         * あせっつからのコピーをするか
         */
        private boolean createDatabase = false;

        public DatabaseHelper(Context context) {
            super(context, DBInfo.DB_FILE_NAME, null, DBInfo.DB_VERSION);

            APILog.d(this, "Construct");

            // TODO DBInfoの取得の仕方変えたらおもしろいかな
            // DB変更のたびDB_VERSION変えれば動く

            this.context = context;
            this.dbPath = context.getDatabasePath(DBInfo.DB_FILE_NAME);
        }

        @Override
        public synchronized SQLiteDatabase getReadableDatabase() {

            APILog.d(this, "getReadableDatabase()",
                    String.format("createDatabase = %s", createDatabase ? "True" : "False"));

            SQLiteDatabase database = super.getReadableDatabase();
            if (createDatabase) {
                try {
                    database = copyDatabase(database);
                } catch (IOException e) {
                    throw new ApplicationException("DB作成失敗");
                }
            }
            return database;
        }

        private SQLiteDatabase copyDatabase(SQLiteDatabase database) throws IOException {

            APILog.d(this, "copyDatabase()", String.format("createDatabase = %s", createDatabase ? "True" : "False"));

            // dbがひらきっぱなしなので、書き換えできるように閉じる
            database.close();

            // コピー！
            InputStream input = context.getAssets().open(DBInfo.DB_FILE_NAME);
            OutputStream output = new FileOutputStream(this.dbPath);
            copy(input, output);

            createDatabase = false;
            // dbを閉じたので、また開く
            return super.getWritableDatabase();
        }

        @Override
        public synchronized SQLiteDatabase getWritableDatabase() {

            APILog.d(this, "getWritableDatabase()",
                    String.format("createDatabase = %s", createDatabase ? "True" : "False"));

            SQLiteDatabase database = super.getWritableDatabase();
            if (createDatabase) {
                try {
                    database = copyDatabase(database);
                } catch (IOException e) {
                }
            }
            return database;
        }

        private static int copy(InputStream input, OutputStream output) throws IOException {

            APILog.d(DatabaseHelper.class, "copy()");
            // -----------/Debug>
            byte[] buffer = new byte[1024 * 4];
            int count = 0;
            int n = 0;
            while (-1 != (n = input.read(buffer))) {
                output.write(buffer, 0, n);
                count += n;
            }
            return count;
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            super.onOpen(db);

            APILog.d(this, "onCreate()", "Create Databases");

            this.createDatabase = true;
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // コンストラクタでバージョンが変わった時に呼ばれる　DB移行処理を書く
            // Dropboxからdbファイル取得して更新

            APILog.d(this, "onUpgrade()");

            onUpgrade(db, oldVersion, newVersion);
        }
    }

    /*
     * Adapter Methods
     */

    public DBAdapter open() {

        APILog.d(this, "open()");

        db = dbHelper.getWritableDatabase();
        return this;
    }

    public void close() {

        APILog.d(this, "close()");

        dbHelper.close();
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

        db.setTransactionSuccessful();
    }

    /**
     * 削除
     * 
     * @param tableName
     * @param where
     * @param whereArgs
     * @return
     * @throws SQLException
     */
    public int delete(String tableName, String where, String[] whereArgs) throws SQLException {
        return db.delete(tableName, where, whereArgs);
    }

    /**
     * 
     * @param tableName
     * @param columns
     * @param values
     * @return true 正常終了 false 異常終了
     */
    public boolean insert(String tableName, String[] columns, String[] values) throws SQLException {
        ContentValues val = new ContentValues();

        for (int i = 0; i < columns.length; i++) {
            val.put(columns[i], values[i]);
        }
        return db.insert(tableName, null, val) > 0;
    }

    /**
     * 
     * @param sql
     * @param arg
     * @return
     */
    public boolean insert(String sql, String... arg) throws DBException {

        SQLiteStatement stmt = db.compileStatement(sql);
        for (int i = 1; i <= arg.length; i++) {
            stmt.bindString(i, arg[i - 1]);
        }

        return 0 < stmt.executeInsert();
    }

    /**
     * @param table String テーブル名
     * @param columns String[] FIELD名
     * @param select String Where句
     * @param selectArg String[] Where句内の？に代入する
     * @param groupBy String GROUP BY句
     * @param having String HAVING句
     * @param orderBy String ORDERBY句
     * @return String[] selectするColumnが一つで結果が複数あるとき、または１つ以上の戻りが考えられるとき
     * @throws SQLException
     */
    public String[] selectMustiple(String table, String[] columns, String select, String[] selectArg, String groupBy,
            String having, String orderBy) {
        ArrayList<String> result = new ArrayList<String>();
        Cursor c = null;

        try {
            c = db.query(table, columns, select, selectArg, groupBy, having, orderBy);
            for (int i = 0; c.moveToNext(); i++) {
                result.add(c.getString(c.getColumnIndex(columns[0])));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            c.close();
        }

        return Arrays.asList(result.toArray()).toArray(new String[result.size()]);
    }

    /**
     * 
     * @param sql
     * @return String[] selectするColumnが一つで結果が複数あるとき、または１つ以上の戻りが考えられるとき
     */
    public String[] selectMustiple(String sql) throws SQLException {

        ArrayList<String> result = new ArrayList<String>();
        Cursor c = null;

        try {
            c = db.rawQuery(sql, null);
            for (int i = 0; c.moveToNext(); i++) {
                result.add(c.getString(0));
            }
        } catch (SQLException e) {
            throw new SQLException();
        } finally {
            c.close();
        }
        return Arrays.asList(result.toArray()).toArray(new String[result.size()]);
    }

    /**
     * @param table String テーブル名
     * @param columns String[] FIELD名
     * @param select String Where句
     * @param selectArg String[] Where句内の？に代入する
     * @param groupBy String GROUP BY句
     * @param having String HAVING句
     * @param orderBy String ORDERBY句
     * @return ArrayList<String[]> レコード単位で取得するとき
     * @throws DBException
     */
    public ArrayList<String[]> selectRecord(String table, String[] columns, String select, String[] selectArg,
            String groupBy, String having, String orderBy) throws DBException {

        ArrayList<String[]> result = new ArrayList<String[]>();

        try {
            Cursor c = db.query(table, columns, select, selectArg, groupBy, having, orderBy);
            // カーソルから取得
            while (c.moveToNext()) {
                // カラムの数配列を生成
                ArrayList<String> ary = new ArrayList<String>();
                // レコードを取得
                for (int i = 0; i < c.getColumnCount(); i++) {
                    // c.getColumnIndex(columns[i])
                    // を挟むとTable.Columnで指定した時にExceptionはく
                    ary.add(c.getString(i));
                }
                result.add((String[]) ary.toArray(new String[0]));
            }
            c.close();
        } catch (SQLException e) {
            throw new DBException();
        }

        return result;
    }

    /**
     * 
     * @param table
     * @param columns
     * @param select
     * @param selectArg
     * @param groupBy
     * @param having
     * @param orderBy
     * @return 戻り値が一つの時
     * @throws SQLException
     */
    public String select(String table, String[] columns, String select, String[] selectArg, String groupBy,
            String having, String orderBy) throws SQLException {
        String result = null;
        Cursor c = db.query(table, columns, select, selectArg, groupBy, having, orderBy);

        c.moveToFirst();
        result = c.getString(0);
        c.close();

        return result;
    }

    // **************************************************************************
    // Argement SQL
    // **************************************************************************
    /**
     * 
     * @param sql
     * @param arg
     * @return 戻り値が一つの時
     */
    public String selectForString(String sql, String... arg) {

        SQLiteStatement stmt = db.compileStatement(sql);
        for (int i = 1; i <= arg.length; i++) {
            stmt.bindString(i, arg[i - 1]);
        }

        return stmt.simpleQueryForString();
    }

    /**
     * 
     * @param sql
     * @return ArrayList<String[]> レコード単位で取得するとき
     */
    public ArrayList<String[]> selectRecord(String sql, String... selectionArgs) {

        ArrayList<String[]> result = new ArrayList<String[]>();

        Cursor c = null;
        try {
            c = db.rawQuery(sql, selectionArgs);
            // 取得したカラム名を取得
            String[] columns = c.getColumnNames();
            for (String col : columns) {
                APILog.d(this, String.format("SQLを発行しました。 selectRecord() getColumnName[%s]", col));
            }
            while (c.moveToNext()) {

                // カラムの数配列を生成
                String[] ary = new String[columns.length];

                // レコードを取得
                for (int i = 0; i < c.getColumnCount(); i++) {
                    // c.getColumnIndex(columns[i])
                    // を挟むとTable.Columnで指定した時にExceptionはく
                    ary[i] = c.getString(c.getColumnIndex(columns[i]));
                    APILog.d(this, String.format("COLUM[%s] VALUES[%s]", columns[i], ary[i]));
                }
                result.add(ary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            if (c != null) {
                c.close();
            }
        }

        return result;
    }
    // **************************************************************************
    // SQLiteDatabase access
    // **************************************************************************
    // public SQLiteStatement compileStatement(String sql) {
    // return db.compileStatement(sql);
    // }
}
