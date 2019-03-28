package com.android.pacificist.jam;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;

/**
 * Created by pacificist on 2019/3/26.
 */
public class JamObserver implements Runnable {

    private final static String TAG = "JamObserver";

    private final static long INTERVAL = 1000;
    private final static int NULL_MSG_HASH_CODE = -1;

    private Application mApplication;

    private MessageQueueProxy mProxy;

    private int mActivityCount = 0;

    private HandlerThread mWorkThread;
    private Handler mWorkHandler;

    private int mLastMsgHashCode = NULL_MSG_HASH_CODE;
    private int mLastSendMsgHashCode = NULL_MSG_HASH_CODE;

    private Application.ActivityLifecycleCallbacks mCallback
            = new Application.ActivityLifecycleCallbacks() {

        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityStarted(Activity activity) {
            mActivityCount++;
            mWorkHandler.removeCallbacks(JamObserver.this);
            mWorkHandler.postDelayed(JamObserver.this, INTERVAL);
        }

        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Override
        public void onActivityStopped(Activity activity) {
            if (mActivityCount > 0) {
                mActivityCount--;
            }
            if (mActivityCount == 0) {
                mWorkHandler.removeCallbacks(JamObserver.this);
            }
        }

        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle bundle) {

        }

        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

    private static JamObserver sObserver = null;

    private JamObserver() {

    }

    public static JamObserver get() {
        if (sObserver == null) {
            sObserver = new JamObserver();
        }
        return sObserver;
    }

    public void init(Application application) {
        if (application == null) {
            return;
        }
        if (mApplication == null) {
            mApplication = application;
            mApplication.registerActivityLifecycleCallbacks(mCallback);

            mProxy = new MessageQueueProxy();

            mWorkThread = new HandlerThread("anr_observer_thread");
            mWorkThread.start();

            mWorkHandler = new Handler(mWorkThread.getLooper());
            mWorkHandler.postDelayed(this, INTERVAL);
        }
    }

    @Override
    public void run() {
        checkMainThread();
        if (mActivityCount > 0) {
            mWorkHandler.postDelayed(this, INTERVAL);
        }
    }

    private void checkMainThread() {
        int currentMsgHashCode = getMessageHashCodeIfBlock();
        if (mLastMsgHashCode == currentMsgHashCode && mLastMsgHashCode != NULL_MSG_HASH_CODE) {
            if (mLastSendMsgHashCode != mLastMsgHashCode) {
                Log.d(TAG, "jam now!!!");
                mLastSendMsgHashCode = mLastMsgHashCode;
            }
        } else {
            mLastMsgHashCode = currentMsgHashCode;
        }
    }

    private int getMessageHashCodeIfBlock() {
        Message msg = mProxy.getCurrentMessage();
        if (msg != null) {
            if (msg.getWhen() < SystemClock.uptimeMillis()) {
                return msg.hashCode();
            }
        }
        return NULL_MSG_HASH_CODE;
    }

    public void quit() {
        if (mApplication != null) {
            mApplication.unregisterActivityLifecycleCallbacks(mCallback);
        }
        mApplication = null;
        if (mWorkThread != null) {
            mWorkThread.quit();
        }
        mWorkThread = null;
        mWorkHandler = null;
    }
}
