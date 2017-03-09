package com.javathlon;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewActivity extends Activity {

    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        webView = (WebView) findViewById(R.id.webView);

        Bundle b = getIntent().getExtras();

        if (b != null && b.getString("url") != null) {

            webView.setWebViewClient(new WebViewController());

            webView.loadUrl(b.getString("url"));
            webView.requestFocus();
        }


    }

    class WebViewController extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }
}
