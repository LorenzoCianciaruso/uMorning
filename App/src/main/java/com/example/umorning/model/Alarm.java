package com.example.umorning.model;

import java.io.Serializable;
import java.util.Date;

public class Alarm implements Serializable {

    private int id;
    private int delay; //tempo che l'utente ci mette a uscire di casa
    private String name;
    private String address;
    private String city;
    private String country;
    private String startLatitude;
    private String startLongitude;
    private String endLatitude;
    private String endLongitude;
    private String locationName;

    //TODO il costruttore dovrà prendere un evento e settare i campi
    //TODO settare l'id in maniera intelligente (hash dei campi??)


    public int getId() {
        return id;
    }

    public int getDelay() {
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

    public Date getDate() {
        return date;
    }

    private Date date;
    private boolean activated;




}
