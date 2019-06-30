package com.android.pacificist.helloandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.android.pacificist.helloandroid.business.Contract;
import com.android.pacificist.helloandroid.business.Presenter;

public class MainActivity extends AppCompatActivity implements Contract.View {

    private TextView mDataView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDataView = findViewById(R.id.data_content);

        new Presenter(this).show(false, true);
    }

    @Override
    public void onDataLoaded(String data) {
        mDataView.setText(data);
    }

    @Override
    public void onDataLoadFailed(Exception e) {
        mDataView.setText(R.string.app_name);
    }
}
