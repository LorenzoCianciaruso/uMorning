package com.example.umorning.model;

import java.io.Serializable;

public class Alarm implements Serializable {
    private int id;
    private Event event;
    private int delay; //tempo che l'utente ci mette a uscire di casa
    private boolean activated;


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
