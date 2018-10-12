package com.android.pacificist.helloandroid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by pacificist on 2018/10/11.
 */
public abstract class CustomViewItem<VH extends RecyclerView.ViewHolder>
        implements ICustomViewItem {
    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    public VH createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        throw new IllegalStateException(this.getClass().getSimpleName() + " do not overwrite createViewHolder");
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, int position) {
        throw new IllegalStateException(this.getClass().getSimpleName() + " do not overwrite bindViewHolder");
    }
}
