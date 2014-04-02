package com.example.umorning.model;

import java.util.Date;

public class Alarm {

    private long id;
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
    private Date date;
    private boolean activated;

    //TODO il costruttore dovrà prendere un evento e settare i campi
    //TODO settare l'id in maniera intelligente (hash dei campi??)

    public Alarm(Event e){
        this.name=e.getName();

    }
    public Alarm(){
        id=1;
        delay=2;
        name="ciao";

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

    public Date getDate() {
        return date;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setDelay(int delay) {
        this.delay = delay;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setStartLatitude(String startLatitude) {
        this.startLatitude = startLatitude;
    }

    public void setStartLongitude(String startLongitude) {
        this.startLongitude = startLongitude;
    }

    public void setEndLatitude(String endLatitude) {
        this.endLatitude = endLatitude;
    }

    public void setEndLongitude(String endLongitude) {
        this.endLongitude = endLongitude;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setActivated(boolean activated) {
        this.activated = activated;
    }
}
