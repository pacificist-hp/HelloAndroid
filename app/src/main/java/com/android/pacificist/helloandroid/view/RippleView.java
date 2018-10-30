package com.android.pacificist.helloandroid.view;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.RelativeLayout;

import com.android.pacificist.helloandroid.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pacificist on 2018/10/26.
 */
public class RippleView extends RelativeLayout {

    private int mColor;
    private float mStroke;
    private float mRadius;
    private int mDuration;
    private int mDelay;
    private int mScale;
    private String mStyle;

    private Paint mPaint;

    private boolean mAnimating = false;

    private AnimatorSet mAnimatorSet;

    private List<CircleView> mCircleViewList = new ArrayList<>();

    public RippleView(Context context) {
        super(context);
        init();
    }

    public RippleView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RippleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mColor = getContext().getColor(R.color.color_ripple);
        mRadius = getResources().getDimension(R.dimen.rippleRadius);
        mDuration = getResources().getInteger(R.integer.ripple_duration_time);
        mScale = getResources().getInteger(R.integer.ripple_scale);
        mStyle = getContext().getString(R.string.ripple_style);
        mDelay = mDuration / mScale;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        if (TextUtils.equals(mStyle, getContext().getString(R.string.ripple_style_fill))) {
            mStroke = 0;
            mPaint.setStyle(Paint.Style.FILL);
        } else {
            mStroke = getResources().getDimension(R.dimen.rippleStrokeWidth);
            mPaint.setStyle(Paint.Style.STROKE);
        }
        mPaint.setColor(mColor);

        initCircleViews();
    }

    private void initCircleViews() {
        LayoutParams circleParams = new LayoutParams((int) (2 * (mRadius + mStroke)),
                (int) (2 * (mRadius + mStroke)));
        circleParams.addRule(CENTER_IN_PARENT, TRUE);

        mAnimatorSet = new AnimatorSet();
        mAnimatorSet.setInterpolator(new AccelerateDecelerateInterpolator());
        List<Animator> animators = new ArrayList<>();

        for (int i = 0; i < mScale; i++) {
            CircleView circleView = new CircleView(getContext());
            addView(circleView, circleParams);
            mCircleViewList.add(circleView);

            final ObjectAnimator scaleXAnimator = ObjectAnimator.ofFloat(
                    circleView, "ScaleX", 1.0f, mScale);
            scaleXAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            scaleXAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleXAnimator.setStartDelay(i * mDelay);
            scaleXAnimator.setDuration(mDuration);
            animators.add(scaleXAnimator);

            final ObjectAnimator scaleYAnimator = ObjectAnimator.ofFloat(
                    circleView, "ScaleY", 1.0f, mScale);
            scaleYAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            scaleYAnimator.setRepeatMode(ObjectAnimator.RESTART);
            scaleYAnimator.setStartDelay(i * mDelay);
            scaleYAnimator.setDuration(mDuration);
            animators.add(scaleYAnimator);

            final ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(
                    circleView, "Alpha", 1.0f, 0f);
            alphaAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            alphaAnimator.setRepeatMode(ObjectAnimator.RESTART);
            alphaAnimator.setStartDelay(i * mDelay);
            alphaAnimator.setDuration(mDuration);

            animators.add(alphaAnimator);
        }
        mAnimatorSet.playTogether(animators);
    }

    private class CircleView extends View {

        public CircleView(Context context) {
            super(context);
            this.setVisibility(View.INVISIBLE);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            int centre = (Math.min(getWidth(), getHeight())) / 2;
            canvas.drawCircle(centre, centre, centre - mStroke, mPaint);
        }
    }

    public void startRippleAnimation(){
        if(!isRippleAnimating()){
            for(CircleView circleView : mCircleViewList){
                circleView.setVisibility(VISIBLE);
            }
            mAnimatorSet.start();
            mAnimating =true;
        }
    }

    public void stopRippleAnimation(){
        if(isRippleAnimating()){
            mAnimatorSet.end();
            mAnimating =false;
        }
    }

    public boolean isRippleAnimating(){
        return mAnimating;
    }
}
