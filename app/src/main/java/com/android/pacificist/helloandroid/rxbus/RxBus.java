package com.android.pacificist.helloandroid.rxbus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by pacificist on 2018/10/19.
 */
public class RxBus {

    private final Subject<Object> mBus;

    private RxBus() {
        mBus = PublishSubject.create().toSerialized();
    }

    public static RxBus get() {
        return RxBusInstance.instance;
    }

    public void post(Object obj) {
        mBus.onNext(obj);
    }

    public <T> Observable<T> toObservable(Class<T> tClass) {
        return mBus.ofType(tClass);
    }

    public Observable<Object> toObservable() {
        return mBus;
    }

    public boolean hasObservers() {
        return mBus.hasObservers();
    }

    private static class RxBusInstance {
        private static final RxBus instance = new RxBus();
    }
}
