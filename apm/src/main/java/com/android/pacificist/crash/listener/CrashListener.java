package com.android.pacificist.crash.listener;

/**
 * Created by pacificist on 2019/5/11.
 */
public interface CrashListener {
    void onCrash(Thread thread, Throwable throwable);
}
