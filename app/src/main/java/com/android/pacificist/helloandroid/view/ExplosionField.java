package com.android.pacificist.helloandroid.view;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by pacificist on 2018/10/31.
 */
public class ExplosionField extends View {

    private static final Canvas mCanvas = new Canvas();

    private List<ExplosionAnimator> mExplosionAnimators = new ArrayList<>();

    public ExplosionField(Context context) {
        super(context);
    }

    public ExplosionField(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExplosionField(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private static Bitmap createBitmapFromView(View view) {
        Bitmap bitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        if (bitmap != null) {
            synchronized (mCanvas) {
                mCanvas.setBitmap(bitmap);
                view.draw(mCanvas);
                mCanvas.setBitmap(null);
            }
        }
        return bitmap;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // canvas is relative to the view
        super.onDraw(canvas);
        for (ExplosionAnimator explosionAnimator : mExplosionAnimators) {
            explosionAnimator.draw(canvas);
        }
    }

    public void explode(final View view) {
        Rect rect = new Rect();
        view.getGlobalVisibleRect(rect);  // rect is relative to the screen now
        int[] location = new int[2];
        view.getLocationOnScreen(location);
        rect.offset(0, -location[1]); // rect is relative to the view now

        final ExplosionAnimator animator = new ExplosionAnimator(this, createBitmapFromView(view), rect);
        mExplosionAnimators.add(animator);

        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationStart(Animator animation) {
                view.animate().alpha(0f).setDuration(150).start();
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.animate().alpha(1f).setDuration(150).start();
                mExplosionAnimators.remove(animation);
            }
        });
        animator.start();
    }


    private class ExplosionAnimator extends ValueAnimator {

        public static final int DEFAULT_DURATION = 1500;

        private Particle[][] mParticles;
        private Paint mPaint;
        private View mView;

        public ExplosionAnimator(View view, Bitmap bitmap, Rect bound) {
            setFloatValues(0.0f, 1.0f);
            setDuration(DEFAULT_DURATION);

            mPaint = new Paint();
            mView = view;
            mParticles = generateParticles(bitmap, bound);
        }

        private Particle[][] generateParticles(Bitmap bitmap, Rect bound) {
            int widthCount = bound.width() / Particle.PART_WH;
            int heightCount = bound.height() / Particle.PART_WH;
            Particle[][] particles = new Particle[heightCount][widthCount];

            int bmPartWidth = bitmap.getWidth() / widthCount;
            int bmPartHeight = bitmap.getHeight() / heightCount;

            for (int row = 0; row < heightCount; row++) {
                for (int column = 0; column < widthCount; column++) {
                    particles[row][column] = Particle.generateParticle(
                            bitmap.getPixel(column * bmPartWidth, row * bmPartHeight),
                            bound, new Point(column, row));
                }
            }
            return particles;
        }

        public void draw(Canvas canvas) {
            if (!isStarted()) {
                return;
            }

            for (Particle[] particle : mParticles) {
                for (Particle p : particle) {
                    p.advance((Float) getAnimatedValue());
                    mPaint.setColor(p.color);
                    mPaint.setAlpha((int) (Color.alpha(p.color) * p.alpha));
                    // p.cx and p.cy are relative to canvas now
                    canvas.drawCircle(p.cx, p.cy, p.radius, mPaint);
                }
            }

            mView.invalidate();
        }

        @Override
        public void start() {
            super.start();
            mView.invalidate();
        }
    }

    private static class Particle {

        public static final int PART_WH = 8;
        private static Random random = new Random();

        float cx;
        float cy;
        float radius;
        int color;
        float alpha;
        Rect mBound;

        public static Particle generateParticle(int color, Rect bound, Point point) {
            int row = point.y;
            int column = point.x;

            Particle particle = new Particle();
            particle.mBound = bound;
            particle.color = color;
            particle.alpha = 1f;

            // different particles can be superimposed over one another
            particle.radius = PART_WH;
            particle.cx = bound.left + PART_WH * column;
            particle.cy = bound.top + PART_WH * row;

            return particle;
        }

        public void advance(float factor) {
            cx = cx + factor * random.nextInt(mBound.width()) * (random.nextFloat() - 0.5f);
            cy = cy + factor * random.nextInt(mBound.height() / 2);

            radius = radius - factor * random.nextInt(2);

            alpha = (1f - factor) * (1 + random.nextFloat());
        }
    }
}
