package com.example.umorning.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.umorning.R;
import com.example.umorning.external_services.GoogleGeocode;
import com.example.umorning.external_services.GoogleTrafficRequest;
import com.example.umorning.internal_services.AlarmBroadcastReceiver;
import com.example.umorning.internal_services.GpsLocalizationService;
import com.example.umorning.model.Alarm;
import com.example.umorning.model.DatabaseHelper;

import java.util.Calendar;
import java.util.GregorianCalendar;

public class AlarmFromEventActivity extends Activity {

    //campi dell'interfaccia
    private TextView nameT;
    private TextView addressT;
    private TextView cityT;
    private TextView countryT;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private NumberPicker delayPicker;
    private CheckBox activation;

    //campi di alarm
    private int id;
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
    private Calendar expectedTime;
    private boolean activated;
    private boolean toDelete;


    private Alarm toUpdate;
    private DatabaseHelper db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_new_alarm);

        //getta tutti i puntatori alla grafica
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker1);
        timePicker.setIs24HourView(true);
        nameT = (TextView) findViewById(R.id.event_name);
        addressT = (TextView) findViewById(R.id.address);
        cityT = (TextView) findViewById(R.id.city);
        countryT = (TextView) findViewById(R.id.country);
        delayPicker =(NumberPicker) findViewById(R.id.numberPicker);
        delayPicker.setMaxValue(480);
        delayPicker.setMinValue(1);
        activation = (CheckBox) findViewById(R.id.checkBox);

        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        delay = prefs.getLong("DELAY", 30);
        date = Calendar.getInstance();
        delayPicker.setValue((int) delay);
        activation.setChecked(true);

        id = getIntent().getIntExtra("alarmId", 0);

        db = new DatabaseHelper(this);
        //solo se è una modifica
        if (id !=0) {
            toUpdate = db.getAlarm(id);
            nameT.setText(toUpdate.getName());
            addressT.setText(toUpdate.getAddress());
            cityT.setText(toUpdate.getCity());
            countryT.setText(toUpdate.getCountry());
            delayPicker.setValue((int) (toUpdate.getDelay()));
            date=toUpdate.getDate();
            expectedTime=toUpdate.getExpectedTime();
            activation.setChecked(toUpdate.isActivated());
        }

        int year=date.get(Calendar.YEAR);
        int month=date.get(Calendar.MONTH);
        int day=date.get(Calendar.DAY_OF_MONTH);
        int hour=date.get(Calendar.HOUR_OF_DAY);
        int min=date.get(Calendar.MINUTE);
        datePicker.updateDate(year, month, day);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(min);

    }

    public void onSavePressed(View view) {
        //ottengo la posizione attuale
        GpsLocalizationService gps = new GpsLocalizationService(this);
        // controlla se il GPS è attivo
        if (gps.canGetLocation()) {
            gps.getLocation();
            startLatitude = gps.getLatitude();
            startLongitude = gps.getLongitude();
        } else {
            // Chiedi all'utente di andare nelle impostazioni
            gps.showSettingsAlert();
        }

        //il salvataggio in un thread perchè deve accedere a internet
        new Thread(new Runnable() {
            public void run() {
                saveAlarm();
            }
        }).start();
    }

    public void onDeletePressed (View view) {
        db.deleteAlarm(id);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        finish();
    }

    private void saveAlarm() {
        //prendi i campi che l'utente ha settato
        name = nameT.getText().toString();
        address = addressT.getText().toString();
        city = cityT.getText().toString();
        country = countryT.getText().toString();
        activated=activation.isChecked();
        delay=delayPicker.getValue();
        date.set(datePicker.getYear(),datePicker.getMonth(),datePicker.getDayOfMonth(),timePicker.getCurrentHour(),timePicker.getCurrentMinute());

        if (activated) {
            if (address!=toUpdate.getAddress()||city!=toUpdate.getCity()||country!=toUpdate.getCountry()) {
                //traduci indirizzo in coordinate
                GoogleGeocode gg = new GoogleGeocode(address, city, country);
                endLatitude = gg.getLatitude();
                endLongitude = gg.getLongitude();
            }
            //richiesta traffico
            GoogleTrafficRequest trafficRequest = new GoogleTrafficRequest(startLatitude, startLongitude, endLatitude, endLongitude);
            long trafficMillis = trafficRequest.getTripDurationInMillis();

            //ottengo l'ora della sveglia sottraendo traffico e tempo per prepararsi
            expectedTime.setTimeInMillis(date.getTimeInMillis() - trafficMillis - (delay * 1000 * 60));

            if (expectedTime.after((Calendar.getInstance().getTimeInMillis()+60*60*1000))) {
                //chiama un alarmservice
                Intent myIntent = new Intent(this, AlarmBroadcastReceiver.class);
                myIntent.putExtra("AlarmId", toUpdate.getId());
                PendingIntent intent = PendingIntent.getService(this, 0, myIntent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);

                //imposta l'ora e fa partire
                alarmManager.set(AlarmManager.RTC_WAKEUP, expectedTime.getTimeInMillis(), intent);
            }
        }

        toDelete=false;
        //salva nel db aggiornando o creando
        Alarm updated = new Alarm(id, delay, name, address, city, country, startLatitude, startLongitude, endLatitude, endLongitude, location, date, expectedTime, activated, toDelete);
        if (id == 0){
            db.addAlarm(updated);
        }
        else {
            db.updateAlarm(updated);

        }

        //TODO print di debug
        System.out.println("SSSSSSS sveglia modificata con id  " + id);

        finish();
    }

}