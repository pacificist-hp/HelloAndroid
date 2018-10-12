package com.android.pacificist.helloandroid.cardview;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.pacificist.helloandroid.CustomViewItem;
import com.android.pacificist.helloandroid.R;

/**
 * Created by pacificist on 2018/10/12.
 */
public class CardViewItem extends CustomViewItem<CardViewItem.CardViewHolder> {

    @Override
    public int getLayoutRes() {
        return R.layout.layout_card_view;
    }

    @Override
    public CardViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(getLayoutRes(), parent, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new CardViewHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        public CardViewHolder(View itemView) {
            super(itemView);
        }
    }
}
