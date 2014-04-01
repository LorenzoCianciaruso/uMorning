package com.example.umorning.fragments;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umorning.R;
import com.example.umorning.external_services.GoogleTrafficRequest;
import com.example.umorning.external_services.MetwitRequest;
import com.example.umorning.internal_services.GpsLocalizationService;

import com.google.android.gms.common.GooglePlayServicesUtil;

public class HomeFragment extends Fragment {


    private ImageView weatherIcon;
    private TextView country;
    private TextView locality;
    private TextView temperature;

    private double latitude = 0;
    private double longitude = 0;

    private GpsLocalizationService gps;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        return rootView;
    }


    @Override
    public void onStart() {
        super.onStart();

        gps = new GpsLocalizationService(getActivity());

        //trova riferimenti layout
        locality = (TextView) getView().findViewById(R.id.locality);
        country = (TextView) getView().findViewById(R.id.country);
        temperature = (TextView) getView().findViewById(R.id.temperature);
        weatherIcon = (ImageView) getView().findViewById(R.id.weatherIcon);

        updateUI();


        // check if GPS enabled
        if (gps.canGetLocation()) {

            latitude = gps.getLatitude();
            longitude = gps.getLongitude();

            Toast.makeText(getActivity().getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            new AsyncTaskMeteoRequest().execute(latitude, longitude);
            new AsyncTaskTrafficRequest().execute(latitude, longitude, 45.0, 9.0);

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

            //richiesta meteo
            MetwitRequest weatherInfo = new MetwitRequest(latitude, longitude);

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
            editor.commit();

            updateUI();

        }
    }

    //TODO probabilmente levare il traffico nel main fragment non serve
    private class AsyncTaskTrafficRequest extends AsyncTask<Double, Void, GoogleTrafficRequest> {

        @Override
        protected GoogleTrafficRequest doInBackground(Double... params) {

            double startLatitude = params[0];
            double startLongitude = params[1];
            double endLatitude = params[2];
            double endLongitude = params[3];

            GoogleTrafficRequest trafficInfo = new GoogleTrafficRequest(startLatitude, startLongitude, endLatitude, endLongitude);

            return trafficInfo;
        }


        @Override
        protected void onPostExecute(GoogleTrafficRequest traffic) {

        }
    }

    private void updateUI() {

        SharedPreferences prefs = getActivity().getSharedPreferences("uMorning", 0);
        String loc = prefs.getString("Locality", " ");
        String cou = prefs.getString("Country", " ");
        String temp = prefs.getString("Temperature", " ");
        String icon = prefs.getString("Icon", " ");


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
        } else if (icon.equals("windy")) {
            weatherIcon.setImageResource(R.drawable.windy);
        }

    }

}





















