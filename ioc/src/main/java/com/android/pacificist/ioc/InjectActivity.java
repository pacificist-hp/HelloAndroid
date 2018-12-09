package com.android.pacificist.ioc;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

public class InjectActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            InjectUtil.injectContent(this);
            InjectUtil.injectViews(this);
            InjectUtil.injectEvents(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
