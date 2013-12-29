package com.example.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Lorenzo on 29/12/13.
 */
public class MeteoService {

    public void onCreate(){
        sendHttpRequest();
    }

    private void sendHttpRequest() {
        HttpClient httpclient;
        httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost("https://api.metwit.com/token?grant_type=client_credentials");

        httppost.setHeader("Authorization","eW91cmNsaWVudF9pZDpaS1U5bUxGZHBJS2M5VHlNaU9zZ1Bt");

        httppost.setRequestProperty("Content-Type", "application/www-form-urlencoded");

        try {
            // Add your data
           // List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            //nameValuePairs.add(new BasicNameValuePair("grant_type", "client_credentials"));
            //httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            System.out.println("bind fatto");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            System.out.println("inviata richiesta");

            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            System.out.println(responseString);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            System.out.println("exp1");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("exp2");
        }






    }




}
