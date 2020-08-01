package com.android.pacificist.helloandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.android.pacificist.bridge.BridgeClient;
import com.android.pacificist.bridge.Value;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        int bridgeId = -1;
        try {
            bridgeId = BridgeClient.instance().loadCode(readAssets("log.js"));
            if (bridgeId != -1) {
                Value[] params = {new Value(9)};
                Value value = BridgeClient.instance().invoke(bridgeId, "log_func", params);
                Log.d("bridge_app", "invoke: " + value);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bridgeId != -1) {
                BridgeClient.instance().release(bridgeId);
            }
        }
    }

    private String readAssets(final String path) throws IOException {
        InputStreamReader inputStreamReader = new InputStreamReader(getResources().getAssets().open(path));
        BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

        String line;
        StringBuilder sb = new StringBuilder();

        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line).append("\n");
        }

        bufferedReader.close();

        return sb.toString();
    }
}
