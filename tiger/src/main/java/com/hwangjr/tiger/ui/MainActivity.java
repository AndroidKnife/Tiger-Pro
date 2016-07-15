package com.hwangjr.tiger.ui;

import com.hwangjr.mvp.base.view.activity.ActivityView;
import com.hwangjr.tiger.R;

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
