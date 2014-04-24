package com.example.umorning.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.umorning.R;
import com.example.umorning.external_services.GoogleGeocode;
import com.example.umorning.external_services.GoogleTrafficRequest;
import com.example.umorning.external_services.HttpRequest;
import com.example.umorning.internal_services.AlarmBroadcastReceiver;
import com.example.umorning.internal_services.GpsLocalizationService;
import com.example.umorning.model.Alarm;
import com.example.umorning.model.DatabaseHelper;

import java.util.Calendar;

public class AlarmEditActivity extends Activity {

    //campi dell'interfaccia
    private TextView nameT;
    private TextView addressT;
    private TextView cityT;
    private TextView countryT;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private NumberPicker delayPicker;
    private CheckBox activation;
    private Button saveButton;
    private Button deleteButton;

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
        delayPicker = (NumberPicker) findViewById(R.id.numberPicker);
        delayPicker.setMaxValue(480);
        delayPicker.setMinValue(1);
        activation = (CheckBox) findViewById(R.id.checkBox);
        saveButton = (Button) findViewById(R.id.button1);
        deleteButton = (Button) findViewById(R.id.button);

        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        delay = prefs.getLong("DELAY", 30);
        date = Calendar.getInstance();
        expectedTime = Calendar.getInstance();
        delayPicker.setValue((int) delay);
        activation.setChecked(true);
        saveButton.setEnabled(true);
        deleteButton.setEnabled(true);

        id = getIntent().getIntExtra("alarmId", 0);

        db = new DatabaseHelper(this);
        //solo se è una modifica
        if (id != 0) {
            toUpdate = db.getAlarm(id);
            nameT.setText(toUpdate.getName());
            addressT.setText(toUpdate.getAddress());
            cityT.setText(toUpdate.getCity());
            countryT.setText(toUpdate.getCountry());
            delayPicker.setValue((int) (toUpdate.getDelay()));
            date = toUpdate.getDate();
            expectedTime = toUpdate.getExpectedTime();
            activation.setChecked(toUpdate.isActivated());
            //TODO
            endLatitude = toUpdate.getEndLatitude();
            endLongitude = toUpdate.getEndLongitude();
        }

        int year = date.get(Calendar.YEAR);
        int month = date.get(Calendar.MONTH);
        int day = date.get(Calendar.DAY_OF_MONTH);
        int hour = date.get(Calendar.HOUR_OF_DAY);
        int min = date.get(Calendar.MINUTE);
        datePicker.updateDate(year, month, day);
        timePicker.setCurrentHour(hour);
        timePicker.setCurrentMinute(min);

    }

    public void onSavePressed(View view) {
        saveButton.setEnabled(false);
        deleteButton.setEnabled(false);
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

    public void onDeletePressed(View view) {
        db.deleteAlarm(id);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        if (getIntent().getIntExtra("fromEvent", 0) == 1) {
            db.deleteAlarm(id);
        }
        finish();
    }

    private void saveAlarm() {
        //prendi i campi che l'utente ha settato
        name = nameT.getText().toString();
        address = addressT.getText().toString();
        city = cityT.getText().toString();
        country = countryT.getText().toString();
        activated = activation.isChecked();
        delay = delayPicker.getValue();
        date.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth(), timePicker.getCurrentHour(), timePicker.getCurrentMinute());

        if (activated) {
            if (id == 0 || (id != 0 && (!address.equals(toUpdate.getAddress()) || !city.equals(toUpdate.getCity()) || !country.equals(toUpdate.getCountry())))) {
                //traduci indirizzo in coordinate
                GoogleGeocode gg = new GoogleGeocode(address, city, country);
                endLatitude = gg.getLatitude();
                endLongitude = gg.getLongitude();
            }
            long trafficMillis = 0;
            //richiesta traffico
            if (HttpRequest.isOnline(this)) {
                GoogleTrafficRequest trafficRequest = new GoogleTrafficRequest(startLatitude, startLongitude, endLatitude, endLongitude);
                trafficMillis = trafficRequest.getTripDurationInMillis();
            } else {
                Toast.makeText(this, "No internet Connection", Toast.LENGTH_SHORT);
            }

            //ottengo l'ora della sveglia sottraendo traffico e tempo per prepararsi
            expectedTime.setTimeInMillis(date.getTimeInMillis() - trafficMillis - (delay * 1000 * 60));

        }

        toDelete = false;
        //salva nel db aggiornando o creando
        Alarm updated = new Alarm(id, delay, name, address, city, country, startLatitude, startLongitude, endLatitude, endLongitude, date, expectedTime, activated, toDelete);
        if (id == 0) {
            id = (int) db.addAlarm(updated);
        } else {
            db.updateAlarm(updated);
        }
        //considera il refresh rate
        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        long refreshRate = prefs.getLong("REFRESH", 60);
        //metti la sveglia se è in questo segmento temporale
        if (expectedTime.getTimeInMillis() < (System.currentTimeMillis() + (refreshRate * 60 * 1000)) && activated) {
            //chiama un alarmservice
            Intent myIntent = new Intent(this, AlarmBroadcastReceiver.class);
            myIntent.putExtra("alarmId", id);
            PendingIntent intent = PendingIntent.getService(this, 0, myIntent, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
            //imposta l'ora e fa partire
            alarmManager.set(AlarmManager.RTC_WAKEUP, expectedTime.getTimeInMillis(), intent);
        }
        finish();
    }
}