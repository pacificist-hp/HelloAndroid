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

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class MainActivity extends AppCompatActivity {

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
        registerEvent();

        mCustomViews.setLayoutManager(mLayoutManager);
        mAdapter = new CustomViewAdapter<>(getCustomViewItems());
        mCustomViews.setAdapter(mAdapter);
    }

    private List<CustomViewItem> getCustomViewItems() {
        List<CustomViewItem> items = new ArrayList<>();
        items.add(new ScratchCardItem());
        items.add(new CircleProgressBarItem());
        items.add(new GifViewItem());
        items.add(new CardViewItem());
        return items;
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
