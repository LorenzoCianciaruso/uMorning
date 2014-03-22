package com.example.umorning;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.umorning.util.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

public class MetwitRequest {

    private double latitude;
    private double longitude;


    private String icon;
    private String temperature;
    private String locality;

    public String getCountry() {
        return country;
    }

    private String country;

    public MetwitRequest(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    //start thread che invia richiesta http a metwit
    public void askForWeather() {
                String url = "https://api.metwit.com/v2/weather/?location_lat=" + latitude + "&location_lng=" + longitude;
                String result = new HttpRequest().getRequest(url);

                try {
                    JSONObject jObject = new JSONObject(result);
                    JSONObject jsonWeather = jObject.getJSONArray("objects").getJSONObject(0);
                    locality = jsonWeather.getJSONObject("location").getString("locality");
                    country = jsonWeather.getJSONObject("location").getString("country");
                    String urlIcon = jsonWeather.getString("icon");
                    String[] urlSplitted = urlIcon.split("/");
                    icon = urlSplitted[5];
                    jsonWeather = jsonWeather.getJSONObject("weather");
                    //parse da json a int e conversione da fahrenheit a gradi centigradi
                    int temperatureFahrenheit = jsonWeather.getJSONObject("measured").getInt("temperature") - 273;
                    temperature = Integer.toString(temperatureFahrenheit);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }



    public String getIcon() {
        return icon;
    }

    public String getTemperature() {
        return temperature;
    }

    public String getLocality() {
        return locality;
    }


}



