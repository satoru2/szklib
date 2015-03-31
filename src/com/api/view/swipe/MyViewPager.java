package com.api.view.swipe;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;

public class MyViewPager extends ViewPager {

    private static final String TAG = "SwipeableSeleterView";
    
    public MyViewPager(Context context) {
        this(context, null);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);

        Log.d(TAG, String.format("onLayout getCurrentItem() [%d]", getCurrentItem()));
        if (getCurrentItem() != 1) {
            Log.d(TAG, "setCurrentItem");
            setCurrentItem(1, false);
        }
    }
}