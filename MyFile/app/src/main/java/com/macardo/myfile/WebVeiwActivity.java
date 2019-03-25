package com.macardo.myfile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebVeiwActivity extends AppCompatActivity {
    private WebView mWebView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_veiw);
        initViews();
    }

    private void initViews() {
        mWebView = findViewById(R.id.webview);
        mWebView.loadUrl("file:///android_asset/macardo.txt");
    }

}
