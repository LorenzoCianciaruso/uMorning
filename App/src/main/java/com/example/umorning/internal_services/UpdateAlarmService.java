package com.example.umorning.internal_services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import com.example.umorning.external_services.GoogleTrafficRequest;
import com.example.umorning.model.Alarm;
import com.example.umorning.model.DatabaseHelper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
    private double startLatitude;
    private double startLongitude;
    private double endLatitude;
    private double endLongitude;
    private String location;
    private Calendar date;
    private boolean activated;
    private PendingIntent intent;

    private List <Alarm> alarms = new ArrayList<Alarm>();

    @Override
    public void onCreate() {
        //apri l'applicazione e aggiorna le sveglie
        //TOdo gps attivo
        if (true) {
            alarms = db.getAllAlarms();
            for (Alarm a : alarms) {
                if (a.isActivated()) {

                    //copio l'allarme in locale
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
                    intent.cancel();

                    //aggiornamento
                    //richiesta traffico
                    GoogleTrafficRequest trafficRequest = new GoogleTrafficRequest(startLatitude,startLongitude,endLatitude,endLongitude);
                    long trafficMillis = new Long (trafficRequest.getTripDuration());

                    //ottengo l'ora della sveglia sottraendo traffico e tempo per prepararsi
                    Calendar timeOfAlarm = new GregorianCalendar();
                    timeOfAlarm.setTimeInMillis(date.getTimeInMillis()-trafficMillis-delay);

                    //chiama un alarmservice
                    Intent myIntent = new Intent(this, AlarmService.class);
                    intent = PendingIntent.getService(this, 0, myIntent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);

                    //imposta l'ora e fa partire
                    alarmManager.set(AlarmManager.RTC_WAKEUP, timeOfAlarm.getTimeInMillis(), intent);

                    //aggiorna l'allarme e lo salva nel DB
                    Alarm updated = new Alarm(id, delay, name, address, city, country, startLatitude, startLongitude, endLatitude, endLongitude, location, date, activated, intent);
                    db.updateAlarm(id,updated);
                }
            }
        }
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
