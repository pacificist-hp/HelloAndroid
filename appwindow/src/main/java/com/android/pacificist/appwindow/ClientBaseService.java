package com.android.pacificist.appwindow;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import static com.android.pacificist.appwindow.ClientConstant.TAG;

public abstract class ClientBaseService extends Service implements IClientCallback {

    private ClientWindow mClientWindow;

    @Override
    public void onCreate() {
        super.onCreate();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setForeground();
        }

        View view = LayoutInflater.from(this).inflate(getLayout(), null, false);
        onViewCreated(view);
        ClientManager.get(this);

        mClientWindow = new ClientWindow(view, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            Log.w(TAG, this.toString() + " may be killed 1 second ago.");
            return Service.START_STICKY;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            setForeground();
        }

        String area = intent.getStringExtra("area");
        if (!TextUtils.isEmpty(area)) {
            handleClientArea(area);
        }

        return Service.START_STICKY;
    }

    private void handleVisible(boolean visible) {
        mClientWindow.getView().setVisibility(visible ? View.VISIBLE : View.INVISIBLE);
    }

    private void handleClientArea(String area) {
        ClientArea clientArea = ClientArea.NONE;
        try {
            clientArea = ClientArea.valueOf(area);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (clientArea == ClientArea.NONE) {
            mClientWindow.dismiss();
        } else {
            mClientWindow.show(clientArea);
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void setForeground() {
        NotificationChannel channel = new NotificationChannel("channel_id", "window", NotificationManager.IMPORTANCE_HIGH);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.createNotificationChannel(channel);

        Notification notification = new Notification.Builder(getApplicationContext(), "channel_id").build();
        startForeground(1, notification);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopForeground(true);
    }
}
