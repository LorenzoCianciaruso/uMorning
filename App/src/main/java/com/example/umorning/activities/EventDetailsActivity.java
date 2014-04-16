package com.example.umorning.activities;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ShareCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.example.umorning.R;
import com.example.umorning.external_services.Facebook;
import com.google.android.gms.maps.CameraUpdateFactory;
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
    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        name = getIntent().getStringExtra("name");
        latitude = getIntent().getDoubleExtra("latitude", 45.0);
        longitude = getIntent().getDoubleExtra("longitude", 9.0);
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

        setUpMapIfNeeded();

    }

    @Override
    protected void onResume() {
        super.onResume();
        //se esiste sessione attiva
        fb = new Facebook(this);
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
        if (!fb.isLogged()) {
            shareFb.setVisible(false);
            shareFb.setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.fb_share) {
            fb.publishStory(this, name, place, url, time);
        } else if (id == R.id.add) {


        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpMapIfNeeded() {

        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude,longitude), 15));
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(name));
        }
    }


}
