package com.client.surface;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class WindowSurfaceView extends SurfaceView implements SurfaceHolder.Callback {
    private SurfaceHolder mSurfaceHolder;
    private Canvas mCanvas;
    private Paint mPaint;

    public WindowSurfaceView(Context context) {
        super(context);
        init();
    }

    public WindowSurfaceView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public WindowSurfaceView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
        setFocusable(true);
        setFocusableInTouchMode(true);

        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                drawCircle();
            }
        }).start();
    }

    private void drawCircle() {
        try {
            mCanvas = mSurfaceHolder.lockCanvas();
            mCanvas.drawColor(Color.GREEN);
            mCanvas.drawCircle(100, 100, 50, mPaint);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mCanvas != null) {
                mSurfaceHolder.unlockCanvasAndPost(mCanvas);
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
}
