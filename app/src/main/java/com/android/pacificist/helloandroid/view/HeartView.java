package com.android.pacificist.helloandroid.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.TypeEvaluator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.pacificist.helloandroid.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by pacificist on 2018/11/17.
 */
public class HeartView extends RelativeLayout {

    private List<Drawable> mHeartDrawables = new ArrayList<>();
    private int mHeartWidth;
    private int mHeartHeight;
    private LayoutParams mHeartLayoutParams;

    private Random mRandom;

    public HeartView(Context context) {
        super(context);
        init();
    }

    public HeartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mHeartDrawables.add(getContext().getDrawable(R.mipmap.heart_blue));
        mHeartDrawables.add(getContext().getDrawable(R.mipmap.heart_blue_dark));
        mHeartDrawables.add(getContext().getDrawable(R.mipmap.heart_green));
        mHeartDrawables.add(getContext().getDrawable(R.mipmap.heart_green_dark));
        mHeartDrawables.add(getContext().getDrawable(R.mipmap.heart_orange));
        mHeartDrawables.add(getContext().getDrawable(R.mipmap.heart_purple));
        mHeartDrawables.add(getContext().getDrawable(R.mipmap.heart_red));
        mHeartDrawables.add(getContext().getDrawable(R.mipmap.heart_red_dark));
        mHeartDrawables.add(getContext().getDrawable(R.mipmap.heart_yellow));

        mHeartWidth = mHeartDrawables.get(0).getIntrinsicWidth();
        mHeartHeight = mHeartDrawables.get(0).getIntrinsicHeight();

        mHeartLayoutParams = new RelativeLayout.LayoutParams(mHeartWidth, mHeartHeight);
        mHeartLayoutParams.addRule(CENTER_HORIZONTAL);
        mHeartLayoutParams.addRule(ALIGN_PARENT_BOTTOM);

        mRandom = new Random();
    }

    public void addHeart() {
        ImageView heart = new ImageView(getContext());
        heart.setImageDrawable(mHeartDrawables.get(mRandom.nextInt(mHeartDrawables.size())));
        heart.setLayoutParams(mHeartLayoutParams);
        addView(heart);
        startAnimation(heart);
    }

    private void startAnimation(View heart) {
        AnimatorSet enterAnimator = generateEnterAnimation(heart);
        ValueAnimator curveAnimator = generatePathAnimation(heart);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setTarget(heart);
        animatorSet.playSequentially(enterAnimator, curveAnimator);
        animatorSet.addListener(new AnimationEndListener(heart));
        animatorSet.start();
    }

    private AnimatorSet generateEnterAnimation(View target) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(target, "alpha", 0.2f, 1f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(target, "scaleX", 0.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(target, "scaleY", 0.5f, 1f);
        AnimatorSet enterAnimation = new AnimatorSet();
        enterAnimation.playTogether(alpha, scaleX, scaleY);
        enterAnimation.setDuration(150);
        enterAnimation.setTarget(target);
        return enterAnimation;
    }

    private ValueAnimator generatePathAnimation(View target) {
        CurveEvaluator evaluator = new CurveEvaluator(generateCTRLPointF(1), generateCTRLPointF(2));
        ValueAnimator valueAnimator = ValueAnimator.ofObject(evaluator,
                new PointF((getWidth() - mHeartWidth) / 2, getHeight() - mHeartHeight),
                new PointF((getWidth()) / 2 + (mRandom.nextBoolean() ? 1 : -1) * mRandom.nextInt(100), 0));
        valueAnimator.setDuration(3000);
        valueAnimator.addUpdateListener(new CurveUpdateLister(target));
        valueAnimator.setTarget(target);

        return valueAnimator;
    }

    private PointF generateCTRLPointF(int value) {
        PointF pointF = new PointF();
        pointF.x = getWidth() / 2 - mRandom.nextInt(100);
        pointF.y = mRandom.nextInt(getHeight() / value);

        return pointF;
    }

    /**
     * 计算对象当前运动的具体位置 Point
     */
    private class CurveEvaluator implements TypeEvaluator<PointF> {

        // 三阶贝塞儿曲线的两个控制点
        private PointF ctrlPointF1;
        private PointF ctrlPointF2;

        public CurveEvaluator(PointF ctrlPointF1, PointF ctrlPointF2) {
            this.ctrlPointF1 = ctrlPointF1;
            this.ctrlPointF2 = ctrlPointF2;
        }

        @Override
        public PointF evaluate(float fraction, PointF startValue, PointF endValue) {

            // 三阶贝塞儿曲线
            float leftTime = 1.0f - fraction;
            PointF resultPointF = new PointF();

            resultPointF.x = (float) Math.pow(leftTime, 3) * startValue.x
                    + 3 * (float) Math.pow(leftTime, 2) * fraction * ctrlPointF1.x
                    + 3 * leftTime * (float) Math.pow(fraction, 2) * ctrlPointF2.x
                    + (float) Math.pow(fraction, 3) * endValue.x;
            resultPointF.y = (float) Math.pow(leftTime, 3) * startValue.y
                    + 3 * (float) Math.pow(leftTime, 2) * fraction * ctrlPointF1.y
                    + 3 * leftTime * fraction * fraction * ctrlPointF2.y
                    + (float) Math.pow(fraction, 3) * endValue.y;

            return resultPointF;
        }
    }

    /**
     * 动画曲线路径更新监听器, 用于动态更新动画作用对象的位置
     */
    private class CurveUpdateLister implements ValueAnimator.AnimatorUpdateListener {
        private View target;

        public CurveUpdateLister(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator animation) {
            // 获取当前动画运行的状态值, 使得动画作用对象沿着曲线(涉及贝塞儿曲线)运动
            PointF pointF = (PointF) animation.getAnimatedValue();
            target.setX(pointF.x);
            target.setY(pointF.y);
            target.setAlpha(1 - animation.getAnimatedFraction());
        }
    }

    /**
     * 动画结束监听器,用于释放无用的资源
     */
    private class AnimationEndListener extends AnimatorListenerAdapter {
        private View target;

        public AnimationEndListener(View target) {
            this.target = target;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            removeView(target);
        }
    }
}
