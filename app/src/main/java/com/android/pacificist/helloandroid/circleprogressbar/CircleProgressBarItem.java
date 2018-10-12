package com.android.pacificist.helloandroid.circleprogressbar;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.pacificist.helloandroid.CustomViewItem;
import com.android.pacificist.helloandroid.R;

/**
 * Created by pacificist on 2018/10/13.
 */
public class CircleProgressBarItem extends CustomViewItem<CircleProgressBarItem.CircleProgressBarHolder> {
    @Override
    public int getLayoutRes() {
        return R.layout.layout_circle_progress_bar;
    }

    @Override
    public CircleProgressBarHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(getLayoutRes(), parent, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new CircleProgressBarHolder(view);
    }

    @Override
    public void bindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    public static class CircleProgressBarHolder extends RecyclerView.ViewHolder {
        public CircleProgressBarHolder(View itemView) {
            super(itemView);
        }
    }
}
