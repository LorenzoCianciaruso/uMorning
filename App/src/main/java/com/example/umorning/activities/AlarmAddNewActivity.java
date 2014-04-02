package com.example.umorning.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TimePicker;

import com.example.umorning.R;
import com.example.umorning.external_services.GoogleTrafficRequest;
import com.example.umorning.internal_services.AlarmService;
import com.example.umorning.internal_services.GpsLocalizationService;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AlarmAddNewActivity extends Activity {
    TimePicker timepicker;
    Long trafficMillis;
    Long userTimeMillis;
    Calendar timeOfArrival;
    Calendar timeOfAlarm;
    Double startLatitude;
    Double startLongitude;
    Double arrivalLatitude;
    Double arrivalLongitude;
    String arrivalLocation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_alarm);
        timepicker = (TimePicker) findViewById(R.id.timePicker1);
        timepicker.setIs24HourView(true);

        //TODO forse tutta sta roba va in save alarm qui sotto ne parliamo quando c'è la grafica

        //TODO usare l'altro costruttore se non abbiamo le coordinate

        //ottengo la posizione attuale
        GpsLocalizationService gps = new GpsLocalizationService(this);
        // controlla se il GPS è attivo
        if (gps.canGetLocation()) {
            //TODO gestire le coordinate 0 0 per adesso si può catchare l'eccezione
            startLatitude = gps.getLatitude();
            startLongitude = gps.getLongitude();
        } else {
            // Chiedi all'utente di andare nelle impostazioni
            gps.showSettingsAlert();
        }
        //richiesta traffico
        GoogleTrafficRequest trafficRequest = new GoogleTrafficRequest(startLatitude,startLongitude,arrivalLatitude,arrivalLongitude);
        trafficMillis = new Long (trafficRequest.getTripDuration());

        //tempo per prepararsi
        SettingsActivity ud = new SettingsActivity();
        userTimeMillis = ud.getUserDelay();

        //ottengo l'ora della sveglia sottraendo traffico e tempo per prepararsi
        timeOfArrival = new GregorianCalendar();
        timeOfAlarm = new GregorianCalendar();
        timeOfAlarm.setTimeInMillis(timeOfArrival.getTimeInMillis()-trafficMillis-userTimeMillis);

        //chiama un alarmservice
        //TODO passare qualche campo significativo per poter riconoscere gli intent
        Intent myIntent = new Intent(this, AlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);

        //imposta l'ora e fa partire
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeOfAlarm.getTimeInMillis(), pendingIntent);

        //print di prova
        System.out.println ("allarme alle "+timeOfAlarm+" appuntamento alle "+timeOfArrival);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    public void saveAlarm(View view) {
        /* Do something in response to button
		Intent myIntent = new Intent(MainActivity.this, AlarmAddNewActivity.class);
		startActivity(myIntent);
        Intent myIntent = new Intent(AlarmAddNewActivity.this, MainActivity.class);
        startActivity(myIntent);
        myIntent.putExtra("index", 2);
        startActivity(myIntent);
        overridePendingTransition(R.anim.fadeout, R.anim.fadein);*/
        finish();
        overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}