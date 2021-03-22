package com.android.pacificist.appwindow;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.DragEvent;
import android.view.View;

import java.lang.ref.WeakReference;

import static com.android.pacificist.appwindow.ClientConstant.TAG;

class ClientDragManager {

    private ClientWindow mClientWindow;

    private boolean mIsShadowEmpty = false;

    ClientDragManager(ClientWindow clientWindow) {
        mClientWindow = clientWindow;

        mClientWindow.getView().setOnRootLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                View.DragShadowBuilder builder;
                if (mIsShadowEmpty) {
                    builder = new EmptyShadowBuilder(v);
                } else {
                    builder = new View.DragShadowBuilder(v);
                }
                ViewCompat.startDragAndDrop(v, null, builder, v, 0);
                return true;
            }
        });

        mClientWindow.getView().setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent event) {
                if (mClientWindow.getView() == event.getLocalState()) {
                    if (event.getAction() == DragEvent.ACTION_DRAG_STARTED) {
                        mClientWindow.getView().setIntercept(true);
                        mClientWindow.getView().setVisibility(View.INVISIBLE);
                    } else if (event.getAction() == DragEvent.ACTION_DRAG_ENDED) {
                        mClientWindow.getView().setVisibility(View.VISIBLE);
                        mClientWindow.getView().setIntercept(false);
                        mClientWindow.move(event.getX(), event.getY());
                    }
                } else {
                    Log.w(TAG, "onDrag other view: " + event.getAction() + "," + event.getX() + "," + event.getY());
                }

                return true;
            }
        });
    }

    void setShadowEmpty(boolean isEmpty) {
        mIsShadowEmpty = isEmpty;
    }

    private static class EmptyShadowBuilder extends View.DragShadowBuilder {
        private final WeakReference<View> viewRef;

        public EmptyShadowBuilder(View view) {
            super();
            viewRef = new WeakReference<>(view);
        }

        @Override
        public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
            final View view = viewRef.get();
            if (view != null) {
                outShadowSize.x = view.getWidth();
                outShadowSize.y = view.getHeight();
                outShadowTouchPoint.x = outShadowSize.x / 2;
                outShadowTouchPoint.y = outShadowSize.y / 2;
            }
        }

        @Override
        public void onDrawShadow(Canvas canvas) {
            canvas.drawColor(Color.WHITE);
        }
    }
}
