package com.example.umorning.activities;

import android.app.Activity;
import android.app.PendingIntent;
import android.os.Bundle;
import android.view.View;
import android.widget.TimePicker;

import com.example.umorning.R;
import com.example.umorning.model.Alarm;
import com.example.umorning.model.DatabaseHelper;

import java.util.Calendar;

public class AlarmAddNewActivity extends Activity {
    TimePicker timepicker;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_alarm);
        timepicker = (TimePicker) findViewById(R.id.timePicker1);
        timepicker.setIs24HourView(true);
        db = new DatabaseHelper(this);

        //recupero la sveglia dal db o ne creo una nuova
        //TODO passare l'id e settarlo in id passare -1 se è creazione e non modifica
        Alarm a;
        if (id==-1){
            a = new Alarm();
        }
        else{
            a = db.getAlarm(id);
        }
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
        this.intent=a.getIntent();

        //TODO forse tutta sta roba va in save alarm qui sotto ne parliamo quando c'è la grafica

        //TODO usare l'altro costruttore se non abbiamo le coordinate
        /*
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

        //tempo per prepararsi dalle preferenze
        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        prefs.getLong("DELAY", 4);

        //ottengo l'ora della sveglia sottraendo traffico e tempo per prepararsi
        timeOfArrival = new GregorianCalendar();
        timeOfAlarm = new GregorianCalendar();
        timeOfAlarm.setTimeInMillis(timeOfArrival.getTimeInMillis()-trafficMillis-userTimeMillis);

        //chiama un alarmservice
        Intent myIntent = new Intent(this, AlarmService.class);
        PendingIntent pendingIntent = PendingIntent.getService(this, 0, myIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);

        //imposta l'ora e fa partire
        alarmManager.set(AlarmManager.RTC_WAKEUP, timeOfAlarm.getTimeInMillis(), pendingIntent);

        //print di prova
        System.out.println ("allarme alle "+timeOfAlarm+" appuntamento alle "+timeOfArrival);

        */
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

    public void onSavePressed(View view){
        Alarm updated = new Alarm(id, delay, name, address, city, country, startLatitude, startLongitude, endLatitude, endLongitude, location, date, activated, intent);
        db.updateAlarm(id,updated);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}