package com.android.pacificist.helloandroid;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pacificist on 2018/8/6.
 */
public class FlowLayout extends ViewGroup {

    private List<View> mLineViews = new ArrayList<>();

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new MarginLayoutParams(getContext(), attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int sizeWidth = MeasureSpec.getSize(widthMeasureSpec);
        int sizeHeight = MeasureSpec.getSize(heightMeasureSpec);

        int width = 0;
        int height = 0;

        int lineWidth = 0;
        int lineHeight = 0;

        int cCount = getChildCount();
        for (int i = 0; i < cCount; i++) {
            TextView cView = (TextView) getChildAt(i);
            measureChild(cView, widthMeasureSpec, heightMeasureSpec);
            MarginLayoutParams cParams = (MarginLayoutParams) cView.getLayoutParams();
            int cWidth = cView.getMeasuredWidth() + cParams.leftMargin + cParams.rightMargin;
            int cHeight = cView.getMeasuredHeight() + cParams.topMargin + cParams.bottomMargin;
            if (lineWidth + cWidth > sizeWidth) {
                width = Math.max(lineWidth, cWidth);
                lineWidth = cWidth;
                height += lineHeight;
                lineHeight = cHeight;
            } else {
                lineWidth += cWidth;
                lineHeight = Math.max(lineHeight, cHeight);
            }
        }
        width = Math.max(width, lineWidth);
        height += lineHeight;

        setMeasuredDimension((widthMode == MeasureSpec.EXACTLY) ? sizeWidth : width,
                (heightMode == MeasureSpec.EXACTLY) ? sizeHeight : height);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int width = getWidth();
        int lineWidth = 0;
        int lineHeight = 0;
        int top = 0;

        mLineViews.clear();

        for (int i = 0; i < getChildCount(); i++) {
            View cView = getChildAt(i);
            int cWidth = cView.getMeasuredWidth();
            int cHeight = cView.getMeasuredHeight();
            MarginLayoutParams cParams = (MarginLayoutParams) cView.getLayoutParams();

            if (cWidth + cParams.leftMargin + cParams.rightMargin + lineWidth > width) {
                layoutLineViews(top);
                top += lineHeight;
                lineWidth = 0;
                lineHeight = 0;
                mLineViews.clear();
            }

            lineWidth += cWidth + cParams.leftMargin + cParams.rightMargin;
            lineHeight = Math.max(lineHeight, cHeight + cParams.topMargin + cParams.bottomMargin);
            mLineViews.add(cView);
        }

        layoutLineViews(top);
    }

    private void layoutLineViews(int top) {
        int left = 0;
        for (int i = 0; i < mLineViews.size(); i++) {
            View v = mLineViews.get(i);
            if (v.getVisibility() == View.GONE)
                continue;

            MarginLayoutParams lp = (MarginLayoutParams) v.getLayoutParams();
            int vl = left + lp.leftMargin;
            int vt = top + lp.topMargin;
            int vr = Math.min(vl + v.getMeasuredWidth(), getWidth() - lp.rightMargin);
            int vb = vt + v.getMeasuredHeight();
            v.layout(vl, vt, vr, vb);
            left += v.getMeasuredWidth() + lp.leftMargin + lp.rightMargin;
        }
    }
}
