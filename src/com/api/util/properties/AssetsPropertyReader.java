package com.api.util.properties;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import android.content.Context;
import android.content.res.AssetManager;

import com.api.exception.ApplicationException;

public class AssetsPropertyReader {
    private Context context;
    private Properties properties;

    public AssetsPropertyReader(Context context) {
        this.context = context;
        /**
         * Constructs a new Properties object.
         */
        properties = new Properties();
    }

    public Properties getProperties(String FileName) {

        try {
            /**
             * getAssets() Return an AssetManager instance for your
             * application's package. AssetManager Provides access to an
             * application's raw asset files;
             */
            AssetManager assetManager = context.getAssets();
            /**
             * Open an asset using ACCESS_STREAMING mode. This
             */
            InputStream inputStream = assetManager.open(FileName);
            /**
             * Loads properties from the specified InputStream,
             */
            properties.load(inputStream);

        } catch (IOException e) {
            throw new ApplicationException(String.format(" %s Properties File Not Exist", FileName), e);
        }
        return properties;

    }

}
