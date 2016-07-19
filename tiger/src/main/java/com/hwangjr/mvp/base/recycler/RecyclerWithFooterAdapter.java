package com.hwangjr.mvp.base.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.List;

public abstract class RecyclerWithFooterAdapter<D, V extends RecyclerViewHolder> extends RecyclerAdapter<D, V> {

    public static final int VIEW_TYPE_FOOTER = -2;

    private RecyclerViewHolder footerView;

    public RecyclerWithFooterAdapter(RecyclerView recyclerView) {
        this(recyclerView, null);
    }

    public RecyclerWithFooterAdapter(RecyclerView recyclerView, List<D> data) {
        super(recyclerView, data);
    }

    private boolean isFooter(int position) {
        return (hasFooter() && position == (getItemCount() - 1));
    }

    protected boolean hasFooter() {
        return getFooterViewHolder() != null;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_FOOTER) {
            return getFooterViewHolder();
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public int getItemCount() {
        if (hasFooter()) {
            return super.getItemCount() + 1;
        }
        return super.getItemCount();
    }

    @Override
    public int getItemViewType(int position) {
        return hasFooter() && isFooter(position) ? VIEW_TYPE_FOOTER : super.getItemViewType(position);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (hasFooter()) {
            if (!isFooter(position)) {
                super.onBindViewHolder(holder, position);
            }
        } else {
            super.onBindViewHolder(holder, position);
        }
    }

    public void setFooterView(RecyclerViewHolder footerView) {
        this.footerView = footerView;
    }

    protected <F extends RecyclerViewHolder> F getFooterViewHolder() {
        return (F) footerView;
    }
}