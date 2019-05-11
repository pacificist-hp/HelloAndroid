package com.android.pacificist.crash;

import com.android.pacificist.crash.interceptor.CrashInterceptor;
import com.android.pacificist.crash.interceptor.NoSystemThreadInterceptor;

/**
 * Created by pacificist on 2019/5/11.
 */
public class CrashObserver {

    private static CrashObserver sObserver;

    private CrashInterceptor mInterceptor = new NoSystemThreadInterceptor();

    private CrashObserver() {

    }

    public static CrashObserver get() {
        if (sObserver == null) {
            sObserver = new CrashObserver();
        }
        return sObserver;
    }

    public void init() {
        CrashHandler.get().enable();
        CrashHandler.get().addCrashInterceptor(mInterceptor);
    }

    public void quit() {
        CrashHandler.get().disable();
        CrashHandler.get().removeCrashInterceptor(mInterceptor);
    }
}
