package com.example.umorning.model;

import android.app.PendingIntent;

import java.util.Calendar;

public class Alarm {

    private int id;
    private long delay; //tempo che l'utente ci mette a uscire di casa
    private String name;
    private String address;
    private String city;
    private String country;
    private String startLatitude;
    private String startLongitude;
    private String endLatitude;
    private String endLongitude;
    private String locationName;
    private Calendar date;
    private boolean activated;
    private PendingIntent intent;

    public Alarm (int id, long delay, String name, String address, String city, String country, String startLatitude, String startLongitude, String endLatitude, String endLongitude, String locationName, Calendar date, boolean activated, PendingIntent intent){
        this.id= id;
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

    public String getStartLatitude() {
        return startLatitude;
    }

    public String getStartLongitude() {
        return startLongitude;
    }

    public String getEndLatitude() {
        return endLatitude;
    }

    public String getEndLongitude() {
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

    public PendingIntent getIntent() {
        return intent;
    }

}
