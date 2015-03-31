package com.api.view.tenkeypad;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * KeyPad から値を受け取る専用クラス<br>
 * 
 */
public class TenKeyPadInput extends TextView {

    /**
     * NameSpace
     */
    private static final String NAME_SPACE = "http://ikuzus/firstApp/savings/view/tenkeypad/input/TenKeyPadInput";

    /**
     * Content Text
     */
    private String mText;

    /**
     * コンストラクタ<br>
     * 
     * @param context Activity<br>
     * @param attrs 属性<br>
     */
    public TenKeyPadInput(Context context, AttributeSet attrs) {

        super(context, attrs);

        if (attrs != null) {
            // 初期表示テキスト
            mText = attrs.getAttributeValue(NAME_SPACE, "text");

            // 設定されていなければ空文字
            if (mText == null) {
                mText = "";
            }

            // 表示する
            this.setResultForInput(mText);
        }
    }

    /**************************************************************************************
     * public メソッド<br>
     * 
     **************************************************************************************/

    /**
     * 表示内容から "," を抜いた値を返す<br>
     * 
     * @return String mTexts
     */
    @Override
    public String getText() {
        return this.mText;
    }

    /**
     * KeyPad からのコールメソッド<br>
     * 値が入力された時に呼ばれる<br>
     * 
     * @param text
     */
    public void setResultForInput(String text) {
        this.mText = text;
        setText(this.getDispText());
    }

    /**************************************************************************************
     * private メソッド<br>
     * 
     **************************************************************************************/
    /**
     * 表示するテキストを取得
     */
    private String getDispText() {
        if (this.mText.equals("")) {
            return "";
        }
        return String.format("%,d", Integer.parseInt(this.mText));
    }
}
