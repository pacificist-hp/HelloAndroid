package com.android.pacificist.helloandroid;

import android.os.RemoteException;
import android.util.Log;

/**
 * Created by pacificist on 2018/7/24.
 */
public class Computer extends IComputer.Stub {

    private static final String TAG = "BinderPool/IComputer";

    @Override
    public int add(int a, int b) throws RemoteException {
        Log.d(TAG, "add in IComputer.Stub");
        return a + b;
    }
}
