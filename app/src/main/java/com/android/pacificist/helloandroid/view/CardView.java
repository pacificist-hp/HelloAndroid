package com.android.pacificist.helloandroid.view;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;

import com.android.pacificist.helloandroid.R;

/**
 * Created by pacificist on 2018/10/12.
 */
public class CardView extends View {

    private final static int[] ID_CARDS = new int[] {R.mipmap.card_3,
            R.mipmap.card_6, R.mipmap.card_10, R.mipmap.card_a};

    private Bitmap[] mCards = new Bitmap[ID_CARDS.length];

    public CardView(Context context) {
        this(context, null);
    }

    public CardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        for (int i = 0; i < ID_CARDS.length; i++) {
            mCards[i] = BitmapFactory.decodeResource(getResources(),
                    ID_CARDS[i]);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        for (int i = 0; i < mCards.length; i++) {
            canvas.save();
            if (i < mCards.length - 1) {
                canvas.clipRect(0, 0, 100, mCards[i].getHeight());
            }
            canvas.drawBitmap(mCards[i], 0, 0, null);
            canvas.restore();

            canvas.translate(100, 0);
        }
        canvas.restore();
    }
}
