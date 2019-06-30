package com.android.pacificist.helloandroid.business;

/**
 * Created by pacificist on 2019/6/30.
 */
public class Contract {

    public interface View {
        void onDataLoaded(String data);
        void onDataLoadFailed(Exception e);
    }

    public interface Presenter {
        void show(boolean forceRemote, boolean forceUpdate);
    }
}
