package com.android.pacificist.helloandroid;

import android.app.Application;

import com.android.pacificist.memoryobserver.MemoryObserver;

/**
 * Created by pacificist on 2018/7/8.
 */

public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MemoryObserver.init(this);
    }
}
