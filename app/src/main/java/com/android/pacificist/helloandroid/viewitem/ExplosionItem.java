package com.android.pacificist.helloandroid.viewitem;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.android.pacificist.helloandroid.R;
import com.android.pacificist.helloandroid.floor.CustomViewItem;
import com.android.pacificist.helloandroid.view.ExplosionField;
import com.android.pacificist.helloandroid.view.HeartView;

/**
 * Created by pacificist on 2018/11/4.
 */
public class ExplosionItem extends CustomViewItem<ExplosionItem.ExplosionHolder> {

    @Override
    public int getLayoutRes() {
        return R.layout.layout_explosion_field;
    }

    @Override
    public RecyclerView.ViewHolder createViewHolder(LayoutInflater inflater, ViewGroup parent) {
        View view = inflater.inflate(getLayoutRes(), parent, false);
        view.setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ExplosionHolder(view);
    }

    @Override
    public void bindViewHolder(final ExplosionHolder holder, int position) {
        holder.calculator.setOnClickListener(holder.EXPLOSION_LISTENER);
        holder.email.setOnClickListener(holder.EXPLOSION_LISTENER);
        holder.text.setOnClickListener(holder.EXPLOSION_LISTENER);
        holder.addHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.heart.addHeart();
            }
        });
    }

    public static class ExplosionHolder extends RecyclerView.ViewHolder {
        private View calculator, email, text;
        private ExplosionField explosionField;
        private View addHeart;
        private HeartView heart;

        public ExplosionHolder(View itemView) {
            super(itemView);
            calculator = itemView.findViewById(R.id.explosion_calculator);
            email = itemView.findViewById(R.id.explosion_email);
            text = itemView.findViewById(R.id.explosion_text);
            explosionField = itemView.findViewById(R.id.explosion_field);
            addHeart = itemView.findViewById(R.id.add_heart);
            heart = itemView.findViewById(R.id.heart_view);
        }

        public final View.OnClickListener EXPLOSION_LISTENER = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                explosionField.explode(view);
            }
        };
    }
}
