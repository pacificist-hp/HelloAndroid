package com.android.pacificist.helloandroid;

import android.animation.LayoutTransition;
import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;

/**
 * Created by pacificist on 2021/1/16.
 */
public class LayoutChangesActivity extends AppCompatActivity {

    private FrameLayout.LayoutParams leftViewParams;
    private FrameLayout.LayoutParams rightViewParams;

    private WindowManager.LayoutParams leftWindowParams;
    private WindowManager.LayoutParams rightWindowParams;

    private View left;
    private View right;

    private FrameLayout rootView;

    private WindowManager windowManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_changes);

        rootView = findViewById(R.id.root_view);
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        rootView.setLayoutTransition(layoutTransition); // equals android:animateLayoutChanges="true"

        windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);

        init();

        rootView.postDelayed(new Runnable() {
            @Override
            public void run() {
//                rootView.addView(left, leftViewParams);
//                rootView.addView(right, rightViewParams);

                windowManager.addView(left, leftWindowParams);
                windowManager.addView(right, rightWindowParams);
            }
        }, 1000);

        rootView.postDelayed(new Runnable() {
            @Override
            public void run() {
//                rootView.updateViewLayout(left, rightViewParams);
//                rootView.updateViewLayout(right, leftViewParams);

                windowManager.updateViewLayout(left, rightWindowParams);
                windowManager.updateViewLayout(right, leftWindowParams);
            }
        }, 2000);

        rootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                windowManager.removeView(left);
                windowManager.removeView(right);
            }
        }, 4000);
    }

    private void init() {
        leftViewParams = new FrameLayout.LayoutParams(300, 500);
        leftViewParams.gravity = Gravity.TOP | Gravity.LEFT;
        leftViewParams.topMargin = 40;
        leftViewParams.leftMargin = 40;

        rightViewParams = new FrameLayout.LayoutParams(300, 501);
        rightViewParams.gravity = Gravity.TOP | Gravity.RIGHT;
        rightViewParams.topMargin = 40;
        rightViewParams.rightMargin = 40;

        leftWindowParams = getBaseWindowParams();
        leftWindowParams.width = 300;
        leftWindowParams.height = 500;
        leftWindowParams.x = 40;
        leftWindowParams.y = 150;

        rightWindowParams = getBaseWindowParams();
        rightWindowParams.width = 300;
        rightWindowParams.height = 500;
        rightWindowParams.x = 380;
        rightWindowParams.y = 150;

        left = new View(this);
        left.setBackgroundColor(Color.GREEN);

        right = new View(this);
        right.setBackgroundColor(Color.YELLOW);
    }

    private WindowManager.LayoutParams getBaseWindowParams() {
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_TOAST;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }

        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
        layoutParams.gravity = Gravity.LEFT | Gravity.TOP;

        return layoutParams;
    }
}
