package com.android.pacificist.helloandroid;

import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/7/7.
 */

public class RxJavaUtil {
    private static final String TAG = "rxjava";

    public static void work() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.d(TAG,"Observable.call");
                subscriber.onNext("Hello");
                subscriber.onNext("Rxjava");
                subscriber.onCompleted();
                Log.d(TAG,"Observable.call end");
            }
        }).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, s);
                    }
                });
    }
}
