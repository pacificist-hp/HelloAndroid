package com.android.pacificist.helloandroid;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

/**
 * Created by pacificist on 2018/10/11.
 */
public interface ICustomViewItem<VH extends RecyclerView.ViewHolder> {
    int getLayoutRes();
    VH createViewHolder(LayoutInflater inflater, ViewGroup parent);
    void bindViewHolder(VH holder, int position);
}
