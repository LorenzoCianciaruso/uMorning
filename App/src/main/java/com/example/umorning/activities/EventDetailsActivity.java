package com.example.umorning.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ShareCompat;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.TextView;

import com.example.umorning.R;
import com.example.umorning.external_services.Facebook;
import com.example.umorning.model.Alarm;
import com.example.umorning.model.Badge;
import com.example.umorning.model.DatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

public class EventDetailsActivity extends FragmentActivity {

    private GoogleMap mMap;
    private String url;
    private String name;
    private Calendar date;
    private String time;
    private String place;
    private Facebook fb;
    private ShareActionProvider mShareActionProvider;
    private double latitude;
    private double longitude;
    private DatabaseHelper db;
    private int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        name = getIntent().getStringExtra("name");
        latitude = getIntent().getDoubleExtra("latitude", 45.0);
        longitude = getIntent().getDoubleExtra("longitude", 9.0);
        place = getIntent().getStringExtra("place");
        url = getIntent().getStringExtra("url");
        long dateInMillis = getIntent().getLongExtra("dateInMillis", 0000);
        String organizer = getIntent().getStringExtra("organizer");

        TextView nameView = (TextView) findViewById(R.id.event_name);
        TextView dateTimeView = (TextView) findViewById(R.id.date_time);
        TextView placeView = (TextView) findViewById(R.id.place);
        TextView urlView = (TextView) findViewById(R.id.url);
        TextView organizerView = (TextView) findViewById(R.id.organizer);

        date = new GregorianCalendar();
        date.setTimeInMillis(dateInMillis);
        SimpleDateFormat df = new SimpleDateFormat("c d LLLL yyyy HH:mm");
        time = df.format(date.getTime());


        if (!name.equals("")) {
            nameView.setText(name);
        } else {
            nameView.setHeight(0);
        }

        if (!place.equals("")) {
            placeView.setText(place);
        } else {
            placeView.setHeight(0);
        }

        if (url!=null) {
            urlView.setText(url);
        } else {
            urlView.setHeight(0);
        }

        if (!organizer.equals("")) {
            organizerView.setText(organizer);
        } else {
            organizerView.setHeight(0);
        }

        dateTimeView.setText(time);

        db = new DatabaseHelper(this);

        String text = "<a href=" + url + " \">Link to the event</a>";
        urlView.setMovementMethod(LinkMovementMethod.getInstance());
        urlView.setText(Html.fromHtml(text));

    }

    @Override
    protected void onResume() {
        super.onResume();
        //se esiste sessione attiva
        fb = new Facebook(this);
        setUpMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.event_details, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain").setText("I'm using uMorning! Try it! I activated an alarm for " + name + " on" + time + " at " + place + "!! Don't arrive late to your appointments, be smart!").getIntent();
        mShareActionProvider.setShareIntent(shareIntent);
        MenuItem shareFb = menu.findItem(R.id.fb_share);
        if (!fb.isLogged()) {
            shareFb.setVisible(false);
            shareFb.setEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int idItem = item.getItemId();
        if (idItem == R.id.fb_share) {
            fb.publishStory(this, name, place, url, time);
            if(db.aquireBadge(Badge.SHARE)){
                Badge badge = db.getBadge(Badge.SHARE);
                Intent myIntent = new Intent(this, BadgeAcquisitionActivity.class);
                myIntent.putExtra("badgeAquired", badge);
                startActivity(myIntent);
            }
        } else if (idItem == R.id.add) {
            //salva nel db
            SharedPreferences prefs = getSharedPreferences("uMorning", 0);
            long delay = prefs.getLong("DELAY", 30);
            Alarm alarm = new Alarm(id, delay, name, place, "", "", 0, 0, latitude, longitude, date, date, true);
            db = new DatabaseHelper(this);
            id = (int) db.addAlarm(alarm);

            //controllo se ha acquisito un badge
            switch(id){
                case 1: if(db.aquireBadge(Badge.FIRST_ALARM)){
                    Badge badge = db.getBadge(Badge.FIRST_ALARM);
                    Intent myIntent = new Intent(this, BadgeAcquisitionActivity.class);
                    myIntent.putExtra("badgeAquired", badge);
                    startActivity(myIntent);
                }
                        break;
                case 5: if(db.aquireBadge(Badge.FIVE_ALARMS)){
                    Badge badge = db.getBadge(Badge.FIVE_ALARMS);
                    Intent myIntent = new Intent(this, BadgeAcquisitionActivity.class);
                    myIntent.putExtra("badgeAquired", badge);
                    startActivity(myIntent);
                }
                        break;
                case 100: if(db.aquireBadge(Badge.HUNDRED_ALARMS)){
                    Badge badge = db.getBadge(Badge.HUNDRED_ALARMS);
                    Intent myIntent = new Intent(this, BadgeAcquisitionActivity.class);
                    myIntent.putExtra("badgeAquired", badge);
                    startActivity(myIntent);
                }
            }

            Intent myIntent = new Intent(this, AlarmEditActivity.class);
            myIntent.putExtra("alarmId", id);
            myIntent.putExtra("fromEvent", 1);
            startActivityForResult(myIntent, 0);
        }
        return super.onOptionsItemSelected(item);
    }

    private void setUpMap() {
        if (mMap == null)
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
        if (mMap != null) {
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15));
            mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title(name));
        }
        mMap.setMyLocationEnabled(true);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        if (getIntent().getIntExtra("fromEvent", 0) == 1) {
            db.deleteAlarm(id);
        }
        finish();
    }

}
