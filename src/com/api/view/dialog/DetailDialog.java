package com.api.view.dialog;
////package ikuzus.firstApp.savings.dialog;
//
//import ikuzus.kakeibo.kai.R;
//import android.app.Dialog;
//import android.app.DialogFragment;
//import android.app.FragmentManager;
//import android.graphics.Color;
//import android.graphics.drawable.ColorDrawable;
//import android.os.Bundle;
//import android.view.View;
//import android.view.View.OnClickListener;
//import android.view.Window;
//import android.view.WindowManager;
//import android.widget.Button;
//import android.widget.TextView;
//
//public class DetailDialog extends DialogFragment {
//
//    /**
//     * 画面項目
//     */
//    /** dialog */
//    private Dialog dialog;
//    /** 編集ボタン */
//    private Button mDetailEdit;
//    /** 戻るボタン */
//    private Button mDetailBack;
//    /**
//     * 表示内容 年 月 日 項目 支払方法 金額 備考
//     */
//    private static final int[] CONTENT = { R.id.mDetailYear, R.id.mDetailMonth,
//            R.id.mDetailDay, R.id.mDetailItem, R.id.mDetailPayWay,
//            R.id.mDetailAmount, R.id.mDetailRemark };
//
//    /** 月表示の時 */
//    private static final int MONTH_INDEX = 1;
//    private String[] detailAry;
//
//    /** リスナーフラグ */
//    private DialogListener listener;
//
//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        dialog = new Dialog(getActivity());
//        // タイトル非表示
//        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
//        // フルスクリーン
//        dialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
//        // 背景を透明にする
//        dialog.getWindow().setBackgroundDrawable(
//                new ColorDrawable(Color.TRANSPARENT));
//
//        // レイアウト指定
//        dialog.setContentView(R.layout.dialog_detail);
//
//        if (detailAry != null) {
//            for (int i = 0; i < CONTENT.length; i++) {
//                int id = CONTENT[i];
//                TextView content = (TextView) dialog.findViewById(id);
//                if (i != MONTH_INDEX) {
//                    content.setText(detailAry[i]);
//                } else {
//                    content.setText(String.valueOf(Integer
//                            .parseInt(detailAry[i]) + 1));
//                }
//
//            }
//        }
//        // 編集ボタン
//        mDetailEdit = (Button) dialog.findViewById(R.id.mDetailEdit);
//        mDetailBack = (Button) dialog.findViewById(R.id.mDetailBack);
//
//        if (null != listener) {
//
//            mDetailEdit.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onClickedPositive(dialog);
//                }
//            });
//
//            mDetailBack.setOnClickListener(new OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    listener.onClickedNegative(dialog);
//                }
//            });
//        }
//
//        return dialog;
//    }
//
//    public void show(FragmentManager fragmentManager) {
//        super.show(fragmentManager, null);
//    }
//
//    /*
//     * 表示内容をセット
//     */
//    public DetailDialog setDetailAry(String[] detailAry) {
//        this.detailAry = detailAry;
//        return this;
//    }
//
//    public DetailDialog onClickListener(DialogListener listener) {
//        this.listener = listener;
//        return this;
//    }
//
//}
