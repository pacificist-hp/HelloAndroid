package com.android.pacificist.helloandroid;

import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.pacificist.ioc.InjectActivity;
import com.android.pacificist.ioc.inject.LayoutInject;
import com.android.pacificist.ioc.inject.OnClickInject;
import com.android.pacificist.ioc.inject.ViewInject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

@LayoutInject(value = R.layout.activity_main)
public class MainActivity extends InjectActivity {

    @ViewInject(value = R.id.tx_time)
    private TextView timeView;

    @OnClickInject({R.id.tx_time, R.id.btn_show_time})
    public void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.tx_time:
                Toast.makeText(this, getString(R.string.now)
                        + ": " + timeView.getText(), Toast.LENGTH_SHORT).show();
                break;
            case R.id.btn_show_time:
                timeView.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
                        Locale.getDefault()).format(new Date()));
                break;
            default:
                break;
        }
    }
}
