package com.example.umorning.internal_services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.example.umorning.model.Alarm;
import com.example.umorning.model.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class UpdateAlarmService extends Service {

    DatabaseHelper db;

    //campi di alarm
    private long id;
    private long delay;
    private String name;
    private String address;
    private String city;
    private String country;
    private String startLatitude;
    private String startLongitude;
    private String endLatitude;
    private String endLongitude;
    private String location;
    private Calendar date;
    private boolean activated;
    private PendingIntent intent;

    private List <Alarm> alarms = new ArrayList<Alarm>();

    @Override
    public void onCreate() {
        //apri l'applicazione e aggiorna le sveglie
        //TOdo gps attivo
        if ()
        alarms = db.getAllAlarms();
        for (Alarm a :alarms) {
            if (a.isActivated()){
                this.id = a.getId();
                this.delay = a.getDelay();
                this.name = a.getName();
                this.address = a.getAddress();
                this.city = a.getCity();
                this.country = a.getCountry();
                this.startLatitude = a.getStartLatitude();
                this.startLongitude = a.getStartLongitude();
                this.endLatitude = a.getEndLatitude();
                this.endLongitude = a.getEndLongitude();
                this.location = a.getLocationName();
                this.date = a.getDate();
                this.activated = a.isActivated();
                this.intent = a.getIntent();
            }
        }
        Intent dialogIntent = new Intent(getBaseContext(), AlarmRingActivity.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        getApplication().startActivity(dialogIntent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "AlarmService.onBind()", Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "AlarmService.onDestroy()", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this, "AlarmService.onUnbind()", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }
}
