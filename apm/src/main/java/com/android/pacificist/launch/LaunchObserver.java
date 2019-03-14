package com.android.pacificist.launch;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.MessageQueue;
import android.util.Log;

/**
 * Created by pacificist on 2019/3/14.
 */
public class LaunchObserver {

    private static final String TAG = "LaunchObserver";

    private static Application.ActivityLifecycleCallbacks sCallback
            = new Application.ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {
            ++sCreatedPageCount;
        }

        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

    public static int sCreatedPageCount = 0;

    public static void init(Application application) {
        if (application == null) {
            return;
        }
        application.registerActivityLifecycleCallbacks(sCallback);

        new Handler(application.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Looper.myQueue().addIdleHandler(new MessageQueue.IdleHandler() {
                    @Override
                    public boolean queueIdle() {
                        if (sCreatedPageCount == 0) {
                            Log.d(TAG, "next time will be hot launch");
                        } else {
                            Log.d(TAG, "this time is cold start");
                        }
                        return false;
                    }
                });
            }
        }, 3000);
    }
}
