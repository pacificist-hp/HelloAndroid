package com.android.pacificist.memoryobserver;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import java.util.Map;

/**
 * Created by pacificist on 2018/7/8.
 */

public class MemoryObserver {
    private static final String TAG = "MemoryObserver";

    private static Application.ActivityLifecycleCallbacks sWatcher
            = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle bundle) {

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
            ActivityDumper.watch(activity);
        }
    };

    private static ComponentCallbacks2 sReporter = new ComponentCallbacks2() {
        @Override
        public void onTrimMemory(int level) {
            if (level == TRIM_MEMORY_UI_HIDDEN && MemoryDumper.isAllocatedMemoryTooMany()) {
                new ReportTask().execute();
            }
        }

        @Override
        public void onConfigurationChanged(Configuration configuration) {

        }

        @Override
        public void onLowMemory() {

        }
    };

    public static void init(Application application) {
        //LeakCanary.install(application);
        if (application == null) {
            return;
        }
        application.registerActivityLifecycleCallbacks(sWatcher);
        application.registerComponentCallbacks(sReporter);
    }

    public static void quit(Application application) {
        if (application == null) {
            return;
        }
        application.unregisterActivityLifecycleCallbacks(sWatcher);
        application.unregisterComponentCallbacks(sReporter);
    }

    private static class ReportTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            GcTrigger.DEFAULT.runGc();
            Map<String, String> memoryInfo = MemoryDumper.getMemoryInfo();
            Log.w(TAG, "memory info:");
            for (Map.Entry<String, String> entry : memoryInfo.entrySet()) {
                Log.w(TAG, entry.getKey() + " : " + entry.getValue());
            }
            Log.w(TAG, "activity info:");
            Map<String, Integer> activities = ActivityDumper.getNumberOfActivities();
            for (Map.Entry<String, Integer> entry : activities.entrySet()) {
                // report the num of activities which can not be collected by gc
                Log.w(TAG, entry.getKey() + " : " + String.valueOf(entry.getValue()));
            }
            return null;
        }
    }
}
