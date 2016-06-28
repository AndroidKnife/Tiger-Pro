package com.hwangjr.mvp.base.recycler;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewStub;

import com.hwangjr.mvp.R;
import com.hwangjr.mvp.base.view.DataView;
import com.hwangjr.mvp.base.view.pulltorefresh.PullToRefreshFragmentView;
import com.hwangjr.mvp.widget.DividerItemDecoration;

import in.srain.cube.views.ptr.PtrFrameLayout;

public abstract class PullToRefreshRecyclerFragmentView<P extends PullToRefreshRecyclerFragmentPresenter>
        extends PullToRefreshFragmentView<P> implements DataView {
    private static final int ITEM_LEFT_TO_LOAD_MORE = 10;

    public static final int LAYOUT_MANAGER_TYPE_LINEAR = 0;
    public static final int LAYOUT_MANAGER_TYPE_GRID = 1;
    public static final int LAYOUT_MANAGER_TYPE_STAGGERED_GRID = 2;

    private RecyclerView recyclerView;
    private int[] lastScrollPositions;
    private int layoutManagerType = LAYOUT_MANAGER_TYPE_LINEAR;

    private ViewStub moreProgress;
    private View moreProgressView;
    private boolean isLoadingMore;
    private int moreProgressId;

    protected RecyclerView.OnScrollListener internalOnScrollListener;
    protected RecyclerView.OnScrollListener externalOnScrollListener;

    protected int getRecyclerViewID() {
        return R.id.recycler_view;
    }

    protected int getMoreProgressViewID() {
        return R.layout.mvp_more_progress;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    @Override
    protected int getPullMode() {
        return PULL_MODE_REFRESH_LOADMORE;
    }

    @Override
    protected void initView() {
        super.initView();
        if (recyclerView == null) {
            if (getPullMode() == PULL_MODE_REFRESH_LOADMORE) {
                this.moreProgressId = getMoreProgressViewID();
                moreProgress = (ViewStub) mainView.findViewById(R.id.more_progress);
                moreProgress.setLayoutResource(moreProgressId);
                if (moreProgressId != 0) {
                    moreProgressView = moreProgress.inflate();
                }
                hideMoreProgress();
            }

            this.initRecyclerView(mainView);
            if (recyclerView != null) {
                recyclerView.setLayoutManager(getLayoutManager());
            }
            this.customizeRecyclerView();
            this.setAdapter(getAdapter());
        }
    }

    @Override
    protected void initData(@Nullable Bundle savedInstanceState) {
        super.initData(savedInstanceState);
        loadData();
    }

    @Override
    public void loadData() {
        presenter.loadData();
    }

    @Override
    public void reloadData() {
        presenter.reloadData();
    }

    private void initRecyclerView(View view) {
        View recyclerView = view.findViewById(getRecyclerViewID());
        if (recyclerView instanceof RecyclerView) {
            this.recyclerView = (RecyclerView) recyclerView;
        } else {
            throw new IllegalArgumentException(getClass().getSimpleName() + " can't work without a RecyclerView!");
        }

        if (this.recyclerView != null) {
            internalOnScrollListener = new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    if (getPullMode() == PULL_MODE_REFRESH_LOADMORE) {
                        processOnMore();
                    }
                    if (externalOnScrollListener != null) {
                        externalOnScrollListener.onScrolled(recyclerView, dx, dy);
                    }
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (externalOnScrollListener != null) {
                        externalOnScrollListener.onScrollStateChanged(recyclerView, newState);
                    }
                }
            };
            this.recyclerView.addOnScrollListener(internalOnScrollListener);
            this.recyclerView.setItemAnimator(new DefaultItemAnimator());
        }
    }

    protected RecyclerView.ItemDecoration getItemDecoration() {
        int space = (int) getContext().getResources().getDimension(R.dimen.padding_small);
        return new DefaultSpaceItemDecoration(space);
    }

    protected RecyclerView.ItemDecoration getItemDividerDecoration(int direction) {
        return new DividerItemDecoration(getActivity(), direction);
    }

    private boolean haveMore() {
        return presenter.hasMoreData();
    }

    private void processOnMore() {
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        int lastVisibleItemPosition = getLastVisibleItemPosition(layoutManager);
        int visibleItemCount = layoutManager.getChildCount();
        int totalItemCount = layoutManager.getItemCount();

        if (((totalItemCount - lastVisibleItemPosition) <= ITEM_LEFT_TO_LOAD_MORE ||
                (totalItemCount - lastVisibleItemPosition) == 0 && totalItemCount > visibleItemCount)
                && !isLoadingMore) {
            if (haveMore()) {
                showMoreProgress();
                setLoadingMore(true);
                loadMore(totalItemCount);
            } else {
                hideMoreProgress();
                setLoadingMore(false);
            }
        }
    }

    @Override
    public void onRefreshBegin(PtrFrameLayout frame) {
        reloadData();
    }

    private int getLastVisibleItemPosition(RecyclerView.LayoutManager layoutManager) {
        int lastVisibleItemPosition = -1;
        if (layoutManager instanceof LinearLayoutManager) {
            layoutManagerType = LAYOUT_MANAGER_TYPE_LINEAR;
        } else if (layoutManager instanceof GridLayoutManager) {
            layoutManagerType = LAYOUT_MANAGER_TYPE_GRID;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            layoutManagerType = LAYOUT_MANAGER_TYPE_STAGGERED_GRID;
        } else {
            throw new RuntimeException(
                    "Unsupported LayoutManager used. Valid ones are LinearLayoutManager, GridLayoutManager and StaggeredGridLayoutManager");
        }

        switch (layoutManagerType) {
            case LAYOUT_MANAGER_TYPE_LINEAR:
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case LAYOUT_MANAGER_TYPE_GRID:
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                break;
            case LAYOUT_MANAGER_TYPE_STAGGERED_GRID:
                StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                if (lastScrollPositions == null) {
                    lastScrollPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                }

                staggeredGridLayoutManager.findLastVisibleItemPositions(lastScrollPositions);
                lastVisibleItemPosition = findMax(lastScrollPositions);
                break;
        }
        return lastVisibleItemPosition;
    }

    private int findMax(int[] lastPositions) {
        int max = Integer.MIN_VALUE;
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    private void setAdapterInternal(RecyclerView.Adapter adapter, boolean compatibleWithPrevious,
                                    boolean removeAndRecycleExistingViews) {
        if (recyclerView != null) {
            if (compatibleWithPrevious) {
                recyclerView.swapAdapter(adapter, removeAndRecycleExistingViews);
            } else {
                recyclerView.setAdapter(adapter);
            }

            if (adapter != null && getPullMode() == PULL_MODE_REFRESH_LOADMORE) {
                adapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
                    @Override
                    public void onItemRangeChanged(int positionStart, int itemCount) {
                        super.onItemRangeChanged(positionStart, itemCount);
                        onUpdated();
                    }

                    @Override
                    public void onItemRangeInserted(int positionStart, int itemCount) {
                        super.onItemRangeInserted(positionStart, itemCount);
                        onUpdated();
                    }

                    @Override
                    public void onItemRangeRemoved(int positionStart, int itemCount) {
                        super.onItemRangeRemoved(positionStart, itemCount);
                        onUpdated();
                    }

                    @Override
                    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                        super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                        onUpdated();
                    }

                    @Override
                    public void onChanged() {
                        super.onChanged();
                        onUpdated();
                    }

                    private void onUpdated() {
                        hideMoreProgress();
                        isLoadingMore = false;
                    }
                });
            }
        }
    }

    protected void customizeRecyclerView() {
    }

    protected abstract RecyclerView.LayoutManager getLayoutManager();

    protected abstract RecyclerAdapter getAdapter();

    protected void loadMore(int totalItemCount) {
        presenter.loadMore(totalItemCount);
    }

    public void setAdapter(RecyclerView.Adapter adapter) {
        setAdapterInternal(adapter, false, true);
    }

    public void swapAdapter(RecyclerView.Adapter adapter, boolean removeAndRecycleExistingViews) {
        setAdapterInternal(adapter, true, removeAndRecycleExistingViews);
    }

    public void setOnScrollListener(RecyclerView.OnScrollListener listener) {
        externalOnScrollListener = listener;
    }

    public boolean isLoadingMore() {
        return isLoadingMore;
    }

    public void setLoadingMore(boolean isLoadingMore) {
        this.isLoadingMore = isLoadingMore;
    }

    public View getMoreProgressView() {
        return moreProgressView;
    }

    public void showMoreProgress() {
        if (moreProgress != null) {
            moreProgress.setVisibility(View.VISIBLE);
        }
    }

    public void hideMoreProgress() {
        if (moreProgress != null) {
            moreProgress.setVisibility(View.GONE);
        }
    }

    public void showLoadMoreFinished() {
        hideMoreProgress();
        isLoadingMore = false;
    }

    private static class DefaultSpaceItemDecoration extends RecyclerView.ItemDecoration {
        private int space;

        public DefaultSpaceItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            if (parent.getChildAdapterPosition(view) == 0) {
                outRect.top = space;
            }
        }
    }
}
