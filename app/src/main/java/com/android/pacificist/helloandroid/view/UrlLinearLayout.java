package com.android.pacificist.helloandroid.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

/**
 * 可以填充 URL 内容作为 background 的 LinearLayout
 *
 * Created by pacificist on 2020/1/4.
 */
public class UrlLinearLayout extends LinearLayout implements Target {

    private Bitmap mBitmap = null;

    public UrlLinearLayout(@NonNull Context context) {
        super(context);
    }

    public UrlLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public UrlLinearLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setBackgroundUrl(final String url) {
        if (TextUtils.isEmpty(url)) {
            mBitmap = null;
            postInvalidate();
        } else {
            Picasso.get().load(url).into(this);
        }
    }

    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        mBitmap = bitmap;
        postInvalidate();
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {

    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }

    /*
     ViewGroup 绘制时，不一定进入 onDraw，但一定进入 dispatchDraw
     先绘制自定义背景，然后执行 super.dispatchDraw 绘制子 view
     */
    @Override
    protected void dispatchDraw(Canvas canvas) {
        if (mBitmap != null) {
            canvas.drawBitmap(mBitmap,
                    new Rect(0, 0, mBitmap.getWidth(), mBitmap.getHeight()),
                    new Rect(0, 0, getWidth(), getHeight()),
                    new Paint());

            /* fitXY
            mDrawable.setBounds(0, 0, getWidth(), getHeight());
            mDrawable.draw(canvas);
             */

            /* centerCrop
            if (mDrawable.getIntrinsicWidth() > 0 && mDrawable.getIntrinsicHeight() > 0) {
                float scaleX = getWidth() / (float) mDrawable.getIntrinsicWidth();
                float scaleY = getHeight() / (float) mDrawable.getIntrinsicHeight();
                if (scaleX > scaleY) {
                    // drawable 的宽缩放到与 view 相等，高缩放到大于 view 的高
                    int offsetY = (int) ((mDrawable.getIntrinsicHeight() * scaleX - getHeight()) / 2);
                    mDrawable.setBounds(0, -offsetY, getWidth(), getHeight() + offsetY);
                } else {
                    // drawable 的高缩放到与 view 相等，宽缩放到大于 view 的宽
                    int offsetX = (int) ((mDrawable.getIntrinsicWidth() * scaleY - getWidth()) / 2);
                    mDrawable.setBounds(-offsetX, 0, getWidth() + offsetX, getHeight());
                }
                mDrawable.draw(canvas);
            }
             */

        }

        super.dispatchDraw(canvas);
    }
}
