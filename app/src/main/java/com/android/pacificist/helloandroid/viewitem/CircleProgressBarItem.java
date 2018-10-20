package com.android.pacificist.helloandroid.viewitem;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.pacificist.helloandroid.view.CircleProgressBar;
import com.android.pacificist.helloandroid.floor.CustomViewItem;
import com.android.pacificist.helloandroid.R;
import com.android.pacificist.helloandroid.view.HorizontalFlow;

import java.util.ArrayList;
import java.util.List;

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
    public void bindViewHolder(final CircleProgressBarHolder holder, int position) {
        holder.horizontalFlow.bind(initItems(holder.horizontalFlow.getContext()));
        holder.horizontalFlow.setSelectedItem("2");
        holder.horizontalFlow.setOnItemClickListener(new HorizontalFlow.OnItemClickListener() {
            @Override
            public void onItemClick(HorizontalFlow.IHorizontalItem item) {
                if (item instanceof HorizontalItem) {
                    holder.circleProgressBar.setSpeed(((HorizontalItem)item).speed);
                }
            }
        });
    }

    public static class CircleProgressBarHolder extends RecyclerView.ViewHolder {
        public CircleProgressBar circleProgressBar;
        public HorizontalFlow horizontalFlow;

        public CircleProgressBarHolder(View itemView) {
            super(itemView);
            circleProgressBar = itemView.findViewById(R.id.circle_progress_bar);
            horizontalFlow = itemView.findViewById(R.id.horizontal_flow);
        }
    }

    private List<HorizontalItem> initItems(Context context) {
        List<HorizontalItem> items = new ArrayList<>();
        items.add(new HorizontalItem(context, "1", 5));
        items.add(new HorizontalItem(context, "2", 10));
        items.add(new HorizontalItem(context, "3", 20));
        items.add(new HorizontalItem(context, "4", 50));
        return items;
    }

    private static class HorizontalItem implements HorizontalFlow.IHorizontalItem {
        public Context context;
        public String id;
        public int speed;

        public HorizontalItem(Context context, String id, int speed) {
            this.context = context;
            this.id = id;
            this.speed = speed;
        }

        @Override
        public String getKey() {
            return id;
        }

        @Override
        public String getContent() {
            return context.getString(R.string.perfix_circle_speed, speed * 360f / 1000);
        }
    }
}
