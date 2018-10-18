package com.android.pacificist.helloandroid;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by pacificist on 2018/10/11.
 */
public class CustomViewAdapter<T extends ICustomViewItem> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<T> mItems;
    private Map<Integer, T> mViewTypes = new HashMap<>();
    private LayoutInflater mInflater = null;

    public CustomViewAdapter(@Nullable List<T> items) {
        mItems = items == null ? new ArrayList<T>() : items;
    }

    @Override
    public int getItemViewType(int position) {
        // ViewType is used for the reuse of ViewHolders.
        // Two items with the same viewType will have the same ViewHolder
        // and will share one ViewHolder object if needed.
        T item = mItems.get(position);
        if (item != null && !mViewTypes.containsKey(item.getLayoutRes())) {
            // ViewHolder is a Collection of views.
            // All items with a same layout should have a same ViewHolder.
            mViewTypes.put(item.getLayoutRes(), item);
        }
        return item.getLayoutRes();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // ViewHolders can be created in different ways from different ViewTypes.
        T item = mViewTypes.get(viewType);
        if (item == null) {
            throw new IllegalStateException("Do not find view by type: " + viewType);
        }
        if (mInflater == null) {
            mInflater = LayoutInflater.from(parent.getContext());
        }
        return item.createViewHolder(mInflater, parent);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        // Because holder may be reused, we must reset all the views in holder from item's data.
        mItems.get(position).bindViewHolder(holder, position);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    public void addItem(T item) {
        mItems.add(item);
        notifyItemInserted(mItems.indexOf(item));
    }

    public void removeItem(T item) {
        int index = mItems.indexOf(item);
        mItems.remove(index);
        notifyItemRemoved(index);
    }
}
