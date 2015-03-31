package com.api.view.dialog;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.satoru.api.R;

public class SACustomDialog extends DialogFragment {

    /** SACustomDialog.TextView */
    private TextView text;
    /** 表示するメッセージ */
    private String message;
    /** PositiveButton セットフラグ */
    private boolean isPositiveButton;
    /** NegativeButton セットフラグ */
    private boolean isNegativeButton;
    /** NeutralButton セットフラグ */
    private boolean isNeutralButton;
    /** PositiveButton */
    private Button positiveButton;
    /** NegativeButton */
    private Button negativeButton;
    /** NeutralButton */
    private Button neutralButton;
    /** positiveButtonListener */
    private Dialog.OnClickListener positiveButtonListener;
    /** NegativeButtonListener */
    private Dialog.OnClickListener negativeButtonListener;
    /** NeutralButtonListener */
    private Dialog.OnClickListener neutralButtonListener;
    /** PositiveButton */
    public static final int POSITIVE = 1;
    /** NegativeButton */
    public static final int NEGATIVE = 2;
    /** NeutralButton */
    public static final int NEUTRAL = 3;
    /** dialog */
    Dialog dialog;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dialog = new Dialog(getActivity());
        // タイトル非表示
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // フルスクリーン
        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        dialog.setContentView(R.layout.dialog_custom);
        text = (TextView) dialog.findViewById(R.id.message);
        text.setText(message);
        // 背景を透明にする
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));

        // PositiveButton ボタンのリスナ
        positiveButton = (Button) dialog.findViewById(R.id.positive_button);
        positiveButton.setText("OK!");
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                positiveButtonListener.onClick(dialog, POSITIVE);
            }
        });

        // NegativeButton ボタンのﾘｽﾅ
        negativeButton = (Button) dialog.findViewById(R.id.negative_button);
        negativeButton.setText("NG");
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                negativeButtonListener.onClick(dialog, NEGATIVE);
            }
        });

        // NeutralButton ボタンのﾘｽﾅ
        neutralButton = (Button) dialog.findViewById(R.id.neutral_button);
        neutralButton.setText("微妙");
        neutralButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                neutralButtonListener.onClick(dialog, NEUTRAL);
            }
        });
        // PositiveButtonが設定されていたら
        if (!isPositiveButton) {
            positiveButton.setVisibility(View.GONE);
        }
        // NegativeButtonが設定されていたら
        if (!isNegativeButton) {
            negativeButton.setVisibility(View.GONE);
        }
        // NeutralButtonが設定されていたら
        if (!isNeutralButton) {
            neutralButton.setVisibility(View.GONE);
        }

        return dialog;
    }

    @Override
    public void show(FragmentManager fragmentManager, String message) {
        this.message = message;
        super.show(fragmentManager, null);
    }

    /**
     * PositiveButtonを設定
     *
     * @param positiveButtonListener
     */
    public void setPositiveButton(
            DialogInterface.OnClickListener positiveButtonListener) {

        // PositiveButtonを表示
        this.isPositiveButton = true;
        // Listenerをセット
        this.positiveButtonListener = positiveButtonListener;

    }

    /**
     * NegativeButtonを設定
     *
     * @param negativeButtonListener
     */
    public void setNegativeButton(
            DialogInterface.OnClickListener negativeButtonListener) {
        // NegativeButtonを表示
        this.isNegativeButton = true;
        // Listenerをセット
        this.negativeButtonListener = negativeButtonListener;
    }

    /**
     * NeutralButtonを設定
     *
     * @param neutralButton
     */
    public void setNeutralButton(DialogInterface.OnClickListener neutralButton) {
        // NeutralButtonを表示
        this.isNeutralButton = true;
        // Listenerをセット
        this.neutralButtonListener = neutralButton;
    }

}
