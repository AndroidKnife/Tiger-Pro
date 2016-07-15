package com.hwangjr.tiger.ui;

import com.hwangjr.mvp.base.presenter.ActivityPresenter;
import com.hwangjr.mvp.di.scopes.PerActivity;

import javax.inject.Inject;

@PerActivity
public class MainActivityPresenter extends ActivityPresenter<MainActivity> {
    @Inject
    public MainActivityPresenter() {
    }
}
