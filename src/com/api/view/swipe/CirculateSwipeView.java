package com.api.view.swipe;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;

public class CirculateSwipeView extends LinearLayout {

    /**
     * Context
     */
    private static final String NAME_SPACE = "";
    /**
     * Adapter
     */
    private SelecterAdapter mSelecterAdapter;
    /**
     *  スワイプする数
     */
    private int contentNum;

    /**
     * Construct
     * @param context Activity<br>
     */
    public CirculateSwipeView(Context context) {
        this(context, null);
    }

    /**
     * Construct
     * @param context Activity<br>
     * @param attrs AttributeSet<br>
     */
    public CirculateSwipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        if (attrs != null) {
            this.contentNum = attrs.getAttributeIntValue(NAME_SPACE, "ContentNumber",3);
        }
        mSelecterAdapter = new SelecterAdapter(context);
    }

    /**
     *
     */
    public void setSelecterListener() {
        // TODO
    }

    public void setText(String text) {
        mSelecterAdapter.setText(text);
    }

    private class SelecterAdapter extends CirculationSwipeAdapter {

        public SelecterAdapter(Context context) {
            super(context);
        }

        @Override
        public View createContent(Context mContext, int newPosition) {
            SelecterView select = new SelecterView(mContext);
            select.setText(String.valueOf(newPosition));
            return select;
        }

        @Override
        public void updateContent(View v, int newPosition) {
            SelecterView select = (SelecterView) v;
            select.refresh();
        }

        @Override
        public int getPagerChildCount() {
            return contentNum;
        }

        @Override
        public void addViewPager(ViewPager viewPager) {
            addView(viewPager, new LinearLayout.LayoutParams(
                    LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }

        public void setText(String text) {
            SelecterView view = (SelecterView) getFocusedView();
            if (view != null) {
                view.setText(text);
            }

        }

    }
}
