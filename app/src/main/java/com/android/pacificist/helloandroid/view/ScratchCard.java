package com.android.pacificist.helloandroid.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.android.pacificist.helloandroid.R;
import com.android.pacificist.helloandroid.bus.CustomViewBus;
import com.android.pacificist.helloandroid.bus.CustomViewEvent;

/**
 * Created by pacificist on 2018/10/18.
 */
public class ScratchCard extends View {

    private Paint mCleanPaint = new Paint();
    private Paint mHintPaint = new Paint();
    private Path mPath = new Path();
    private Canvas mCanvas = null;
    private Bitmap mBitmap = null;

    public ScratchCard(Context context) {
        super(context);
        initPaint();
    }

    public ScratchCard(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    public ScratchCard(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initPaint();
    }

    private void initPaint() {
        mCleanPaint.setStyle(Paint.Style.STROKE);
        mCleanPaint.setStrokeJoin(Paint.Join.ROUND);
        mCleanPaint.setStrokeCap(Paint.Cap.ROUND);
        mCleanPaint.setStrokeWidth(100);
        mCleanPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));

        mHintPaint.setStyle(Paint.Style.FILL);
        mHintPaint.setTextAlign(Paint.Align.CENTER);
        mHintPaint.setColor(Color.BLUE);
        mHintPaint.setTextSize(50);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);

        if (mBitmap == null) {
            mBitmap = Bitmap.createBitmap(getWidth(), getHeight(), Bitmap.Config.ARGB_8888);
        }
        if (mCanvas == null) {
            mCanvas = new Canvas(mBitmap);
            mCanvas.drawColor(Color.LTGRAY);
            Paint.FontMetricsInt fontMetrics = mHintPaint.getFontMetricsInt();
            mCanvas.drawText(getContext().getString(R.string.hint_scratch_float),
                    getWidth() / 2, (getHeight() - fontMetrics.bottom - fontMetrics.top) / 2, mHintPaint);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mBitmap != null && mCanvas != null) {
            mCanvas.drawPath(mPath, mCleanPaint);
            canvas.drawBitmap(mBitmap, 0, 0, null);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath.moveTo(event.getX(), event.getY());
                CustomViewBus.get().post(new CustomViewEvent(CustomViewEvent.KEY_DISABLE_SCROLL, null));
                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_UP:
                CustomViewBus.get().post(new CustomViewEvent(CustomViewEvent.KEY_ENABLE_SCROLL, null));
                break;
            case MotionEvent.ACTION_CANCEL:
                CustomViewBus.get().post(new CustomViewEvent(CustomViewEvent.KEY_ENABLE_SCROLL, null));
                break;
            default:
                break;
        }
        invalidate();
        return true;
    }
}
