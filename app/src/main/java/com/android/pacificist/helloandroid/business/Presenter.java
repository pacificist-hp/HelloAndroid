package com.android.pacificist.helloandroid.business;

import android.text.TextUtils;

import com.android.pacificist.helloandroid.EasyCache;
import com.android.pacificist.helloandroid.MainApplication;

/**
 * Created by pacificist on 2019/6/30.
 */
public class Presenter implements Contract.Presenter {

    private int mDataHash = EasyCache.DEF_HASH;

    private Contract.View mView;

    public Presenter(Contract.View view) {
        mView = view;
    }

    @Override
    public void show(boolean forceRemote, boolean forceUpdate) {
        String data = new Repository().loadData(forceRemote);
        if (TextUtils.isEmpty(data)) {
            if (forceUpdate && mView != null) {
                mView.onDataLoadFailed(new Exception());
            }
        } else {
            int hash = EasyCache.getCache(MainApplication.app).readCacheHash("debug");
            if (mDataHash != hash || forceUpdate) {
                mDataHash = hash;
                if (mView != null) {
                    mView.onDataLoaded(data);
                }
            }
        }
    }
}
