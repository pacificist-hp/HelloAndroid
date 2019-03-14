package com.android.pacificist.memory;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.support.v7.widget.AppCompatImageView;
import android.util.Log;

/**
 * Created by pacificist on 2018/8/21.
 */
public class TraceImageView extends AppCompatImageView {

    private final static String TAG = "TraceImageView";

    public TraceImageView(Context context) {
        super(context);
    }

    public TraceImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TraceImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        Drawable drawable = getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            BitmapDrawable bd = (BitmapDrawable) drawable;
            Log.w(TAG, "w1=" + bd.getBitmap().getWidth() + ", h1=" + bd.getBitmap().getHeight());
        }

        /*
        this.buildDrawingCache();
        Log.w(TAG, "w2=" + getDrawingCache().getWidth() + ", h2=" + getDrawingCache().getHeight());
        */
    }
}
