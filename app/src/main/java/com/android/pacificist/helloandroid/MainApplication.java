package com.android.pacificist.helloandroid;

import android.app.Application;
import android.os.Handler;

import com.android.pacificist.helloandroid.matrix.MatrixManager;
import com.android.pacificist.memory.MemoryObserver;

/**
 * Created by pacificist on 2018/7/8.
 */

public class MainApplication extends Application {

    private Handler mHandler = new Handler();

    @Override
    public void onCreate() {
        super.onCreate();
        MemoryObserver.init(this);
        MatrixManager.init(this);
    }
}
