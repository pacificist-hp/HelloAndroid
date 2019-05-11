package com.android.pacificist.crash;

import android.util.Log;

import com.android.pacificist.Util;
import com.android.pacificist.crash.interceptor.CrashInterceptor;
import com.android.pacificist.crash.listener.CrashListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pacificist on 2019/5/11.
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {

    private final static String TAG = "CrashHandler";

    private Thread.UncaughtExceptionHandler mDefaultUncaughtExceptionHandler;

    private List<CrashInterceptor> mInterceptors = new ArrayList<>();
    private List<CrashListener> mListeners = new ArrayList<>();

    private static CrashHandler sHandler;

    private CrashHandler() {

    }

    static CrashHandler get() {
        if (sHandler == null) {
            sHandler = new CrashHandler();
        }
        return sHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {

        try {
            // A throwable can be ignored sometime if it occurs in a non-main thread
            if (!Util.isMainThread(thread)) {
                for (CrashInterceptor ignore : mInterceptors) {
                    if (ignore.isIntercepted(thread, throwable)) {
                        onUncaughtException(thread, throwable);
                        return;
                    }
                }
            }

            onUncaughtException(thread, throwable);
        } catch (Exception e) {

        }

        if (mDefaultUncaughtExceptionHandler != null) {
            mDefaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
        }
    }

    private void onUncaughtException(Thread thread, Throwable throwable) {
        Log.e(TAG, "thread:" + thread.getName() + ", throwable:" + Log.getStackTraceString(throwable));
        for (CrashListener listener : mListeners) {
            listener.onCrash(thread, throwable);
        }
    }

    public void addCrashInterceptor(CrashInterceptor interceptor) {
        mInterceptors.add(interceptor);
    }

    public void removeCrashInterceptor(CrashInterceptor interceptor) {
        mInterceptors.remove(interceptor);
    }

    public void addCrashListener(CrashListener listener) {
        mListeners.add(listener);
    }

    public void removeCrashListener(CrashListener listener) {
        mListeners.remove(listener);
    }

    public void enable() {
        Thread.UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (currentHandler instanceof CrashHandler) {
            return;
        }

        mDefaultUncaughtExceptionHandler = currentHandler;
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public void disable() {
        Thread.UncaughtExceptionHandler currentHandler = Thread.getDefaultUncaughtExceptionHandler();
        if (!(currentHandler instanceof CrashHandler)) {
            return;
        }
        if (mDefaultUncaughtExceptionHandler != null) {
            Thread.setDefaultUncaughtExceptionHandler(mDefaultUncaughtExceptionHandler);
        }
    }
}
