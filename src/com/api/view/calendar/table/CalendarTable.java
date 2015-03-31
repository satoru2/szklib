package com.api.view.calendar.table;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.util.SparseArray;

import com.api.API.log.APILog;
import com.api.time.CalendarTime;
import com.api.view.calendar.cell.CalendarCell;

public class CalendarTable implements Iterable<SparseArray<CalendarCell>> {

    private List<SparseArray<CalendarCell>> calendarTable;

    public static int[] DayOfWeek = new int[] { CalendarTime.SUNDAY, CalendarTime.MONDAY, CalendarTime.TUESDAY,
            CalendarTime.WEDNESDAY, Calendar.THURSDAY, Calendar.FRIDAY, Calendar.SATURDAY, };
    private int maxRow = 6;
    private int maxCol = 7;

    /**
     * 空のカレンダーテーブルを作成する。<br>
     * 
     * @param context
     */
    public CalendarTable(Context context) {
        this.initialize(context);
    }

    /**
     * CalendarTableを初期化
     * 
     * @param context
     */
    private void initialize(Context context) {
        this.calendarTable = new ArrayList<SparseArray<CalendarCell>>();
        for (int i = 0; i < maxRow; i++) {
            SparseArray<CalendarCell> row = new SparseArray<CalendarCell>();
            for (int dayOfWeek : DayOfWeek) {
                CalendarCell cell = new CalendarCell(context);
                row.put(dayOfWeek, cell);
            }
            this.calendarTable.add(row);
        }
    }

    /**
     * 日付指定で取得
     * 
     * @param key
     * @return
     */
    public CalendarCell get(CalendarTime time) {
        APILog.d(CalendarTable.class,
                String.format("get(CalendarTime time) time Value  : %d/%d/%d", time.year, time.month, time.monthDay));

        // 行０列０からの個数を計算
        int distance = time.monthDay + time.getSkipCount();
        // 行を決定
        int rowNum = --distance / maxCol;

        if (rowNum > maxRow) {
            APILog.d(CalendarTable.class,
                    String.format("CalendarTable Not Has Date  : %d/%d/%d", time.year, time.month, time.monthDay));
            return null;
        }

        SparseArray<CalendarCell> row = this.calendarTable.get(rowNum);

        // 列を決定
        int colNum = distance % maxCol;

        if (colNum > maxCol) {
            APILog.d(CalendarTable.class,
                    String.format("CalendarTable Not Has Date  : %d/%d/%d", time.year, time.month, time.monthDay));
            return null;
        }

        CalendarCell cell = row.get(row.keyAt(colNum));

        APILog.d(
                CalendarTable.class,
                String.format("get(CalendarTime time) Return Value  : %d/%d/%d", cell.getYear(), cell.getMonth(),
                        cell.getDay()));
        return cell;
    }

    @Override
    public Iterator<SparseArray<CalendarCell>> iterator() {
        return new CalendarIterator(this.calendarTable);
    }
}
