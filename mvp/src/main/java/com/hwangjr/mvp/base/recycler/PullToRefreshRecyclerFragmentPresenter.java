package com.hwangjr.mvp.base.recycler;

import com.hwangjr.mvp.base.presenter.FragmentPresenter;

public abstract class PullToRefreshRecyclerFragmentPresenter<V extends PullToRefreshRecyclerFragmentView>
        extends FragmentPresenter<V> {

    protected boolean hasMoreData = true;

    public boolean hasMoreData() {
        return hasMoreData;
    }

    public abstract void loadData();

    public void reloadData() {
        loadData();
    }

    public abstract void loadMore(int totalItemCount);
}