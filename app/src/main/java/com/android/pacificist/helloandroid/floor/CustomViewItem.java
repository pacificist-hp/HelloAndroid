package com.android.pacificist.helloandroid.floor;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by pacificist on 2018/10/11.
 */
public abstract class CustomViewItem<VH extends RecyclerView.ViewHolder>
        implements ICustomViewItem<VH> {
    @Override
    public int getLayoutRes() {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        throw new IllegalStateException(this.getClass().getSimpleName() + " do not overwrite createViewHolder");
    }

    @Override
    public void bindViewHolder(VH holder, int position) {
        throw new IllegalStateException(this.getClass().getSimpleName() + " do not overwrite bindViewHolder");
    }
}
