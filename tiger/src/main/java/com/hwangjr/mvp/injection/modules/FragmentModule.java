package com.hwangjr.mvp.injection.modules;

import android.support.v4.app.Fragment;

import com.hwangjr.mvp.injection.scopes.PerFragment;

import dagger.Module;
import dagger.Provides;

@Module
public class FragmentModule {

    private final Fragment fragment;

    public FragmentModule(Fragment fragment) {
        this.fragment = fragment;
    }

    @PerFragment
    @Provides
    public Fragment provideFragment() {
        return fragment;
    }
}