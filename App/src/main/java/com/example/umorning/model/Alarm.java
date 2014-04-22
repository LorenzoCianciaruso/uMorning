package com.example.umorning.model;

import java.util.Calendar;

public class Alarm {

    private int id;
    private long delay; //tempo che l'utente ci mette a uscire di casa
    private String name;
    private String address;
    private String city;
    private String country;
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private Calendar date;
    private Calendar expectedTime;
    private boolean activated;
    private boolean toDelete;

    public Alarm(int id, long delay, String name, String address, String city, String country, double startLatitude, double startLongitude, double endLatitude, double endLongitude, Calendar date, Calendar expectedTime, boolean activated, boolean toDelete) {
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
        this.date = date;
        this.expectedTime = expectedTime;
        this.activated = activated;
        this.toDelete = toDelete;
    }

    public int getId() {
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

    public boolean isActivated() {
        return activated;
    }

    public boolean isToDelete () {
        return toDelete;
    }

    public Calendar getDate() {
        return date;
    }

    public Calendar getExpectedTime() {
        return expectedTime;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
