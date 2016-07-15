package com.hwangjr.mvp.base.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Collection;
import java.util.List;

public abstract class RecyclerSectionAdapter<T, VH extends RecyclerViewHolder> extends RecyclerAdapter<T, VH> {
    protected static final int VIEW_TYPE_SECTION = -2;

    public RecyclerSectionAdapter(RecyclerView recyclerView) {
        super(recyclerView);
    }

    public RecyclerSectionAdapter(RecyclerView recyclerView, List<T> data) {
        super(recyclerView, data);
    }

    protected abstract Collection<Integer> getSections();

    protected abstract RecyclerViewHolder onCreateSectionViewHolder(ViewGroup parent);

    protected abstract void onBindSectionViewHolder(RecyclerViewHolder sectionView, int sectionIndex);

    protected boolean checkSectionForPosition(int position) {
        int headerIndex = hasHeader() ? 1 : 0;
        Collection<Integer> sections = getSections();
        if (sections != null && sections.size() > 0) {
            int index = 0;
            for (Integer value : sections) {
                value = value + index + headerIndex;
                if (value == position) {
                    return true;
                }
                index++;
            }
        }
        return false;
    }

    private int getLastSectionIndex(int position) {
        int headerIndex = hasHeader() ? 1 : 0;
        Collection<Integer> sections = getSections();
        if (sections != null && sections.size() > 0) {
            int index = 0;
            for (Integer value : sections) {
                value = value + index + headerIndex;
                if (value >= position) {
                    return index;
                }
                index++;
            }
            return index--;
        }
        return 0;
    }

    @Override
    public final int getItemCount() {
        int sectionCount = getSections() != null ? getSections().size() : 0;
        return super.getItemCount() + sectionCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (checkSectionForPosition(position)) {
            return VIEW_TYPE_SECTION;
        } else {
            position = position - getLastSectionIndex(position);
            return super.getItemViewType(position);
        }
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_SECTION) {
            return onCreateSectionViewHolder(parent);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        if (checkSectionForPosition(position)) {
            onBindSectionViewHolder(holder, getLastSectionIndex(position));
        } else {
            position = position - getLastSectionIndex(position);
            super.onBindViewHolder(holder, position);
        }
    }
}
