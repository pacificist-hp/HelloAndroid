package com.client.surface;

import android.view.View;

import com.android.pacificist.appwindow.ClientBaseService;

public class WindowService extends ClientBaseService {
    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void onViewCreated(View view) {

    }

    @Override
    public void onViewSizeChanged(int width, int height) {

    }
}
