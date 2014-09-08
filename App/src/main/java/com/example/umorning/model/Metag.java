package com.example.umorning.model;

/**
 * Created by Lorenzo on 10/07/2014.
 */
public class Metag {

    private Geo geo;
    private Status weather;

    public void setWeather(MetagsEnum weather) {
        Status status = new Status(weather);
        this.weather = status;
    }

    public void setGeo(double lat, double lng){
        Geo geo = new Geo(lat, lng);
        this.geo = geo;
    }

    private class Status {
        private String status;
        Status(MetagsEnum status) {
            this.status = status.getValue();
        }
    }

    private class Geo {
        private Double lat;
        private Double lng;

        Geo(Double lat, Double lng) {
            this.lat = lat;
            this.lng = lng;
        }
    }

}


