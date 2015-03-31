package com.api.application.font;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.content.res.AssetManager;

import com.api.API.API;

/**
 * Assets フォルダのzipファイルを操作する。
 * 
 * @author aturo
 * 
 */
public class ZipFileFactory {

    private ZipFileFactory() {

    }

    /**
     * Assets に格納されている zip ファイルをデフォルトパスに解凍する。<br>
     * 
     * @param context ApplicationContext<br>
     * @param fontName 拡張子まで指定<br>
     * @throws IOException
     */
    public static void createFile(String fontName) throws IOException {
        Context context = API.getBean().getApplicationContext();
        String path = null;

        AssetManager am = context.getResources().getAssets();
        InputStream is = am.open(fontName, AssetManager.ACCESS_STREAMING);
        ZipInputStream zis = new ZipInputStream(is);
        ZipEntry ze = zis.getNextEntry();

        if (ze != null) {
            path = context.getFilesDir().toString() + "/" + ze.getName();
            FileOutputStream fos = new FileOutputStream(path, false);
            byte[] buf = new byte[1024];
            int size = 0;

            while ((size = zis.read(buf, 0, buf.length)) > -1) {
                fos.write(buf, 0, size);
            }
            fos.flush();
            fos.close();
            zis.closeEntry();
        }
        zis.close();
    }

}