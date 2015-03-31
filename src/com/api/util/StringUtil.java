package com.api.util;

import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.graphics.Color;

public class StringUtil {
    /**
     * ロケール指定した
     * 
     * @param value
     * @return
     */
    public static String toUpperCase(String value) {
        return value.toUpperCase(Locale.getDefault());
    }

    /**
     * 空の場合にTrueを返す
     * 
     * @param value
     * @return
     */
    public static boolean isEmpty(String value) {
        return value == null || value.equals("");
    }

    /**
     * 整数かどうか判定する
     * 
     * @param value 検証文字列
     * @return 整数の場合/True
     */
    public static boolean isNumber(String value) {
        if (isEmpty(value))
            return false;
        String regex = "\\A[-]?[0-9]+\\z";
        Pattern p = Pattern.compile(regex);
        Matcher m1 = p.matcher(value);
        return m1.find();
    }

    /**
     * 指定した文字列がカラーか判定する
     * 
     * @param value　
     * @param value 検証文字列
     * @return カラーの場合/True
     */
    public static boolean isColor(String value) {
        if (isEmpty(value))
            return false;
        try {
            Color.parseColor(value);
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
