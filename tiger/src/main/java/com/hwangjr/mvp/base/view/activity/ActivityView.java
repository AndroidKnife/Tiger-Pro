package com.hwangjr.mvp.base.view.activity;

import android.os.Bundle;

import com.hwangjr.mvp.base.BaseActivity;
import com.hwangjr.mvp.base.presenter.ActivityPresenter;
import com.hwangjr.mvp.base.view.MVPView;

import javax.inject.Inject;

public abstract class ActivityView<P extends ActivityPresenter> extends BaseActivity implements MVPView<P> {

    @Inject
    protected P presenter;

    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }

    public boolean isActive() {
        return !isFinishing();
    }

    protected abstract void injectComponent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        injectComponent();
        presenter.attach(this);
        presenter.onCreate(savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.onResume();
    }

    @Override
    public void onPause() {
        presenter.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        presenter.onSaveInstanceState(savedInstanceState);
    }
}