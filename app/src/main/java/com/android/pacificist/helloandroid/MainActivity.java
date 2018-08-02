package com.android.pacificist.helloandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.android.pacificist.helloandroid.annotation.AspectAnnotation;

public class MainActivity extends AppCompatActivity {

    private Button mOKView;
    private Button mCancelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        track("onCreate enter");

        setContentView(R.layout.activity_main);
        mOKView = findViewById(R.id.id_btn_ok);
        mCancelView = findViewById(R.id.id_btn_cancel);
        mOKView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Aspect", "click OK");
            }
        });
        mCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("Aspect", "click cancel");
            }
        });

        track("onCreate quit");
    }

    @AspectAnnotation
    private void track(String str) {

    }
}
