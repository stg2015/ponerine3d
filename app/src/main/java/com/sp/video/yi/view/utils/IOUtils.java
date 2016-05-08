package com.sp.video.yi.view.utils;

import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;


/**
 * @author Yangz
 * @version 14-5-19
 */
public class IOUtils {

    static String UTF8 = "utf-8";
    static int BUFFER_SIZE = 8 * 1024;

    public static void silentlyClose(Closeable... closeables) {
        for (Closeable cl : closeables) {
            try {
                if (cl != null) {
                    cl.close();
                }
            } catch (Exception e) {
                Log.e("IOUtils", "silentlyClose", e);
            }
        }
    }

    public static String readToString(InputStream is) throws IOException {
        byte[] data = readToByteArray(is);
        return new String(data, UTF8);
    }

    public static byte[] readToByteArray(InputStream is) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[BUFFER_SIZE];
        int len;
        while ((len = is.read(buffer)) != -1) {
            baos.write(buffer, 0, len);
        }
        return baos.toByteArray();
    }

}
