package com.api.view.tenkeypad;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * TenKeyPadDisp 基底クラス<br>
 * ３桁ごとのカンマ区切り、一文字づつ後ろにつなげて表示する機能を提供<br>
 *
 */
public class TenKeyPadDisp extends TextView {

    /**
     * 表示文字列
     */
    private String mText = "";

    /**
     * コンストラクタ<br>
     *
     * @param context
     *            Activity<br>
     * @param attrs
     *            属性<br>
     */
    public TenKeyPadDisp(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.initView(context);
    }

    /**************************************************************************************
     *
     * public メソッド
     **************************************************************************************/
    /**
     * ボタン入力でテキストをセット
     */
    public void concatContent(String text) {
        // 0は上書き
        if (mText.equals("0")) {
            this.mText = text;
            super.setText(this.mText);
            return;
        }

        if (mText.length() < 7) {
            this.mText = this.mText.concat(text);
            super.setText(mText.equals("") ? "" : String.format("%,d",
                    Integer.parseInt(this.mText)));
        }
    }

    /**
     * ボタン入力でテキストをセット
     *
     * @param text
     */
    public void concatContent(CharSequence text) {
        this.concatContent((String) text);
    }

    /**
     * 表示文字を上書き
     */
    public void setDisp(String text) {
        this.mText = text;
        super.setText(mText.equals("") ? "" : String.format("%,d",
                Integer.parseInt(this.mText)));
    }

    /**
     * 表示文字を上書き
     */
    public void setDisp(CharSequence text) {
        this.setDisp((String) text);
    }

    /**
     * 表示テキストを","なしで取得
     */
    @Override
    public String getText() {
        return this.mText;
    }

    /**************************************************************************************
     *
     * private メソッド
     **************************************************************************************/

    /**
     * TenKeyPadDisp 初期化<br>
     *
     * @param context
     *            Activity<br>
     */
    protected void initView(final Context context) {
    }

    /**
     * 一文字消す キャレット実装したら変わると思う
     */
    public void backSpace() {
        if (this.mText.length() != 0) {

            if (this.mText.length() == 1) {

                this.mText = "";
                super.setText(this.mText);

            } else {
                this.mText = mText.substring(0, mText.length() - 1);
                super.setText(String.format("%,d", Integer.parseInt(this.mText)));
            }

        }
    }

    /**
     * すべて消す
     */
    public void clearAll() {
        this.mText = "";
        super.setText("");
    }

}
