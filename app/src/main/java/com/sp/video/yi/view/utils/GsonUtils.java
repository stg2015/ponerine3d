package com.sp.video.yi.view.utils;

import com.fuckhtc.gson.Gson;

/**
 * GsonUtils
 * Date: 13-10-17
 *
 * @author Yangz
 */
public class GsonUtils {

    private GsonUtils() {
    }

    public static String convertString(Object obj) {
        return new Gson().toJson(obj);
    }

    public static <T> T convertObject(String src, Class<T> clazz) {
        return new Gson().fromJson(src, clazz);
    }

}