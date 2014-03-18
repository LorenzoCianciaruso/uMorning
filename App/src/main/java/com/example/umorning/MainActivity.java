package com.example.umorning;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

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

    private double latitude = 0;
    private double longitude = 0;

    private GpsLocalizationService gps;


    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //TODO spostare tutta questa robaccia nella classe setalarm activity e fare un bottone nella main che ci vada con tutte le variabili sopra
        //prende ora della sveglia dal date picker
        btnSetAlarm = (Button) findViewById(R.id.btnSetAlarm);
        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tpResult = (TimePicker) findViewById(R.id.timePicker);
                dpResult = (DatePicker) findViewById(R.id.datePicker);
                year = dpResult.getYear();
                month = dpResult.getMonth();
                day = dpResult.getDayOfMonth();
                hour = tpResult.getCurrentHour();
                minute = tpResult.getCurrentMinute();

                //chiama un alarmservice
                Intent myIntent = new Intent(MainActivity.this, MyAlarmService.class);
                PendingIntent pendingIntent = PendingIntent.getService(MainActivity.this, 0, myIntent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

                //imposta l'ora e fa partire
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(year, month, day, hour, minute);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        });


        //TODO il todo finisce qui


        //toglie l'icona e il titolo del app dal actionbar
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);


    }

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

    /**
     * A placeholder fragment containing a simple view.
     */
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

    private class AsyncTaskMeteoRequest extends AsyncTask<Double, Void, MetwitRequest> {

        @Override
        protected MetwitRequest doInBackground(Double... params) {

            double latitude = params[0];
            double longitude = params[1];

            MetwitRequest weatherInfo = new MetwitRequest(latitude,longitude);

            weatherInfo.sendHttpRequest();

            return weatherInfo;
        }


           /* String url = "https://api.metwit.com/v2/weather/?location_lat="+latitude+"&location_lng="+longitude;

            try{

            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
            HttpConnectionParams.setSoTimeout(httpParameters, 5000);

            HttpClient client = new DefaultHttpClient(httpParameters);
            HttpGet request = new HttpGet(url);
            HttpResponse response = client.execute(request);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();

            StringBuilder builder = new StringBuilder();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = reader.readLine()) != null) {
                builder.append(line);
            }

            String result = builder.toString();

            JSONObject jObject = new JSONObject(result);
            JSONObject jsonWeather = jObject.getJSONArray("objects").getJSONObject(0);

            String locality = jsonWeather.getJSONObject("location").getString("locality");

            String iconURL = jsonWeather.getString("icon");

            jsonWeather = jsonWeather.getJSONObject("weather");


            //parse da json a int e conversione da fahrenheit a gradi centigradi
            int temperatureFahrenheit = jsonWeather.getJSONObject("measured").getInt("temperature") - 273 ;

            String temperature = Integer.toString(temperatureFahrenheit);

                return iconURL;

        }
        catch (IOException e) {

            Log.e("Tag", "Could not get HTML: " + e.getMessage());
        } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }*/

        @Override
        protected void onPostExecute(MetwitRequest weatherInfo) {
           // try {

                Toast.makeText(getApplicationContext(), "URL : " + weatherInfo.getIconURL() + "\nTemperature : " + weatherInfo.getTemperature(), Toast.LENGTH_LONG).show();

                //URL url = new URL(weatherInfo.getIconURL());
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

            /*String url = "https://maps.googleapis.com/maps/api/distancematrix/json?origins=" + startLatitude + "," + startLongitude + "&destinations=" + endLatitude + "," + endLongitude + "&mode=driving&language=en-US&sensor=false&key=AIzaSyDj6lm3eLSuOhG4rLXL66WUBg7C7XEDYcA";

            try {

                HttpParams httpParameters = new BasicHttpParams();
                HttpConnectionParams.setConnectionTimeout(httpParameters, 5000);
                HttpConnectionParams.setSoTimeout(httpParameters, 5000);

                HttpClient client = new DefaultHttpClient(httpParameters);
                HttpGet request = new HttpGet(url);
                HttpResponse response = client.execute(request);
                HttpEntity entity = response.getEntity();
                InputStream is = entity.getContent();


                StringBuilder builder = new StringBuilder();
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }

                String result = builder.toString();
                JSONObject jObject = new JSONObject(result);
                JSONArray jsonRows = jObject.getJSONArray("rows");
                JSONObject jsonElement = (JSONObject) jsonRows.get(0);
                JSONArray jsonElem = jsonElement.getJSONArray("elements");
                JSONObject jsonE = (JSONObject) jsonElem.get(0);
                JSONObject jsonDuration = jsonE.getJSONObject("duration");

                // valore in secondi della durata del viaggio
                int value = jsonDuration.getInt("value");


            } catch (IOException e) {
                Log.e("Tag", "Could not get HTML: " + e.getMessage());
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;*/

        }

        @Override
        protected void onPostExecute(GoogleTraffic traffic){

        }


    }
}



