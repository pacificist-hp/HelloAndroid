package com.android.pacificist.helloandroid;

import android.text.TextUtils;
import android.util.Log;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.functions.Func2;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Created by Administrator on 2018/7/7.
 */

public class RxJavaUtil {
    private static final String TAG = "RxJavaUtil";

    public static void work() {
        postDelayedInMainThread(new Action0() {
            @Override
            public void call() {
                Log.d(TAG, "work");
                add(loadDataFromCacheAndNet("config")
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<String>() {
                            @Override
                            public void call(String s) {
                                Log.d(TAG, "load config" + ": " + s);
                            }
                        }));
                reduceDemo();
            }
        }, 1000, TimeUnit.MILLISECONDS);
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

    public static void postDelayedInMainThread(Action0 action0, long delayTime, TimeUnit unit) {
        Log.d(TAG, "post delay message");
        // The Subscription object which schedule method returns need not unsubscribe.
        AndroidSchedulers.mainThread().createWorker().schedule(action0, delayTime, unit);
    }

    private static Map<String, String> sCache = new HashMap<>();

    /**
     * load data from cache, and refresh cache from net at the same time.
     */
    public static Observable<String> loadDataFromCacheAndNet(String key) {
        return Observable.just(key)
                .subscribeOn(Schedulers.io())
                .flatMap(new Func1<String, Observable<String>>() {
                    @Override
                    public Observable<String> call(final String s) {
                        String cacheValue = sCache.get(s);
                        Log.d(TAG, "load " + s + "from cache: " + cacheValue);

                        Observable<String> net = Observable.create(new Observable.OnSubscribe<String>() {
                            @Override
                            public void call(Subscriber<? super String> subscriber) {
                                String netValue = s + "-" + System.currentTimeMillis();
                                Log.d(TAG, "load " + s + "from net: " + netValue);
                                sCache.put(s, netValue);
                                subscriber.onNext(netValue);
                                subscriber.onCompleted();
                            }
                        });

                        if (TextUtils.isEmpty(cacheValue)) {
                            return net.subscribeOn(Schedulers.io());
                        }

                        return Observable.concat(Observable.just(cacheValue),
                                net.flatMap(new Func1<String, Observable<String>>() {
                                    @Override
                                    public Observable<String> call(String s) {
                                        return Observable.empty();
                                    }
                                })).subscribeOn(Schedulers.io());
                    }
                });
    }

    private static CompositeSubscription sCompositeSubscription = new CompositeSubscription();

    private static void add(Subscription subscription) {
        sCompositeSubscription.add(subscription);
    }

    public static void destroy() {
        sCompositeSubscription.clear();
    }
}
