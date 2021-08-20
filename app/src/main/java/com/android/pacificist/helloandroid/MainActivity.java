package com.android.pacificist.helloandroid;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.view.View;

import org.wso2.siddhi.android.platform.SiddhiAppController;
import org.wso2.siddhi.android.platform.SiddhiAppService;

public class MainActivity extends AppCompatActivity {

    private String app = "@app:name('foo')@source(type='android-humidity', @map(type='keyvalue'," +
            "fail.on.missing.attribute='false',@attributes(sensor='sensor',vector='humidity')))" +
            "define stream sensorInStream ( sensor string, vector float);" +
            "@sink(type='android-notification' , title='Details',multiple.notifications = 'true'," +
            " @map(type='keyvalue'))define stream outputStream (sensor string, vector float); " +
            "from sensorInStream select * insert into outputStream";

    private String appName;

    private SiddhiAppController appController;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            appController = SiddhiAppController.Stub.asInterface(service);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            appController = null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = new Intent(this, SiddhiAppService.class);
        startService(intent);
        bindService(intent,serviceConnection,BIND_AUTO_CREATE);
    }

    public void startApp(View view) throws RemoteException {
        appName = appController.startSiddhiApp(app);
    }
    public void stopApp(View view) throws RemoteException{
        appController.stopSiddhiApp(appName);
    }
}
