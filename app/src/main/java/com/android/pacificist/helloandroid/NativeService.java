package com.android.pacificist.helloandroid;

import android.util.Log;

/**
 * Created by pacificist on 2018/7/27.
 */
public class NativeService {

    private static final String TAG = "NativeService";

    static {
        try {
            System.loadLibrary("native_service");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void start(String params) {
        int ret =_start(params);
        Log.i(TAG, "Java: NativeService.start: " + ret);
    }

    public static void print(String message) {
        int ret = _print(message);
        Log.i(TAG, "Java: NativeService.print: " + ret);
    }

    private static native int _start(String params);
    private static native int _print(String message);
}
