package com.example.umorning.model;


public class Alarm {
    private int id;
    private Event event;
    private int delay; //tempo che l'utente ci mette a uscire di casa

    public int getId() {
        return id;
    }

    public Event getEvent() {
        return event;
    }

    public int getDelay() {
        return delay;
    }
}
