package com.example.umorning;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MeteoService extends Service {

    @Override
    public void onCreate() {

        sendHttpRequest();
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy(){

    }

    //start thread che invia richiesta http a metwit
    private void sendHttpRequest()  {



        new Thread(new Runnable() {
            //Thread to stop network calls on the UI thread
            public void run() {
                //Request the HTML
                try {
                    String url = "https://api.metwit.com/v2/weather/?location_lat=45.45&location_lng=9.18";
                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
                    HttpConnectionParams.setSoTimeout(httpParameters, 5000);

                    HttpClient client = new DefaultHttpClient(httpParameters);
                    HttpGet request = new HttpGet(url);
                    HttpResponse response = client.execute(request);
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();

                    System.out.println("Risposta: "+convertStreamToString(is));


                    //Do something with the response
                }
                catch (IOException e) {

                    Log.e("Tag", "Could not get HTML: " + e.getMessage());
                }
            }
        }).start();



    }

    //converte flusso in input in una stringa
    private static String convertStreamToString(InputStream is) {

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
}



