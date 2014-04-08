package com.example.umorning.activities;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.umorning.R;
import com.example.umorning.model.Event;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class EventDetailsActivity extends FragmentActivity {

    private GoogleMap mMap;
    private TextView nameView;
    private TextView dateTimeView;
    private TextView placeView;
    private TextView urlView;
    private TextView organizerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        String name = getIntent().getStringExtra("name");
        double latitude = getIntent().getDoubleExtra("latitude", 45.0);
        double longitude = getIntent().getDoubleExtra("longitude", 45.0);
        String place = getIntent().getStringExtra("place");
        String url = getIntent().getStringExtra("url");
        String time = getIntent().getStringExtra("date");
        String organizer = getIntent().getStringExtra("organizer");

        nameView = (TextView) findViewById(R.id.event_name);
        dateTimeView = (TextView) findViewById(R.id.date_time);
        placeView = (TextView) findViewById(R.id.place);
        urlView = (TextView) findViewById(R.id.url);
        organizerView = (TextView) findViewById(R.id.organizer);
        mMap = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        nameView.setText(name);
        dateTimeView.setText(time);
        placeView.setText(place);
        urlView.setText(url);


        String text = "<a href=" +url+ " \">Link to the event</a>";
        urlView.setMovementMethod(LinkMovementMethod.getInstance());
        urlView.setText(Html.fromHtml(text));

        organizerView.setText(organizer);

        GoogleMapOptions options = new GoogleMapOptions();
        mMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longitude)).title(place));
        CameraPosition cp = new CameraPosition(new LatLng(latitude, longitude), 15, 0, 0);

        //TODO prendere coordinate evento e settarle nella mappa
        options.zoomControlsEnabled(false).camera(cp);

        MapFragment mMapFragment = MapFragment.newInstance(options);
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();

        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
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
