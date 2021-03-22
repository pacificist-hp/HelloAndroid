package com.android.pacificist.appwindow;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;

import static com.android.pacificist.appwindow.ClientConstant.DEBUG;

class ClientRootView extends FrameLayout {

    private final static long LONG_CLICK_TIME_OUT = 500;
    private final static long LONG_CLICK_INTERVAL = 2000;
    private final static long LONG_CLICK_RANGE = 48;

    private float mDownX = 0;
    private float mDownY = 0;

    private long mLastDownTime = 0;

    private final Runnable mLongClickRunnable = new Runnable() {
        @Override
        public void run() {
            if (mListener != null) {
                mListener.onLongClick(ClientRootView.this);
            }
        }
    };

    private View.OnLongClickListener mListener = null;

    private boolean mIntercept = false;

    public ClientRootView(@NonNull Context context) {
        super(context);
    }

    public ClientRootView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ClientRootView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setOnRootLongClickListener(View.OnLongClickListener listener) {
        mListener = listener;
    }

    public void setIntercept(boolean intercept) {
        mIntercept = intercept;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIntercept) {
            Log.d(DEBUG, "intercept: " + ev.getAction());
            return true;
        }

        Log.d(DEBUG, "action: " + ev.getAction() + ", pointer: " + ev.getPointerCount());
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            removeCallbacks(mLongClickRunnable);

            long current = System.currentTimeMillis();

            if (ev.getPointerCount() == 1 && current - mLastDownTime > LONG_CLICK_INTERVAL) {
                mDownX = ev.getX();
                mDownY = ev.getY();
                postDelayed(mLongClickRunnable, LONG_CLICK_TIME_OUT);
            }

            mLastDownTime = current;
        } else if (ev.getAction() == MotionEvent.ACTION_UP
                || ev.getAction() == MotionEvent.ACTION_CANCEL) {
            removeCallbacks(mLongClickRunnable);
        } else {
            if (Math.abs(ev.getX() - mDownX) > LONG_CLICK_RANGE
                    || Math.abs(ev.getY() - mDownY) > LONG_CLICK_RANGE) {
                removeCallbacks(mLongClickRunnable);
            }
        }

        return super.onInterceptTouchEvent(ev);
    }
}
