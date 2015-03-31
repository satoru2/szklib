package com.api.view.calendar;

import com.api.view.calendar.CalendarAttribute.Properties;

public interface OnAttrChangeListener {

    /**
     * カレンダーグリッドの属性が変更された事を通知する。
     */
    abstract public void onAttrChangedListener(Properties prop, Object newValue, Object oldValue);
}
