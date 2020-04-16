package com.android.pacificist.helloandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.pacificist.bridge.Bridge;
import com.android.pacificist.bridge.Evaluator;
import com.android.pacificist.bridge.Value;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Bridge.instance().registerFunction("log", 1, new Bridge.Callable() {
            @Override
            public Value call(Evaluator evaluator, Value[] params) {
                Log.d("JavaApplication", params[0].toString());
                return null;
            }
        });
    }
}
