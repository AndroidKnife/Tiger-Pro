package com.hwangjr.mvp.ui;

import com.hwangjr.mvp.R;
import com.hwangjr.mvp.base.view.activity.ActivityView;

public class MainActivity extends ActivityView<MainActivityPresenter> {
    @Override
    protected void injectComponent() {
        getActivityComponent().inject(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }
}
