package com.example.umorning.model;

import java.util.Calendar;

public class Event {
    private String name;
    private String organizer;
    private String address;
    private String city;
    private String country;
    private double latitude;
    private double longitude;
    private String eventURL;
    private Calendar date;
    private String status;
    private int type;

    public Event(String name, String organizer, String address, String city, String country, double latitude, double longitude, String eventURL, Calendar date, String status, int type) {
        this.name = name;
        this.organizer = organizer;
        this.address = address;
        this.city = city;
        this.country = country;
        this.latitude = latitude;
        this.longitude = longitude;
        this.eventURL = eventURL;
        this.date = date;
        this.status = status;
        this.type=type;
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


    public String getName() {
        return name;
    }

    public String getOrganizer() {
        return organizer;
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

    public Calendar getDate() {
        return date;
    }

    public String getStatus() {
        return status;
    }

    public int getType() {
        return type;
    }

}
