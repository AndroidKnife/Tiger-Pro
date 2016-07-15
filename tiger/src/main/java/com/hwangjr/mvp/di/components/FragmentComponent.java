package com.hwangjr.mvp.di.components;

import android.support.v4.app.Fragment;

import com.hwangjr.mvp.di.modules.FragmentModule;
import com.hwangjr.mvp.di.scopes.PerFragment;

import dagger.Component;

@PerFragment
@Component(dependencies = AppComponent.class, modules = {FragmentModule.class})
public interface FragmentComponent {
    Fragment getFragment();
}