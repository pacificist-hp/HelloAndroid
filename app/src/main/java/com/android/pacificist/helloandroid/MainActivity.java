package com.android.pacificist.helloandroid;

import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "BinderPool/MainActivity";

    private ISecurer mSecurer = null;
    private IComputer mComputer = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                doWork();
            }
        }).start();
    }

    private void doWork() {
        try {
            visitSecurer("this is a test");
            visitComputer(3, 2);
            visitSecurer("My name is LiLei");
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private void visitSecurer(String msg) throws RemoteException {
        if (mSecurer == null) {
            IBinder bSecurer = BinderPool.getInstance(this).queryBinder(BinderPool.BINDER_SECURER);
            if (bSecurer != null) {
                mSecurer = Securer.asInterface(bSecurer);
            }
        }
        if (mSecurer != null) {
            Log.d(TAG, "plain text: " + msg);
            String cipher = mSecurer.encrypt(msg);
            Log.d(TAG, "cipher text: " + cipher);
            String plain = mSecurer.decrypt(cipher);
            Log.d(TAG, "plain text: " + plain);
        }
    }

    private void visitComputer(int a, int b) throws RemoteException {
        if (mComputer == null) {
            IBinder bComputer = BinderPool.getInstance(this).queryBinder(BinderPool.BINDER_COMPUTER);
            if (bComputer != null) {
                mComputer = Computer.asInterface(bComputer);
            }
        }
        if (mComputer != null) {
            int sum = mComputer.add(a, b);
            Log.d(TAG, a + " + " + b + " = " + sum);
        }
    }
}
