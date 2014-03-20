package com.example.umorning;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.StringTokenizer;


public class WebViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        final WebView myWebView = (WebView) findViewById(R.id.webView);

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setLoadsImagesAutomatically(true);

        myWebView.setWebViewClient(new WebViewClient(){
            // you tell the webclient you want to catch when a url is about to load
           // @Override
           // public boolean shouldOverrideUrlLoading(WebView  view, String  url){
           //     return true;
          //  }
            // here you execute an action when the URL you want is about to load
            @Override
            public void onLoadResource(WebView  view, String  url){

                String originalUrl=myWebView.getUrl();





                if( originalUrl.contains("umorning") ){

                    String access_token;

                    String[] splittedString = originalUrl.split("=");

                    access_token = splittedString[2];

                //TODO salvare token
                Intent intent = new Intent(WebViewActivity.this, AccountManagerActivity.class);
                startActivity(intent);
                finish();
            }
            }

        });

        Bundle b = getIntent().getExtras();
        String url = b.getString("url");

        myWebView.loadUrl(url);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.web_view, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
