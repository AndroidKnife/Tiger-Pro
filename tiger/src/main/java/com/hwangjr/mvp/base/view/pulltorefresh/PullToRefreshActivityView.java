package com.hwangjr.mvp.base.view.pulltorefresh;

import android.os.Bundle;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.AbsListView;

import com.hwangjr.mvp.base.presenter.ActivityPresenter;
import com.hwangjr.mvp.base.view.activity.ActivityView;
import com.hwangjr.mvp.utils.DensityUtils;
import com.hwangjr.tiger.R;

import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.MaterialHeader;

public abstract class PullToRefreshActivityView<P extends ActivityPresenter> extends ActivityView<P> implements PtrHandler {

    public static final long REFRESH_TIME_INTERVAL = 2000 * 1000l;

    public static final int PULL_MODE_REFRESH = 0;
    public static final int PULL_MODE_REFRESH_LOADMORE = 1;
    public static final int PULL_MODE_DISABLED = 2;

    protected PtrFrameLayout ptrFrameLayout;
    private long lastLoadDataTime = System.currentTimeMillis();

    public long getLastLoadDataTime() {
        return lastLoadDataTime;
    }

    public void setLastLoadDataTime(long lastLoadDataTime) {
        this.lastLoadDataTime = lastLoadDataTime;
    }

    protected int getPtrFrameLayoutId() {
        return R.id.ptr_frame;
    }

    protected PtrFrameLayout getPtrFrameLayout() {
        return ptrFrameLayout;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
    }

    protected void initView() {
        if (getPullMode() != PULL_MODE_DISABLED && ptrFrameLayout == null && getPtrFrameLayoutId() != 0) {
            ptrFrameLayout = (PtrFrameLayout) findViewById(getPtrFrameLayoutId());

            final MaterialHeader header = new MaterialHeader(this);
            int[] colors = getResources().getIntArray(R.array.material_colors);
            header.setColorSchemeColors(colors);
            header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
            header.setPadding(0, DensityUtils.dip2px(this, 15), 0,
                    DensityUtils.dip2px(this, 10));
            header.setPtrFrameLayout(ptrFrameLayout);

            ptrFrameLayout.setLoadingMinTime(1000);
            ptrFrameLayout.setDurationToCloseHeader(1500);
            ptrFrameLayout.setHeaderView(header);
            ptrFrameLayout.addPtrUIHandler(header);
            ptrFrameLayout.setPullToRefresh(false);
            ptrFrameLayout.autoRefresh(false);
            ptrFrameLayout.setPtrHandler(this);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (System.currentTimeMillis() - getLastLoadDataTime() > REFRESH_TIME_INTERVAL) {
            if (getPullMode() != PULL_MODE_DISABLED && ptrFrameLayout != null) {
                setLastLoadDataTime(System.currentTimeMillis());
                ptrFrameLayout.autoRefresh();
            }
        }
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
        return !canChildScrollUp(content);
    }

    protected int getPullMode() {
        return PULL_MODE_REFRESH;
    }

    public void showRefreshComplete() {
        getPtrFrameLayout().refreshComplete();
    }

    public static boolean canChildScrollUp(View content) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (content instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) content;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
            } else {
                return ViewCompat.canScrollVertically(content, -1) || content.getScrollY() > 0;
            }
        } else {
            return ViewCompat.canScrollVertically(content, -1);
        }
    }
}
