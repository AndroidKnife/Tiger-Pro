package com.hwangjr.mvp.di.components;

import android.app.Activity;

import com.hwangjr.mvp.di.modules.ActivityModule;
import com.hwangjr.mvp.di.scopes.PerActivity;
import com.hwangjr.tiger.ui.MainActivity;

import dagger.Component;

@PerActivity
@Component(dependencies = AppComponent.class, modules = {ActivityModule.class})
public interface ActivityComponent {
    Activity getActivity();

    void inject(MainActivity activity);
}