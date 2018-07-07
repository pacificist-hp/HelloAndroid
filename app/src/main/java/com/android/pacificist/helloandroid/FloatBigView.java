package com.android.pacificist.helloandroid;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Administrator on 2018/7/7.
 */

public class FloatBigView extends LinearLayout {

    public int viewWidth;
    public int viewHeight;

    public FloatBigView(Context context) {
        super(context);
        initView();
    }

    public FloatBigView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public FloatBigView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    private void initView() {
        LayoutInflater.from(getContext()).inflate(R.layout.float_big_view, this);
        View view = findViewById(R.id.big_window_layout);
        viewWidth = view.getLayoutParams().width;
        viewHeight = view.getLayoutParams().height;
        findViewById(R.id.close).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatViewManager.removeBigWindow(getContext());
                FloatViewManager.removeSmallFloatView(getContext());
            }
        });
        findViewById(R.id.back).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FloatViewManager.removeBigWindow(getContext());
                FloatViewManager.addSmallFloatView(getContext());
            }
        });
    }
}
