package com.android.pacificist.helloandroid.viewitem;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.pacificist.helloandroid.R;
import com.android.pacificist.helloandroid.floor.CustomViewItem;
import com.android.pacificist.helloandroid.view.RippleView;

/**
 * Created by pacificist on 2018/10/29.
 */
public class RippleViewItem extends CustomViewItem<RippleViewItem.RippleViewHolder> {
    @Override
    public int getLayoutRes() {
        return R.layout.layout_ripple_view;
    }

    @Override
    public RippleViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(getLayoutRes(), parent, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new RippleViewHolder(view);
    }

    @Override
    public void bindViewHolder(final RippleViewHolder holder, int position) {
        holder.rippleView.startRippleAnimation();
        holder.coreView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.rippleView.isRippleAnimating()) {
                    holder.rippleView.stopRippleAnimation();
                } else {
                    holder.rippleView.startRippleAnimation();
                }
            }
        });
    }

    public static class RippleViewHolder extends RecyclerView.ViewHolder {
        public RippleView rippleView;
        public View coreView;

        public RippleViewHolder(View itemView) {
            super(itemView);
            rippleView = itemView.findViewById(R.id.ripple_view);
            coreView = itemView.findViewById(R.id.core_view);
        }
    }
}
