package com.android.pacificist.helloandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.android.pacificist.helloandroid.floor.CustomViewLayoutManager;
import com.android.pacificist.helloandroid.viewitem.CardViewItem;
import com.android.pacificist.helloandroid.viewitem.CircleProgressBarItem;
import com.android.pacificist.helloandroid.viewitem.ExplosionItem;
import com.android.pacificist.helloandroid.viewitem.GifViewItem;
import com.android.pacificist.helloandroid.bus.CustomViewBus;
import com.android.pacificist.helloandroid.bus.CustomViewEvent;
import com.android.pacificist.helloandroid.viewitem.RippleViewItem;
import com.android.pacificist.helloandroid.viewitem.ScratchCardItem;
import com.android.pacificist.helloandroid.floor.CustomViewAdapter;
import com.android.pacificist.helloandroid.floor.CustomViewItem;

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
    private CustomViewAdapter<CustomViewItem> mAdapter;

    private CompositeDisposable mCompositeDisposable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCustomViews = findViewById(R.id.id_custom_views);
        mLayoutManager = new CustomViewLayoutManager(this);

        mCompositeDisposable = new CompositeDisposable();

        mCustomViews.setLayoutManager(mLayoutManager);
        mAdapter = new CustomViewAdapter<>(null);
        mCustomViews.setAdapter(mAdapter);

        registerEvent();
        asyncLoadCustomViews();
    }

    private void registerEvent() {
        mCompositeDisposable.add(CustomViewBus.get().toObservable().map(new Function<Object, CustomViewEvent>() {
            @Override
            public CustomViewEvent apply(Object o) {
                return (CustomViewEvent) o;
            }
        }).subscribe(new Consumer<CustomViewEvent>() {
            @Override
            public void accept(CustomViewEvent customViewEvent) {
                handleEvent(customViewEvent);
            }
        }));
    }

    private void handleEvent(CustomViewEvent event) {
        if (event == null) {
            return;
        }
        switch (event.key) {
            case CustomViewEvent.KEY_ENABLE_SCROLL:
                mLayoutManager.setScrollable(true);
                break;
            case CustomViewEvent.KEY_DISABLE_SCROLL:
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
                items.add(new RippleViewItem());
                items.add(new ExplosionItem());
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
}
