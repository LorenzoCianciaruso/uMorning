package com.example.umorning.model;

public class Badge {
    private int id;
    private String name;
    private String description;
    private String iconAquired;
    private String iconPending;
    private boolean acquired;

    public Badge (int id, String name, String description, String iconAquired, String iconPending, boolean acquired){
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconAquired = iconAquired;
        this.iconPending = iconPending;
        this.acquired = acquired;
    }


    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getIconAquired() {
        return iconAquired;
    }

    public String getIconPending() {
        return iconPending;
    }

    public boolean isAcquired() {
        return acquired;
    }
}
