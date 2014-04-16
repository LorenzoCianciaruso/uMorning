package com.example.umorning.fragments;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umorning.R;
import com.example.umorning.external_services.HttpRequest;
import com.example.umorning.external_services.MetwitRequest;
import com.example.umorning.internal_services.GpsLocalizationService;
import com.example.umorning.model.Alarm;
import com.example.umorning.model.DatabaseHelper;
import com.google.android.gms.common.GooglePlayServicesUtil;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private ImageView weatherIcon;
    private TextView country;
    private TextView locality;
    private TextView temperature;
    private ProgressBar progress;
    private double latitude;
    private double longitude;

    private ListView list;
    private AlarmsAdapter adapter;
    private List<Alarm> alarms;
    private List<Alarm> activatedAlarms;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onStart() {

        GpsLocalizationService gps;
        super.onStart();

        gps = new GpsLocalizationService(getActivity());

        //trova riferimenti layout
        locality = (TextView) getView().findViewById(R.id.locality);
        country = (TextView) getView().findViewById(R.id.country);
        temperature = (TextView) getView().findViewById(R.id.temperature);
        weatherIcon = (ImageView) getView().findViewById(R.id.weatherIcon);
        progress = (ProgressBar) getView().findViewById(R.id.pbHeaderProgress);
        progress.setVisibility(View.INVISIBLE);

        updateUI();

        // check if GPS enabled
        if (gps.canGetLocation()) {

            try {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            }catch(NullPointerException e){
                SharedPreferences prefs = getActivity().getSharedPreferences("uMorning", 0);
                latitude = Double.parseDouble(prefs.getString("Latitude", "45.529"));
                longitude = Double.parseDouble(prefs.getString("Longitude", "9.0429"));
            }
            if (HttpRequest.isOnline(getActivity())) {
                new AsyncTaskMeteoRequest().execute(latitude,longitude);
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            // Chiedi all'utente di andare nelle impostazioni
            gps.showSettingsAlert();
        }

        DatabaseHelper db = new DatabaseHelper(getActivity()
                .getApplicationContext());
        alarms = new ArrayList<Alarm>();
        activatedAlarms = new ArrayList<Alarm>();
        alarms = db.getAllAlarms();

        //inserisco in activatedAlarms i primi 5 alarmi attivi di alarms
        for(Alarm a: alarms){
            if (a.isActivated()){
               activatedAlarms.add(a);
             }
            Collections.sort(activatedAlarms,new Comparator<Alarm>() {
                @Override
                public int compare(Alarm  a1, Alarm  a2)
                {
                    return  a1.getExpectedTime().compareTo(a2.getExpectedTime());
                }
            });
            activatedAlarms.subList(0,4);
        }

        list = (ListView) getView().findViewById(R.id.listEventsActivated);
        adapter = new AlarmsAdapter(getActivity(), activatedAlarms);
        list.setAdapter(adapter);

    }

    private class AsyncTaskMeteoRequest extends AsyncTask<Double, Void, MetwitRequest> {

        @Override
        protected void onPreExecute(){
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected MetwitRequest doInBackground(Double... params) {

            double latitude = params[0];
            double longitude = params[1];
            MetwitRequest weatherInfo;
            try {
                //richiesta meteo
                weatherInfo = new MetwitRequest(latitude, longitude);
            }catch(NullPointerException e ){
                SharedPreferences prefs = getActivity().getSharedPreferences("uMorning", 0);
                weatherInfo = new MetwitRequest();
                weatherInfo.setLocality(prefs.getString("Locality", " "));
                weatherInfo.setCountry(prefs.getString("Country", " "));
                weatherInfo.setTemperature(prefs.getString("Temperature", " "));
                weatherInfo.setIcon(prefs.getString("Icon", " "));
            }

            //restituisce oggetto meteo contenente informazioni
            return weatherInfo;
        }


        @Override
        protected void onPostExecute(MetwitRequest weatherInfo) {

            //salvo ultime informazioni
            SharedPreferences settings = getActivity().getSharedPreferences("uMorning", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("Locality", weatherInfo.getLocality());
            editor.putString("Country", weatherInfo.getCountry());
            editor.putString("Temperature", weatherInfo.getTemperature());
            editor.putString("Icon", weatherInfo.getIcon());
            editor.putString("Latitude", String.valueOf(latitude));
            editor.putString("Longitude", String.valueOf(longitude));
            editor.commit();
            progress.setVisibility(View.GONE);


            updateUI();
        }
    }

    private void updateUI() {
        SharedPreferences prefs = getActivity().getSharedPreferences("uMorning", 0);
        String loc = prefs.getString("Locality", " ");
        String cou = prefs.getString("Country", " ");
        String temp = prefs.getString("Temperature", " ");
        String icon = prefs.getString("Icon", " ");

        cou = "(" + cou + ")";
        temp = temp + "°C";

        //assegna variabili
        locality.setText(loc);
        country.setText(cou);
        temperature.setText(temp);

        //setta l'icona
        if (icon.equals("sunny")) {
            weatherIcon.setImageResource(R.drawable.sunny);
        } else if (icon.equals("clear_moon")) {
            weatherIcon.setImageResource(R.drawable.clear_moon);
        } else if (icon.equals("cloudy")) {
            weatherIcon.setImageResource(R.drawable.cloudy);
        } else if (icon.equals("foggy")) {
            weatherIcon.setImageResource(R.drawable.foggy);
        } else if (icon.equals("partly_moon")) {
            weatherIcon.setImageResource(R.drawable.partly_moon);
        } else if (icon.equals("partly_sunny")) {
            weatherIcon.setImageResource(R.drawable.partly_sunny);
        } else if (icon.equals("rainy")) {
            weatherIcon.setImageResource(R.drawable.rainy);
        } else if (icon.equals("snowy")) {
            weatherIcon.setImageResource(R.drawable.snowy);
        } else if (icon.equals("stormy")) {
            weatherIcon.setImageResource(R.drawable.stormy);
        } else {
            weatherIcon.setImageResource(R.drawable.windy);
        }
    }
}





















