package com.android.pacificist.helloandroid;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by pacificist on 2020/12/19.
 */
public class BookManagerService extends Service {

    private CopyOnWriteArrayList<Book> bookList = new CopyOnWriteArrayList<>();

    // 由于服务端 Binder 方法运行在 Binder 线程池中，服务功能可以采用同步的方式实现
    private Binder binder = new IBookManager.Stub() {
        @Override
        public List<Book> getBookList() throws RemoteException {
            Log.d("BOOK_SERVICE", "Server: getBookList");
            return bookList;
        }

        @Override
        public void addBook(Book book) throws RemoteException {
            Log.d("BOOK_SERVICE", "Server: addBook");
            bookList.add(book);
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("BOOK_SERVICE", "Server: onCreate");
        bookList.add(new Book(0, "HelloAndroid"));
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("BOOK_SERVICE", "Server: onBind");
        return binder;
    }
}
