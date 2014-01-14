package com.example.app;

import android.app.Activity;
import android.app.Fragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Calendar;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.widget.Button;


import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import android.widget.Toast;

//import com.example.app.GpsLocalizationService.LocalBinder;


public class MainActivity extends Activity {

    private TimePicker tpResult;
    private DatePicker dpResult;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    private Button btnSetAlarm;

    private double latitude=0;
    private double longitude=0;

    private GpsLocalizationService gps;
    //GpsLocalizationService localizationService;
    //boolean isBound = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
       // Intent intent = new Intent(this, GpsLocalizationService.class);
       // bindService(intent, myConnection, Context.BIND_AUTO_CREATE);
       // startService(intent);
        setContentView(R.layout.activity_main);

        //TODO spostare tutta questa robaccia nella classe setalarm activity e fare un bottone nellla main che ci vada
        //TODO spostando tutte le variabili e le import perchè non si riesce a capire un cazzo
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


        //TODO il todo finisce qui




        //toglie l'icona e il titolo del app dal actionbar
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);


    }
/*
    private ServiceConnection myConnection = new ServiceConnection() {

        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            localizationService = binder.getService();
            isBound = true;
        }

        public void onServiceDisconnected(ComponentName arg0) {
            isBound = false;
        }

    };
    */
    @Override
    public void onResume(){
        super.onResume();
        gps = new GpsLocalizationService(MainActivity.this);

        // check if GPS enabled
        if(gps.canGetLocation()){

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();



            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            new AsyncTaskMeteoRequest().execute(latitude,longitude);

        }else{
            System.out.println("SETTINGS");
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }

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
    //TODO sto fragmaent a che cazzo serve?
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

    private class AsyncTaskMeteoRequest extends AsyncTask<Double, Void, String> {

        @Override
        protected String doInBackground(Double... params) {

            double latitude = params[0];
            double longitude = params[1];

            String url = "https://api.metwit.com/v2/weather/?location_lat="+latitude+"&location_lng="+longitude;

            try{

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);

            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            System.out.println("Risposta: "+convertStreamToString(is));


            //Do something with the response
        }
        catch (IOException e) {

            Log.e("Tag", "Could not get HTML: " + e.getMessage());
        }
            //TODO parsing json per estrarre url icona località temperatura
            return null;
        }

        @Override
        protected void onPostExecute(String url){
            //TODO update icona meteo

        }

        //TODO metodo solo per debug
        private String convertStreamToString(InputStream is) {

            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();

            String line = null;
            try {
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return sb.toString();
        }



    }



}
