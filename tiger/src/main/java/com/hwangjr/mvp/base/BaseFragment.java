package com.hwangjr.mvp.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;

import com.hwangjr.mvp.R;
import com.hwangjr.mvp.di.components.ActivityComponent;
import com.hwangjr.mvp.di.components.DaggerFragmentComponent;
import com.hwangjr.mvp.di.components.FragmentComponent;
import com.hwangjr.mvp.di.modules.FragmentModule;
import com.hwangjr.mvp.widget.SnackbarWrapper;
import com.squareup.leakcanary.RefWatcher;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    protected View mainView;
    private Unbinder unbinder;
    private boolean isVisibleToUser;
    private boolean isViewCreated = false;
    private boolean isPageRunning;

    private ActivityComponent activityComponent;
    private FragmentComponent fragmentComponent;

    private ViewStub tipsViewStub;
    private View tipsView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    // Should call after activity created
    @Deprecated
    protected ActivityComponent getActivityComponent() {
        try {
            if (activityComponent == null) {
                activityComponent = ((BaseActivity) getActivity()).getActivityComponent();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return activityComponent;
    }

    protected FragmentComponent getFragmentComponent() {
        try {
            if (fragmentComponent == null) {
                fragmentComponent = DaggerFragmentComponent.builder()
                        .appComponent(AppApplication.getAppComponent())
                        .fragmentModule(new FragmentModule(this)).build();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fragmentComponent;
    }

    protected abstract int getLayoutId();

    protected abstract void initView();

    protected void initData(@Nullable Bundle savedInstanceState) {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mainView == null) {
            mainView = inflater.inflate(getLayoutId(), container, false);
            unbinder = ButterKnife.bind(this, mainView);
            initView();
            isViewCreated = true;
            initData(savedInstanceState);
        }
        return mainView;
    }

    @Override
    public void onResume() {
        super.onResume();
        isPageRunning = true;
        if (getUserVisibleHint()) {
            onUserVisible(true);
        }
    }

    @Override
    public void onPause() {
        onUserVisible(false);
        isPageRunning = false;
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (unbinder != null) {
            unbinder.unbind();
        }
        Fragment fragment = getTargetFragment();
        if (fragment != null && fragment instanceof BaseFragment) {
            BaseFragment targetFragment = (BaseFragment) fragment;
            targetFragment.onFragmentResult(getTargetRequestCode());
        }
        if (mainView != null && mainView.getParent() != null) {
            ((ViewGroup) mainView.getParent()).removeView(mainView);
        }
        super.onDestroy();
        RefWatcher refWatcher = AppApplication.getRefWatcher(getActivity());
        refWatcher.watch(this);
    }

    protected void onFragmentResult(int requestCode) {
    }

    protected boolean isVisibleToUser() {
        return isVisibleToUser;
    }

    protected boolean isViewCreated() {
        return isViewCreated;
    }

    protected boolean isPageRunning() {
        return isPageRunning;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        this.isVisibleToUser = isVisibleToUser;
        onUserVisible(isVisibleToUser);
    }

    protected void onUserVisible(boolean isVisibleToUser) {
    }

    protected View getTipsViewFromActivity() {
        return ((BaseActivity) getActivity()).getTipsView();
    }

    protected void initTipsView() {
        tipsView = getTipsViewFromActivity();
        if (tipsView == null) {
            if (getTipsStubViewId() > 0) {
                tipsViewStub = (ViewStub) mainView.findViewById(getTipsStubViewId());
                if (getTipsRootViewLayoutId() > 0) {
                    tipsViewStub.setLayoutResource(getTipsRootViewLayoutId());
                    tipsView = tipsViewStub.inflate();
                }
            }
        }
    }

    public int getTipsStubViewId() {
        return R.id.mvp_tips;
    }

    public int getTipsRootViewLayoutId() {
        return R.layout.mvp_layout_coordinator;
    }

    public void showTips(String msg) {
        if (tipsViewStub == null) {
            initTipsView();
        }
        if (tipsView != null) {
            SnackbarWrapper.wrapper(tipsView, msg).show();
        }
    }
}