package com.android.pacificist.helloandroid;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private IBookManager mBookManager = null;

    private IBinder.DeathRecipient mDeathRecipient = new IBinder.DeathRecipient() {
        @Override
        public void binderDied() {
            if (mBookManager == null) {
                return;
            }

            mBookManager.asBinder().unlinkToDeath(this, 0);
            mBookManager = null;
        }
    };

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBookManager = IBookManager.Stub.asInterface(service);
            try {
                Log.d("BOOK_SERVICE", "Client: onServiceConnected");

                service.linkToDeath(mDeathRecipient, 0);

                // 客户端发起远程请求时，当前线程会被挂起直至服务端进程返回数据
                List<Book> books = mBookManager.getBookList();

                Log.d("BOOK_SERVICE", "Client: " + books.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBookManager = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, BookManagerService.class);
        Log.d("BOOK_SERVICE", "Client: bindService");
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}
