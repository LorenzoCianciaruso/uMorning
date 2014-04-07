package com.example.umorning.model;

import java.util.Calendar;

public class Event {
    private String name;
    private String organizerName;
    private String address;
    private String city;
    private String country;
    private double latitude;
    private double longitude;
    private String locationName;
    private String eventURL;
    private String getRequestURL;
    private Calendar date;
    private String status;
    private boolean activation;

    public Event(String name, String organizerName, String address, String city, String country, double latitude, double longitude, String locationName, String eventURL, String getRequestURL, Calendar date, String status) {
        this.name = name;
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
        this.status = status;
    }

    public Event(String name, String organizerName, String locationName, Calendar date) {
        this.name = name;
        this.organizerName = organizerName;
        this.locationName = locationName;
        this.date = date;
    }

    //controlla che l'evento non sia spam
    public boolean validEvent() {
        if (this.date == null) {
            return false;
        }
        if (this.name != null && this.name.contains("compleanno")) {
            return false;
        }
        if (this.name != null && this.name.contains("Pasqua")) {
            return false;
        }
        if (this.name != null && this.name.contains("Natale")) {
            return false;
        }
        if (this.name != null && this.name.contains("Epifania")) {
            return false;
        }
        if (this.name != null && this.name.contains("Assunzione")) {
            return false;
        }
        if (this.name != null && this.name.contains("Santo")) {
            return false;
        }
        if (this.name != null && this.name.contains("Santi")) {
            return false;
        }
        if (this.name != null && this.name.contains("Repubblica")) {
            return false;
        }
        if (this.name != null && this.name.contains("Liberazione")) {
            return false;
        }
        if (this.name != null && this.name.contains("Lavoro")) {
            return false;
        }
        if (this.name != null && this.name.contains("Capodanno")) {
            return false;
        }
        if (this.name != null && this.name.contains("Silvestro")) {
            return false;
        }
        if (this.name != null && this.name.contains("Immacolata")) {
            return false;
        }
        if (this.name != null && this.name.contains("Pasquetta")) {
            return false;
        }
        return true;
    }

    public boolean futureEvent() {
        if (this.date != null && this.date.after(Calendar.getInstance())) {
            return true;
        }
        return false;
    }

    //controlla che i campi fondamentali per la sveglia siano definiti
    public boolean checkForAlarm() {
        if (this.date != null && this.locationName != null && this.name != null) {
            return true;
        }
        return false;
    }

    public void setActivation(boolean activation) {
        this.activation = activation;
    }

    public String getName() {
        return name;
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

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getEventURL() {
        return eventURL;
    }

    public String getGetRequestURL() {
        return getRequestURL;
    }

    public Calendar getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public String getLocationName() {
        return locationName;
    }

    public boolean activation() {
        return activation;
    }

}
