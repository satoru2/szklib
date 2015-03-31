package com.api.view.calendar.listener;

import com.api.time.CalendarTime;

public interface OnDateChangeListener {

    /**
     * 
     * @param newVal
     * @param oldVal
     */
    public void onDateChange(CalendarTime newVal, CalendarTime oldVal);
}
