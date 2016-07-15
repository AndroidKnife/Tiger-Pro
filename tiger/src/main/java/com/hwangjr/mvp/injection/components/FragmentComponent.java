package com.hwangjr.mvp.injection.components;

import android.support.v4.app.Fragment;

import com.hwangjr.mvp.injection.modules.FragmentModule;
import com.hwangjr.mvp.injection.scopes.PerFragment;

import dagger.Component;

@PerFragment
@Component(dependencies = AppComponent.class, modules = {FragmentModule.class})
public interface FragmentComponent {
    Fragment getFragment();
}