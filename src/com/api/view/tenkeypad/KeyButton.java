package com.api.view.tenkeypad;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

class KeyButton extends Button {

    public static final String NAME_SPACE = "com.api.view.tenkeypad.KeyButton";

    /**
     * コンストラクタ
     *
     * @param context
     */
    KeyButton(Context context) {
        this(context, null);
    }

    /**
     * コンストラクタ
     *
     * @param context
     * @param text
     */
    public KeyButton(Context context, AttributeSet attr) {
        super(context, attr);
        if (attr != null) {
            // 表示テキスト の設定
            // mText = attr
            // .getAttributeValue(NAME_SPACE, "text");
        }
    }

    @Override
    public String toString() {
        return (String) getText();
    }
}
