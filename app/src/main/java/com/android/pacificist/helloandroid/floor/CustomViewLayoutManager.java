package com.android.pacificist.helloandroid.floor;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;

/**
 * Created by pacificist on 2018/10/20.
 */
public class CustomViewLayoutManager extends LinearLayoutManager {

    private boolean isScrollable = true;

    public CustomViewLayoutManager(Context context) {
        super(context);
    }

    public void setScrollable(boolean scrollable) {
        isScrollable = scrollable;
    }

    @Override
    public boolean canScrollVertically() {
        return isScrollable && super.canScrollVertically();
    }
}
