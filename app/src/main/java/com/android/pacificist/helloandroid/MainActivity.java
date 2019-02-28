package com.android.pacificist.helloandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.android.pacificist.helloandroid.matrix.issueutil.IOIssueUtil;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "Matrix";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.trace_image_view).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.trace_image_view:
                try {
                    switch (new Random().nextInt(3)) {
                        case 0:
                            IOIssueUtil.writeLongFile();
                            break;
                        case 1:
                            IOIssueUtil.leakFileDescriptor();
                            break;
                        case 2:
                            IOIssueUtil.smallBuffer();
                            break;
                        default:
                            break;
                    }
                    Log.i(TAG, "click end");
                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            default:
                break;
        }
    }
}
