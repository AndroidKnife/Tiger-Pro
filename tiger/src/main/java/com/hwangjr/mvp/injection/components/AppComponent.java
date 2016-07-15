package com.hwangjr.mvp.injection.components;

import android.app.Application;

import com.hwangjr.mvp.injection.modules.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    Application getApplication();
}
