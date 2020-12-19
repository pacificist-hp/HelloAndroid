package com.android.pacificist.helloandroid;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

import java.util.List;

/**
 * 服务接口类
 * 所有可以在 Binder 中传输的接口都需要继承 IInterface
 */
public interface IBookManager extends IInterface {

    // 定义服务接口
    List<Book> getBookList() throws android.os.RemoteException;
    void addBook(Book book) throws android.os.RemoteException;

    /**
     * Binder 服务实体类
     * 既继承了 Binder，可通过 Binder 传输，又实现了 IBookManager，封装服务接口
     */
    public static abstract class Stub extends Binder implements IBookManager {

        // Binder 的唯一标识，一般用当前 Binder 的类名表示，用于查询
        private static final java.lang.String DESCRIPTOR = "com.android.pacificist.helloandroid.IBookManager";

        // 服务接口 ID，用于标识在transact过程中client请求的到底是哪个方法
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
         *
         * @param obj Binder 驱动返回到上层的 Binder 对象
         * @return 将 Binder 对象 obj 转换为 Interface 对象返回
         */
        public static IBookManager asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }

            // 根据 DESCRIPTOR 查询本进程中是否有对应的 Binder 对象
            IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (iin instanceof IBookManager) {
                // 如果客户端和服务端位于同一进程，返回服务端的 Stub 对象本身，无需跨进程 transact
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
         * 运行在服务端 Binder 线程池中，客户端发起的跨进程请求通过 Binder 驱动封装后进入这里
         *
         * @param code 客户端请求的服务 code，标识具体请求哪个方法
         * @param data 客户端传给服务端的参数
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
                    // Stub 是抽象类，需由具体的服务实体类实现对应的服务方法
                    List<Book> _result = this.getBookList();
                    reply.writeNoException();
                    reply.writeTypedList(_result);
                    return true;
                }
                case TRANSACTION_addBook: {
                    data.enforceInterface(descriptor);
                    Book _arg0;
                    // 如果data带参数，可以在这里解析出来，传给后续调用的服务方法
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

        /**
         * Binder 服务代理类
         * 封装对服务实体对象的远程调用(IBinder.transact)
         */
        private static class Proxy implements IBookManager {

            // 保存从 Binder 驱动封装并上传的 Binder 代理对象，负责在内核跨进程通信
            private IBinder mRemote;

            // 上层 Binder 代理必须持有一个底层 Binder 代理对象的引用
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
                    // 通过 mRemote 发起远程申请，进入 mRemote 关联的服务端 Binder 的 onTransact 方法
                    // 客户端发起远程请求后，当前线程会被挂起，直至服务端返回数据
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
                    // 如果需要传参数，可以在此处往 _data 中添加
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
