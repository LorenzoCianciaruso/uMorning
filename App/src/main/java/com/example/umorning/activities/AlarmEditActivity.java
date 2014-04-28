package com.example.umorning.activities;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.umorning.R;
import com.example.umorning.external_services.GoogleGeocode;
import com.example.umorning.external_services.GoogleTraffic;
import com.example.umorning.external_services.HttpRequest;
import com.example.umorning.internal_services.AlarmBroadcastReceiver;
import com.example.umorning.internal_services.GpsLocalization;
import com.example.umorning.model.Alarm;
import com.example.umorning.model.DatabaseHelper;

import java.util.Calendar;

public class AlarmEditActivity extends Activity implements
        View.OnClickListener {

    //campi dell'interfaccia
    private TextView nameT;
    private TextView addressT;
    private TextView cityT;
    private TextView countryT;

    //private NumberPicker delayPicker;
    private CheckBox activation;
    private ImageButton saveButton;
    private ImageButton deleteButton;

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

    /* minute, hour, day, mont, year */
    private int min;
    private int hour;
    private int day;
    private int month;
    private int year;

    /* time, date and delay picker */
    private TextView timePickerText;
    private TextView datePickerText;
    private TextView delayPickerText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_alarm);

        // EditText
        nameT = (TextView) findViewById(R.id.event_name);
        addressT = (TextView) findViewById(R.id.address);
        cityT = (TextView) findViewById(R.id.city);
        countryT = (TextView) findViewById(R.id.country);
        // TextView (Pickers)
        datePickerText = (TextView) findViewById(R.id.textDatePicker);
        timePickerText = (TextView) findViewById(R.id.textTimePicker);
        delayPickerText = (TextView) findViewById(R.id.textDelayPicker);
        // On/Off
        activation = (CheckBox) findViewById(R.id.checkBox);
        // Button
        saveButton = (ImageButton) findViewById(R.id.saveButton);
        deleteButton = (ImageButton) findViewById(R.id.deleteButton);

        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        delay = prefs.getLong("DELAY", 30);
        date = Calendar.getInstance();
        expectedTime = Calendar.getInstance();

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

            delay = toUpdate.getDelay();
            date = toUpdate.getDate();
            expectedTime = toUpdate.getExpectedTime();
            activation.setChecked(toUpdate.isActivated());
            endLatitude = toUpdate.getEndLatitude();
            endLongitude = toUpdate.getEndLongitude();
        }

        year = date.get(Calendar.YEAR);
        month = date.get(Calendar.MONTH);
        day = date.get(Calendar.DAY_OF_MONTH);
        hour = date.get(Calendar.HOUR_OF_DAY);
        min = date.get(Calendar.MINUTE);


        datePickerText.setText(day + "/" + (month + 1) + "/" + year);
        if (min < 10)
            timePickerText.setText(hour + ":0" + min);
        else
            timePickerText.setText(hour + ":" + min);
        delayPickerText.setText(delay + " minutes");

        timePickerText.setOnClickListener(this);
        datePickerText.setOnClickListener(this);
        delayPickerText.setOnClickListener(this);
    }

    public void onSavePressed(View view) {
        /*saveButton.setEnabled(false);
        deleteButton.setEnabled(false);*/
        saveButton.setClickable(false);
        deleteButton.setClickable(false);
        //ottengo la posizione attuale
        GpsLocalization gps = new GpsLocalization(this);
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
        onBackPressed();
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
        date.set(year, month, day, hour, min);

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
                GoogleTraffic trafficRequest = new GoogleTraffic(startLatitude, startLongitude, endLatitude, endLongitude);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_alarm_details, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain").setText("").getIntent();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.remove: {
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                if (getIntent().getIntExtra("fromEvent", 0) == 1) {
                    db.deleteAlarm(id);
                }
                finish();
                break;
            }
            case R.id.accept: {
                //ottengo la posizione attuale
                GpsLocalization gps = new GpsLocalization(this);
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
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        final Calendar c = Calendar.getInstance();
        if (v == timePickerText) {
            TimePickerDialog tpd = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hh,
                                              int mm) {
                            // Visualizzo l'ora
                            if (mm < 10)
                                timePickerText.setText(hh + ":0" + mm);
                            else
                                timePickerText.setText(hh + ":" + mm);
                            // Salvo in min e hour
                            hour = hh;
                            min = mm;
                        }
                    }, hour, min, true
            );
            tpd.setTitle("Set time");
            tpd.show();
        }
        if (v == datePickerText) {
            DatePickerDialog dpd = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int y, int m, int d) {
                            datePickerText.setText(d + "/" + (m + 1) + "/" + y);
                            year = y;
                            month = m;
                            day = d;
                        }
                    }, year, month, day
            );
            dpd.setTitle("Set date");
            dpd.show();
        }
        if (v == delayPickerText) {
            RelativeLayout linearLayout = new RelativeLayout(this);
            final NumberPicker aNumberPicker = new NumberPicker(this);
            aNumberPicker.setMaxValue(480);
            aNumberPicker.setMinValue(1);
            aNumberPicker.setValue((int) delay);
            RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                    50, 50);
            RelativeLayout.LayoutParams numPicerParams = new RelativeLayout.LayoutParams(
                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                    RelativeLayout.LayoutParams.WRAP_CONTENT);
            numPicerParams.addRule(RelativeLayout.CENTER_HORIZONTAL);

            linearLayout.setLayoutParams(params);
            linearLayout.addView(aNumberPicker, numPicerParams);

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("Set delay");
            alertDialogBuilder.setView(linearLayout);
            alertDialogBuilder
                    .setCancelable(false)
                    .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            delay = aNumberPicker.getValue();
                            delayPickerText.setText(delay + " minutes");
                        }
                    })
                    .setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    dialog.cancel();
                                }
                            }
                    );
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
        }

    }

}