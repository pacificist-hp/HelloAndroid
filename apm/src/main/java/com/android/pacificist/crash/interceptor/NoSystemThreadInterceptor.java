package com.android.pacificist.crash.interceptor;

import com.android.pacificist.Util;

import java.util.regex.Pattern;

/**
 * Created by pacificist on 2019/5/11.
 */
public class NoSystemThreadInterceptor implements CrashInterceptor {

    private final Pattern NO_NAME_THREAD = Pattern.compile("Thread-\\d+");

    @Override
    public boolean isIntercepted(Thread thread, Throwable throwable) {
        String name = thread.getName();
        if (Util.isStringBlank(name)) {
            return true;
        }
        if (NO_NAME_THREAD.matcher(name).find()) {
            return true;
        }

        return thread.isDaemon();
    }
}
