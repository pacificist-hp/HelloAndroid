package com.android.pacificist.helloandroid;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import java.util.List;

/**
 * Created by pacificist on 2020/12/19.
 */
// 所有可以在 Binder 中传输的接口都需要继承 IInterface
public interface IBookManager extends IInterface {

    // 服务接口
    List<Book> getBookList() throws android.os.RemoteException;
    void addBook(Book book) throws android.os.RemoteException;

    // Binder 服务实体类
    public static abstract class Stub extends Binder implements IBookManager {

        // Binder 的唯一标识，一般用当前 Binder 的类名表示
        private static final java.lang.String DESCRIPTOR = "com.android.pacificist.helloandroid.IBookManager";

        // 服务接口 ID
        static final int TRANSACTION_getBookList = (IBinder.FIRST_CALL_TRANSACTION);
        static final int TRANSACTION_addBook = (IBinder.FIRST_CALL_TRANSACTION + 1);

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an IBookManager interface,
         * generating a proxy if needed.
         */
        public static IBookManager asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }

            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin instanceof IBookManager) {
                // 如果客户端和服务端位于同一进程，返回服务端的 Stub 对象本身
                return ((IBookManager) iin);
            }

            // 如果客户端和服务端不在同一进程，返回系统封装的 Proxy 对象
            return new IBookManager.Stub.Proxy(obj);
        }

        // 返回当前 Binder 对象
        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        /**
         * 运行在服务端 Binder 线程池中，客户端发起的跨进程请求会通过 Binder 驱动封装，并进入这里
         *
         * @param code 客户端请求的服务 code
         * @param data 客户端传给服务的参数
         * @param reply 服务执行完毕后，向 reply 写入返回值
         * @param flags
         * @return 请求成功或失败
         */
        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            String descriptor = DESCRIPTOR;
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(descriptor);
                    return true;
                }
                case TRANSACTION_getBookList: {
                    data.enforceInterface(descriptor);
                    List<Book> _result = this.getBookList();
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case TRANSACTION_addBook: {
                    data.enforceInterface(descriptor);
                    Book _arg0;
                    if ((0 != data.readInt())) {
                        _arg0 = Book.CREATOR.createFromParcel(data);
                    } else {
                        _arg0 = null;
                    }
                    this.addBook(_arg0);
                    reply.writeNoException();
                    return true;
                }
                default: {
                    return super.onTransact(code, data, reply, flags);
                }
            }
        }

        // Binder 服务代理类，封装对服务实体对象的远程调用(IBinder.transact)
        private static class Proxy implements IBookManager {

            // Binder 实体对象
            private IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public IBinder asBinder() {
                return mRemote;
            }

            @Override
            public List<Book> getBookList() throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                List<Book> _result;
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    mRemote.transact(Stub.TRANSACTION_getBookList, _data, _reply, 0);
                    _reply.readException();
                    _result = _reply.createTypedArrayList(Book.CREATOR);
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
                return _result;
            }

            @Override
            public void addBook(Book book) throws RemoteException {
                Parcel _data = Parcel.obtain();
                Parcel _reply = Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    if ((book != null)) {
                        _data.writeInt(1);
                        book.writeToParcel(_data, 0);
                    } else {
                        _data.writeInt(0);
                    }
                    mRemote.transact(Stub.TRANSACTION_addBook, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }
    }
}
