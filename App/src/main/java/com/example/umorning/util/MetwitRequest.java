package com.example.umorning.util;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MetwitRequest {

    private double latitude;
    private double longitude;

    private String iconURL;
    private String temperature;
    private String locality;

    public MetwitRequest( double latitude, double longitude){
       this.latitude=latitude;
        this.longitude=longitude;
    }



    //start thread che invia richiesta http a metwit
    public void sendHttpRequest()  {

        String url = "https://api.metwit.com/v2/weather/?location_lat="+latitude+"&location_lng="+longitude;


                //Request the HTML
                try {

                    HttpParams httpParameters = new BasicHttpParams();
                    HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
                    HttpConnectionParams.setSoTimeout(httpParameters, 5000);

                    HttpClient client = new DefaultHttpClient(httpParameters);
                    HttpGet request = new HttpGet(url);
                    HttpResponse response = client.execute(request);
                    HttpEntity entity = response.getEntity();
                    InputStream is = entity.getContent();

                    StringBuilder builder = new StringBuilder();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        builder.append(line);
                    }

                    String result = builder.toString();

                    JSONObject jObject = new JSONObject(result);
                    JSONObject jsonWeather = jObject.getJSONArray("objects").getJSONObject(0);

                    locality = jsonWeather.getJSONObject("location").getString("locality");

                    iconURL = jsonWeather.getString("icon");

                    jsonWeather = jsonWeather.getJSONObject("weather");


                    //parse da json a int e conversione da fahrenheit a gradi centigradi
                    int temperatureFahrenheit = jsonWeather.getJSONObject("measured").getInt("temperature") - 273 ;

                    temperature = Integer.toString(temperatureFahrenheit);



                }
                catch (IOException e) {

                    Log.e("Tag", "Could not get HTML: " + e.getMessage());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
    }



    public String getIconURL() {
        return iconURL;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getLocality() {
        return locality;
    }





}



