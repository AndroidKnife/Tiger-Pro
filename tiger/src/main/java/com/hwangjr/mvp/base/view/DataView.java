package com.hwangjr.mvp.base.view;

public interface DataView {
    public static final int LOAD_ON_START = 1;
    public static final int LOAD_ON_VISIBLE = 2;
    public static final int LOAD_NOTHING = 3;

    void loadData();

    void reloadData();
}