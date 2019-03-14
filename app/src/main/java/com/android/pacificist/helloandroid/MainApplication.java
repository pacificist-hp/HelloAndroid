package com.android.pacificist.helloandroid;

import android.app.Application;

import com.android.pacificist.helloandroid.matrix.MatrixManager;
import com.android.pacificist.launch.LaunchObserver;
import com.android.pacificist.memory.MemoryObserver;

/**
 * Created by pacificist on 2018/7/8.
 */

public class MainApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        MemoryObserver.init(this);
        LaunchObserver.init(this);
        MatrixManager.init(this);
    }
}
