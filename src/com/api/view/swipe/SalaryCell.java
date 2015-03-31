package com.api.view.swipe;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SalaryCell extends LinearLayout {

    private TextView mTitle;
    private String mTitleText;
    private TextView mValue;
    private String mValueText;
    private Context context;

    private final LinearLayout.LayoutParams TITLE_WEIGHT;
    private final LinearLayout.LayoutParams VALUE_WEIGHT;
    private static final String NAME_SPACE = "http://ikuzus/android/util/swipe/calendar/SwipeableCalendarView";
    /**
     * イニシャライザ
     */
    {
        TITLE_WEIGHT = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 0);
        TITLE_WEIGHT.weight = 2;

        VALUE_WEIGHT = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT, 0);
        VALUE_WEIGHT.weight = 1;

    }

    public SalaryCell(Context context, String title) {
        this(context, title, null);
    }

    public SalaryCell(Context context, String title, String value) {
        super(context);
        this.context = context;
        mTitleText = title;
        mValueText = value == null ? "" : value;

        this.initialize();
    }

    public SalaryCell(Context context, AttributeSet attrs) {
        super(context, attrs);

        if (attrs != null) {
            // paddingTop の設定
            mTitleText = attrs.getAttributeValue(NAME_SPACE, "titleText");
            mValueText = attrs.getAttributeValue(NAME_SPACE, "valueText");
        }

        this.context = context;
//        mTitleText = "";
//        mValueText = "";
        this.initialize();
    }

    private static final String TAG = SalaryCell.class.getSimpleName();
    private void initialize() {

        Log.d(TAG,String.format("SalaryCell initialized Title [%s] Value[%s]", mTitleText,mValueText));

        this.setOrientation(LinearLayout.VERTICAL);

        mTitle = new TextView(context);
        mTitle.setText(mTitleText);
        mTitle.setBackgroundColor(Color.LTGRAY);

        addView(mTitle, TITLE_WEIGHT);

        mValue = new TextView(context);
        mValue.setText(mValueText);


        addView(mValue, VALUE_WEIGHT);
    }

    public void setTitle(String title) {
        mTitle.setText(title);
    }

    public void setValue(String value) {
        mValue.setText(value);
    }

}
