package com.hwangjr.mvp.utils;

import android.content.Context;
import android.util.DisplayMetrics;

public final class DensityUtils {

    public static int dip2px(Context context, float dpValue) {
        final float scale = getDensity(context);
        return (int) (dpValue * scale + 0.5f);
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = getDensity(context);
        return (int) (pxValue / scale + 0.5f);
    }

    public static float getDensity(Context context) {
        return context.getResources()
                .getDisplayMetrics().density;
    }

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    public static String getDisplayPixels(Context context) {
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int screenHeight = dm.heightPixels;

        String displayPixels = screenWidth + "x" + screenHeight;

        return displayPixels;
    }
}
