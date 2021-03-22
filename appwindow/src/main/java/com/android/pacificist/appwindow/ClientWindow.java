package com.android.pacificist.appwindow;

import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class ClientWindow {

    private ClientRootView mRootView;

    private ClientArea mClientArea;
    private ClientWindowManager mClientWindowManager;
    private ClientDragManager mClientDragManager;

    private ClientManager mClientManager;

    private IClientCallback mCallback;

    public ClientWindow(View contentView, IClientCallback callback) {
        mRootView = new ClientRootView(contentView.getContext());
        mRootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT));
        mRootView.addView(contentView);

        mClientManager = ClientManager.get(contentView.getContext());

        mCallback = callback;

        initExtendViews();

        mClientWindowManager = new ClientWindowManager(mRootView.getContext());
        mClientDragManager = new ClientDragManager(this);

        mClientDragManager.setShadowEmpty(false);
    }

    private void initExtendViews() {
        View maxView = new View(mRootView.getContext());
        maxView.setBackgroundResource(R.drawable.ic_window_maximise_48px);
        FrameLayout.LayoutParams maxParams = new FrameLayout.LayoutParams(48, 48);
        maxParams.gravity = Gravity.TOP | Gravity.END;
        maxView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mClientManager.maxAppWindow();
            }
        });
        mRootView.addView(maxView, maxParams);
    }

    public ClientRootView getView() {
        return mRootView;
    }

    public WindowManager.LayoutParams getWindowParams() {
        return mClientArea.getWindowParams();
    }

    public void show(ClientArea clientArea) {
        mClientArea = clientArea;
        if (isAdded()) {
            mClientWindowManager.updateWindow(this);
        } else {
            mClientWindowManager.addWindow(this);
        }
    }

    public void dismiss() {
        mClientArea = ClientArea.NONE;
        if (isAdded()) {
            mClientWindowManager.removeWindow(this);
        }
    }

    private boolean isAdded() {
        return mRootView.getWindowToken() != null || mRootView.getParent() != null;
    }

    void move(float x, float y) {
        mClientManager.moveAppWindow(mClientArea, x, y);
    }

    void onSizeChanged(int width, int height) {
        if (mCallback != null) {
            mCallback.onViewSizeChanged(width, height);
        }
    }
}
