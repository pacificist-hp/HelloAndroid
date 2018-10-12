package com.android.pacificist.helloandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.android.pacificist.helloandroid.cardview.CardViewItem;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mCustomViews;
    private CustomViewAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mCustomViews = findViewById(R.id.id_custom_views);
        mCustomViews.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new CustomViewAdapter<>(getCustomViewItems());
        mCustomViews.setAdapter(mAdapter);
    }

    private List<CustomViewItem> getCustomViewItems() {
        List<CustomViewItem> items = new ArrayList<>();
        items.add(new CardViewItem());
        return items;
    }
}
