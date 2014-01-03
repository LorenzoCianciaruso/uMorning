package com.example.app;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import static android.widget.Toast.*;

/**
 * Created by Lorenzo on 29/12/13.
 */
public class MeteoService extends Service {

    @Override
    public void onCreate() {
        System.out.println("onCreate");
        sendHttpRequest();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy(){

    }

    @Override
    public void onStart(Intent intent, int startId){

    }

    private void sendHttpRequest()  {
        System.out.println("sendhttp");
        String url = "https://api.metwit.com/v2/weather/?location_lat=45.45&location_lng=9.18";
        String responseString = null;
        try{
        HttpClient httpclient = new DefaultHttpClient();
        HttpResponse response = httpclient.execute(new HttpGet(url));
        StatusLine statusLine = response.getStatusLine();
        if(statusLine.getStatusCode() == HttpStatus.SC_OK){
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            response.getEntity().writeTo(out);
            out.close();
            responseString = out.toString();
            //..more logic
        } else{
            //Closes the connection.
            response.getEntity().getContent().close();
            throw new IOException(statusLine.getReasonPhrase());
        }
        }catch(Exception e){
            System.out.println("eccezione qui");
            e.printStackTrace();
        }

        //InputStream instream = getInputStreamFromUrl(url);


        // A Simple JSON Response Read
        //InputStream instream = entity.getContent();
        //String result = convertStreamToString(instream);
        // now you have the string representation of the HTML request
        System.out.println("Risposta : "+ responseString);


}

    private InputStream getInputStreamFromUrl(String url) {
        InputStream content = null;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(new HttpGet(url));
            content = response.getEntity().getContent();
            System.out.println("sono qui");
        } catch (Exception e) {
            //og.("[GET REQUEST]", "Network exception", e);
            System.out.println("eccezione");
        }
        return content;
    }

    private static String convertStreamToString(InputStream is) {
    /*
     * To convert the InputStream to String we use the BufferedReader.readLine()
     * method. We iterate until the BufferedReader return null which means
     * there's no more data to read. Each line will appended to a StringBuilder
     * and returned as String.
     */
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }



        /*
        HttpPost httppost = new HttpPost("https://api.metwit.com/token/");

        String clientID = "129379649";
        String clientSecret = "V_94KAqyEY6qKfKfxpghqvJsL2DcLSkjMJ5Jwp11";
        String authentication = clientID+":"+clientSecret;
        String authentication = "eW91cmNsaWVudF9pZDpaS1U5bUxGZHBJS2M5VHlNaU9zZ1Bt";
        byte[] authenticationEncoded = null;
        try {
            authenticationEncoded = authentication.getBytes("UTF-8");

        } catch (UnsupportedEncodingException e1) {
            //TODO excpetion
        }
        String base64 = Base64.encodeToString(authenticationEncoded, Base64.DEFAULT);



        httppost.setHeader("Authorization","Basic " + authentication);
         httppost.setHeader("Content-Type", "application/www-form-urlencoded");




        try {
            // Add your data
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);

            nameValuePairs.add(new BasicNameValuePair("grant_type", "client_credentials"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            System.out.println("bind fatto");
            // Execute HTTP Post Request
            HttpResponse response = httpclient.execute(httppost);
            System.out.println("inviata richiesta");

            HttpEntity entity = response.getEntity();
            String responseString = EntityUtils.toString(entity, "UTF-8");
            System.out.println("Risposta "+responseString);

        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            System.out.println("exp1");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            System.out.println("exp2");
        }

*/


}



