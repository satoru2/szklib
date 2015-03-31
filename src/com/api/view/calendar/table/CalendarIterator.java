package com.api.view.calendar.table;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import android.util.SparseArray;

import com.api.view.calendar.cell.CalendarCell;

public class CalendarIterator implements Iterator<SparseArray<CalendarCell>> {
    private List<SparseArray<CalendarCell>> calendarTable;
    private int currentRow = 0;

    public CalendarIterator(List<SparseArray<CalendarCell>> data) {

        this.calendarTable = data;
    }

    @Override
    public boolean hasNext() {
        return this.currentRow < 6;
    }

    @Override
    public SparseArray<CalendarCell> next() {
        if (this.currentRow >= 6)
            throw new NoSuchElementException();
        return calendarTable.get(this.currentRow++);
    }

    @Override
    public void remove() {
        // なにもしない
    }

}
