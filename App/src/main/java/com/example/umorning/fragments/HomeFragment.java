package com.example.umorning.fragments;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.umorning.R;
import com.example.umorning.external_services.HttpRequest;
import com.example.umorning.external_services.MetwitRequest;
import com.example.umorning.internal_services.GpsLocalizationService;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class HomeFragment extends Fragment {

    private ImageView weatherIcon;
    private TextView country;
    private TextView locality;
    private TextView temperature;
    private ProgressBar progress;
    private double latitude;
    private double longitude;

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

            Toast.makeText(getActivity().getApplicationContext(), "Your Location is - \nLat: " + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();

            if (HttpRequest.isOnline(getActivity())) {
                new AsyncTaskMeteoRequest().execute(latitude,longitude);
            } else {
                Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }

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
        protected void onPreExecute(){
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected MetwitRequest doInBackground(Double... params) {

            double latitude = params[0];
            double longitude = params[1];
            MetwitRequest weatherInfo = null;

            //richiesta meteo
            weatherInfo = new MetwitRequest(latitude, longitude);

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
        } else  {
            weatherIcon.setImageResource(R.drawable.windy);
        }
    }


}





















