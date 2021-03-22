package com.android.pacificist.appwindow;

import android.graphics.PixelFormat;
import android.os.Build;
import android.view.Gravity;
import android.view.WindowManager;

import static com.android.pacificist.appwindow.ClientConstant.AREA_DIVIDER;
import static com.android.pacificist.appwindow.ClientConstant.AREA_HEIGHT;
import static com.android.pacificist.appwindow.ClientConstant.AREA_LEFT;
import static com.android.pacificist.appwindow.ClientConstant.AREA_TOP;
import static com.android.pacificist.appwindow.ClientConstant.AREA_WIDTH;

/**
 * 窗口样式（大小 + 位置）枚举
 */
public enum ClientArea {
    NONE(0, 0, 0, 0),
    SINGLE(AREA_WIDTH, AREA_HEIGHT, AREA_LEFT, AREA_TOP),
    TOP(AREA_WIDTH, (AREA_HEIGHT - AREA_DIVIDER * 2) / 3, AREA_LEFT, AREA_TOP),
    DOUBLE_BOTTOM(AREA_WIDTH, (AREA_HEIGHT * 2 - AREA_DIVIDER) / 3, AREA_LEFT, AREA_TOP + (AREA_HEIGHT + AREA_DIVIDER) / 3),
    MIDDLE(AREA_WIDTH, (AREA_HEIGHT - AREA_DIVIDER * 2) / 3, AREA_LEFT, AREA_TOP + (AREA_HEIGHT + AREA_DIVIDER) / 3),
    TRIPLE_BOTTOM(AREA_WIDTH, (AREA_HEIGHT - AREA_DIVIDER * 2) / 3, AREA_LEFT, AREA_TOP + (AREA_HEIGHT + AREA_DIVIDER) * 2 / 3);

    private final int width;
    private final int height;
    private final int x;
    private final int y;

    ClientArea(int width, int height, int x, int y) {
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
    }

    private static int getDefaultWindowType() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            return WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            return WindowManager.LayoutParams.TYPE_PHONE;
        }
    }

    public WindowManager.LayoutParams getWindowParams() {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams();
        params.type = getDefaultWindowType();
        params.format = PixelFormat.RGBA_8888;
        params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
                | WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
        params.gravity = Gravity.START | Gravity.TOP;

        params.width = width;
        params.height = height;
        params.x = x;
        params.y = y;

        return params;
    }

    public boolean inside(float x, float y) {
        return x > this.x && y > this.y && x < this.x + width & y < this.y + height;
    }
}

