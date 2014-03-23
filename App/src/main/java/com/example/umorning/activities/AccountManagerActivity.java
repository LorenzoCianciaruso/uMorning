package com.example.umorning.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.umorning.R;
import com.example.umorning.activities.WebViewActivity;
import com.example.umorning.external_services.HttpRequest;


public class AccountManagerActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_manager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.account_manager, menu);
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
//
    public void eventbriteAuth(View view) {


        Intent intent = new Intent(this, WebViewActivity.class);
        Bundle b = new Bundle();
        b.putString("url", "https://www.eventbrite.com/oauth/authorize?response_type=token&client_id=AWF7I3D2E3CAVX6QNW");
        intent.putExtras(b);
        startActivity(intent);
        finish();

    }

    public void postData() {

        new Thread(new Runnable() {
            public void run() {

                SharedPreferences prefs = getSharedPreferences("uMorning", 0);
                String access = prefs.getString("EventbriteToken", "errore");
                String url = "https://www.eventbriteapi.com/v3/users/me/orders/?token=" + access;
                String result = new HttpRequest().getRequest(url);
                System.out.println(result);


            }

        }).start();

    }


    public void facebookAuth(View view) {

    }

}
