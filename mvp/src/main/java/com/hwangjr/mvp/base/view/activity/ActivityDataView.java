package com.hwangjr.mvp.base.view.activity;

import com.hwangjr.mvp.base.presenter.ActivityPresenter;
import com.hwangjr.mvp.base.view.DataView;

public abstract class ActivityDataView<P extends ActivityPresenter> extends ActivityView<P> implements DataView {

    @Override
    public void onResume() {
        super.onResume();
        loadData();
    }

    @Override
    public void reloadData() {
        loadData();
    }
}