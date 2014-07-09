package com.example.umorning.model;

/**
 * Created by Lorenzo on 14/06/2014.
 */
public class WeatherForecasts {

    private double latitude;
    private double longitude;
    private String icon;
    private String temperature;
    private String locality;
    private String country;


    public WeatherForecasts(double latitude, double longitude, String icon, String temperature, String locality, String country) {
        this.latitude = latitude;
        this.longitude = longitude;
        this.icon = icon;
        this.temperature = temperature;
        this.locality = locality;
        this.country = country;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
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

    public String getCountry() {
        return country;
    }
}