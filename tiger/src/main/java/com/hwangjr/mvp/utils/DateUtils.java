package com.hwangjr.mvp.utils;

import android.text.format.DateFormat;

import java.util.Date;

public final class DateUtils {

    public static final String SDF_YYYYMMDD = "yyyy-MM-dd";
    public static final String SDF_YMDHHMMSS = "yyyy-MM-dd HH:mm:ss";

    public static String format(String dateFormat, Date date) {
        return String.valueOf(DateFormat.format(dateFormat, date));
    }
}
