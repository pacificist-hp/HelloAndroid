package com.android.pacificist.helloandroid.barrageview;

import android.animation.Animator;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewPropertyAnimator;
import android.view.animation.LinearInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by pacificist on 2018/10/13.
 */
public class BarrageView extends RelativeLayout {

    public final static int FROM_RIGHT_TO_LEFT = 1;
    public final static int FROM_LEFT_TO_RIGHT = 2;

    private int mDirection = FROM_RIGHT_TO_LEFT;

    private int mRowNum = 3;
    private int mItemMargin;
    private long mDuration = 5000;

    private View[] mLastViewsInRow;

    public BarrageView(Context context) {
        super(context);
        init();
    }

    public BarrageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BarrageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mItemMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                20, getResources().getDisplayMetrics());
        mLastViewsInRow = new View[mRowNum];
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        int childrenCount = getChildCount();
        for (int i = 0; i < childrenCount; i++) {
            final View view = getChildAt(i);
            if (view != null) {
                RelativeLayout.LayoutParams lp = (LayoutParams) view.getLayoutParams();
                if (lp.leftMargin <= 0) {
                    if (mDirection == FROM_RIGHT_TO_LEFT) {
                        // The four coordinates in layout() are relative to parent view
                        view.layout(getWidth(), lp.topMargin, getWidth() + view.getWidth(), lp.topMargin + view.getHeight());
                    } else if (mDirection == FROM_LEFT_TO_RIGHT) {
                        view.layout(-getWidth(), lp.topMargin, 0, lp.topMargin + view.getHeight());
                    }
                }
            }
        }
    }

    public void addBarrageItemView(String text, int textSize, int textColor) {
        View barrageItem = newBarrageItem(text, textSize, textColor);
        addView(barrageItem);
        showAnimate(barrageItem);
    }

    private View newBarrageItem(String text, int textSize, int textColor) {
        final TextView item = new TextView(getContext());
        item.setText(text);
        item.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                textSize, getResources().getDisplayMetrics()));
        item.setTextColor(textColor);

        int w = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        int h = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        item.measure(w, h);

        int row = new Random().nextInt(100) % mRowNum;
        while (needResetRow(row)) {
            row = new Random().nextInt(100) % mRowNum;
        }
        mLastViewsInRow[row] = item;

        RelativeLayout.LayoutParams lp = new LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.topMargin = row * (item.getMeasuredHeight() + mItemMargin);
        item.setLayoutParams(lp);
        item.setBackgroundColor(Color.TRANSPARENT);

        return item;
    }

    private void showAnimate(final View view) {
        ViewPropertyAnimator animator = null;
        if(mDirection == FROM_RIGHT_TO_LEFT){
            animator = view.animate().translationXBy(-(getWidth() + view.getMeasuredWidth()));
        }else if(mDirection == FROM_LEFT_TO_RIGHT) {
            animator = view.animate().translationXBy(getWidth() + view.getMeasuredWidth());
        }
        animator.setDuration(mDuration);
        animator.setInterpolator(new LinearInterpolator());
        animator.start();
        animator.setListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                removeView(view);
                for (int i = 0; i < mLastViewsInRow.length; i++) {
                    if (view.equals(mLastViewsInRow[i])) {
                        mLastViewsInRow[i] = null;
                    }
                }
            }

            @Override
            public void onAnimationCancel(Animator animator) {

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
    }

    private boolean needResetRow(int row) {
        boolean ret = false;
        View view = mLastViewsInRow[row];
        if (view != null) {
            if (mDirection == FROM_RIGHT_TO_LEFT) {
                ret = -view.getTranslationX() < view.getWidth() + 10;
            } else if (mDirection == FROM_LEFT_TO_RIGHT) {
                ret = view.getTranslationX() < view.getWidth() + 10;
            }
        }
        return ret;
    }
}
