package com.run.ultimate_fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebPage extends AppCompatActivity {

    private WebView webView;
    private WebSettings webSettings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_page);
        getSupportActionBar().hide();


        webView = (WebView) findViewById(R.id.webView);
        webView.loadUrl("https://ultimatefitness.company/bookings");



        webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);


        webView.setWebViewClient(new WebViewClient());


    }
}