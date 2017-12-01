package com.landsoft.demoreadxml;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebView;

import com.landsoft.demoreadxml.WebView.MyWebViewClient;

import static android.view.View.SCROLLBARS_INSIDE_OVERLAY;
import static com.landsoft.demoreadxml.Constant.ActionConstant.URL_WEB;

public class DetailWeb extends AppCompatActivity {

    private static final String TAG = "DetailWeb";
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_web);
        webView = findViewById(R.id.web_view);
        Intent intent = getIntent();
        if (intent != null) {
            String urlPath = intent.getStringExtra(URL_WEB);
            if (urlPath != null) {
//                Log.d(TAG, "onCreate: " + urlPath);
                webView.setWebViewClient(new MyWebViewClient());
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setJavaScriptEnabled(true);
                webView.setScrollBarStyle(SCROLLBARS_INSIDE_OVERLAY);
                webView.loadUrl(urlPath);

            } else
                webView.loadData(getResources().getString(R.string.error_url), "text/html", null);
        }


    }
}
