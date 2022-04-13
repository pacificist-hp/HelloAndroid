package com.android.pacificist.helloandroid.viewitem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.android.pacificist.helloandroid.floor.CustomViewItem;
import com.android.pacificist.helloandroid.R;

/**
 * Created by pacificist on 2018/10/18.
 */
public class ScratchCardItem extends CustomViewItem<ScratchCardItem.ScratchCardHolder> {

    @Override
    public int getLayoutRes() {
        return R.layout.layout_scratch_card;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(getLayoutRes(), parent, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ScratchCardHolder(view);
    }

    @Override
    public void bindViewHolder(ScratchCardHolder holder, int position) {

    }

    public static class ScratchCardHolder extends RecyclerView.ViewHolder {
        public ScratchCardHolder(View itemView) {
            super(itemView);
        }
    }
}
