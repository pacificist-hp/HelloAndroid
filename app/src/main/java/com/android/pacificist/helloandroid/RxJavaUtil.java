package com.android.pacificist.helloandroid;

import android.util.Log;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/7/7.
 */

public class RxJavaUtil {
    private static final String TAG = "rxjava";

    public static void work() {
        createDemo();
        reduceDemo();
    }

    private static void createDemo() {
        Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Log.d(TAG, "Observable.call");
                subscriber.onNext("Hello");
                subscriber.onNext("Rxjava");
                subscriber.onCompleted();
                Log.d(TAG, "Observable.call end");
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

    private static void reduceDemo() {
        Integer[] objects = {1, 235, 40, 8080};
        Observable.from(objects).reduce("begin:", new Func2<String, Integer, String>() {
            @Override
            public String call(String s, Integer integer) {
                s = s + integer.toString() + ";";
                return s;
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<String>() {
                    @Override
                    public void call(String s) {
                        Log.d(TAG, s);
                    }
                });

        Observable.from(objects).reduce(new Func2<Integer, Integer, Integer>() {
            @Override
            public Integer call(Integer integer, Integer integer2) {
                return integer.intValue() + integer2.intValue();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<Integer>() {
                    @Override
                    public void call(Integer integer) {
                        Log.d(TAG, integer.toString());
                    }
                });
    }
}
