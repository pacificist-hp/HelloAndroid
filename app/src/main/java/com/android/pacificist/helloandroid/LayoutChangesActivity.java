package com.android.pacificist.helloandroid;

import android.animation.LayoutTransition;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Created by pacificist on 2021/1/16.
 */
public class LayoutChangesActivity extends AppCompatActivity {

    private FrameLayout.LayoutParams leftViewParams;
    private FrameLayout.LayoutParams rightViewParams;

    private View left;
    private View right;

    private FrameLayout rootView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layout_changes);

        rootView = findViewById(R.id.root_view);
        LayoutTransition layoutTransition = new LayoutTransition();
        layoutTransition.enableTransitionType(LayoutTransition.CHANGING);
        rootView.setLayoutTransition(layoutTransition); // equals android:animateLayoutChanges="true"

        init();

        rootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                rootView.addView(left, leftViewParams);
                rootView.addView(right, rightViewParams);

            }
        }, 1000);

        rootView.postDelayed(new Runnable() {
            @Override
            public void run() {
                rootView.updateViewLayout(left, rightViewParams);
                rootView.updateViewLayout(right, leftViewParams);
            }
        }, 2000);
    }

    private void init() {
        leftViewParams = new FrameLayout.LayoutParams(300, 500);
        leftViewParams.gravity = Gravity.TOP | Gravity.LEFT;
        leftViewParams.topMargin = 40;
        leftViewParams.leftMargin = 40;

        rightViewParams = new FrameLayout.LayoutParams(300, 500);
        rightViewParams.gravity = Gravity.TOP | Gravity.RIGHT;
        rightViewParams.topMargin = 40;
        rightViewParams.rightMargin = 40;

        left = new View(this);
        left.setBackgroundColor(Color.GREEN);

        right = new View(this);
        right.setBackgroundColor(Color.YELLOW);
    }
}
