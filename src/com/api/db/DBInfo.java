package com.api.db;


/**
 * オンコーディングだるいから Pragma_TableInfo() とかでなんとかなんないかな
 *
 * @author aturo
 *
 */
public class DBInfo {

    /**
     * 初期化フラグ
     */
    public static boolean isInitialized;
    /*
     * DB情報
     */
    /** DBファイル名 */
    public static  String DB_FILE_NAME;
    /** DB名 */
    public static  String DB_NAME;
    /** バージョン */
    public static  int DB_VERSION;
}
