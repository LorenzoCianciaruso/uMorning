package com.example.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import java.util.Calendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

    private TimePicker tpResult;
    private DatePicker dpResult;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    private Button btnSetAlarm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //toglie l'icona e il titolo del app dal actionbar
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);

        //GPSLocalization
        //Intent gpsIntent = new Intent(this, GpsLocalizationService.class);
        //startService(gpsIntent);
        double latitude;
        double longitude;
        GpsLocalizationService gps = new GpsLocalizationService(this);
        if(gps.canGetLocation()){ // gps enabled} // return boolean true/false
            latitude = gps.getLatitude(); // returns latitude
            longitude = gps.getLongitude(); // returns longitude
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
        }else{
            gps.showSettingsAlert();
        }


        //MeteoService, richiesta informazioni meteo
        Intent meteoIntent = new Intent(this, MeteoService.class);
       // meteoIntent.putExtra("latitude", latitude);
       // meteoIntent.putExtra("longitude", longitude);
        startService(meteoIntent);


        //prende ora della sveglia dal date picker
        btnSetAlarm = (Button) findViewById(R.id.btnSetAlarm);
        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tpResult=(TimePicker) findViewById(R.id.timePicker);
                dpResult=(DatePicker) findViewById(R.id.datePicker);
                year=dpResult.getYear();
                month=dpResult.getMonth();
                day=dpResult.getDayOfMonth();
                hour=tpResult.getCurrentHour();
                minute=tpResult.getCurrentMinute();

                //chiama un alarmservice
                Intent myIntent = new Intent(MainActivity.this, MyAlarmService.class);
                PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, myIntent, 0);
                AlarmManager alarmManager = (AlarmManager)getSystemService(ALARM_SERVICE);

                //imposta l'ora e fa partire
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(year,month,day,hour,minute);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }



}
