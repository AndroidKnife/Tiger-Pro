package com.hwangjr.mvp.injection.modules;

import android.app.Activity;

import com.hwangjr.mvp.injection.scopes.PerActivity;

import dagger.Module;
import dagger.Provides;

@Module
public class ActivityModule {

    private final Activity activity;

    public ActivityModule(Activity activity) {
        this.activity = activity;
    }

    @PerActivity
    @Provides
    public Activity provideActivity() {
        return activity;
    }
}