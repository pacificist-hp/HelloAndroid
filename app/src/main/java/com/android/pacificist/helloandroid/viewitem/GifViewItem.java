package com.android.pacificist.helloandroid.viewitem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.pacificist.helloandroid.floor.CustomViewItem;
import com.android.pacificist.helloandroid.R;

/**
 * Created by pacificist on 2018/10/12.
 */
public class GifViewItem extends CustomViewItem<GifViewItem.GifViewHolder> {
    @Override
    public int getLayoutRes() {
        return R.layout.layout_gift_view;
    }

    @Override
    public GifViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(getLayoutRes(), parent, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new GifViewHolder(view);
    }

    @Override
    public void bindViewHolder(GifViewHolder holder, int position) {

    }

    public static class GifViewHolder extends RecyclerView.ViewHolder {
        public GifViewHolder(View itemView) {
            super(itemView);
        }
    }
}
