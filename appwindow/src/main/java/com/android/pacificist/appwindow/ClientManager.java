package com.android.pacificist.appwindow;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import static com.android.pacificist.appwindow.ClientConstant.TAG;

public class ClientManager {

    private final static String PKG_APP_WINDOW_SERVICE = "com.android.pacificist.helloandroid";
    private final static String CLS_APP_WINDOW_SERVICE = "com.android.pacificist.helloandroid.appwindow.AppWindowService";

    private final static int MSG_REBIND = 0x01;

    private final static long REBIND_INTERVAL = 5000L;
    private final static long REBIND_INTERVAL_LONG = 30000L;

    private final static int REBIND_TIME = 5;
    private final static int REBIND_TIME_LONG = 10;

    private Context mContext;
    private IServer mServer = null;
    private boolean mConnected = false;
    private int mRetryTime = 0;

    private H mHandler;

    private static volatile ClientManager instance = null;

    public static ClientManager get(Context context) {
        if (instance == null) {
            synchronized (ClientManager.class) {
                if (instance == null) {
                    instance = new ClientManager(context);
                }
            }
        }

        return instance;
    }

    private ClientManager(Context context) {
        if (context != null) {
            mContext = context.getApplicationContext();
            mHandler = new H(context.getMainLooper());
            bindAppWindowService();
        }
    }

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            Log.d(TAG, "client onServiceConnected: " + iBinder);
            mConnected = iBinder != null;
            if (mConnected) {
                mServer = IServer.Stub.asInterface(iBinder);

                mRetryTime = 0;
                mHandler.removeMessages(MSG_REBIND);
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            Log.d(TAG, "client onServiceDisconnected");

            mConnected = false;
            mServer = null;
        }

        @Override
        public void onBindingDied(ComponentName name) {
            Log.d(TAG, "client onBindingDied, try to rebind.");

            mConnected = false;
            mServer = null;

            if (mContext != null) {
                mContext.unbindService(mServiceConnection);
            }
            mHandler.sendEmptyMessageDelayed(MSG_REBIND,REBIND_INTERVAL);
        }
    };

    private void bindAppWindowService() {
        Intent intent = new Intent();
        intent.setComponent(new ComponentName(PKG_APP_WINDOW_SERVICE, CLS_APP_WINDOW_SERVICE));
        if (mContext != null) {
            boolean ret = mContext.bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
            Log.d(TAG, "client bind: " + ret);
        }
    }

    private void rebindAppWindowService() {
        if (mConnected || mRetryTime >= REBIND_TIME_LONG) {
            return;
        }

        bindAppWindowService();
        mRetryTime++;

        mHandler.removeMessages(MSG_REBIND);
        if (mRetryTime < REBIND_TIME) {
            mHandler.sendEmptyMessageDelayed(MSG_REBIND, REBIND_INTERVAL);
        } else {
            mHandler.sendEmptyMessageDelayed(MSG_REBIND, REBIND_INTERVAL_LONG);
        }
    }

    public void moveAppWindow(ClientArea area, float x, float y) {
        if (mServer != null && area != null) {
            try {
                mServer.moveAppWindow(area.name(), x, y);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void maxAppWindow() {
        if (mServer != null && mContext != null) {
            try {
                mServer.maxAppWindow(mContext.getPackageName());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class H extends Handler {
        H(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == MSG_REBIND) {
                ClientManager.this.rebindAppWindowService();
            }
        }
    }
}
