package com.hwangjr.mvp.widget;

import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.TextView;

public class SnackbarWrapper {
    public static Snackbar wrapper(View rootView, String msg) {
        Snackbar snackbar = Snackbar.make(rootView, msg, Snackbar.LENGTH_LONG);
        View snackView = snackbar.getView();
        snackView.setBackgroundColor(0xFF5B5B5B);
        TextView textView = (TextView) snackView.findViewById(android.support.design.R.id.snackbar_text);
        textView.setTextColor(0xFFFF9224);
        return snackbar;
    }
}