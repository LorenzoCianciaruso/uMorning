package com.example.umorning;

import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.app.AlarmManager;
//import android.app.Fragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract.Calendars;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.Calendar;

public class HomeFragment extends Fragment {
    private TimePicker tpResult;
    private DatePicker dpResult;

    private ImageView weatherIcon;
    private TextView country;
    private TextView locality;
    private TextView temperature;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    private Button btnSetAlarm;

    private double latitude = 0;
    private double longitude = 0;

    private GpsLocalizationService gps;

    // dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[]{
            Calendars._ID,                           // 0
            Calendars.ACCOUNT_NAME,                  // 1
            Calendars.CALENDAR_DISPLAY_NAME,         // 2
            Calendars.OWNER_ACCOUNT                  // 3
    };

    // The indices for the projection array above.
    private static final int PROJECTION_ID_INDEX = 0;
    private static final int PROJECTION_ACCOUNT_NAME_INDEX = 1;
    private static final int PROJECTION_DISPLAY_NAME_INDEX = 2;
    private static final int PROJECTION_OWNER_ACCOUNT_INDEX = 3;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.fragment_home);

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

//TODO spostare tutta questa robaccia nella classe setalarm activity e fare un bottone nella main che ci vada con tutte le variabili sopra
        //prende ora della sveglia dal date picker
        btnSetAlarm = (Button) rootView.findViewById(R.id.btnSetAlarm);
        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tpResult = (TimePicker) getView().findViewById(R.id.timePicker);
                dpResult = (DatePicker) getView().findViewById(R.id.datePicker);
                year = dpResult.getYear();
                month = dpResult.getMonth();
                day = dpResult.getDayOfMonth();
                hour = tpResult.getCurrentHour();
                minute = tpResult.getCurrentMinute();

                //chiama un alarmservice

                Intent myIntent = new Intent(getActivity(), AlarmService.class);
                PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0, myIntent, 0);
                //AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

                //imposta l'ora e fa partire
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(year, month, day, hour, minute);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        });


        //TODO il todo finisce qui
        //toglie l'icona e il titolo del app dal actionbar
        //getActionBar().setDisplayShowHomeEnabled(false);
        //getActionBar().setDisplayShowTitleEnabled(false);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        gps = new GpsLocalizationService(getActivity());

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            // \n is for new line
            Toast.makeText(getActivity().getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            new AsyncTaskMeteoRequest().execute(latitude, longitude);

            // new AsyncTaskTrafficRequest().execute(latitude, longitude, 45.0, 9.0);

        } else {
            // Chiedi all'utente di andare nelle impostazioni
            gps.showSettingsAlert();
        }

        final int checkPlayStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
/*
        if (checkPlayStatus != ConnectionResult.SUCCESS) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(checkPlayStatus, this, 69, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {

                    //SDK obbligatorio
                    finish();
                }
            });
            dialog.show();
        }
        */
    }

    private class AsyncTaskMeteoRequest extends AsyncTask<Double, Void, MetwitRequest> {

        @Override
        protected MetwitRequest doInBackground(Double... params) {

            double latitude = params[0];
            double longitude = params[1];

            MetwitRequest weatherInfo = new MetwitRequest(latitude, longitude);

            weatherInfo.askForWeather();

            //TODO metto qui solo per provare
            // Projection array. Creating indices for this array instead of doing

/*
            // Run query
            Cursor cur = null;
            ContentResolver cr = getContentResolver();
            Uri uri = Calendars.CONTENT_URI;
            String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
                    + Calendars.ACCOUNT_TYPE + " = ?) AND ("
                    + Calendars.OWNER_ACCOUNT + " = ?))";
            String[] selectionArgs = new String[] {"lory90@gmail.com", "com.google",};

            // Submit the query and get a Cursor object back.
            String selection ="(1=?)";
            String[] selectionArgs = new String[]{"1"};
            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);


            String[] projection =
                    new String[]{
                            Calendars._ID,
                            Calendars.NAME,
                            Calendars.ACCOUNT_NAME,
                            Calendars.ACCOUNT_TYPE};
            Cursor calCursor =
                    getContentResolver().
                            query(Calendars.CONTENT_URI,
                                    projection,
                                    Calendars.VISIBLE + " = 1",
                                    null,
                                    Calendars._ID + " ASC");
            if (calCursor.moveToFirst()) {
                do {
                    long id = calCursor.getLong(0);
                    String displayName = calCursor.getString(1);
                    System.out.println("Ecco i campi"+calCursor.getString(0)+calCursor.getString(1)+calCursor.getString(2)+calCursor.getString(3));
                } while (calCursor.moveToNext());
            }*/


            return weatherInfo;
        }


        @Override
        protected void onPostExecute(MetwitRequest weatherInfo) {
            //trova riferimenti layout
            locality = (TextView) getView().findViewById(R.id.locality);
            country = (TextView) getView().findViewById(R.id.country);
            temperature = (TextView) getView().findViewById(R.id.temperature);
            weatherIcon = (ImageView) getView().findViewById(R.id.weatherIcon);



            //assegna variabili
            locality.setText(weatherInfo.getLocality());
            country.setText(weatherInfo.getCountry());
            temperature.setText(weatherInfo.getTemperature());

            if (weatherInfo.getIcon().equals("sunny")) {
                weatherIcon.setImageResource(R.drawable.sunny);
            } else if (weatherInfo.getIcon().equals("clear_moon")) {
                weatherIcon.setImageResource(R.drawable.clear_moon);
            }
            else if (weatherInfo.getIcon().equals("cloudy")) {
                weatherIcon.setImageResource(R.drawable.cloudy);
            }
            else if (weatherInfo.getIcon().equals("foggy")) {
                weatherIcon.setImageResource(R.drawable.foggy);
            }
            else if (weatherInfo.getIcon().equals("partly_moon")) {
                weatherIcon.setImageResource(R.drawable.partly_moon);
            }
            else if (weatherInfo.getIcon().equals("partly_sunny")) {
                weatherIcon.setImageResource(R.drawable.partly_sunny);
            }
            else if (weatherInfo.getIcon().equals("rainy")) {
                weatherIcon.setImageResource(R.drawable.rainy);
            }
            else if (weatherInfo.getIcon().equals("snowy")) {
                weatherIcon.setImageResource(R.drawable.snowy);
            }
            else if (weatherInfo.getIcon().equals("stormy")) {
                weatherIcon.setImageResource(R.drawable.stormy);
            }
            else if (weatherInfo.getIcon().equals("windy")) {
                weatherIcon.setImageResource(R.drawable.windy);
            }


        }
    }


////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////
    //Todo tutta la roba sotto va messo in EventsFragments, la roba sopra fa la sveglia e verrà spostato in AlarmsFragment
/*

    @Override
    public void onResume() {
        super.onResume();
        gps = new GpsLocalizationService(MainActivity.this);

        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();


            // \n is for new line
            Toast.makeText(getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            new AsyncTaskMeteoRequest().execute(latitude, longitude);

            new AsyncTaskTrafficRequest().execute(latitude, longitude, 45.0, 9.0);

        } else {
            // Chiedi all'utente di andare nelle impostazioni
            gps.showSettingsAlert();
        }

        final int checkPlayStatus = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        if (checkPlayStatus != ConnectionResult.SUCCESS) {
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(checkPlayStatus, this, 69, new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    //SDK obbligatorio
                    finish();
                }
            });
            dialog.show();
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


    //TODO sto fragmaent a che cazzo serve?
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
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

    public boolean startAccountManager(MenuItem menuItem){

        super.onOptionsItemSelected(menuItem);
        this.closeOptionsMenu();
        Intent intent = new Intent(this, AccountManagerActivity.class);

        startActivity(intent);
        return true;
    }

    private class AsyncTaskMeteoRequest extends AsyncTask<Double, Void, MetwitRequest> {

        @Override
        protected MetwitRequest doInBackground(Double... params) {

            double latitude = params[0];
            double longitude = params[1];

            MetwitRequest weatherInfo = new MetwitRequest(latitude,longitude);

            weatherInfo.sendHttpRequest();

            //TODO metto qui solo per provare
            // Projection array. Creating indices for this array instead of doing
*/
/*
            // Run query
            Cursor cur = null;
            ContentResolver cr = getContentResolver();
            Uri uri = Calendars.CONTENT_URI;
            String selection = "((" + Calendars.ACCOUNT_NAME + " = ?) AND ("
                    + Calendars.ACCOUNT_TYPE + " = ?) AND ("
                    + Calendars.OWNER_ACCOUNT + " = ?))";
            String[] selectionArgs = new String[] {"lory90@gmail.com", "com.google",};

            // Submit the query and get a Cursor object back.
            String selection ="(1=?)";
            String[] selectionArgs = new String[]{"1"};
            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);*/

/*
            String[] projection =
                    new String[]{
                            Calendars._ID,
                            Calendars.NAME,
                            Calendars.ACCOUNT_NAME,
                            Calendars.ACCOUNT_TYPE};
            Cursor calCursor =
                    getContentResolver().
                            query(Calendars.CONTENT_URI,
                                    projection,
                                    Calendars.VISIBLE + " = 1",
                                    null,
                                    Calendars._ID + " ASC");
            if (calCursor.moveToFirst()) {
                do {
                    long id = calCursor.getLong(0);
                    String displayName = calCursor.getString(1);
                    System.out.println("Ecco i campi"+calCursor.getString(0)+calCursor.getString(1)+calCursor.getString(2)+calCursor.getString(3));
                } while (calCursor.moveToNext());
            }


            return weatherInfo;
        }


        @Override
        protected void onPostExecute(MetwitRequest weatherInfo) {
            // try {

            Toast.makeText(getApplicationContext(), "URL : " + weatherInfo.getIcon() + "\nTemperature : " + weatherInfo.getTemperature(), Toast.LENGTH_LONG).show();

            //URL url = new URL(weatherInfo.getIcon());
            //HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            //connection.setDoInput(true);
            //connection.connect();
            //InputStream input = connection.getInputStream();
            //Bitmap myBitmap = BitmapFactory.decodeStream(input);

            //TODO aggiorna icona usando myBitmap
            //TODO aggiorna temperatura
            //TODO aggiorna localita


            //} catch (IOException e) {
            //    e.printStackTrace();

            // }

        }
    }


    private class AsyncTaskTrafficRequest extends AsyncTask<Double, Void, GoogleTraffic> {

        @Override
        protected GoogleTraffic doInBackground(Double... params) {

            double startLatitude = params[0];
            double startLongitude = params[1];
            double endLatitude = params[2];
            double endLongitude = params[3];

            GoogleTraffic traffic = new GoogleTraffic(startLatitude,startLongitude,endLatitude,endLongitude);

            traffic.sendHttpRequest();

            return traffic;



        }

        @Override
        protected void onPostExecute(GoogleTraffic traffic){

        }


    }

*/


}
























