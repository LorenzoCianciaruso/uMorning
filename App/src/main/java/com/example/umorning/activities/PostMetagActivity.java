package com.example.umorning.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.umorning.R;
import com.example.umorning.external_services.HttpRequests;
import com.example.umorning.external_services.Metwit;
import com.example.umorning.internal_services.GpsLocalization;
import com.example.umorning.model.Metag;
import com.example.umorning.model.MetagsEnum;

public class PostMetagActivity extends Activity {

    private ImageButton clearButton;
    private ImageButton partlyCloudyButton;
    private ImageButton cloudyButton;
    private ImageButton rainyButton;
    private ImageButton stormyButton;
    private ImageButton windyButton;
    private ImageButton snowyButton;
    private ImageButton snowflurriesButton;
    private ImageButton hailingButton;
    private ImageButton foggyButton;
    private ImageButton heavySeasButton;
    private ImageButton calmSeasButton;
    private GpsLocalization gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_metag);

        clearButton = (ImageButton) findViewById(R.id.clear);
        partlyCloudyButton = (ImageButton) findViewById(R.id.partlyCloudy);
        cloudyButton = (ImageButton) findViewById(R.id.cloudy);
        rainyButton = (ImageButton) findViewById(R.id.rainy);
        stormyButton = (ImageButton) findViewById(R.id.stormy);
        windyButton = (ImageButton) findViewById(R.id.windy);
        snowyButton = (ImageButton) findViewById(R.id.snowy);
        snowflurriesButton = (ImageButton) findViewById(R.id.snowflurries);
        hailingButton = (ImageButton) findViewById(R.id.hailing);
        foggyButton = (ImageButton) findViewById(R.id.foggy);
        heavySeasButton = (ImageButton) findViewById(R.id.heavyseas);
        calmSeasButton = (ImageButton) findViewById(R.id.calmseas);

        gps = new GpsLocalization(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.post_metag, menu);
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

    public void postMetag(View v) {
        double lat;
        double lon;

        if (gps.canGetLocation()) {
            try {
                lat = gps.getLatitude();
                lon = gps.getLongitude();
                Metwit metwit = new Metwit(lat, lon);
                Metag metag = new Metag();

                if (HttpRequests.isOnline(this)) {
                    MetagsEnum metagEnum = getMetag(v);
                    if (metagEnum != null) {
                        metag.setWeather(metagEnum);
                        metwit.postMetag(metag);
                        Toast.makeText(this, "Weather posted", Toast.LENGTH_LONG).show();
                        finish();
                    }
                } else {
                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_LONG).show();
                }
            } catch (NullPointerException e) {
                Toast.makeText(this, "Error in getting the position", Toast.LENGTH_LONG).show();
            }
        } else {
            // Chiedi all'utente di andare nelle impostazioni
            gps.showSettingsAlert();
        }

    }

    /**
     * restituisce enum relativa al bottone premuto
     * @param v view di riferimento
     * @return enum relativa al meteo scelto
     */
    private MetagsEnum getMetag(View v) {
        switch (v.getId()) {
            case R.id.clear:
                return MetagsEnum.CLEAR;
            case R.id.partlyCloudy:
                return MetagsEnum.PARTLYCLOUD;
            case R.id.cloudy:
                return MetagsEnum.CLOUDY;
            case R.id.rainy:
                return MetagsEnum.RAINY;
            case R.id.stormy:
                return MetagsEnum.STORMY;
            case R.id.windy:
                return MetagsEnum.WINDY;
            case R.id.snowy:
                return MetagsEnum.SNOWY;
            case R.id.snowflurries:
                return MetagsEnum.SNOWFLURRIES;
            case R.id.hailing:
                return MetagsEnum.HAILING;
            case R.id.foggy:
                return MetagsEnum.FOGGY;
            case R.id.calmseas:
                return MetagsEnum.CALMSEAS;
            case R.id.heavyseas:
                return MetagsEnum.HEAVYSEAS;

            default:
                return null;

        }


    }

}
