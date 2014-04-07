package com.example.umorning.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.umorning.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

public class EventDetailsActivity extends FragmentActivity {

    private GoogleMap mMap;
    private TextView eventName;
    private TextView dateTime;
    private TextView place;
    private TextView url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        eventName = (TextView) findViewById(R.id.event_name);
        dateTime = (TextView) findViewById(R.id.date_time);
        place = (TextView) findViewById(R.id.place);
        url = (TextView) findViewById(R.id.url);
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        GoogleMapOptions options = new GoogleMapOptions();
        CameraPosition cp = new CameraPosition(new LatLng(45.0363, 9.35643),15,0,0);


        //TODO prendere coordinate eventoe settarle nella mappa
        options.zoomControlsEnabled(false).camera(cp);

        MapFragment mMapFragment = MapFragment.newInstance(options);
        FragmentTransaction fragmentTransaction =
                getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();

        setUpMapIfNeeded();

    }

    @Override
    protected void onResume(){
        super.onResume();
        setUpMapIfNeeded();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.event_details, menu);
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

    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                // The Map is verified. It is now safe to manipulate the map.

            }
        }

    }
}
