package com.example.umorning.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.umorning.R;


public class WebViewActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        final WebView myWebView = (WebView) findViewById(R.id.webView);

        myWebView.getSettings().setJavaScriptEnabled(true);
        myWebView.getSettings().setLoadsImagesAutomatically(true);

        myWebView.setWebViewClient(new WebViewClient(){

            @Override
            public void onLoadResource(WebView  view, String  url){

                String originalUrl=myWebView.getUrl();

                if( originalUrl.contains("umorning") ){

                    String access_token;

                    String[] splittedString = originalUrl.split("=");

                    access_token = splittedString[2];

                    SharedPreferences settings = getSharedPreferences("uMorning", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("EventbriteToken", access_token);
                    editor.commit();


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
        //TODO serve davvero??
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
