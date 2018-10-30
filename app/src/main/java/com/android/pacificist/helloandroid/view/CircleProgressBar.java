package com.android.pacificist.helloandroid.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.android.pacificist.helloandroid.R;
import com.android.pacificist.helloandroid.util.Util;

/**
 * Created by pacificist on 2018/10/12.
 */
public class CircleProgressBar extends View {

    private int mFstColor = Color.GREEN;
    private int mSedColor = Color.RED;
    private int mStroke = Util.dp2px(15, getResources());
    private int mSpeed = 20;
    private boolean mShowProgressValue = true;
    private int mProgressTextColor = Color.BLACK;
    private int mProgressTextSize = Util.dp2px(20, getResources());

    private int mCurrentProgress = 0;
    private boolean mIsNext = false;
    private boolean mPause = false;

    private RectF mOval;
    private Paint mPaint;

    private Thread mRefreshThread = null;
    private final Runnable mRefreshRunnable = new Runnable() {
        @Override
        public void run() {
            while (!Thread.interrupted()) {
                if (!mPause) {
                    mCurrentProgress++;
                    if (mCurrentProgress == 360) {
                        mCurrentProgress = 0;
                        mIsNext = !mIsNext;
                    }
                    postInvalidate();
                }

                try {
                    Thread.sleep(mSpeed);
                } catch (InterruptedException e) {
                    break;
                }
            }
        }
    };

    public CircleProgressBar(Context context) {
        this(context, null);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mOval = new RectF();
        mPaint = new Paint();

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.CircleProgressBar, defStyleAttr, 0);
        for (int i = 0; i < a.getIndexCount(); i++) {
            int attr = a.getIndex(i);
            switch (attr) {
                case R.styleable.CircleProgressBar_fstColor:
                    mFstColor = a.getColor(attr, Color.GREEN);
                    break;
                case R.styleable.CircleProgressBar_sedColor:
                    mSedColor = a.getColor(attr, Color.RED);
                    break;
                case R.styleable.CircleProgressBar_stroke:
                    mStroke = a.getDimensionPixelSize(attr, mStroke);
                    break;
                case R.styleable.CircleProgressBar_speed:
                    mSpeed = a.getInt(attr, 20);
                    break;
                case R.styleable.CircleProgressBar_showProgressValue:
                    mShowProgressValue = a.getBoolean(attr, true);
                    break;
                case R.styleable.CircleProgressBar_progressTextColor:
                    mProgressTextColor = a.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.CircleProgressBar_progressTextSize:
                    mProgressTextSize = a.getDimensionPixelSize(attr, mProgressTextColor);
                default:
                    break;
            }
        }
        a.recycle();

        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                mPause = !mPause;
            }
        });
    }

    public void setSpeed(int speed) {
        mSpeed = speed;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int centre = getWidth() / 2;
        int radius = centre - mStroke / 2;

        mOval.left = centre - radius;
        mOval.top = centre - radius;
        mOval.right = centre + radius;
        mOval.bottom = centre + radius;

        mPaint.setStrokeWidth(mStroke);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        mPaint.setColor(mIsNext ? mSedColor : mFstColor);
        canvas.drawCircle(centre, centre, radius, mPaint);
        mPaint.setColor(mIsNext ? mFstColor : mSedColor);
        canvas.drawArc(mOval, -90, mCurrentProgress, false, mPaint);

        if (mShowProgressValue) {
            mPaint.setStyle(Paint.Style.FILL);
            mPaint.setTextAlign(Paint.Align.CENTER);
            mPaint.setColor(mProgressTextColor);
            mPaint.setTextSize(mProgressTextSize);
            Paint.FontMetricsInt fontMetrics = mPaint.getFontMetricsInt();
            int baseline = (int) (mOval.bottom + mOval.top - fontMetrics.bottom - fontMetrics.top) / 2;
            canvas.drawText(String.valueOf(mCurrentProgress), mOval.centerX(), baseline, mPaint);
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (mRefreshThread == null) {
            mRefreshThread = new Thread(mRefreshRunnable);
            mRefreshThread.start();
        }
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mRefreshThread.isAlive()) {
            mRefreshThread.interrupt();
            mRefreshThread = null;
        }
    }
}
