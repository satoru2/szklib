package com.api.view.calendar;

import java.util.ArrayList;
import java.util.Calendar;

import android.content.Context;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.api.API.log.APILog;
import com.api.application.font.FontFactory;
import com.api.application.font.Fonts;
import com.api.exception.ApplicationException;
import com.api.time.CalendarTime;
import com.api.time.TimeUtility;
import com.api.view.calendar.CalendarAttribute.Properties;
import com.api.view.calendar.cell.CalendarCell;
import com.api.view.calendar.cell.CalendarCell.Fields;
import com.api.view.calendar.listener.OnDateChangeListener;
import com.api.view.calendar.listener.OnFocusChangedListener;
import com.api.view.calendar.listener.OnFocusedDateTouchListener;
import com.api.view.calendar.table.CalendarTable;

/**
 * 指定した年月日のカレンダーを表示するクラス カレント日付を取得するにはこのViewにonTouchListenerを設定して取得します。<br>
 * xml で各属性は以下のように指定する<br>
 * <code>
 * <com.api.view.calendar.CalendarGrid
        xmlns:app="http://com.api.view.calendar/CalendarGrid"
        android:id="@+id/cal"
        android:layout_width="wrap_content"
        android:layout_height="wrap_content"
        app:border_color="red"
        app:border_width="5"
        app:currentday="1990/07/02"
        app:focused_textcolor="white"
        app:focusedback_ground="yellow"
        app:font="none"
        app:outrange_backfground="black"
        app:outrange_textcolor="blue"
        app:textcolor="white" />
 * <code>
 */
public class CalendarGrid extends LinearLayout {

    /**
     * 最大表示週
     */
    public static final int MAX_WEEK = 6;

    /**
     * 日付情報
     */
    private CalendarTable calendarTable;
    /**
     * プロパティ情報
     */
    private CalendarAttribute mAttrs;
    /**
     * 枠線
     */
    private ArrayList<View> _borders;
    /**
     * フォーカスが当たっているセル
     */
    private CalendarCell focusedCell;

    /**
     * Listeners
     */
    private OnDateChangeListener onDateChangeListener;
    private OnFocusedDateTouchListener onFocusedDateTouchListener;
    private OnFocusChangedListener onFocusChangedListener;

    /**
     * CalendarGrid の初期化<br>
     * android.text.format.Time の初期値で初期化する。<br>
     * 
     * @param context ActivityContext
     */
    public CalendarGrid(Context context) {
        this(context, new CalendarAttribute());
    }

    /**
     * CalendarGrid の初期化<br>
     * xml から使用するさいに呼ばれるコンストラクタ、通常 Activity からは使用しない<br>
     * <
     * 
     * @param context ActivityContext
     * @param attrs AttributeSet
     */
    public CalendarGrid(Context context, AttributeSet attrs) {
        this(context, new CalendarAttribute(attrs));
    }

    /**
     * カレンダーグリッドの初期化
     * 
     * @param context
     * @param attrs
     */
    CalendarGrid(Context context, CalendarAttribute attrs) {
        super(context, attrs.getAttributeSet());
        // 固定
        this.setOrientation(VERTICAL);

        // プロパティの設定
        mAttrs = attrs;
        mAttrs.setOnAttrChangeListener(new OnAttrChangeListener() {

            @Override
            public void onAttrChangedListener(Properties prop, Object newValue, Object oldValue) {
                // プロパティの変更を通知する
                updateAttr(prop, newValue, oldValue);
            }
        });
        // ヘッダーの設定（曜日表示）
        this.createHeader(context);
        // 日付Viewを作成
        this.createDayViews(context);
        // 表示する
        this.updateLayout((CalendarTime) this.mAttrs.getValue(Properties.CURRENT_DAY));
    }

    /************************************************************
     * 
     * public
     * 
     *************************************************************/
    /**
     * 日付を設定する<br>
     * 日は１日で固定する<br>
     * 
     * @param year 年
     * @param month 月
     */
    public void set(int year, int month) {
        this.mAttrs.setAttr(Properties.CURRENT_DAY, CalendarTime.getInstance(year, month, 1));
    }

    /**
     * 日付を設定する
     * 
     * @param year 年
     * @param month 月
     * @param day 日
     */
    public void set(int year, int month, int day) {
        if (!TimeUtility.isDate(year, month + 1, day))
            throw new ApplicationException(String.format("InValid Date %d/%d/%d", year, month, day));
        this.mAttrs.setAttr(Properties.CURRENT_DAY, CalendarTime.getInstance(year, month, day));
    }

    /**
     * 日付を設定する
     * 
     * @param time CalendarTime
     */
    public void set(CalendarTime time) {
        if (!TimeUtility.isDate(time.year, time.month + 1, time.monthDay))
            throw new ApplicationException(String.format("InValid Date %d/%d/%d", time.year, time.month, time.monthDay));
        this.mAttrs.setAttr(Properties.CURRENT_DAY, time);
    }

    /**
     * 指定した月数分移動する
     */
    public void moveMonthBy(int num) {
        CalendarTime time = ((CalendarTime) mAttrs.getValue(Properties.CURRENT_DAY)).addMonth(num);
        this.mAttrs.setAttr(Properties.CURRENT_DAY, time);
    }

    /**
     * 現在の日付を取得する
     * 
     * @return CalendarTime
     */
    public CalendarTime getCurrentTime() {
        return (CalendarTime) this.mAttrs.getValue(Properties.CURRENT_DAY);
    }

    /**
     * 指定した日付にフォーカスを設定する
     * 
     * @param day
     */
    public void setFocus(int day) {

        // 日を取得
        CalendarTime time = (CalendarTime) this.mAttrs.getValue(Properties.CURRENT_DAY);
        if (day < 0) {
            time.monthDay = 1;
        } else if (day > time.getActualMaximum(Time.MONTH_DAY)) {
            time.monthDay = time.getActualMaximum(Time.MONTH_DAY);
        } else {
            time.monthDay = day;
        }

        this.mAttrs.setAttr(Properties.CURRENT_DAY, time);
    }

    /**
     * 指定した日付にフォーカスを設定する
     * 
     * @param CalendarCell
     */
    void setFocus(CalendarCell cell) {
        // 日を取得
        CalendarTime time = (CalendarTime) this.mAttrs.getValue(Properties.CURRENT_DAY);
        time.monthDay = cell.getDay();
        this.mAttrs.setAttr(Properties.CURRENT_DAY, time);
    }

    /**
     * フォーカスが当たっているセルを返す
     * 
     * @return
     */
    public CalendarCell getFocus() {
        return this.focusedCell;
    }

    /**
     * リスナーを設定します
     * 
     * @param onDataChangeListener
     */
    public void setOnDataChangeListener(OnDateChangeListener onDataChangeListener) {
        this.onDateChangeListener = onDataChangeListener;
    }

    /**
     * リスナーを設定するよ
     */
    public void setOnFocusedDateTouchListener(OnFocusedDateTouchListener onFocusedDateTouchListener) {
        this.onFocusedDateTouchListener = onFocusedDateTouchListener;
    }

    /**
     * リスナーを設定するよ
     */
    public void setOnFocusChangedListener(OnFocusChangedListener onFocusChangedListener) {
        this.onFocusChangedListener = onFocusChangedListener;
    }

    /************************************************************
     * 
     * private
     * 
     *************************************************************/
    /**
     * 曜日表示用のビューを生成する
     * 
     * @param Context context
     */
    private void createHeader(Context context) {
        float scaleDensity = context.getResources().getDisplayMetrics().density;
        LinearLayout header = new LinearLayout(context);

        // 現在時刻を取得
        CalendarTime time = (CalendarTime) mAttrs.getValue(Properties.CURRENT_DAY);
        Calendar calendar = time.toCalendar();
        // 週の頭をセット
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);

        for (int i = 0; i < CalendarTime.WEEK_DAY; i++) {
            // 日付部分
            TextView textView = new TextView(context);
            textView.setGravity(Gravity.CENTER); // 中央に表示
            textView.setPadding(0, 0, (int) (scaleDensity * 4), 0);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0, LayoutParams.WRAP_CONTENT);
            llp.weight = 1;
            header.addView(textView, llp);

            if (!Fonts.isNone((Fonts) this.mAttrs.getValue(Properties.FONT_NAME))) {
                textView.setTypeface(FontFactory.getTypeFace((Fonts) this.mAttrs.getValue(Properties.FONT_NAME)));
            }
            // テキストに日曜日から順に曜日を表示
            textView.setText(DateFormat.format("E", calendar.getTime()));
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        addView(header, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
    }

    /**
     * 日付表示用のビューを生成する
     * 
     * @param Context context
     */
    private void createDayViews(Context context) {
        float scaleDensity = context.getResources().getDisplayMetrics().density;

        // 空のカレンダーテーブルを作成
        this.calendarTable = new CalendarTable(context);
        _borders = new ArrayList<View>();

        for (SparseArray<CalendarCell> row : this.calendarTable) {

            // 横罫線
            LinearLayout yoko = new LinearLayout(context);
            View yokosen = new View(context);
            _borders.add(yokosen);
            yokosen.setBackgroundColor((Integer) this.mAttrs.getValue(Properties.BORDER_COLOR));
            // 横罫線を追加
            yoko.addView(
                    yokosen,
                    new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (Integer) this.mAttrs
                            .getValue(Properties.BORDER_WIDTH)));
            addView(yoko,
                    new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (Integer) this.mAttrs
                            .getValue(Properties.BORDER_WIDTH)));

            LinearLayout weekLine = new LinearLayout(context);

            // 1週間分の日付ビュー作成
            for (int i = 0; i < row.size(); i++) {
                int dayOfWeek = row.keyAt(i);
                CalendarCell cell = row.get(dayOfWeek);
                // 縦罫線
                View view = new View(context);
                view.setBackgroundColor((Integer) this.mAttrs.getValue(Properties.BORDER_COLOR));

                weekLine.addView(view,
                        new LinearLayout.LayoutParams((Integer) this.mAttrs.getValue(Properties.BORDER_WIDTH),
                                LayoutParams.MATCH_PARENT));
                _borders.add(view);

                cell.setTag(cell);
                cell.setGravity(Gravity.TOP | Gravity.START);
                cell.setPadding(0, (int) (scaleDensity * 4), (int) (scaleDensity * 4), 0);
                if (!Fonts.isNone((Fonts) this.mAttrs.getValue(Properties.FONT_NAME))) {
                    cell.setTypeface(FontFactory.getTypeFace((Fonts) this.mAttrs.getValue(Properties.FONT_NAME)));
                }
                cell.setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        switch (event.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            onTouchedDayView(v);
                            break;
                        case MotionEvent.ACTION_UP:
                            v.performClick();
                            break;
                        default:
                            break;
                        }
                        return false;
                    }
                });

                LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(0, (int) (scaleDensity * 48));
                llp.weight = 1;
                weekLine.addView(cell, llp);
            }

            // 罫線
            View view = new View(context);
            _borders.add(view);

            view.setBackgroundColor((Integer) this.mAttrs.getValue(Properties.BORDER_COLOR));
            weekLine.addView(view,
                    new LinearLayout.LayoutParams((Integer) this.mAttrs.getValue(Properties.BORDER_WIDTH),
                            LayoutParams.MATCH_PARENT));
            this.addView(weekLine, new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        }
        // 一番最後横罫線
        View view = new View(context);
        _borders.add(view);
        view.setBackgroundColor((Integer) this.mAttrs.getValue(Properties.BORDER_COLOR));
        this.addView(
                view,
                new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, (Integer) this.mAttrs
                        .getValue(Properties.BORDER_WIDTH)));
    }

    /**
     * CalendarAttribute の設定を反映する<br>
     */
    private void updateAttr(Properties prop, Object newValue, Object oldValue) {

        switch (prop) {
        case BACKGROUND:

            super.setBackgroundColor((Integer) newValue);

            break;
        case BORDER_COLOR:

            for (View view : _borders) {
                view.setBackgroundColor((Integer) newValue);
            }
            break;
        case BORDER_WIDTH:

            for (View view : _borders) {
                view.getLayoutParams().width = (Integer) newValue;
            }

            break;
        case CURRENT_DAY:
            // 日付を設定する
            this.setCurrentDay((CalendarTime) newValue, oldValue == null ? null : (CalendarTime) oldValue);

            if (onDateChangeListener != null) {
                onDateChangeListener.onDateChange((CalendarTime) newValue, oldValue == null ? null
                        : (CalendarTime) oldValue);
            }
            break;
        case FOCUSED_BACKGROUND:
        case FOCUSED_TEXT_COLOR:

            this.setFocus(((CalendarTime) this.mAttrs.getValue(Properties.CURRENT_DAY)).monthDay);

            break;
        case FONT_NAME:
            // フォントの途中変更は受け付けない
            throw new ApplicationException("Accepts No Operation Do Not Changes Font");
        case HOLIDAY_BACKGROUND:
            break;
        case HOLIDAY_TEXT_COLOR:
            break;
        case OUT_RANGE_BACKGROUND:

            for (SparseArray<CalendarCell> row : this.calendarTable) {
                for (int i = 0; i < row.size(); i++) {
                    CalendarCell cell = row.get(row.keyAt(i));
                    if (!cell.isRange()) {
                        cell.setBackgroundColor((Integer) newValue);
                    }
                }
            }

            break;
        case OUT_RANGE_TEXT_COLOR:

            for (SparseArray<CalendarCell> row : this.calendarTable) {
                for (int i = 0; i < row.size(); i++) {
                    CalendarCell cell = row.get(row.keyAt(i));
                    if (!cell.isRange()) {
                        cell.setTextColor(Fields.Content, (Integer) newValue);
                        cell.setTextColor(Fields.Day, (Integer) newValue);
                    }
                }
            }

            break;
        case TEXT_COLOR:

            for (SparseArray<CalendarCell> row : this.calendarTable) {
                for (int i = 0; i < row.size(); i++) {
                    CalendarCell cell = row.get(row.keyAt(i));
                    if (cell.isRange()) {
                        cell.setTextColor(Fields.Content, (Integer) newValue);
                        cell.setTextColor(Fields.Day, (Integer) newValue);
                    }
                }
            }
            break;
        default:
            break;
        }
    }

    /**
     * カレント日付をセットする
     * 
     * @param newValue
     * @param oldValue
     */
    private void setCurrentDay(CalendarTime newValue, CalendarTime oldValue) {
        APILog.d(CalendarGrid.class, "setCurrentDay()");
        APILog.d(CalendarGrid.class,
                String.format("setCurrentDay() newValue : %d/%d/%d", newValue.year, newValue.month, newValue.monthDay));
        if (oldValue != null) {
            APILog.d(CalendarGrid.class, String.format("setCurrentDay() oldValue %d/%d/%d", oldValue.year,
                    oldValue.month, oldValue.monthDay));
        } else {
            APILog.d(CalendarGrid.class, "setCurrentDay() oldValue is Null");
        }

        // 処理判定
        if (oldValue == null || newValue.year != oldValue.year || newValue.month != oldValue.month) {
            // 変更前が存在しないまたは 年か月が違えばすべて更新
            this.updateLayout(newValue);
        } else if (newValue.monthDay != oldValue.monthDay) {
            // 同一年月内で日が違えばフォーカス移動
            this.moveFocus(newValue, oldValue);
        }
    }

    /**
     * フォーカスの移動を行う
     * 
     * @param newValue
     * @param oldValue
     */
    private void moveFocus(CalendarTime newValue, CalendarTime oldValue) {

        CalendarCell newFocus = this.calendarTable.get(newValue);
        this.focus(newFocus, false);

        if (oldValue != null) {
            CalendarCell oldFocus = this.calendarTable.get(oldValue);
            this.focus(oldFocus, true);
        }
    }

    /**
     * フォーカスを設定する
     * 
     * @param cell カレンダーセル
     * @param remove 削除の場合/True
     */
    private void focus(CalendarCell cell, boolean remove) {

        Fonts font = (Fonts) this.mAttrs.getValue(Properties.FONT_NAME);

        if (!remove) {
            int textColor = (Integer) this.mAttrs.getValue(Properties.FOCUSED_TEXT_COLOR);
            int bkColor = (Integer) this.mAttrs.getValue(Properties.FOCUSED_BACKGROUND);

            if (cell.focus(textColor, bkColor, font)) {
                this.focusedCell = cell;
            } else {
                this.focusedCell = null;
            }

            if (onFocusChangedListener != null) {
                CalendarTime time = null;
                if (this.focusedCell != null) {
                    time = CalendarTime.getInstance(this.focusedCell.getYear(), this.focusedCell.getMonth(),
                            this.focusedCell.getDay());
                }
                // フォーカス変更を通知
                this.onFocusChangedListener.onFocusChanged(time);
            }
        } else {
            int textColor;
            int bkColor;
            if (cell.isRange()) {
                textColor = (Integer) this.mAttrs.getValue(Properties.TEXT_COLOR);
                bkColor = (Integer) this.mAttrs.getValue(Properties.BACKGROUND);
            } else {
                textColor = (Integer) this.mAttrs.getValue(Properties.OUT_RANGE_TEXT_COLOR);
                bkColor = (Integer) this.mAttrs.getValue(Properties.OUT_RANGE_BACKGROUND);
            }

            cell.removeFocus(textColor, bkColor, font);
        }
    }

    /**
     * カレント日付で画面表示を更新する
     */
    private void updateLayout(CalendarTime newTime) {

        // 日付調整用のインスタンスを取得
        CalendarTime work = newTime.copy();
        // 1日から
        work.monthDay = 1;
        // 前月分を減算
        work.addDay(newTime.getSkipCount() * -1);

        for (SparseArray<CalendarCell> row : this.calendarTable) {
            for (int i = 0; i < row.size(); i++) {
                CalendarCell dayView = row.get(row.keyAt(i));

                if (newTime.month == work.month) {
                    // 当月範囲内を設定
                    dayView.setRange(true);
                    // 背景色を設定
                    dayView.setBackgroundColor((Integer) this.mAttrs.getValue(Properties.BACKGROUND));

                } else {
                    // 当月範囲外の設定
                    dayView.setRange(false);
                    dayView.setBackgroundColor((Integer) this.mAttrs.getValue(Properties.OUT_RANGE_BACKGROUND));
                }

                // 日付を設定
                dayView.set(work.year, work.month, work.monthDay);

                // フォーカスを設定
                if (newTime.year == work.year && newTime.month == work.month && work.monthDay == newTime.monthDay) {
                    this.focus(dayView, false);
                } else {
                    this.focus(dayView, true);
                }

                APILog.d(CalendarGrid.class,
                        String.format("Setting Date %d/%d/%d", work.year, work.month, work.monthDay));

                // 日付を加算
                work.addDay(1);
            }
        }
    }

    /**
     * 日付をタップした時に呼ばれるメソッド
     */
    private void onTouchedDayView(View v) {

        CalendarCell cell = (CalendarCell) v;
        APILog.d(CalendarGrid.class,
                String.format("onTouchedDayView() %d/%d/%d", cell.getYear(), cell.getMonth(), cell.getDay()));
        if (cell.isRange()) {

            if (!cell.isFocus()) {
                // フォーカスを設定
                this.setFocus(cell);
            } else if (onFocusedDateTouchListener != null) {
                CalendarTime time = (CalendarTime) this.mAttrs.getValue(Properties.CURRENT_DAY);
                onFocusedDateTouchListener.onFocusedDateTouch(time);
            }

        }
    }
}