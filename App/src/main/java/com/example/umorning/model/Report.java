package com.example.umorning.model;

import java.util.Calendar;

public class Report {
    private int id;
    private long delay; //tempo che l'utente ci mette a uscire di casa
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private Calendar date;
    private Calendar expectedTime;
    private boolean weatherOk;
    private MetagsEnum realWeather;
    private boolean timeOk;
    private boolean late;


    public Report(int id, long delay, double startLatitude, double startLongitude, double endLatitude, double endLongitude, Calendar date, Calendar expectedTime) {
        this.id = id;
        this.delay = delay;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.date = date;
        this.expectedTime = expectedTime;
        this.weatherOk = false;
        this.realWeather = null;
        this.timeOk = false;
        this.late = false;
    }



    public long getDelay() {
        return delay;
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

    public Calendar getDate() {
        return date;
    }

    public Calendar getExpectedTime() {
        return expectedTime;
    }

    public boolean isWeatherOk() {
        return weatherOk;
    }

    public MetagsEnum getRealWeather() {
        return realWeather;
    }

    public boolean isTimeOk() {
        return timeOk;
    }

    public boolean isLate() {
        return late;
    }

    public int getId() {
        return id;
    }

    public void setLate(boolean late) {
        this.late = late;
    }

    public void setTimeOk(boolean timeOk) {
        this.timeOk = timeOk;
    }

    public void setRealWeather(MetagsEnum realWeather) {
        this.realWeather = realWeather;
    }

    public void setWeatherOk(boolean weatherOk) {
        this.weatherOk = weatherOk;
    }
}
