package com.example.umorning.model;


public class Event {

    private String name;
    private String place;
    private String latitude;
    private String longitude;
    private String url;
    private String date;
    private String hour;
    private boolean partecipation;

    public Event(String name, String place, String latitude, String longitude, String url, String date, String hour, boolean partecipation){
        this.name=name;
        this.place=place;
        this.latitude=latitude;
        this.longitude=longitude;
        this.url=url;
        this.date=date;
        this.hour=hour;
        this.partecipation=partecipation;
    }

    public String getName() {
        return name;
    }

    public String getPlace() {
        return place;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getUrl() {
        return url;
    }

    public String getDate() {
        return date;
    }

    public String getHour() {
        return hour;
    }

    public boolean isPartecipation() {
        return partecipation;
    }

    public void setPartecipation(boolean partecipation) {
        this.partecipation = partecipation;
    }


}
