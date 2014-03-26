package com.example.umorning.model;


public class Event {

    private String name;
    private String id;
    private String organizerName;
    private String address;
    private String city;
    private String country;
    private String latitude;
    private String longitude;
    private String locationName;
    private String eventURL;
    private String getRequestURL;
    private String date;
    private String hour;
    private String status;
    private boolean partecipation;

//hd

    public Event(String name, String id, String organizerName, String address, String city, String country, String latitude, String longitude,String locationName, String eventURL, String getRequestURL, String date, String hour, String status) {
        this.name = name;
        this.id = id;
        this.organizerName = organizerName;
        this.address = address;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locationName = locationName;
        this.eventURL = eventURL;
        this.getRequestURL = getRequestURL;
        this.date = date;
        this.hour = hour;
        this.status = status;
    }

    public Event(String name, String id, String organizerName, String locationName) {
        this.id = id;
        this.name = name;
        this.organizerName = organizerName;
        this.locationName = locationName;
    }

    public void setPartecipation(boolean partecipation) {
        this.partecipation = partecipation;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public String getOrganizerName() {
        return organizerName;
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

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getEventURL() {
        return eventURL;
    }

    public String getGetRequestURL() {
        return getRequestURL;
    }

    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }

    public String getStatus() {
        return status;
    }

    public String getLocationName() {
        return locationName;
    }

    public boolean isPartecipation() {
        return partecipation;
    }
}
