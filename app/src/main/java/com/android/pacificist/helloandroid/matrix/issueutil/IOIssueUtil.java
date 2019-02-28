package com.android.pacificist.helloandroid.matrix.issueutil;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class IOIssueUtil {

    private static final String TAG = "Matrix";
    private static final String FILE = "/sdcard/io_issue.txt";

    private static void writeFile(int num) throws IOException {
        File f = new File(FILE);
        if (f.exists()) {
            f.delete();
        }
        byte[] data = new byte[1024];
        for (int i = 0; i < data.length; i++) {
            data[i] = 'a';
        }
        FileOutputStream fos = new FileOutputStream(f);
        for (int i = 0; i < num; i++) {
            fos.write(data);
        }
        fos.flush();
        fos.close();
    }

    public static void writeLongFile() throws IOException {
        Log.i(TAG, "IOIssueUtil.writeLongFile");
        writeFile(8 * 1000000);
    }

    public static void leakFileDescriptor() throws IOException {
        Log.i(TAG, "IOIssueUtil.leakFileDescriptor");
        writeFile(1);
        File f = new File(FILE);
        byte[] buf = new byte[1024];
        FileInputStream fis = new FileInputStream(f);
        while (fis.read(buf) != -1) {
        }

        //need to trigger gc to detect leak
        new Thread(new Runnable() {
            @Override
            public void run() {
                Runtime.getRuntime().gc();
                Runtime.getRuntime().runFinalization();
                Runtime.getRuntime().gc();
            }
        }).start();
    }

    public static void smallBuffer() throws IOException {
        Log.i(TAG, "IOIssueUtil.smallBuffer");
        writeFile(3);
        File f = new File(FILE);
        byte[] buf = new byte[8];
        FileInputStream fis = new FileInputStream(f);
        while (fis.read(buf) != -1) {
        }
        fis.close();
    }
}
