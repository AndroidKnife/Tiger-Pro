package com.hwangjr.mvp.utils;

import android.os.Environment;

public class SDCardUtils {
    public static boolean hasSDCard() {
        return Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED);
    }
}
