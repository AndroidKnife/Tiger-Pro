package com.hwangjr.mvp.base.presenter;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hwangjr.mvp.base.view.MVPView;

public interface Presenter<V extends MVPView> {
    void attach(V view);

    void onCreate(@Nullable Bundle savedInstanceState);

    void onResume();

    void onPause();

    void onDestroy();

    void onSaveInstanceState(Bundle outState);

    void onRestoreInstanceState(Bundle savedInstanceState);
}