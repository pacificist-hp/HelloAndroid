package com.android.pacificist.helloandroid.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

public class TextResourceReader {
    public static String readRawResource(Context context, int resId) {
        StringBuilder body = new StringBuilder();
        try {
            InputStream inputStream = context.getResources().openRawResource(resId);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

            String nextLine;
            while ((nextLine = bufferedReader.readLine()) != null) {
                body.append(nextLine).append('\n');
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return body.toString();
    }
}
