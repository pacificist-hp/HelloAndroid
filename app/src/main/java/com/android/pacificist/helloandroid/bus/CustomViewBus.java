package com.android.pacificist.helloandroid.bus;

import io.reactivex.Observable;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * Created by pacificist on 2018/10/19.
 */
public class CustomViewBus {

    private final Subject<Object> mBus;

    private CustomViewBus() {
        mBus = PublishSubject.create().toSerialized();
    }

    public static CustomViewBus get() {
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
        private static final CustomViewBus instance = new CustomViewBus();
    }
}
