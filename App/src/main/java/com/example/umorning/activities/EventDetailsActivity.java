package com.example.umorning.activities;

import android.app.FragmentTransaction;
import android.content.ClipData;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ShareCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.example.umorning.R;
import com.example.umorning.external_services.Facebook;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class EventDetailsActivity extends FragmentActivity {

    private GoogleMap mMap;
    private TextView nameView;
    private TextView dateTimeView;
    private TextView placeView;
    private TextView urlView;
    private TextView organizerView;
    private MenuItem shareFb;
    private String url;
    private String name;
    private String time;
    private String place;
    private Facebook fb;
    private ShareActionProvider mShareActionProvider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        name = getIntent().getStringExtra("name");
        double latitude = getIntent().getDoubleExtra("latitude", 45.0);
        double longitude = getIntent().getDoubleExtra("longitude", 9.0);
        place = getIntent().getStringExtra("place");
        url = getIntent().getStringExtra("url");
        time = getIntent().getStringExtra("date");
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

        if (url != null) {
            urlView.setText(url);
        } else {
            urlView.setVisibility(View.INVISIBLE);
        }

        String text = "<a href=" + url + " \">Link to the event</a>";
        urlView.setMovementMethod(LinkMovementMethod.getInstance());

        urlView.setText(Html.fromHtml(text));
        organizerView.setText(organizer);

        GoogleMapOptions options = new GoogleMapOptions();
        CameraPosition cp = new CameraPosition(new LatLng(latitude, longitude), 15, 0, 0);
        options.zoomControlsEnabled(false).camera(cp);
        mMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .visible(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        MapFragment mMapFragment = MapFragment.newInstance(options);

        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.map, mMapFragment);
        fragmentTransaction.commit();

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();

        fb = new Facebook(this);
        //se esiste sessione attiva
        if (!fb.isLogged()) {
            shareFb.setVisible(false);
            shareFb.setEnabled(false);
        }
        setUpMapIfNeeded();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.event_details, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();

        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain").setText("").getIntent();
        mShareActionProvider.setShareIntent(shareIntent);
        shareFb = menu.findItem(R.id.fb_share);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == R.id.fb_share) {
            fb.publishStory(this, name, place, url, time);
        } else if (id == R.id.add) {
            //TODO add new alarm
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
