package com.hwangjr.mvp.base;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewStub;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import com.hwangjr.mvp.MVPApplication;
import com.hwangjr.mvp.injection.components.ActivityComponent;
import com.hwangjr.mvp.injection.components.DaggerActivityComponent;
import com.hwangjr.mvp.injection.modules.ActivityModule;
import com.hwangjr.mvp.utils.ActivityAssistant;
import com.hwangjr.mvp.utils.StatusBarUtils;
import com.hwangjr.mvp.widget.SnackbarWrapper;
import com.hwangjr.tiger.R;
import com.squareup.leakcanary.RefWatcher;

import java.lang.reflect.Field;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity {
    public static final String FIX_FRAGMENT_OVERLAPPING = "fix.fragment.overlapping";
    static final String FRAGMENTS_TAG = "android:support:fragments";

    private ActivityComponent activityComponent;
    private Unbinder unbinder;
    private boolean fixFragmentOverlapping;

    private ViewStub tipsViewStub;
    private View tipsView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        translucentStatus();
        if (activityComponent == null) {
            activityComponent = DaggerActivityComponent.builder()
                    .appComponent(MVPApplication.getAppComponent())
                    .activityModule(new ActivityModule(this)).build();
        }
    }

    protected ActivityComponent getActivityComponent() {
        return activityComponent;
    }

    protected abstract int getLayoutId();

    @Override
    protected void onDestroy() {
        unbinder.unbind();
        super.onDestroy();
        fixInputMethodManagerLeak(this);
        RefWatcher refWatcher = MVPApplication.getRefWatcher(this);
        refWatcher.watch(this);
    }

    private void translucentStatus() {
        StatusBarUtils.setColor(this, getResources().getColor(R.color.colorPrimaryDark));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && getWindow().getAttributes().softInputMode == WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE) {
            ActivityAssistant.assistActivity(this);
        }
    }

    private void fixInputMethodManagerLeak(Context destContext) {
        if (destContext != null) {
            InputMethodManager imm = (InputMethodManager) destContext.getSystemService(
                    INPUT_METHOD_SERVICE);
            if (imm != null) {
                String[] arr = new String[]{"mCurRootView", "mServedView", "mNextServedView"};
                Field f = null;
                Object objGet = null;
                for (int i = 0; i < arr.length; i++) {
                    String param = arr[i];
                    try {
                        f = imm.getClass()
                                .getDeclaredField(param);
                        if (!f.isAccessible()) {
                            f.setAccessible(true);
                        }
                        objGet = f.get(imm);
                        if (objGet != null && objGet instanceof View) {
                            View vGet = (View) objGet;
                            if (vGet.getContext() == destContext) {
                                f.set(imm, null);
                            } else {
                                break;
                            }
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FIX_FRAGMENT_OVERLAPPING, fixFragmentOverlapping);
        if (fixFragmentOverlapping) {
            // drop fragments state, it's not the best way to fix overlapping
            outState.putSerializable(FRAGMENTS_TAG, null);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fixFragmentOverlapping = savedInstanceState.getBoolean(FIX_FRAGMENT_OVERLAPPING, false);
    }

    protected void fixFragmentOverlapping() {
        fixFragmentOverlapping = true;
    }

    public void initTipsView() {
        if (getTipsStubViewId() > 0) {
            tipsViewStub = (ViewStub) findViewById(getTipsStubViewId());
            if (getTipsRootViewLayoutId() > 0) {
                tipsViewStub.setLayoutResource(getTipsRootViewLayoutId());
                tipsView = tipsViewStub.inflate();
            }
        }
    }

    public View getTipsView() {
        if (tipsViewStub == null) {
            initTipsView();
        }
        return tipsView;
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
