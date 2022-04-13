package com.android.pacificist.helloandroid.viewitem;

import android.graphics.Color;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.android.pacificist.helloandroid.floor.CustomViewItem;
import com.android.pacificist.helloandroid.R;
import com.android.pacificist.helloandroid.view.BarrageView;

/**
 * Created by pacificist on 2018/10/12.
 */
public class CardViewItem extends CustomViewItem<CardViewItem.CardViewHolder> {

    private final static int[] TEXT = new int[] {
            R.string.apply_for_landlord,
            R.string.rob_landlord,
            R.string.double_three,
            R.string.have_bigger_card,
            R.string.bomb,
            R.string.have_no_bigger_card
    };

    private final static int MSG_ADD_BARRAGE = 1;

    private final Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_ADD_BARRAGE:
                    BarrageView view = (BarrageView) msg.obj;
                    view.addBarrageItemView(view.getResources().getString(TEXT[msg.arg1]), 14, Color.BLUE);
                    Message message = new Message();
                    message.what = MSG_ADD_BARRAGE;
                    message.arg1 = (msg.arg1 + 1) % TEXT.length;
                    message.obj = view;
                    mHandler.sendMessageDelayed(message, 1500);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public int getLayoutRes() {
        return R.layout.layout_card_view;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(getLayoutRes(), parent, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new CardViewHolder(view);
    }

    @Override
    public void bindViewHolder(CardViewHolder holder, int position) {
        Message msg = new Message();
        msg.what = MSG_ADD_BARRAGE;
        msg.arg1 = 0;
        msg.obj = holder.barrageView;
        mHandler.sendMessage(msg);
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder {
        private BarrageView barrageView;

        public CardViewHolder(View itemView) {
            super(itemView);
            barrageView = itemView.findViewById(R.id.barrage_view);
        }
    }
}
