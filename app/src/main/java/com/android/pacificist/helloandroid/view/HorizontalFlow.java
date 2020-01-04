package com.android.pacificist.helloandroid.view;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.pacificist.helloandroid.R;
import com.android.pacificist.helloandroid.util.Util;

import java.util.List;

/**
 * Created by pacificist on 2018/10/16.
 */
public class HorizontalFlow extends HorizontalScrollView {

    private UrlLinearLayout mContentView;
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
        mContentView = new UrlLinearLayout(getContext());
        mContentView.setLayoutParams(new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mContentView.setOrientation(LinearLayout.HORIZONTAL);
        mContentView.setBackgroundUrl("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1578126263998&di=f2bfc70d7ccc8eed6d9d3778ac928019&imgtype=0&src=http%3A%2F%2Fhbimg.b0.upaiyun.com%2F3e237cc558e41d6496eb26d9b8dacca6730984f1b15f-JuZZbj_fw658");
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
                params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                        Util.dp2px(30, getResources()));
            }
            if (i == 0) {
                ((MarginLayoutParams) params).setMargins(Util.dp2px(10, getResources()),
                        0, Util.dp2px(10, getResources()), 0);
            } else {
                ((MarginLayoutParams) params).setMargins(0,
                        0, Util.dp2px(10, getResources()), 0);
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

    public interface OnItemClickListener {
        void onItemClick(IHorizontalItem item);
    }

    public interface IHorizontalItem {
        String getKey();

        String getContent();
    }
}
