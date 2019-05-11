package com.android.pacificist;

import android.os.Looper;
import android.text.TextUtils;

/**
 * Created by pacificist on 2019/5/11.
 */
public class Util {

    public static boolean isStringBlank(final String str) {
        if (TextUtils.isEmpty(str)) {
            return true;
        }

        for (int i = 0; i < str.length(); i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                return false;
            }
        }

        return true;
    }

    public static boolean isMainThread(Thread thread) {
        if (thread != null) {
            return Looper.getMainLooper().getThread() == thread;
        }

        return false;
    }
}
