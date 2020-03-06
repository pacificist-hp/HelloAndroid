package com.android.pacificist.helloandroid.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ScrollView;

import com.android.pacificist.helloandroid.R;

import java.util.List;

/**
 * 一款支持实曝光的ScrollView
 *
 * Created by pacificist on 2020/3/6.
 */
public class ExposeScrollView extends ScrollView {

    private OnViewExposeListener mListener;

    // 调用方配置需要观察曝光的 child view
    private List<View> mExposeViewList;

    public ExposeScrollView(Context context) {
        super(context);
        init();
    }

    public ExposeScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ExposeScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        this.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        checkExposeView();
                    }
                });
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        checkExposeView();
    }

    private void checkExposeView() {
        if (mExposeViewList == null || mExposeViewList.size() == 0) {
            return;
        }

        for (View view : mExposeViewList) {
            if (!"true".equals(view.getTag(R.id.tag_scroll_child_view_expose))
                    && isViewVisible(view)) {
                if (mListener != null) {
                    mListener.onExpose(view);
                }
                // 已经曝光的 view 不再处理
                view.setTag(R.id.tag_scroll_child_view_expose, "true");
            }
        }
    }

    public void setOnViewExposeListener(OnViewExposeListener listener) {
        mListener = listener;
    }

    public void setExposeViewList(List<View> viewList) {
        mExposeViewList = viewList;
    }

    /**
     * ScrollView 内部的 child view 是否可见
     *
     * @param view
     * @return
     */
    private boolean isViewVisible(View view) {
        if (view == null) {
            return false;
        }

        Rect scrollBounds = new Rect();
        this.getHitRect(scrollBounds);
        return view.getLocalVisibleRect(scrollBounds);
    }

    public interface OnViewExposeListener {
        void onExpose(View view);
    }
}
