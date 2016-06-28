package com.hwangjr.mvp.di.components;

import android.app.Application;

import com.hwangjr.mvp.di.modules.AppModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {AppModule.class})
public interface AppComponent {

    Application getApplication();
}
