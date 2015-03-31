package com.api.view.swipe;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelecterView extends LinearLayout{

    Context context;
    CharSequence oldText;
    public SelecterView(Context context) {
        this(context, null);
    }
    public SelecterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        setOrientation(VERTICAL);
    }
    public void setText(String text) {
        removeView(getChildAt(0));
        TextView textView = new TextView(context);
        textView.setText(text);
        addView(textView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
    }
    public void refresh(){
        TextView view = (TextView) getChildAt(0);
        oldText = view.getText();
        removeView(getChildAt(0));
        TextView textView = new TextView(context);
        textView.setText(oldText);
        addView(textView, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
    }
}
