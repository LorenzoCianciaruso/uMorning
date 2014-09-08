package com.example.umorning.model;

import com.example.umorning.R;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Badge implements Serializable{
    //define id badge
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

    //campi
    private int id;
    private String name;
    private String description;
    private int iconAcquired;
    private int iconPending;
    private boolean aquired;

    public Badge (int id, String name, String description, int iconAcquired, int iconPending, boolean aquired){
        this.id = id;
        this.name = name;
        this.description = description;
        this.iconAcquired = iconAcquired;
        this.iconPending = iconPending;
        this.aquired = aquired;
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

    public int getIconAcquired() {
        return iconAcquired;
    }

    public int getIconPending() {
        return iconPending;
    }

    public boolean isAquired() {
        return aquired;
    }

    public void aquire() {
        this.aquired = true;
    }

    //definizioni dei badge usate per inizializzare il db
    public static List<Badge> createBadges() {
        List<Badge> list = new ArrayList<Badge>();

        //primo uso
        int id = Badge.FIRST_USAGE;
        String name = "Beginner";
        String description = "Use uMorning for the first time!";
        int iconAcquired = R.drawable.first_usage;
        int iconPending = R.drawable.badge_pending;
        boolean aquired = false;
        list.add(new Badge(id, name, description, iconAcquired, iconPending, aquired));

        //primo allarme creato
        id = Badge.FIRST_ALARM;
        name = "Noob";
        description = "Save your first alarm!";
        iconAcquired = R.drawable.first_alarm;
        //iconPending = "";
        aquired = false;
        list.add(new Badge(id, name, description, iconAcquired, iconPending, aquired));

        //5 allarmi
        id = Badge.FIVE_ALARMS;
        name = "Scheduler";
        description = "Create more then 5 alarms!";
        iconAcquired = R.drawable.five_alarms;
        //iconPending = "";
        aquired = false;
        list.add(new Badge(id, name, description, iconAcquired, iconPending, aquired));

        //100 allarmi
        id = Badge.HUNDRED_ALARMS;
        name = "Workhaolic";
        description = "Wake up 100 times!";
        iconAcquired = R.drawable.hundred_alarm;
        //iconPending = "";
        aquired = false;
        list.add(new Badge(id, name, description, iconAcquired, iconPending, aquired));

        //login con facebook
        id = Badge.LOGIN_FB;
        name = "Blue F";
        description = "Log in with Facebook!";
        iconAcquired = R.drawable.login_fb;
        //iconPending = "";
        aquired = false;
        list.add(new Badge(id, name, description, iconAcquired, iconPending, aquired));

        //login con Eventbrite
        id = Badge.LOGIN_EB;
        name = "Eventsfinder";
        description = "Log in with Eventbrite!";
        iconAcquired = R.drawable.login_eb;
        //iconPending = "";
        aquired = false;
        list.add(new Badge(id, name, description, iconAcquired, iconPending, aquired));

        //share con Facebook
        id = Badge.SHARE;
        name = "Supporter";
        description = "Share your experience on Facebook!";
        iconAcquired = R.drawable.share;
        //iconPending = "";
        aquired = false;
        list.add(new Badge(id, name, description, iconAcquired, iconPending, aquired));

        //prima sveglia
        id = Badge.FIRST_RING;
        name = "Wake-up!!";
        description = "uMorning first word!";
        iconAcquired = R.drawable.first_ring;
        //iconPending = "";
        aquired = false;
        list.add(new Badge(id, name, description, iconAcquired, iconPending, aquired));

        //vai alle impostazioni
        id = Badge.SETTINGS;
        name = "Tuner";
        description = "Personalize uMorning!";
        iconAcquired = R.drawable.settings;
        //iconPending = "";
        aquired = false;
        list.add(new Badge(id, name, description, iconAcquired, iconPending, aquired));

        //cambia suoneria
        id = Badge.RINGTONE;
        name = "Juke-box";
        description = "Explore uMorning personalized tones!";
        iconAcquired = R.drawable.ringtone;
        //iconPending = "";
        aquired = false;
        list.add(new Badge(id, name, description, iconAcquired, iconPending, aquired));

        //short delay
        id = Badge.SHORT_PREP_TIME;
        name = "Flash";
        description = "Get ready in a very short time!";
        iconAcquired = R.drawable.short_time;
        //iconPending = "";
        aquired = false;
        list.add(new Badge(id, name, description, iconAcquired, iconPending, aquired));

        //long delay
        id = Badge.LONG_PREP_TIME;
        name = "Diva";
        description = "Spend hours to be perfect!";
        iconAcquired = R.drawable.long_time;
        //iconPending = "";
        aquired = false;
        list.add(new Badge(id, name, description, iconAcquired, iconPending, aquired));

        return list;
    }

}
