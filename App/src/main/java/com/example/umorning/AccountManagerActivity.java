package com.example.umorning;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


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

    public void eventbriteAuth(View view) {
        //Intent intent = new Intent(Intent.ACTION_VIEW,
        //ri.parse("https://www.eventbrite.com/oauth/authorize?response_type=token&client_id=AWF7I3D2E3CAVX6QNW"));
        //startActivity(intent);

        Intent intent = new Intent(this, WebViewActivity.class);
        Bundle b = new Bundle();
        b.putString("url", "https://www.eventbrite.com/oauth/authorize?response_type=token&client_id=AWF7I3D2E3CAVX6QNW");
        intent.putExtras(b); //Put your id to your next Intent
        startActivity(intent);
        finish();

        postData();




    }

    public void postData() {
        // Create a new HttpClient and Post Header
        HttpClient httpclient = new DefaultHttpClient();

        HttpPost httppost = new HttpPost("https://www.eventbrite.com/oauth/token");

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
// then you use
        String access = prefs.getString("EventbriteToken", null);


        try {
            // Add your data


            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            nameValuePairs.add(new BasicNameValuePair("code", access));
            nameValuePairs.add(new BasicNameValuePair("client_secret", "IAHAVFS5X7LLPMSPDYAW63LAZPFWGL6MHBOULWP363TRPVOG5V"));
            nameValuePairs.add(new BasicNameValuePair("client_id","AWF7I3D2E3CAVX6QNW"));
            nameValuePairs.add(new BasicNameValuePair("grant_tyoe", "authorization_code"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);

            //String result = EntityUtils.toString(response);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
        } catch (IOException e) {
            // TODO Auto-generated catch block
        }
    }

    public void facebookAuth(View view){

    }

}
