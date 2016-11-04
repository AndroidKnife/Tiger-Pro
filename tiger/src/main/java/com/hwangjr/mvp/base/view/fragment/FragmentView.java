package com.hwangjr.mvp.base.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hwangjr.mvp.base.BaseFragment;
import com.hwangjr.mvp.base.presenter.FragmentPresenter;
import com.hwangjr.mvp.base.view.MVPView;

import javax.inject.Inject;

public abstract class FragmentView<P extends FragmentPresenter> extends BaseFragment implements MVPView<P> {

    @Inject
    protected P presenter;

    public void setPresenter(P presenter) {
        this.presenter = presenter;
    }

    public boolean isActive() {
        return isAdded();
    }

    protected abstract void injectComponent();

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        injectComponent();
        presenter.attach(this);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        presenter.onSaveInstanceState(savedInstanceState);
        presenter.onCreate(savedInstanceState);
    }

    @Override
    protected void initData(@Nullable Bundle arguments) {
        super.initData(arguments);
        presenter.initData(arguments);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        presenter.onActivityCreated(savedInstanceState);
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
    public void onDestroyView() {
        presenter.onDestroyView();
        super.onDestroyView();
    }

    @Override
    public void onDestroy() {
        presenter.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        presenter.onSaveInstanceState(outState);
    }
}