package com.android.pacificist.helloandroid.business;

import android.app.Application;
import android.text.TextUtils;
import android.util.Log;

import com.android.pacificist.helloandroid.EasyCache;
import com.android.pacificist.helloandroid.MainApplication;

/**
 * Created by pacificist on 2019/6/30.
 */
public class Repository {

    private static final String TAG = "EasyCache";

    public String loadData(boolean forceRemote) {
        String data = "";
        if (!forceRemote) {
            data = EasyCache.getCache(MainApplication.app).readCache("debug");
            Log.d(TAG, "local:" + data);
        }

        if (TextUtils.isEmpty(data) || forceRemote) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {

            }
            data = "This is debug data from remote";
            EasyCache.getCache(MainApplication.app).saveCache("debug", data);
            Log.d(TAG, "remote:" + data);
        }

        return data;
    }
}
