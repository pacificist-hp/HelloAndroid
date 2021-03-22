package com.android.pacificist.appwindow;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;

import static com.android.pacificist.appwindow.ClientConstant.TAG;

class ClientWindowManager {

    private WindowManager mWm;

    ClientWindowManager(Context ctx) {
        mWm = (WindowManager) ctx.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
    }

     void addWindow(ClientWindow clientWindow) {
        View view = clientWindow.getView();
        WindowManager.LayoutParams params = clientWindow.getWindowParams();

        view.setLayoutParams(params);
        Log.d(TAG, "addView: " + view);
        try {
            mWm.addView(view, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        clientWindow.onSizeChanged(params.width, params.height);
    }

     void updateWindow(ClientWindow clientWindow) {
        View view = clientWindow.getView();
        WindowManager.LayoutParams current = (WindowManager.LayoutParams) view.getLayoutParams();

        WindowManager.LayoutParams params = clientWindow.getWindowParams();

        boolean isSizeChanged = current.width != params.width || current.height != params.height;

        Log.d(TAG, "updateViewLayout: " + view);
        try {
            mWm.updateViewLayout(view, params);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (isSizeChanged) {
            clientWindow.onSizeChanged(params.width, params.height);
        }
    }

    void removeWindow(ClientWindow clientWindow) {
        View view = clientWindow.getView();
        Log.d(TAG, "removeView: " + view);

        try {
            mWm.removeView(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
