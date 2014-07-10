package com.example.umorning.model;

/**
 * Created by Lorenzo on 10/07/2014.
 */
public class Metag {

    private Status weather;

    private Geo geo;

    public Metag(String weather, Double lat, Double lng) {

        Status status = new Status(weather);
        this.weather = status;

        Geo geoTag = new Geo(lat, lng);
        this.geo = geoTag;
    }

    private class Status {

        private String status;

        Status(String status) {
            this.status = status;
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


