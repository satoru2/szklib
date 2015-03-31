package com.api.view.calendar.cell;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.api.application.font.FontFactory;
import com.api.application.font.Fonts;
import com.satoru.api.R;

/**
 * カレンダーの日付を表すクラス
 * 
 * @author aturo
 * 
 */
public class CalendarCell extends LinearLayout {

    public enum Fields {
        Day, Holiday, Content
    }

    private TextView mTvDay;
    private TextView mTvHoliday;
    private TextView mTvContent;
    /**
     * 当月範囲内かどうか
     */
    private boolean isRange;
    /**
     * 祝日かどうか
     */
    private boolean isHoliday;
    /**
     * フォーカスがあるかどうか
     */
    private boolean isFocus;

    private int year;
    private int month;
    private int day;

    /**
     * コンストラクタ
     * 
     * @param context
     * @param attrs
     */
    public CalendarCell(Context context) {
        this(context, null);
    }

    /**
     * コンストラクタ
     * 
     * @param context
     * @param attrs
     */
    public CalendarCell(Context context, AttributeSet attrs) {
        super(context, attrs);

        View layout = LayoutInflater.from(context).inflate(R.layout.stday, this);

        this.mTvDay = (TextView) layout.findViewById(R.id.mTvDay);
        this.mTvDay.setTextSize(12);
        this.mTvHoliday = (TextView) layout.findViewById(R.id.mTvHoliday);
        this.mTvContent = (TextView) layout.findViewById(R.id.mTvContent);
    }

    /**
     * 表示する項目をセットする
     * 
     * @param key 表示項目
     * @return 指定した表示項目テキスト
     */
    public void setText(Fields field, CharSequence text) {

        switch (field) {
        case Content:
            this.mTvContent.setText(text);
            break;
        case Holiday:
            this.mTvHoliday.setText(text);
            break;
        case Day:
            this.mTvDay.setText(text);
            break;
        }

    }

    /**
     * テキストカラー指定
     * 
     * @param color
     * @param key
     */
    public void setTextColor(Fields field, int colors) {

        switch (field) {
        case Day:
            this.mTvDay.setTextColor(colors);
            break;
        case Holiday:
            this.mTvHoliday.setTextColor(colors);
            break;
        case Content:
            this.mTvContent.setTextColor(colors);
            break;
        }
    }

    /**
     * フォントを設定する
     * 
     * @param typeFace
     */
    public void setTypeface(Typeface typeFace) {
        this.mTvDay.setTypeface(typeFace);
        this.mTvHoliday.setTypeface(typeFace);
        this.mTvContent.setTypeface(typeFace);
    }

    /**
     * フォントを設定する
     * 
     * @param typeFace
     * @param op
     */
    public void setTypeface(Typeface typeFace, int op) {
        this.mTvDay.setTypeface(typeFace, op);
        this.mTvHoliday.setTypeface(typeFace, op);
        this.mTvContent.setTypeface(typeFace, op);
    }

    /**
     * 日付情報をセットする
     * 
     * @param year
     * @param month
     * @param day
     */
    public void set(int year, int month, int day) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.setText(Fields.Day, String.valueOf(day));
    }

    /**
     * フォーカスを設定する
     * 
     * @param text
     * @param background
     */
    public boolean focus(int text, int background, Fonts font) {
        if (this.isRange) {
            // 赤文字
            setTextColor(Fields.Content, text);
            setTextColor(Fields.Day, text);
            // 太字
            setTypeface(Fonts.isNone(font) ? Typeface.DEFAULT : FontFactory.getTypeFace(font), Typeface.BOLD);
            // 日付の背景
            setBackgroundColor(background);
            this.isFocus = true;
            return true;
        }
        this.isFocus = false;
        return false;
    }

    /**
     * フォーカスを削除する
     * 
     * @param text
     * @param background
     */
    public void removeFocus(int text, int background, Fonts font) {
        if (this.isRange) {
            // 文字色
            setTextColor(Fields.Content, text);
            setTextColor(Fields.Day, text);
            // 太字
            setTypeface(Fonts.isNone(font) ? Typeface.DEFAULT : FontFactory.getTypeFace(font), Typeface.NORMAL);
            // 日付の背景
            setBackgroundColor(background);
        }
        this.isFocus = false;
    }

    /**
     * 当月範囲内かどうかを取得する
     * 
     * @return
     */
    public boolean isRange() {
        return isRange;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
        this.setText(Fields.Day, String.valueOf(day));
    }

    /**
     * 当月範囲内かどうかを設定する
     * 
     * @param isRange
     */
    public void setRange(boolean isRange) {
        this.isRange = isRange;
    }

    public boolean isHoliday() {
        return isHoliday;
    }

    public void setHoliday(boolean isHoliday) {
        this.isHoliday = isHoliday;
    }

    public boolean isFocus() {
        return isFocus;
    }

    @Override
    public boolean performClick() {
        super.performClick();
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            break;
        case MotionEvent.ACTION_UP:
            performClick();
            break;
        default:
            break;
        }
        return true;
    }
}
