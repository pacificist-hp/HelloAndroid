package com.android.pacificist.helloandroid;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.android.pacificist.helloandroid.appwindow.AppWindowService;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<String> apps = new ArrayList<>();
        apps.add("com.client.map");
        apps.add("com.client.music");
        apps.add("com.client.surface");
        startAppWindowService(apps);
    }

    private void startAppWindowService(ArrayList<String> apps) {
        Intent intent = new Intent(this, AppWindowService.class);
        intent.putExtra("type",  "appWindow");

        Bundle bundle = new Bundle();
        bundle.putStringArrayList("packages", apps);
        intent.putExtras(bundle);

        startService(intent);
    }
}
