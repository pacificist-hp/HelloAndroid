package com.android.pacificist.helloandroid;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.pacificist.helloandroid.cardview.CardViewItem;
import com.android.pacificist.helloandroid.circleprogressbar.CircleProgressBarItem;
import com.android.pacificist.helloandroid.gifview.GifViewItem;
import com.android.pacificist.helloandroid.rxbus.RxBus;
import com.android.pacificist.helloandroid.rxbus.RxEvent;
import com.android.pacificist.helloandroid.scratchcard.ScratchCardItem;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class CustomViewActivity extends AppCompatActivity {

    private RecyclerView mCustomViews;
    private CustomViewLayoutManager mLayoutManager;
    private CustomViewAdapter mAdapter;

    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCustomViews = findViewById(R.id.id_custom_views);
        mLayoutManager = new CustomViewLayoutManager(this);

        mCompositeDisposable = new CompositeDisposable();

        mCustomViews.setLayoutManager(mLayoutManager);
        mAdapter = new CustomViewAdapter<CustomViewItem>(null);
        mCustomViews.setAdapter(mAdapter);

        registerEvent();
        asyncLoadCustomViews();
    }

    private void registerEvent() {
        mCompositeDisposable.add(RxBus.get().toObservable().map(new Function<Object, RxEvent>() {
            @Override
            public RxEvent apply(Object o) {
                return (RxEvent) o;
            }
        }).subscribe(new Consumer<RxEvent>() {
            @Override
            public void accept(RxEvent rxEvent) {
                handleEvent(rxEvent);
            }
        }));
    }

    private void handleEvent(RxEvent event) {
        if (event == null) {
            return;
        }
        switch (event.key) {
            case RxEvent.KEY_ENABLE_SCROLL:
                mLayoutManager.setScrollable(true);
                break;
            case RxEvent.KEY_DISABLE_SCROLL:
                mLayoutManager.setScrollable(false);
                break;
            default:
                break;
        }
    }

    private void asyncLoadCustomViews() {
        mCompositeDisposable.add(Observable.create(new ObservableOnSubscribe<List<CustomViewItem>>() {
            @Override
            public void subscribe(ObservableEmitter<List<CustomViewItem>> emitter) {
                List<CustomViewItem> items = new ArrayList<>();
                items.add(new ScratchCardItem());
                items.add(new CircleProgressBarItem());
                items.add(new GifViewItem());
                items.add(new CardViewItem());
                emitter.onNext(items);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<List<CustomViewItem>>() {
                    @Override
                    public void accept(List<CustomViewItem> customViewItems) {
                        mAdapter.addItems(customViewItems);
                    }
                }));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mCompositeDisposable.dispose();
    }

    private class CustomViewLayoutManager extends LinearLayoutManager {

        private boolean isScrollable = true;

        public CustomViewLayoutManager(Context context) {
            super(context);
        }

        public void setScrollable(boolean scrollable) {
            isScrollable = scrollable;
        }

        @Override
        public boolean canScrollVertically() {
            return isScrollable && super.canScrollVertically();
        }
    }
}
