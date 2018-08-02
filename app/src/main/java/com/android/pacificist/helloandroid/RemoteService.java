package com.android.pacificist.helloandroid;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by pacificist on 2018/7/31.
 */
public class RemoteService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        NativeService.print("call NativeService.print in client process");
        return super.onStartCommand(intent, flags, startId);
    }
}
