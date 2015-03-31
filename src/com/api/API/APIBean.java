package com.api.API;

import java.io.Serializable;
import java.util.Properties;

import android.content.Context;
import android.graphics.Typeface;

import com.api.application.font.FontFactory;
import com.api.application.font.Fonts;

public class APIBean implements Serializable {

    private static final long serialVersionUID = -5639712960249461857L;

    private static final String E001 = "YOU MUST CALL API.Initialize(Context)";

    private Properties sProperties;
    private boolean sDebug;
    private Fonts sFont;
    private Context sApplicationContext;

    void setProperties(Properties _properties) {
        this.sProperties = _properties;
    }

    public Properties getProperties() {
        if (this.sProperties == null)
            throw new NullPointerException(E001);
        return this.sProperties;
    }

    void setDebug(boolean _debug) {
        this.sDebug = _debug;
    }

    public boolean isDebug() {
        return this.sDebug;
    }

    void setFonts(Fonts _font) {
        this.sFont = _font;
    }

    public Fonts getFonts() {
        if (this.sFont == null)
            throw new NullPointerException(E001);
        return this.sFont;
    }

    void setApplicationContext(Context _context) {
        this.sApplicationContext = _context;
    }

    public Context getApplicationContext() {
        return this.sApplicationContext;
    }

    public Typeface getTypeface() {
        return FontFactory.getTypeFace(this.sFont);
    }
}
