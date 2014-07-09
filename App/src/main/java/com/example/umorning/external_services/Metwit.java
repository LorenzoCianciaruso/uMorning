package com.example.umorning.external_services;

import com.example.umorning.model.WeatherForecasts;
import com.github.kevinsawicki.http.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Metwit {

    private double latitude;
    private double longitude;
    private String token;

    //private String icon;
    //private String temperature;
    //private String locality;
    //private String country;

    public Metwit(double latitude, double longitude) throws NullPointerException {
        this.latitude = latitude;
        this.longitude = longitude;
        //askForWeather();
    }

  /*  public Metwit(String icon, String temperature, String locality, String country) {
        this.icon = icon;
        this.temperature = temperature;
        this.locality = locality;
        this.country = country;
    }*/

    //start thread che invia richiesta http a metwit
    public WeatherForecasts askForWeather() throws NullPointerException {
        String url = "https://api.metwit.com/v2/weather/?location_lat=" + latitude + "&location_lng=" + longitude;
        String result = new HttpRequests().getRequest(url);
        WeatherForecasts weathFor = null;

        try {
            JSONObject jObject = new JSONObject(result);

            JSONObject jsonWeather = jObject.getJSONArray("objects").getJSONObject(0);
            String locality = jsonWeather.getJSONObject("location").getString("locality");
            String country = jsonWeather.getJSONObject("location").getString("country");
            String urlIcon = jsonWeather.getString("icon");
            String[] urlSplitted = urlIcon.split("/");
            String icon = urlSplitted[5];
            jsonWeather = jsonWeather.getJSONObject("weather");
            //parse da json a int e conversione da fahrenheit a gradi centigradi
            int temperatureFahrenheit = jsonWeather.getJSONObject("measured").getInt("temperature") - 273;
            String temperature = Integer.toString(temperatureFahrenheit);

            weathFor = new WeatherForecasts(latitude,longitude,icon,temperature,locality,country);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return weathFor;
    }

    private String getAuthorizationToken(){

        String url = "https://api.metwit.com/token/";

        Map<String, String> data = new HashMap<String, String>();
        data.put("grant_type", "client_credentials");

        HttpRequest r = HttpRequest.post(url)
                .basic("129379649", "QePJVqOllqI4qITm8K05Q0zFEvHkFfFo9GKdhgeV").form(data);

        String response = r.body();

        try {
            JSONObject jObject = new JSONObject(response);
            token = jObject.getString("access_token");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;

    }

    public void postMetag( ) {

        //TODO da mettere a posto parametro in input
        String url = "https://api.metwit.com/metags/";

        //TODO prendere latitudine longitude meteo e parsare secondo la richiesta da fare

        String json = "";

        new Thread(new Runnable() {
            public void run(){
                getAuthorizationToken();
            }
        }).start();

        HttpRequest r = HttpRequest.post(url)
                .header("Authorization", "Bearer " + token)
                .contentType("application/json")
                .send(json);
    }

}




