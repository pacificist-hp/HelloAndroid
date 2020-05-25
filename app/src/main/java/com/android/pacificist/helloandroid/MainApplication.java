package com.android.pacificist.helloandroid;

import android.app.Application;
import android.util.Log;

import com.android.pacificist.bridge.BridgeClient;
import com.android.pacificist.bridge.Value;

/**
 * Created by pacificist on 2020/4/25.
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BridgeClient.instance().registerFunctionCallable("log", 2, new BridgeClient.Callable() {
            @Override
            public Value call(Value[] params) {
                Log.d("bridge_app", params[0].toString() + ": " + params[1].toString());
                return new Value("app callable success");
            }
        });
    }
}
