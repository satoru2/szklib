package com.api.view.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;

import com.api.application.font.FontFactory;
import com.api.application.font.Fonts;
import com.api.view.dialog.KeyPadDialog;
import com.api.view.dialog.KeyPadDialog.OnResultListener;
import com.api.view.tenkeypad.TenKeyPadInput;
import com.satoru.api.R;

/**
 * 
 * @author aturo
 * 
 */
public class InputFragment extends Fragment implements OnResultListener {

    private TenKeyPadInput input;
    private Fonts font;
    private String mOrientation = KeyPadDialog.HORIAZONTAL;

    private static final String STATE_TEXT = "inputText";
    private static final String STATE_ORIENTATION = "orientation";
    private static final String STATE_FONT = "font";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_input, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        input = (TenKeyPadInput) getView().findViewById(R.id.fragment_input);
        input.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                showKeyPadDialog();
            }
        });
        // フォント設定
        if (this.font != null && this.font != Fonts.NONE) {
            this.input.setTypeface(FontFactory.getTypeFace(this.font));
        }

        if (savedInstanceState != null) {
            // 表示文字列を再セット
            this.input.setResultForInput(savedInstanceState.getString(STATE_TEXT));
            this.mOrientation = savedInstanceState.getString(STATE_ORIENTATION);
            this.font = Fonts.parse(savedInstanceState.getString(STATE_FONT));
        }

    }

    /**
     * 入力フォーム表示
     */
    private void showKeyPadDialog() {
        // KeyPadDialog 表示
        KeyPadDialog dialog;

        if (this.font == null) {
            dialog = KeyPadDialog.newInstance(this.input.getText(), this);
        } else {
            dialog = KeyPadDialog.newInstance(this.input.getText(), this.font, this);
        }

        dialog.setOrientation(this.mOrientation);
        dialog.show(getActivity().getFragmentManager(), null);
    }

    @Override
    public void onSettlemented(String text) {
        input.setResultForInput(text);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(STATE_TEXT, this.input.getText());
        outState.putString(STATE_FONT, this.font.name());
        outState.putString(STATE_ORIENTATION, this.mOrientation);
    }

    /**
     * 値を取得
     * 
     * @return
     */
    public String getValue() {
        return this.input.getText();
    }

    /**
     * KeyPadDialog 方向設定
     * 
     * @param orientation
     */
    public void setKeyPadOrientation(String orientation) {
        this.mOrientation = orientation;
    }

    /**
     * フォント設定
     */
    public void setTypeface(Fonts font) {
        this.font = font;
    }
}
