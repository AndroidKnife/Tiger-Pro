package com.hwangjr.mvp.base.recycler;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public abstract class RecyclerAdapter<D, V extends RecyclerViewHolder> extends RecyclerView.Adapter<RecyclerViewHolder> {

    public static final int VIEW_TYPE_HEADER = -1;
    private Context context;
    private List<D> data;
    private RecyclerViewHolder headerView;
    protected boolean isScrolling;
    private OnItemClickListener listener;

    public RecyclerAdapter(RecyclerView recyclerView) {
        this(recyclerView, null);
    }

    public RecyclerAdapter(RecyclerView recyclerView, List<D> data) {
        this.context = recyclerView.getContext();
        this.data = (data == null ? new ArrayList<>() : data);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                isScrolling = (newState == RecyclerView.SCROLL_STATE_DRAGGING || newState == RecyclerView.SCROLL_STATE_SETTLING);
            }
        });
    }

    public List<D> getData() {
        return data;
    }

    private boolean isHeader(int position) {
        return (hasHeader() && position == 0);
    }

    protected boolean hasHeader() {
        return getHeaderViewHolder() != null;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        RecyclerViewHolder viewHolder;
        if (viewType != VIEW_TYPE_HEADER) {
            View itemView = LayoutInflater.from(context)
                    .inflate(getItemLayoutID(viewType), parent, false);
            viewHolder = createItemViewHolder(itemView, viewType);
        } else {
            viewHolder = getHeaderViewHolder();
        }
        return viewHolder;
    }

    @Override
    public int getItemCount() {
        if (hasHeader()) {
            return data.size() + 1;
        }
        return data.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (hasHeader()) {
            return isHeader(position) ? VIEW_TYPE_HEADER : getViewType(position - 1);
        } else {
            return getViewType(position);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (hasHeader()) {
            if (!isHeader(position)) {
                int index = position - 1;
                onBindItemViewHolder((V) holder, position, index, isScrolling);
                ((V) holder).itemView.setOnClickListener(getOnClickListener(index));
            }
        } else {
            onBindItemViewHolder((V) holder, position, position, isScrolling);
            ((V) holder).itemView.setOnClickListener(getOnClickListener(position));
        }
    }

    public void setHeaderView(RecyclerViewHolder headerView) {
        this.headerView = headerView;
    }

    protected <H extends RecyclerViewHolder> H getHeaderViewHolder() {
        return (H) headerView;
    }

    protected abstract int getItemLayoutID(int viewType);

    protected abstract V createItemViewHolder(View itemView, int viewType);

    protected abstract int getViewType(int position);

    protected abstract void onBindItemViewHolder(V holder, int position, int index,
                                                 boolean isScrolling);

    public void add(D elem) {
        data.add(elem);
        notifyItemInserted(data.size() - 1);
    }

    public void addAll(List<D> elem) {
        final int positionStart = data.size();
        data.addAll(elem);
        notifyItemRangeInserted(positionStart, elem.size());
    }

    public void set(D oldElem, D newElem) {
        set(data.indexOf(oldElem), newElem);
    }

    public void set(int index, D elem) {
        if (index >= data.size()) {
            return;
        }

        data.set(index, elem);
        notifyItemChanged(index);
    }

    public void remove(D elem) {
        remove(data.indexOf(elem));
    }

    public void remove(int index) {
        if (index < data.size()) {
            data.remove(index);
            notifyItemRemoved(index);
        }
    }

    public void replaceAll(List<D> elem) {
        data.clear();
        data.addAll(elem);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener l) {
        listener = l;
    }

    public View.OnClickListener getOnClickListener(final int position) {
        return view -> {
            if (listener != null && view != null && getData().size() > position) {
                listener.onItemClick(view, getData().get(position), position);
            }
        };
    }

    public interface OnItemClickListener<D> {
        void onItemClick(View view, D data, int position);
    }
}