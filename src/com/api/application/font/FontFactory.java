package com.api.application.font;

import java.io.IOException;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Typeface;

import com.api.API.API;
import com.api.exception.ApplicationException;

public class FontFactory {

    private static HashMap<String, Typeface> typefaces;

    static {
        typefaces = new HashMap<String, Typeface>();
    }

    private FontFactory() {
    }

    public static Typeface getTypeFace(String fontName) {
        return getTypeFace(Fonts.parse(fontName));
    }

    public static Typeface getTypeFace(Fonts font) {
        Context context = API.getBean().getApplicationContext();

        if (typefaces.get(font.name()) == null) {
            if (!context.getFileStreamPath(font.getTtfPath()).exists()) {
                try {
                    ZipFileFactory.createFile(font.getZipPath());
                } catch (IOException e) {
                    throw new ApplicationException("フォント生成失敗", e);
                }

            }
            typefaces.put(font.name(), Typeface.createFromFile(context.getFilesDir() + "/" + font.getTtfPath()));
        }

        return typefaces.get(font.name());
    }

}
