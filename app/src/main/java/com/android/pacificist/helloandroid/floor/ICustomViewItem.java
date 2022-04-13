package com.android.pacificist.helloandroid.floor;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by pacificist on 2018/10/11.
 */
public interface ICustomViewItem<VH extends RecyclerView.ViewHolder> {
    int getLayoutRes();
    RecyclerView.ViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent);
    void bindViewHolder(VH holder, int position);
}
