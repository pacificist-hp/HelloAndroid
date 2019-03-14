package com.android.pacificist.memory;

import android.app.Activity;

import java.util.HashMap;
import java.util.Map;
import java.util.WeakHashMap;

/**
 * Created by pacificist on 2018/7/22.
 */

public class ActivityDumper {

    /**
     * WeakHashMap will remove a key object automatically when it is collected by gc
     *
     * Key:   activity object
     * Value: nothing
     */
    private static WeakHashMap<Activity, Integer> sActivities = new WeakHashMap<>();

    public static void watch(Activity activity) {
        sActivities.put(activity, 0);
    }

    public static Map<String, Integer> getActivitiesInfo() {
        Map<String, Integer> info = new HashMap<>();
        for (Activity a : sActivities.keySet()) {
            String key = a.getLocalClassName();
            Integer value = info.get(key);
            if (value == null) {
                info.put(key, 1);
            } else {
                info.put(key, value + 1);
            }
        }
        return info;
    }
}
