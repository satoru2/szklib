package com.api.time;

import java.util.Calendar;
import java.util.Locale;

import android.text.format.Time;

import com.api.util.StringUtil;

public class CalendarTime extends Time {

    private CalendarTime() {
        super();
    }

    /**
     * CalendarTime のインスタンスを初期化するモード
     * 
     */
    public enum Mode {
        /**
         * 指定なし 1970/1/1で初期化する
         */
        None,
        /**
         * 今日の日付で初期化する
         */
        Today,
        /**
         * 昨日の日付で初期化する
         */
        Yesterday,
        /**
         * 明日の日付で初期化する
         */
        Tomorrow,
        /**
         * 明後日の日付で初期化する
         */
        DayAfterTomorrow,
        /**
         * 来月の現在日で初期化する<br>
         * 来月の最大日<今日の日の場合、来月の最大日で初期化する。
         */
        NextMonth,
        /**
         * 先月の現在日で初期化する<br>
         * 先月の最大日<今日の日の場合、先月の最大日で初期化する。
         */
        PrevMonth
    }

    /**
     * 日付指定で初期化する
     * 
     * @param year
     * @param month
     * @param day
     * @return CalendarTime
     */
    public static CalendarTime getInstance(int year, int month, int day) {

        CalendarTime time = new CalendarTime();
        time.set(day, month, year);

        return time;
    }

    /**
     * カレンダークラスをもとにインスタンスを作成する
     * 
     * @param cal
     * @return
     */
    public static CalendarTime getInstance(Calendar cal) {
        CalendarTime time = new CalendarTime();
        time.set(cal.getTimeInMillis());
        return time;
    }

    /**
     * 日付モードでインスタンスを取得する
     * 
     * @param mode
     * @return
     */
    public static CalendarTime getInstance(Mode mode) {

        CalendarTime today = null;
        if (mode != Mode.None) {
            today = new CalendarTime();
            today.setToNow();
        }

        switch (mode) {
        case None:
            return new CalendarTime();
        case Today:
            return today;
        case DayAfterTomorrow:
            return today.addDay(2);
        case NextMonth:
            return today.addMonth(1);
        case PrevMonth:
            return today.addMonth(-1);
        case Tomorrow:
            return today.addDay(1);
        case Yesterday:
            return today.addDay(-1);
        default:
            throw new IllegalArgumentException();
        }
    }

    /**
     * CalendarTime を Calendar に変換して返す
     * 
     * @return Calendar
     * 
     */
    public Calendar toCalendar() {
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(super.toMillis(false));
        return cal;
    }

    /**
     * 当月１日を設定したカレンダーを返す
     * 
     * @return Calendar
     */
    public Calendar toCalendarFirstOfMonth() {
        Calendar cal = toCalendar();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        return cal;
    }

    /**
     * 当月の最大日を設定したカレンダーを返す
     * 
     * @return Calendar
     */
    public Calendar toCalendarAtMaximum() {
        Calendar cal = toCalendar();
        cal.set(super.year, super.month, super.getActualMaximum(MONTH_DAY));
        return cal;
    }

    /**
     * このインスタンスに月を加算する
     * 
     * @param num 加算する月
     * @return 加算したこのインスタンス
     */
    public CalendarTime addMonth(int num) {
        Calendar cal = this.toCalendar();
        cal.add(Calendar.MONTH, num);
        super.set(cal.getTimeInMillis());
        return this;
    }

    /**
     * このインスタンスに日を加算する
     * 
     * @param num 加算する日付
     * @return 加算したこのインスタンス
     */
    public CalendarTime addDay(int num) {
        Calendar cal = this.toCalendar();
        cal.add(Calendar.DAY_OF_MONTH, num);
        super.set(cal.getTimeInMillis());
        return this;
    }

    /**
     * 同一の内容の新しいインスタンスを返す
     * 
     * @return 新しいインスタンスの CalendarTime
     */
    public CalendarTime copy() {
        CalendarTime time = getInstance(Mode.None);
        time.set(this);
        return time;
    }

    /**
     * カレンダーの最初の空白の個数を求める
     * 
     * @param targetCalendar 指定した月のCalendarのInstance
     * @return skipCount
     */
    public int getSkipCount() {
        // 空白の個数
        int skipCount;

        // 指定された年月のカレンダーを取得
        Calendar cal = toCalendarFirstOfMonth();

        int firstDayOfWeekOfMonth = cal.get(Calendar.DAY_OF_WEEK); // 1日の曜日
        if (Calendar.SUNDAY > firstDayOfWeekOfMonth) {
            skipCount = firstDayOfWeekOfMonth - Calendar.SUNDAY + CalendarTime.WEEK_DAY;
        } else {
            skipCount = firstDayOfWeekOfMonth - Calendar.SUNDAY;
        }

        // カレンダー情報を初期化
        cal.clear();
        return skipCount;
    }

    // TODO 休日情報
    /**
     * 休日情報仕様はあとで考える
     * 
     * @return
     */
    public String getHoliday() {
        return "";
    }

    public String getHoliday(int month) {

        return "";
    }

    public String getHoliday(Time month) {
        return "";
    }

    // -- static ------------------------------------------------
    /**
     * 文字列から CalendarTime を取得する
     * 
     * @param value 文字列（Modeに列挙されている文字列または日付文字列）
     * @return CalendarTime オブジェクト
     */
    public static CalendarTime parseTime(String value) {
        if (StringUtil.isEmpty(value)) {
            throw new IllegalArgumentException("Value is empty");
        }

        CalendarTime time;
        if (TimeUtility.isDate(value)) {
            time = CalendarTime.getInstance(TimeUtility.toCalendar(value));
        } else {
            time = CalendarTime.getInstance(Mode.valueOf(value));
        }

        return time;
    }

    /**
     * 文字列を CalendarTime に解析できるか判定する
     * 
     * @param value 文字列
     * @return 解析可能な場合/True
     */
    public static boolean canParse(String value) {

        if (StringUtil.isEmpty(value)) {
            return false;
        }

        if (TimeUtility.isDate(value)) {
            return true;
        }

        for (Mode mode : Mode.values()) {
            if (StringUtil.toUpperCase(mode.name()).equals(StringUtil.toUpperCase(value))) {
                return true;
            }
        }

        return false;
    }
}
