package com.hwangjr.mvp.base.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import butterknife.ButterKnife;

public abstract class RecyclerViewHolder<D> extends RecyclerView.ViewHolder {

    public RecyclerViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    protected abstract void bindView(D data);
}