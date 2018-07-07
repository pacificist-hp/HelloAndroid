package com.android.pacificist.helloandroid;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/7/7.
 */

public class FloatSmallView extends LinearLayout {

    private WindowManager.LayoutParams mParams;

    /**
     * 记录当前手指位置在屏幕上的横坐标值
     */
    private float xInScreen;

    /**
     * 记录当前手指位置在屏幕上的纵坐标值
     */
    private float yInScreen;

    /**
     * 记录手指按下时在屏幕上的横坐标的值
     */
    private float xDownInScreen;

    /**
     * 记录手指按下时在屏幕上的纵坐标的值
     */
    private float yDownInScreen;

    /**
     * 记录手指按下时在小悬浮窗的View上的横坐标的值
     */
    private float xInView;

    /**
     * 记录手指按下时在小悬浮窗的View上的纵坐标的值
     */
    private float yInView;

    public FloatSmallView(Context context) {
        super(context);
        initView();
    }

    public FloatSmallView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FloatSmallView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.float_small_view, this);
        ((TextView) findViewById(R.id.percent)).setText(R.string.small_float_view_content);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                xInView = event.getX();
                yInView = event.getY();
                xDownInScreen = event.getRawX();
                yDownInScreen = event.getRawY() - getStatusBarHeight();
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                break;
            case MotionEvent.ACTION_MOVE:
                xInScreen = event.getRawX();
                yInScreen = event.getRawY() - getStatusBarHeight();
                updateViewPosition();
                break;
            case MotionEvent.ACTION_UP:
                if (xDownInScreen == xInScreen && yDownInScreen == yInScreen) {
                    openBigWindow();
                }
                break;
            default:
                break;
        }
        return true;
    }

    private int getStatusBarHeight() {
        int resId = getContext().getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            return getContext().getResources().getDimensionPixelOffset(resId);
        }
        return 0;
    }

    public void setParams(WindowManager.LayoutParams params) {
        mParams = params;
    }

    private void updateViewPosition() {
        if (mParams != null) {
            mParams.x = (int) (xInScreen - xInView);
            mParams.y = (int) (yInScreen - yInView);
            ((WindowManager) getContext().getSystemService(
                    Context.WINDOW_SERVICE)).updateViewLayout(this, mParams);
        }
    }

    private void openBigWindow() {
        FloatViewManager.removeSmallFloatView(getContext());
        FloatViewManager.addBigFloatView(getContext());
    }
}
