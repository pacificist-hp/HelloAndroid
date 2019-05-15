package com.android.pacificist.helloandroid;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.EditText;

import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

public class MainActivity extends AppCompatActivity {

    private EditText mInputView;
    private WebView mResultView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInputView = findViewById(R.id.input_view);
        mResultView = findViewById(R.id.result_view);

        mResultView.getSettings().setJavaScriptEnabled(true);
        mResultView.getSettings().setAllowFileAccessFromFileURLs(true);
        mResultView.getSettings().setAllowUniversalAccessFromFileURLs(true);

        findViewById(R.id.search_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String input = mInputView.getText().toString();
                if (!TextUtils.isEmpty(input)) {
                    new SearchTask().execute(input);
                }
            }
        });
    }

    private class SearchTask extends AsyncTask<String, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE))
                    .hideSoftInputFromWindow(mInputView.getWindowToken(), 0);
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            mResultView.loadDataWithBaseURL("https://m.baidu.com",
                    result, "text/html" , "utf-8", null);
            mInputView.setText("");
        }

        @Override
        protected String doInBackground(String... params) {
            // The connection URL
            final String url = "https://m.baidu.com/s?word={query}";

            // Create a new RestTemplate instance
            RestTemplate restTemplate = new RestTemplate();

            // Add the String message converter
            restTemplate.getMessageConverters().add(new StringHttpMessageConverter());

            // Make the HTTP GET request, marshaling the response to a String
            return restTemplate.getForObject(url, String.class, params[0]);
        }
    }
}
