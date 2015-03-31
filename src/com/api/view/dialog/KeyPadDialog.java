package com.api.view.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.api.application.font.FontFactory;
import com.api.application.font.Fonts;
import com.api.view.tenkeypad.TenKeyPad;
import com.satoru.api.R;

/**
 * KeyPadDialog 基本的な機能を提供<br>
 */
public class KeyPadDialog extends DialogFragment {

    public final static String HORIAZONTAL = "horizontal";
    public final static String VERTICAL = "vertical";

    private final static String STATE_TEXT = "text";
    private final static String STATE_FONT = "font";
    private final static String STATE_ORIENTATION = "orientation";

    /**
     * 入力が確定した時によばれる KeyPadDialogを利用して値を取得する場合はこれを継承する。
     * 
     */
    public interface OnResultListener {
        public void onSettlemented(String text);
    }

    /**
     * KeyPad
     */
    private TenKeyPad keyPad;

    /**
     * KeyPadDialog のインスタンスを生成する<br>
     * 
     * @param param 表示テキスト<br>
     * @return dialog<br>
     */
    public static KeyPadDialog newInstance(String param, OnResultListener listener) {
        return newInstance(param, Fonts.NONE, listener);
    }

    /**
     * フォント指定でこのインスタンスを生成する。
     * 
     * @param param
     * @param listener
     * @param font
     * @return
     */
    public static KeyPadDialog newInstance(String param, Fonts font, OnResultListener listener) {

        // ダイアログを作成
        KeyPadDialog dialog = new KeyPadDialog();
        // バンドル作成
        Bundle arguments = new Bundle();
        arguments.putString(STATE_TEXT, param);
        arguments.putString(STATE_FONT, font.name());
        arguments.putString(STATE_ORIENTATION, HORIAZONTAL);
        dialog.setArguments(arguments);
        // セット
        dialog.setTargetFragment((Fragment) listener, 0);
        return dialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        Dialog dialog = super.onCreateDialog(savedInstanceState);

        // タイトル非表示
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        // フルスクリーン
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);

        dialog.setContentView(this.getContentView());

        // KeyPad を取得
        keyPad = (TenKeyPad) dialog.findViewById(this.getKeyPadId());
        // KeyPad の表示文字列を取得
        keyPad.setValue(getArguments().getString(STATE_TEXT));

        // フォント設定
        if (!getArguments().getString(STATE_FONT).equals(Fonts.NONE.name())) {
            keyPad.setTypeface(FontFactory.getTypeFace(getArguments().getString(STATE_FONT)));
        }

        // リスナーを設定
        keyPad.setOnEnterClickListener(new TenKeyPad.OnEnterClickListener() {

            @Override
            public void onEnterClickListener(String resultText) {
                onSettlement(resultText);
            }
        });

        return dialog;
    }

    /**************************************************************************************
     * Override 推奨メソッド<br>
     * 
     **************************************************************************************/
    /**
     * このメソッドをOverride するとデフォルトではないレイアウトが作成できる。<br>
     * Overrideする際は getKeyPadId もOverrideすること。<br>
     * 
     * @return
     */
    protected int getContentView() {
        String orientation = getArguments().getString(STATE_ORIENTATION);
        if (HORIAZONTAL.equals(orientation)) {
            return R.layout.dialog_tenkeypad;
        } else {
            return R.layout.dialog_tenkeypad_vertical;
        }
    }

    /**
     * このメソッドをOverride するとデフォルトではないレイアウトが作成できる。<br>
     * Overrideする際は getContentView もOverrideすること。<br>
     * 
     * @return
     */
    protected int getKeyPadId() {
        return R.id.tenkeypad;
    }

    /**************************************************************************************
     * public メソッド<br>
     * 
     **************************************************************************************/

    @Override
    public void onPause() {
        super.onPause();

        // 画面更新内容を Bundle に保存
        getArguments().putString(STATE_TEXT, keyPad.getValue());
    }

    /**************************************************************************************
     * private メソッド<br>
     * 
     **************************************************************************************/
    private void onSettlement(String text) {

        // 通知
        if (getTargetFragment() != null) {
            ((OnResultListener) getTargetFragment()).onSettlemented(text);
        }
        // ダイアログを閉じる
        dismiss();
    }

    /**
     * 方向指定<br>
     * インスタンスを生成してからshowDialogまでに呼ぶこと。<br>
     * 
     * @param orientation
     */
    public void setOrientation(String orientation) {
        getArguments().putString(STATE_ORIENTATION, orientation);
    }
}
