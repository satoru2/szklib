package com.api.exception;

/**
 * このアプリケーションでプログラムによるバグが発生した時に、
 * アプリを落とすためのクラス
 *
 */
@SuppressWarnings("serial")
public class ApplicationException extends RuntimeException {

    /**
     * コンストラクタ
     */
    @Deprecated
    public ApplicationException() {
        super();
    }

    /**
     * コンストラクタ
     * @param str エラーメッセージ
     */
    public ApplicationException(String str) {
        super(str);
    }

    /**
     * コンストラクタ
     * @param str エラーメッセージ
     * @param e
     */
    public ApplicationException(String str, Throwable e) {
        super(str, e);
    }
}
