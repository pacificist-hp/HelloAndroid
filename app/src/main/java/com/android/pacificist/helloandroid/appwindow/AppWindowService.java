package com.android.pacificist.helloandroid.appwindow;

import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import com.android.pacificist.appwindow.ClientArea;
import com.android.pacificist.appwindow.IServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppWindowService extends Service {

    private final static String TAG = "AppWindowService";

    private final static Map<String, String> sClients = new HashMap<>();
    static {
        sClients.put("com.client.map", "com.client.map.WindowService");
        sClients.put("com.client.music", "com.client.music.WindowService");
        sClients.put("com.client.surface", "com.client.surface.WindowService");
    }

    private AreaManager mAreaManager;

    private final IServer.Stub mBinder = new IServer.Stub() {
        @Override
        public void moveAppWindow(String source, float targetX, float targetY) throws RemoteException {
            Log.d(TAG, "moveAppWindow: " + source + " to " + targetX + "," + targetY);
            if (!mAreaManager.isEmptyStyle()) {
                ClientArea sourceArea = ClientArea.NONE;
                try {
                    sourceArea = ClientArea.valueOf(source);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                ClientArea targetArea = mAreaManager.getPointArea(targetX, targetY);
                Log.d(TAG, "moveAppWindow: " + source + " to " + targetArea.name());

                if (sourceArea != ClientArea.NONE && targetArea != ClientArea.NONE
                        && sourceArea != targetArea) {
                    String sourcePkg = mAreaManager.getClientInArea(sourceArea);
                    String targetPkg = mAreaManager.getClientInArea(targetArea);
                    addOrRemoveAppWindow(sourcePkg, targetArea);
                    addOrRemoveAppWindow(targetPkg, sourceArea);
                }
            }
        }

        @Override
        public void maxAppWindow(String pkg) throws RemoteException {
            ArrayList<String> packages = new ArrayList<>();
            packages.add(pkg);

            Bundle bundle = new Bundle();
            bundle.putStringArrayList("packages", packages);

            handleAppWindow(bundle);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mAreaManager = new AreaManager();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (intent == null) {
            Log.w(TAG, "AppWindowService may be killed 1 second ago");
            return Service.START_STICKY;
        }

        String type = intent.getStringExtra("type");
        Bundle data = intent.getExtras();
        switch (type) {
            case "appWindow":
                handleAppWindow(data);
                break;
            case "areaStyle":
                handleAreaStyle();
                break;
            default:
                break;
        }

        return Service.START_STICKY;
    }

    private void handleAppWindow(Bundle bundle) {
        if (bundle != null) {
            List<String> apps = bundle.getStringArrayList("packages");
            removeNonTargetWindows(apps);

            mAreaManager.setCurrentStyle(apps);

            ClientArea[] areas = mAreaManager.getCurrentAreas();
            for (int i = 0; i < areas.length; i++) {
                addOrRemoveAppWindow(apps.get(i), areas[i]);
            }
        }
    }

    private void removeNonTargetWindows(List<String> targets) {
        for (String pkg : mAreaManager.getAllClients()) {
            if (targets == null || !targets.contains(pkg)) {
                addOrRemoveAppWindow(pkg, ClientArea.NONE);
            }
        }
    }

    private void handleAreaStyle() {
        ArrayList<String> apps = new ArrayList<>();
        int next = (mAreaManager.getCurrentStyle() + 1) % 4;
        if (next > 0) {
            apps.add("com.client.map");
        }
        if (next > 1) {
            apps.add("com.client.music");
        }
        if (next > 2) {
            apps.add("com.client.surface");
        }

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("packages", apps);
        handleAppWindow(bundle);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    private void addOrRemoveAppWindow(String pkg, ClientArea clientArea) {
        if (TextUtils.isEmpty(pkg) || !sClients.containsKey(pkg)) {
            return;
        }

        Intent intent = new Intent();
        intent.setComponent(new ComponentName(pkg, sClients.get(pkg)));

        if (clientArea == ClientArea.NONE) {
            mAreaManager.removeClient(pkg);
        } else {
            mAreaManager.addClient(clientArea, pkg);
        }

        intent.putExtra("area", clientArea.name());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(intent);
        } else {
            startService(intent);
        }
    }
}
