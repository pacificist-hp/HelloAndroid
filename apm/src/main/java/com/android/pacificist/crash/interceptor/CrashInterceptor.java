package com.android.pacificist.crash.interceptor;

/**
 * Created by pacificist on 2019/5/11.
 */
public interface CrashInterceptor {

    boolean isIntercepted(Thread thread, Throwable throwable);
}
