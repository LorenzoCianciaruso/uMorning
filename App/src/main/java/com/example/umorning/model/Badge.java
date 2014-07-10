package com.example.umorning.model;

import java.util.ArrayList;
import java.util.List;

public class Badge {
    //define of badge' ids
    public static final int FIRST_USAGE = 1;
    public static final int FIRST_ALARM = 2;
    public static final int FIVE_ALARMS = 3;
    public static final int HUNDRED_ALARMS = 4;
    public static final int LOGIN_FB = 5;
    public static final int LOGIN_EB = 6;
    public static final int SHARE = 7;
    public static final int FIRST_RING = 8;
    public static final int SETTINGS = 9;
    public static final int RINGTONE = 10;
    public static final int SHORT_PREP_TIME = 11;
    public static final int LONG_PREP_TIME = 12;

    //fields
    private int id;
    private String name;
    private String description;
    private String iconAcquired;
    private String iconPending;
    private boolean acquired;

    public Badge (int id, String name, String description, String iconAcquired, String iconPending, boolean acquired){
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconAcquired = iconAcquired;
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

    public String getIconAcquired() {
        return iconAcquired;
    }

    public String getIconPending() {
        return iconPending;
    }

    public boolean isAcquired() {
        return acquired;
    }

    public void acquire() {
        this.acquired= true;
    }

    //this contains all the badges definiton used the first time to create the db
    public static List<Badge> createBadges(){
        List <Badge> list = new ArrayList<Badge>();

        //first usage
        int id = FIRST_USAGE;
        String name = "Beginner";
        String description = "Use uMorning for the first time";
        String iconAcquired = "";
        String iconPending = "";
        boolean acquired = false;
        list.add(new Badge(id, name, description, iconAcquired,iconPending,acquired));

        //first alarm created
        id = FIRST_ALARM;
        name = "Noob";
        description = "Save your first alarm";
        iconAcquired = "";
        iconPending = "";
        acquired = false;
        list.add(new Badge(id, name, description, iconAcquired,iconPending,acquired));

        //five alarms created
        id = FIVE_ALARMS;
        name = "Scheduler";
        description = "Create more then 5 alarms";
        iconAcquired = "";
        iconPending = "";
        acquired = false;
        list.add(new Badge(id, name, description, iconAcquired,iconPending,acquired));


        //five alarms created
        id = HUNDRED_ALARMS;
        name = "Workhaolic";
        description = "Wake up 100 times";
        iconAcquired = "";
        iconPending = "";
        acquired = false;
        list.add(new Badge(id, name, description, iconAcquired,iconPending,acquired));

        //TODO per ciancia far acquisire questi badge
        //TODO basta fare databasehelper.acquire(Badge.DEFINE_BADGE_ID) ma va fatto nel posto giusto
        //login with facebook
        id = LOGIN_FB;
        name = "Blue F";
        description = "Log with Facebook";
        iconAcquired = "";
        iconPending = "";
        acquired = false;
        list.add(new Badge(id, name, description, iconAcquired,iconPending,acquired));

        //login with Eventbrite
        id = LOGIN_EB;
        name = "Blue F";
        description = "Log with Eventbrite";
        iconAcquired = "";
        iconPending = "";
        acquired = false;
        list.add(new Badge(id, name, description, iconAcquired,iconPending,acquired));

        //share with Facebook
        id = SHARE;
        name = "Supporter";
        description = "Share your experience on Facebook";
        iconAcquired = "";
        iconPending = "";
        acquired = false;
        list.add(new Badge(id, name, description, iconAcquired,iconPending,acquired));

        //TODO fine del todo di ciancia
        //first ring
        id = FIRST_RING;
        name = "Wake-up!!";
        description = "uMorning fist word";
        iconAcquired = "";
        iconPending = "";
        acquired = false;
        list.add(new Badge(id, name, description, iconAcquired,iconPending,acquired));

        //access the settings
        id = SETTINGS;
        name = "Tuner";
        description = "Personalize uMorning";
        iconAcquired = "";
        iconPending = "";
        acquired = false;
        list.add(new Badge(id, name, description, iconAcquired,iconPending,acquired));

        //change ringtone
        id = RINGTONE;
        name = "Juke-box";
        description = "Explore uMorning personalized tones";
        iconAcquired = "";
        iconPending = "";
        acquired = false;
        list.add(new Badge(id, name, description, iconAcquired,iconPending,acquired));

        //short delay
        id = SHORT_PREP_TIME;
        name = "Flash";
        description = "Get ready in a very short time";
        iconAcquired = "";
        iconPending = "";
        acquired = false;
        list.add(new Badge(id, name, description, iconAcquired,iconPending,acquired));

        //long delay
        id = LONG_PREP_TIME;
        name = "Diva";
        description = "Spend hours to be perfect";
        iconAcquired = "";
        iconPending = "";
        acquired = false;
        list.add(new Badge(id, name, description, iconAcquired,iconPending,acquired));

        return list;
    }

}
