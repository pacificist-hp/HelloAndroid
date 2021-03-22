package com.android.pacificist.appwindow;

import android.view.View;

public interface IClientCallback {
    int getLayout();

    void onViewCreated(View view);
    void onViewSizeChanged(int width, int height);
}
