package com.hwangjr.mvp.base.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hwangjr.mvp.base.view.MVPView;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public abstract class ActivityPresenter<V extends MVPView> implements Presenter<V> {

    protected V view;
    protected CompositeSubscription subscriptions = new CompositeSubscription();

    public void addSubscription(Subscription subscription) {
        subscriptions.add(subscription);
    }

    public void unsubscribe() {
        subscriptions.unsubscribe();
    }

    public boolean isViewAttached() {
        return this.view != null;
    }

    @Override
    public void attach(V view) {
        this.view = view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
    }

    @Override
    public void onResume() {
    }

    @Override
    public void onPause() {
    }

    @Override
    public void onDestroy() {
        unsubscribe();
        this.view = null;
    }
}