package com.android.pacificist.helloandroid;

import android.app.Application;

/**
 * Created by pacificist on 2019/6/30.
 */
public class MainApplication extends Application {

    public static Application app;

    @Override
    public void onCreate() {
        super.onCreate();
        app = this;
    }
}
