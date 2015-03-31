package com.api.view.calendar;

import android.content.Context;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.api.view.swipe.BaseSwipeView;

public class SwipeableCalendar extends BaseSwipeView<CalendarGrid> {

    public SwipeableCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected SparseArray<CalendarGrid> getViews(Context context, AttributeSet attrs) {
        SparseArray<CalendarGrid> ary = new SparseArray<CalendarGrid>();
        for (int i = 0; i < 3; i++) {
            CalendarGrid cal = new CalendarGrid(context, new CalendarAttribute(attrs));
            if (i == 0) {
                cal.moveMonthBy(-1);
            } else if (i == 1) {
                cal.moveMonthBy(1);
            }
            ary.put(i, cal);
        }
        return ary;
    }

    @Override
    protected void update(View v, int newPosition) {
        CalendarGrid cal = (CalendarGrid) v;
        if (newPosition == 0) {
            cal.moveMonthBy(-1);
        } else if (newPosition == 1) {
            cal.moveMonthBy(1);
        }
    }
}