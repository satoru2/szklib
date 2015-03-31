package com.api.view.tenkeypad;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Vibrator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.api.exception.ApplicationException;
import com.api.view.dialog.KeyPadDialog;
import com.satoru.api.R;

/**
 * KeyPad 基底クラス<br>
 * KeyPad の基本的な機能を提供する<br>
 *
 */
public class TenKeyPad extends LinearLayout {
    private final static String NAME_SPACE = "http://com/api/view/tenkeypad";

    /**
     * EnterKey 入力で呼ばれるリスナー
     *
     */
    public interface OnEnterClickListener {
        public void onEnterClickListener(String resultText);
    }

    /**
     * NumberButtons
     */
    private final int[] mNumberButtons = { R.id.button1, R.id.button2,
            R.id.button3, R.id.button4, R.id.button5, R.id.button6,
            R.id.button7, R.id.button8, R.id.button9, R.id.button0 };

    /**
     * Number Buttons Instance
     */
    private KeyButton[] mNumberButtonsInstance;

    /**
     * AC
     */
    private KeyButton mKeyButtonAC;

    /**
     * BS
     */
    private KeyButton mKeyButtonBS;

    /**
     * Enter
     */
    private KeyButton mKeyButtonEnter;
    /**
     * TargetDisp KeyPad 表示部分
     */
    protected TenKeyPadDisp mTenKeyPadDisp;

    /**
     * Context
     */
    private Context mContext;

    /**
     * OnEnterClickListener
     */
    private OnEnterClickListener mOnEnterClickListener;

    /**
     * Vibrator
     */
    private Vibrator vibrator;

    /**
     * Orientation
     */
    private String mOrientation;

    /**
     * コンストラクタ
     *
     * @param context
     */
    public TenKeyPad(Context context) {
        this(context, null);
    }

    /**
     * 通常使うコンストラクタ<br>
     *
     * @param context
     *            Activity<br>
     * @param attrs
     *            属性<br>
     */
    public TenKeyPad(Context context, AttributeSet attrs) {
        super(context, attrs);

        // コンテキストをセット
        this.mContext = context;
        // バイブ作成
        vibrator = (Vibrator) mContext
                .getSystemService(Context.VIBRATOR_SERVICE);
        if (attrs != null) {
            this.mOrientation = attrs.getAttributeValue(NAME_SPACE,
                    "orientation");
            if (this.mOrientation == null) {
                this.mOrientation = KeyPadDialog.HORIAZONTAL;
            }
        }
        // 初期化
        this.initView();
    }

    /**************************************************************************************
     * public メソッド<br>
     *
     **************************************************************************************/

    /**
     * OnEnterClickListener をセット
     */
    public void setOnEnterClickListener(
            OnEnterClickListener onEnterClickListener) {
        this.mOnEnterClickListener = onEnterClickListener;
    }

    /**
     * 表示テキストをセット
     */
    public void setValue(String value) {
        this.mTenKeyPadDisp.setDisp(value);
    }

    /**
     * 表示内容を返す
     */
    public String getValue() {
        return this.mTenKeyPadDisp.getText();
    }

    /**************************************************************************************
     *
     * private メソッド
     **************************************************************************************/
    /*
     * 初期化
     */
    private void initView() {

        // レイアウト設定
        int layoutId = 0;
        if (KeyPadDialog.HORIAZONTAL.equals(this.mOrientation)) {
            layoutId = R.layout.tenkeypad;
        } else if (KeyPadDialog.VERTICAL.equals(this.mOrientation)) {
            layoutId = R.layout.tenkeypad_vertical;
        } else {
            throw new ApplicationException("IllegalArgement TenKeyPad KeypadID");
        }
        View layout = LayoutInflater.from(mContext).inflate(layoutId, this);
        // KeyPad の表示部分を取得
        mTenKeyPadDisp = (TenKeyPadDisp) layout.findViewById(R.id.keyPadDisp);

        this.mNumberButtonsInstance = new KeyButton[mNumberButtons.length];
        // ボタンイベント設定
        for (int i = 0; i < mNumberButtons.length; i++) {
            this.mNumberButtonsInstance[i] = (KeyButton) layout
                    .findViewById(mNumberButtons[i]);
            this.mNumberButtonsInstance[i]
                    .setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            onNumberClicked(v);
                        }
                    });
        }

        // BS ボタン
        this.mKeyButtonBS = (KeyButton) layout.findViewById(R.id.buttonBS);
        this.mKeyButtonBS.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onBSClicked(v);
            }
        });

        // AC ボタン
        this.mKeyButtonAC = (KeyButton) layout.findViewById(R.id.buttonAC);
        this.mKeyButtonAC.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onACClicked(v);
            }
        });

        // Enter ボタン
        this.mKeyButtonEnter = (KeyButton) layout
                .findViewById(R.id.buttonEnter);
        this.mKeyButtonEnter.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                onEnterClicked(v);
            }
        });
    }

    /**************************************************************************************
     *
     * ボタンクリック時処理 メソッド
     **************************************************************************************/
    /**
     * 数字ボタン<br>
     *
     * @param v
     *            Button<br>
     */
    private void onNumberClicked(View v) {
        // バイブ
        vibrator.vibrate(10);
        // KeyPadDisp に文字を設定
        this.mTenKeyPadDisp.concatContent(v.toString());
    }

    /**
     * BSボタン<br>
     *
     * @param v
     *            Button<br>
     */
    private void onBSClicked(View v) {
        // バイブ
        vibrator.vibrate(10);
        // 一文字消す
        this.mTenKeyPadDisp.backSpace();
    }

    /**
     * ACボタン<br>
     *
     * @param v
     *            Button<br>
     */
    private void onACClicked(View v) {
        // バイブ
        vibrator.vibrate(10);
        // 全て消す
        this.mTenKeyPadDisp.clearAll();
    }

    /**
     * Enterボタン<br>
     *
     * @param v
     *            Button<br>
     */
    private void onEnterClicked(View v) {

        // バイブ
        vibrator.vibrate(20);
        // 入力内容を通知
        if (this.mOnEnterClickListener != null) {
            this.mOnEnterClickListener.onEnterClickListener(this.mTenKeyPadDisp
                    .getText());
        }

        // 次回活性時のために表示部分を消す
        mTenKeyPadDisp.clearAll();
    }

    public void setTypeface(Typeface tf) {
        this.mTenKeyPadDisp.setTypeface(tf);
        for (int i = 0; i < this.mNumberButtonsInstance.length; i++) {
            this.mNumberButtonsInstance[i].setTypeface(tf);
        }
        this.mKeyButtonAC.setTypeface(tf);
        this.mKeyButtonBS.setTypeface(tf);
        this.mKeyButtonEnter.setTypeface(tf);

    }

    /**************************************************************************************
     * protected メソッド
     *
     **************************************************************************************/

}
