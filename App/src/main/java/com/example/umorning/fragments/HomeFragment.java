package com.example.umorning.fragments;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.example.umorning.R;
import com.example.umorning.activities.AccountManagerActivity;
import com.example.umorning.activities.AlarmDetailsActivity;
import com.example.umorning.activities.UserSettingActivity;
import com.example.umorning.external_services.HttpRequests;
import com.example.umorning.external_services.Metwit;
import com.example.umorning.internal_services.GpsLocalization;
import com.example.umorning.model.Alarm;
import com.example.umorning.model.DatabaseHelper;
import com.example.umorning.model.Metag;
import com.example.umorning.model.MetagsEnum;
import com.example.umorning.model.WeatherForecasts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeFragment extends Fragment {

    private ImageView weatherIcon;
    private TextView country;
    private TextView locality;
    private TextView temperature;
    private ProgressBar progress;
    private double latitude;
    private double longitude;
    private AsyncTaskMeteoRequest metwitRequest;
    private ListView list;
    private AlarmsAdapter adapter;
    private List<Alarm> alarms;
    private List<Alarm> activatedAlarms;
    private GpsLocalization gps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }


    @Override
    public void onStart() {
        super.onStart();
        gps = new GpsLocalization(getActivity());

        //trova riferimenti layout
        locality = (TextView) getView().findViewById(R.id.locality);
        country = (TextView) getView().findViewById(R.id.country);
        temperature = (TextView) getView().findViewById(R.id.temperature);
        weatherIcon = (ImageView) getView().findViewById(R.id.weatherIcon);
        progress = (ProgressBar) getView().findViewById(R.id.pbHeaderProgress);
        progress.setVisibility(View.INVISIBLE);
        updateUI();

        startMetwitRequest();

        DatabaseHelper db = new DatabaseHelper(getActivity()
                .getApplicationContext());
        alarms = new ArrayList<Alarm>();
        activatedAlarms = new ArrayList<Alarm>();
        alarms = db.getAllAlarms();

        //inserisco in activatedAlarms i primi 5 alarmi attivi di alarms
        for (Alarm a : alarms) {
            if (a.isActivated()) {
                activatedAlarms.add(a);
            }
            Collections.sort(activatedAlarms, new Comparator<Alarm>() {
                @Override
                public int compare(Alarm a1, Alarm a2) {
                    return a1.getExpectedTime().compareTo(a2.getExpectedTime());
                }
            });
        }

        if (activatedAlarms.size() > 5) {
            activatedAlarms = activatedAlarms.subList(0, 5);
        }

        list = (ListView) getView().findViewById(R.id.listEventsActivated);
        adapter = new AlarmsAdapter(getActivity(), activatedAlarms);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {

                Intent myIntent = new Intent(getActivity(), AlarmDetailsActivity.class);
                myIntent.putExtra("alarmId", activatedAlarms.get(i).getId());
                startActivityForResult(myIntent, 0);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (metwitRequest != null) {
            if (!metwitRequest.isCancelled()) {
                metwitRequest.cancel(true);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh: {
                startMetwitRequest();
                break;
            }
            case R.id.action_accounts: {
                Intent i = new Intent(getActivity(), AccountManagerActivity.class);
                startActivity(i);
                break;
            }
            case R.id.menu_settings: {
                Intent i = new Intent(getActivity(), UserSettingActivity.class);
                startActivityForResult(i, 1);
                break;
            }
            case R.id.post_metag: {
                //TODO postmetag
               // Intent i = new Intent(getActivity(), PostMetagActivity.class);
               // startActivityForResult(i, 1);
                break;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    private void startMetwitRequest() {
        // check if GPS enabled
        if (gps.canGetLocation()) {
            try {
                latitude = gps.getLatitude();
                longitude = gps.getLongitude();
            } catch (NullPointerException e) {
                SharedPreferences prefs = getActivity().getSharedPreferences("uMorning", 0);
                latitude = Double.parseDouble(prefs.getString("Latitude", "45.529"));
                longitude = Double.parseDouble(prefs.getString("Longitude", "9.0429"));
            }
            if (HttpRequests.isOnline(getActivity())) {
                metwitRequest = new AsyncTaskMeteoRequest();
                metwitRequest.execute(latitude, longitude);
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

        } else {
            // Chiedi all'utente di andare nelle impostazioni
            gps.showSettingsAlert();
        }
    }

    private class AsyncTaskMeteoRequest extends AsyncTask<Double, Void, WeatherForecasts> {

        @Override
        protected void onPreExecute() {
            progress.bringToFront();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected WeatherForecasts doInBackground(Double... params) {

            double latitude = params[0];
            double longitude = params[1];
            Metwit metwitManager;
            WeatherForecasts weather;
            try {
                //richiesta meteo
                metwitManager = new Metwit(latitude, longitude);
                weather = metwitManager.askForWeather();

            } catch (NullPointerException e) {
                SharedPreferences prefs = getActivity().getSharedPreferences("uMorning", 0);
                //metwitManager = new Metwit(prefs.getString("Icon", " "), prefs.getString("Temperature", " "), prefs.getString("Locality", " "), prefs.getString("Country", " "));
                weather = new WeatherForecasts(latitude,longitude,prefs.getString("Icon", " "), prefs.getString("Temperature", " "), prefs.getString("Locality", " "), prefs.getString("Country", " "));
            }

            //restituisce oggetto meteo contenente informazioni
            return weather;
        }


        @Override
        protected void onPostExecute(WeatherForecasts weather) {

            //salvo ultime informazioni
            SharedPreferences settings = getActivity().getSharedPreferences("uMorning", 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString("Locality", weather.getLocality());
            editor.putString("Country", weather.getCountry());
            editor.putString("Temperature", weather.getTemperature());
            editor.putString("Icon", weather.getIcon());
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
        //TODO da aggiornare con i nuovi nomi
        if (icon.equals(MetagsEnum.SUNNY.getValue())) {
            weatherIcon.setImageResource(R.drawable.sunny);
        } else if (icon.equals(MetagsEnum.CLEAR_MOON.getValue())) {
            weatherIcon.setImageResource(R.drawable.clear_moon);
        } else if (icon.equals(MetagsEnum.CLOUDY.getValue())) {
            weatherIcon.setImageResource(R.drawable.cloudy);
        } else if (icon.equals(MetagsEnum.FOGGY.getValue())) {
            weatherIcon.setImageResource(R.drawable.foggy);
        } else if (icon.equals(MetagsEnum.PARTLY_MOON.getValue())) {
            weatherIcon.setImageResource(R.drawable.partly_moon);
        } else if (icon.equals(MetagsEnum.PARTLY_SUNNY.getValue())) {
            weatherIcon.setImageResource(R.drawable.partly_sunny);
        } else if (icon.equals(MetagsEnum.RAINY.getValue())) {
            weatherIcon.setImageResource(R.drawable.rainy);
        } else if (icon.equals(MetagsEnum.SNOWY.getValue())) {
            weatherIcon.setImageResource(R.drawable.snowy);
        } else if (icon.equals(MetagsEnum.STORMY.getValue())) {
            weatherIcon.setImageResource(R.drawable.stormy);
        } else {
            weatherIcon.setImageResource(R.drawable.windy);
        }
    }
}








