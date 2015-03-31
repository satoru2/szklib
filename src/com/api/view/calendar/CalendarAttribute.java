package com.api.view.calendar;

import java.util.HashMap;
import java.util.Map;

import android.graphics.Color;
import android.util.AttributeSet;

import com.api.API.API;
import com.api.application.font.Fonts;
import com.api.time.CalendarTime;
import com.api.time.CalendarTime.Mode;
import com.api.util.StringUtil;

/**
 * カレンダークラスの属性クラス
 * 
 * @author aturo
 * 
 */
public class CalendarAttribute {

    /**
     * NameSpace
     */
    private static final String NAME_SPACE = "http://com.api.view.calendar/CalendarGrid";

    /**
     * プロパティ変更通知
     */
    private OnAttrChangeListener _listener;

    /**
     * プロパティコレクション
     */
    private Map<Properties, Object> _properties;

    /**
     * xml から作った時の AttributeSet
     */
    private AttributeSet _attrs;

    /**
     * プロパティ列挙
     * 
     * @author aturo
     * 
     */
    enum Properties {
        /**
         * 指定フォント
         */
        FONT_NAME("font", API.getBean().getFonts(), Fonts.class)
        /**
         * 枠線色
         */
        , BORDER_COLOR("border_color", Color.GRAY, Color.class)
        /**
         * 枠線太さ
         */
        , BORDER_WIDTH("border_width", 1, Integer.class)
        /**
         * フォーカステキストカラー
         */
        , FOCUSED_TEXT_COLOR("focused_textcolor", Color.RED, Color.class)
        /**
         * フォーカス時背景色<br>
         * ForestGreen
         * 
         */
        , FOCUSED_BACKGROUND("focusedback_ground", Color.parseColor("#228B22"), Color.class)
        /**
         * 文字色
         */
        , TEXT_COLOR("textcolor", Color.BLACK, Color.class)
        /**
         * 背景色
         */
        , BACKGROUND("background", Color.TRANSPARENT, Color.class)
        /**
         * 範囲外文字色（当月外）
         */
        , OUT_RANGE_TEXT_COLOR("outrange_textcolor", Color.DKGRAY, Color.class)
        /**
         * 範囲外背景色（当月外）
         */
        , OUT_RANGE_BACKGROUND("outrange_backfground", Color.LTGRAY, Color.class)
        /**
         * 祝日背景色
         */
        , HOLIDAY_BACKGROUND("holiday_background", Color.parseColor("#EB7988"), Color.class)
        /**
         * 祝日文字色
         */
        , HOLIDAY_TEXT_COLOR("holiday_textcolor", Color.RED, Color.class)
        /**
         * カレント日付
         */
        , CURRENT_DAY("currentday", CalendarTime.getInstance(Mode.Today), CalendarTime.class);
        /**
         * プロパティ名（xml指定）
         */
        private String _propName;
        /**
         * 初期値
         */
        private Object _defVal;
        /**
         * 型情報
         */
        private Class<?> _clazz;

        private Properties(String propName, Object defVal, Class<?> clazz) {
            _propName = propName;
            _defVal = defVal;
            _clazz = clazz;
        }

        /**
         * xmlプロパティ名を取得する
         * 
         * @return xmlプロパティ名
         */
        String getPropName() {
            return _propName;
        }

        /**
         * 初期値を取得する
         * 
         * @return 初期値
         */
        Object getDefaultValue() {
            return _defVal;
        }

        /**
         * プロパティの型を取得する
         * 
         * @return 型
         */
        Class<?> getClazz() {
            return _clazz;
        }

        /**
         * 指定した値が有効か判定する
         * 
         * @param clazz 判定クラス
         * @return 判定可能/ True
         */
        static boolean isValid(Properties prop, Object value) {

            if (prop._clazz == Fonts.class) {
                return !Fonts.exist((String) value);
            } else if (prop._clazz == Integer.class) {
                return StringUtil.isNumber(value.toString());
            } else if (prop._clazz == Color.class) {
                return StringUtil.isColor(value.toString());
            } else if (prop._clazz == CalendarTime.class) {
                if (CalendarTime.canParse(value.toString())) {
                    // 解析できる文字列の場合 True
                    return true;
                }
                // 解析できない場合
                return value instanceof CalendarTime;

            } else if (prop._clazz == String.class) {
                return true;
            }

            return false;
        }

        /**
         * 指定した文字列を定義された値に変換して返す
         * 
         * @param prop
         * @return
         */
        static Object parse(Properties prop, String value) {
            if (prop._clazz == Fonts.class) {
                return Fonts.parse(value);
            } else if (prop._clazz == String.class) {
                return value;
            } else if (prop._clazz == Integer.class) {
                return Integer.parseInt(value);
            } else if (prop._clazz == Color.class) {
                return Color.parseColor(value);
            } else if (prop._clazz == CalendarTime.class) {
                return CalendarTime.parseTime(value.toString());
            }

            throw new IllegalArgumentException(String.format(
                    "InValidArgument : targetPropety[%s] value[%s] valueType [%s]", prop.getPropName(),
                    value.toString(), prop.getClazz().toString()));
        }
    }

    /**
     * カレンダー属性クラスを初期化する
     */
    CalendarAttribute() {
        // 属性情報の初期化
        this._properties = new HashMap<Properties, Object>();
        // 初期値の登録
        this.setDefaultValue();
    }

    /**
     * AttributeSet からカレンダー属性クラスを初期化する
     * 
     * @param attrs　AttributesSet
     */
    CalendarAttribute(AttributeSet attrs) {
        this();
        _attrs = attrs;
        this.setAttribute();
    }

    /**
     * リスナーをセットする
     * 
     * @param listener セットするリスナー
     */
    public void setOnAttrChangeListener(OnAttrChangeListener listener) {
        _listener = listener;
    }

    /**
     * プロパティをセットする
     * 
     * @param prop 対象プロパティ
     * @param value 値
     */
    public void setAttr(Properties prop, Object value) {

        // 指定した値が有効か検証
        if (!Properties.isValid(prop, value))
            throw new IllegalArgumentException(String.format(
                    "InValidArgument : targetPropety[%s] value[%s] valueType [%s]", prop.getPropName(),
                    value.toString(), prop.getClazz().toString()));

        Object oldVal = _properties.get(prop);
        // 値を格納
        _properties.put(prop, value);

        // 変更があれば
        if (_listener != null && changed(prop, value, oldVal))
            // リスナーがセットされていたら通知
            _listener.onAttrChangedListener(prop, value, oldVal);
    }

    /**
     * 変更があるかを判定
     * 
     * @param prop
     * @param value
     * @param oldVal
     * @return
     */
    private boolean changed(Properties prop, Object value, Object oldVal) {
        if (prop.getClazz() == Integer.class) {
            return (Integer) value != (Integer) oldVal;
        } else if (prop.getClazz() == String.class) {
            return !((String) value).equals((String) oldVal);
        } else if (prop.getClazz() == CalendarTime.class) {
            return CalendarTime.compare((CalendarTime) value, (CalendarTime) oldVal) != 0;
        }
        return false;
    }

    /**
     * 全てのプロパティの更新を通知する
     */
    public void all() {
        if (_listener != null) {
            for (Properties prop : Properties.values()) {
                if (Properties.FONT_NAME == prop)
                    // フォント名は更新を通知しない
                    continue;
                Object value = _properties.get(prop);
                _listener.onAttrChangedListener(prop, value, null);
            }
        }
    }

    /**
     * プロパティに設定されている値を取得する
     * 
     * @param prop 対象プロパティ
     * @return 値
     */
    public Object getValue(Properties prop) {
        if (prop == Properties.CURRENT_DAY)
            return ((CalendarTime) _properties.get(prop)).copy();
        return _properties.get(prop);
    }

    /**
     * Properties に定義された初期値を登録する。
     */
    private void setDefaultValue() {
        for (Properties prop : Properties.values()) {
            this._properties.put(prop, prop.getDefaultValue());
        }
    }

    /**
     * プロパティのセット<br>
     * 
     * @param attrs
     */
    private void setAttribute() {
        if (_attrs == null) {
            return;
        }

        String strProp;
        for (Properties prop : Properties.values()) {
            // 値を取得
            strProp = _attrs.getAttributeValue(NAME_SPACE, prop.getPropName());
            // 空値でない場合
            if (!StringUtil.isEmpty(strProp)) {
                // 有効か検証する
                if (!Properties.isValid(prop, strProp)) {
                    throw new IllegalArgumentException(String.format(
                            "InValidArgument : targetPropety[%s] value[%s] valueType [%s]", prop.getPropName(),
                            strProp, prop.getClazz().toString()));
                }
                // 値を格納する
                _properties.put(prop, Properties.parse(prop, strProp));
            }
        }
    }

    /**
     * AttributeSet を取得する
     * 
     * @return AttributeSet
     */
    public AttributeSet getAttributeSet() {
        return _attrs;
    }
}
