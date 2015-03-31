package com.api.view.swipe;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;
import android.widget.LinearLayout;

public abstract class BaseSwipeView<T extends View> extends LinearLayout {

    protected SwipeAdaptar _adapter;
    protected SparseArray<T> _views;

    public BaseSwipeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        _views = this.getViews(context, attrs);
        if (_views == null)
            throw new NullPointerException("Argument is null");
        if (_views.size() % 3 != 0)
            throw new IllegalArgumentException("Argument size is not multiple of 3");

        _adapter = new SwipeAdaptar(context, attrs);
    }

    /**
     * スワイプするViewをセットする
     * 
     * @param views
     */
    protected abstract SparseArray<T> getViews(Context context, AttributeSet attrs);

    protected abstract void update(View v, int newPosition);

    /**
     * 無限スワイプアダプタークラス
     * 
     * @author aturo
     * 
     */
    private class SwipeAdaptar extends CirculationSwipeAdapter {

        public SwipeAdaptar(Context context, AttributeSet attrs) {
            super(context);
        }

        @Override
        public View createContent(Context context, int newPosition) {
            return _views.get(newPosition);
        }

        @Override
        public void updateContent(View v, int newPosition) {
            update(v, newPosition);
        }

        @Override
        public int getPagerChildCount() {
            return _views.size();
        }

        @Override
        public void addViewPager(ViewPager viewPager) {
            addView(viewPager, new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
        }
    }

}
