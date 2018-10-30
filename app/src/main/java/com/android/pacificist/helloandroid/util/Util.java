package com.android.pacificist.helloandroid.util;

import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by pacificist on 2018/10/29.
 */
public class Util {
    public static int dp2px(float dp, Resources resources) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
    }
}
