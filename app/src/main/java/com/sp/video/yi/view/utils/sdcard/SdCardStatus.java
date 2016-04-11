package com.sp.video.yi.view.utils.sdcard;

import android.content.Context;
import android.os.Environment;

import com.nd.hy.android.commons.util.Ln;

import java.io.File;

/**
 * Created by huangb on 2015/5/18,0018.
 */
public class SdCardStatus {
    private static String CACHE_FOLDER_NAME;
    private static String NONE_SD_CARD_PROMPT = "您的手机中sd卡不存在";

    public SdCardStatus() {
    }

    public static void init(Context context, String cacheFolderName) {
        CACHE_FOLDER_NAME = cacheFolderName;
        hasSdCard();
    }

    public static boolean hasSdCard() {
        String sdCardPath = null;
        sdCardPath = getSDPath();
        if(null == sdCardPath) {
            Ln.e(NONE_SD_CARD_PROMPT, new Object[0]);
            return false;
        } else {
            return true;
        }
    }

    public static String getDefaulstCacheDirInSdCard() throws IllegalStateException {
        String sdCardPath = null;
        sdCardPath = getSDPath();
        if(null == sdCardPath) {
            throw new IllegalStateException(NONE_SD_CARD_PROMPT);
        } else {
            return sdCardPath + File.separator + CACHE_FOLDER_NAME;
        }
    }

    public static String getSDPath() {
        boolean sdCardExist = Environment.getExternalStorageState().equals("mounted");
        if(sdCardExist) {
            return Environment.getExternalStorageDirectory().getAbsolutePath();
        } else {
            DevMountInfo dev = DevMountInfo.getInstance();
            DevInfo info = dev.getExternalInfo();
            if(null == info) {
                return null;
            } else {
                String sd2Path = info.getPath();
                return sd2Path != null && sd2Path.length() > 0?sd2Path:null;
            }
        }
    }
}