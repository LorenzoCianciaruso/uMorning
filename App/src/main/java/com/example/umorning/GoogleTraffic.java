package com.example.umorning.util;

import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class GoogleTraffic {

    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;

  

    private int tripDuration;
    private int tripDistance;



    public GoogleTraffic(double startLatitude, double startLongitude, double endLatitude, double endLongitude){
        this.startLatitude=startLatitude;
        this.startLongitude=startLongitude;
        this.endLatitude=endLatitude;
        this.endLongitude=endLongitude;
    }

    public void askForTraffic() {

                String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + startLatitude + "," + startLongitude + "&destinations=" + endLatitude + "," + endLongitude + "&mode=driving&language=en-US&sensor=false&key=AIzaSyDj6lm3eLSuOhG4rLXL66WUBg7C7XEDYcA";
                String result = new HttpRequest().getRequest(url);

        try {
            JSONObject jObject = new JSONObject(result);
            JSONArray jsonRows = jObject.getJSONArray("rows");
            JSONObject jsonElement = (JSONObject) jsonRows.get(0);
            JSONArray jsonElem = jsonElement.getJSONArray("elements");
            JSONObject jsonE = (JSONObject) jsonElem.get(0);
            JSONObject jsonDuration = jsonE.getJSONObject("duration");

            // valore in secondi della durata del viaggio
            tripDuration = jsonDuration.getInt("value");
        } catch (JSONException e){
            e.printStackTrace();
        }
            }

            //TODO tripdistance


    public int getTripDuration() {
        return tripDuration;
    }

    public int getTripDistance() {
        return tripDistance;
    }

}
