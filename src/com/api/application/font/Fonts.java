package com.api.application.font;

import java.util.Locale;

import com.api.util.StringUtil;

public enum Fonts {

    AZUKI("azuki.ttf", "fonts/azuki.zip"), NONE("", "");

    private String ttfPath;
    private String zipPath;

    private Fonts(String ttfPath, String zipPath) {
        this.ttfPath = ttfPath;
        this.zipPath = zipPath;
    }

    public String getTtfPath() {
        return this.ttfPath;
    }

    public String getZipPath() {
        return this.zipPath;
    }

    public static Fonts parse(String fontName) {
        return valueOf(fontName.toUpperCase(Locale.ENGLISH));
    }

    /**
     * 指定した文字列が存在するか判定する
     * 
     * @param fontName
     * @return 空値または存在しないまたはNone/True
     */
    public static boolean exist(String fontName) {
        if (StringUtil.isEmpty(fontName))
            return false;
        try {
            valueOf(fontName);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static boolean isNone(Fonts font) {
        return NONE == font;
    }
}