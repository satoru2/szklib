package com.api.exception;

@SuppressWarnings("serial")
public class DBException extends Exception {

    /**
     * コンストラクタ
     */
    public DBException() {
        super();
    }
    /**
     * コンストラクタ<br>
     * @param msg メッセージ<br>
     */
    public DBException(String msg) {
        super(msg);
    }
}
