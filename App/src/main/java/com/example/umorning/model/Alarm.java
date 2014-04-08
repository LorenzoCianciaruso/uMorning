package com.example.umorning.model;

import android.content.Intent;

import java.util.Calendar;

public class Alarm {

    private long id;
    private long delay; //tempo che l'utente ci mette a uscire di casa
    private String name;
    private String address;
    private String city;
    private String country;
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private String locationName;
    private Calendar date;
    private boolean activated;
    private Intent intent;

    public Alarm(long id, long delay, String name, String address, String city, String country, double startLatitude, double startLongitude, double endLatitude, double endLongitude, String locationName, Calendar date, boolean activated, Intent intent) {
        this.id = id;
        this.delay = delay;
        this.name = name;
        this.address = address;
        this.city = city;
        this.country = country;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.locationName = locationName;
        this.date = date;
        this.activated = activated;
        this.intent = intent;
    }

    public long getId() {
        return id;
    }

    public long getDelay() {
        return delay;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public String getLocationName() {
        return locationName;
    }

    public boolean isActivated() {
        return activated;
    }

    public Calendar getDate() {
        return date;
    }

    public Intent getIntent() {
        return intent;
    }
}
