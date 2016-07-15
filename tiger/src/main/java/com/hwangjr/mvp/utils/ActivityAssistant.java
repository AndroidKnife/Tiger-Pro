package com.hwangjr.mvp.utils;

import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.view.View;
import android.widget.FrameLayout;

public class ActivityAssistant {
    // For more information, see
    // https://code.google.com/p/android/issues/detail?id=5497
    // To use this class, simply invoke assistActivity() on an Activity that already has its content view set.

    private View mChildOfContent;
    private int mUsableHeightPrevious;
    private FrameLayout.LayoutParams frameLayoutParams;
    private int mWindowHeight;

    public static void assistActivity(Activity activity) {
        new ActivityAssistant(activity);
    }

    private ActivityAssistant(final Activity activity) {
        final FrameLayout content = (FrameLayout) activity.findViewById(android.R.id.content);
        mWindowHeight = activity.getWindowManager().getDefaultDisplay().getHeight();
        mChildOfContent = content.getChildAt(0);
        mChildOfContent.getViewTreeObserver()
                .addOnGlobalLayoutListener(() -> {
                    possiblyResizeChildOfContent(activity);
                });
        frameLayoutParams = (FrameLayout.LayoutParams) mChildOfContent.getLayoutParams();
    }

    private void possiblyResizeChildOfContent(Activity activity) {
        int usableHeightNow = computeUsableHeight();
        if (usableHeightNow != mUsableHeightPrevious) {
            int usableHeightSansKeyboard = mWindowHeight;
            int heightDifference = usableHeightSansKeyboard - usableHeightNow;
            if (heightDifference > (usableHeightSansKeyboard / 4)) {
                frameLayoutParams.height = usableHeightSansKeyboard - heightDifference
                        + getStatusBarHeight(activity);
            } else {
                frameLayoutParams.height = usableHeightSansKeyboard;
            }
            mChildOfContent.requestLayout();
            mUsableHeightPrevious = usableHeightNow;
        }
    }

    private int computeUsableHeight() {
        Rect r = new Rect();
        mChildOfContent.getWindowVisibleDisplayFrame(r);
        return (r.bottom - r.top);
    }

    public int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources()
                    .getDimensionPixelSize(resourceId);
        }
        return result;
    }
}
