package com.android.pacificist.helloandroid;

import android.content.DialogInterface;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private static final int[] TEXT_SIZE = new int[]{20, 24, 28, 32};
    private static final Random sRandom = new Random();

    private FlowLayout mFlowView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFlowView = findViewById(R.id.flow_layout);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_settings) {
            final EditText inputServer = new EditText(this);
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle(R.string.title_new_impression)
                    .setView(inputServer)
                    .setNegativeButton(R.string.cancel, null)
                    .setPositiveButton(R.string.ok,
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    addImpression(inputServer.getText().toString());
                                }
                            }).show();
        }
        return true;
    }

    private void addImpression(String impression) {
        if (TextUtils.isEmpty(impression))
            return;

        TextView tv = new TextView(this);
        ViewGroup.MarginLayoutParams lp = new ViewGroup.MarginLayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(8, 8, 8, 8);
        tv.setLayoutParams(lp);
        tv.setText(impression);
        tv.setTextColor(Color.WHITE);
        tv.setTextSize(TEXT_SIZE[sRandom.nextInt(TEXT_SIZE.length)]);
        tv.setBackgroundResource(R.drawable.bg_flag);

        mFlowView.addView(tv);
    }
}
