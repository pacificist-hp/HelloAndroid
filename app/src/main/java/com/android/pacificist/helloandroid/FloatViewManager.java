package com.android.pacificist.helloandroid;

import android.content.Context;
import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;

/**
 * Created by Administrator on 2018/7/7.
 */

class FloatViewManager {

    private static FloatSmallView sSmallFloatView;
    private static FloatBigView sBigFloatView;
    private static WindowManager.LayoutParams sSmallParams;
    private static WindowManager.LayoutParams sBigParams;

    static void addSmallFloatView(Context context) {
        if (sSmallFloatView == null) {
            sSmallFloatView = new FloatSmallView(context);
            sSmallFloatView.setParams(getSmallViewParams(context));
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                wm.addView(sSmallFloatView, getSmallViewParams(context));
            }
        }
    }

    private static WindowManager.LayoutParams getSmallViewParams(Context context) {
        if (sSmallParams == null) {
            sSmallParams = new WindowManager.LayoutParams();
            sSmallParams.type = getRightWindowType();
            sSmallParams.format = PixelFormat.RGBA_8888;
            sSmallParams.gravity = Gravity.LEFT | Gravity.TOP;
            sSmallParams.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                    | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            sSmallParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            sSmallParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                sSmallParams.x = wm.getDefaultDisplay().getWidth();
                sSmallParams.y = wm.getDefaultDisplay().getHeight() / 2;
            }
        }
        return sSmallParams;
    }

    static void removeSmallFloatView(Context context) {
        if (sSmallFloatView != null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                wm.removeView(sSmallFloatView);
            }
            sSmallFloatView = null;
        }
    }

    static void addBigFloatView(Context context) {
        if (sBigFloatView == null) {
            sBigFloatView = new FloatBigView(context);
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                wm.addView(sBigFloatView, getBigViewParams(context));
            }
        }
    }

    private static WindowManager.LayoutParams getBigViewParams(Context context) {
        if (sBigParams == null) {
            sBigParams = new WindowManager.LayoutParams();
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                sBigParams.x = wm.getDefaultDisplay().getWidth() / 2 - sBigFloatView.viewWidth / 2;
                sBigParams.y = wm.getDefaultDisplay().getHeight() / 2 - sBigFloatView.viewHeight / 2;
            }
            sBigParams.type = getRightWindowType();
            sBigParams.format = PixelFormat.RGBA_8888;
            sBigParams.gravity = Gravity.LEFT | Gravity.TOP;
            sBigParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
            sBigParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        }
        return sBigParams;
    }

    static void removeBigWindow(Context context) {
        if (sBigFloatView != null) {
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            if (wm != null) {
                wm.removeView(sBigFloatView);
            }
            sBigFloatView = null;
        }
    }

    static boolean isWindowShowing() {
        return sSmallFloatView != null || sBigFloatView != null;
    }

    private static int getRightWindowType() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT
                && Build.VERSION.SDK_INT < Build.VERSION_CODES.N_MR1) {
            return WindowManager.LayoutParams.TYPE_TOAST;
        } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            return WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
        } else {
            return WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        }
    }
}
