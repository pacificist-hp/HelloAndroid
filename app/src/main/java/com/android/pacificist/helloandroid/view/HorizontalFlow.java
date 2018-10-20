package com.android.pacificist.helloandroid.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.pacificist.helloandroid.R;

import java.util.List;

/**
 * Created by pacificist on 2018/10/16.
 */
public class HorizontalFlow extends HorizontalScrollView {

    private LinearLayout mContentView;
    private View mSelectedView = null;

    private OnItemClickListener mItemClickListener = null;

    public HorizontalFlow(Context context) {
        super(context);
        init();
    }

    public HorizontalFlow(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HorizontalFlow(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setHorizontalScrollBarEnabled(false);
        mContentView = new LinearLayout(getContext());
        mContentView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mContentView.setOrientation(LinearLayout.HORIZONTAL);
        addView(mContentView);
    }

    public void bind(List<? extends IHorizontalItem> items) {
        mContentView.removeAllViews();
        if (items == null || items.size() < 1) {
            return;
        }
        for (int i = 0; i < items.size(); i++) {
            LinearLayout view = (LinearLayout) LayoutInflater.from(getContext())
                    .inflate(R.layout.layout_horizontal_item, null);

            ViewGroup.LayoutParams params = view.getLayoutParams();
            if (params == null) {
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, dp2px(30));
            }
            if (i == 0) {
                ((MarginLayoutParams) params).setMargins(dp2px(10), 0, dp2px(10), 0);
            } else {
                ((MarginLayoutParams) params).setMargins(0, 0, dp2px(10), 0);
            }
            view.setLayoutParams(params);

            view.setTag(items.get(i));
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (!view.isSelected()) {
                        IHorizontalItem item = (IHorizontalItem) view.getTag();
                        setSelectedItem(item.getKey());
                        if (mItemClickListener != null) {
                            mItemClickListener.onItemClick(item);
                        }
                    }
                }
            });

            ((TextView) view.findViewById(R.id.horizontal_item_content)).setText(items.get(i).getContent());

            mContentView.addView(view);
        }
    }

    public void setSelectedItem(String key) {
        for (int i = 0; i < mContentView.getChildCount(); i++) {
            View view = mContentView.getChildAt(i);
            if (TextUtils.equals(key, ((IHorizontalItem) view.getTag()).getKey())) {
                view.setSelected(true);
                mSelectedView = view;
                showFullItemView(view);
            } else {
                view.setSelected(false);
            }
        }
    }

    private void showFullItemView(View view) {
        if (view != null) {
            int[] locationOfView = new int[2];
            view.getLocationOnScreen(locationOfView);

            int[] locationOfList = new int[2];
            getLocationOnScreen(locationOfList);

            if (locationOfView[0] < locationOfList[0]) {
                smoothScrollBy(locationOfView[0] - locationOfList[0], 0);
            } else if (locationOfView[0] + view.getWidth() > locationOfList[0] + getWidth()) {
                smoothScrollBy(locationOfView[0] + view.getWidth() - locationOfList[0] - getWidth(), 0);
            }
        }
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (mSelectedView != null) {
            showFullItemView(mSelectedView);
        }
    }

    private int dp2px(float dp) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    public interface OnItemClickListener {
        void onItemClick(IHorizontalItem item);
    }

    public interface IHorizontalItem {
        String getKey();

        String getContent();
    }
}
