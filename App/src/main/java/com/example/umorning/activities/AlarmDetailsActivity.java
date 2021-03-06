package com.example.umorning.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ShareCompat;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ShareActionProvider;
import android.widget.TextView;
import com.example.umorning.R;
import com.example.umorning.external_services.Facebook;
import com.example.umorning.model.Alarm;
import com.example.umorning.model.DatabaseHelper;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.text.SimpleDateFormat;

public class AlarmDetailsActivity extends FragmentActivity {

    private int id;
    private int toDeleteId;
    private GoogleMap mMap;
    private Facebook fb;
    private ShareActionProvider mShareActionProvider;
    private double latitude;
    private double longitude;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.alarm_details);

        id = getIntent().getIntExtra("alarmId", 0);
        toDeleteId=id;
        db = new DatabaseHelper(this);

        Alarm current = db.getAlarm(id);

        TextView nameView = (TextView) findViewById(R.id.event_name);
        TextView dateTimeView = (TextView) findViewById(R.id.date_time);
        TextView placeView = (TextView) findViewById(R.id.place);

        if (!current.getName().equals("")) {
            nameView.setText(current.getName());
        } else {
            nameView.setHeight(0);
        }

        if (!current.getAddress().equals("")) {
            placeView.setText(current.getAddress());
        } else {
            placeView.setHeight(0);
        }

        SimpleDateFormat df = new SimpleDateFormat("c d LLLL yyyy HH:mm");
        String formattedDate = df.format(current.getDate().getTime());
        dateTimeView.setText(formattedDate);

        latitude = current.getEndLatitude();
        longitude = current.getEndLongitude();

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
        getMenuInflater().inflate(R.menu.alarm_details, menu);
        MenuItem item = menu.findItem(R.id.menu_item_share);
        mShareActionProvider = (ShareActionProvider) item.getActionProvider();
        Intent shareIntent = ShareCompat.IntentBuilder.from(this)
                .setType("text/plain").setText("").getIntent();
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

        switch (item.getItemId()) {
            case R.id.edit: {
                Intent myIntent = new Intent(this, AlarmEditActivity.class);
                myIntent.putExtra("alarmId", id);
                startActivityForResult(myIntent, 0);
                finish();
                break;
            }

            case R.id.delete:{
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

                // set title
                alertDialogBuilder.setTitle("Delete Alarm");

                // set dialog message
                alertDialogBuilder
                        .setMessage("Click yes to delete alarm!")
                        .setCancelable(false)
                        .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                //Alarm toDelete = db.getAlarm(id);
                                db.deleteAlarm(toDeleteId);
                                finish();
                                dialog.cancel();
                            }
                        })
                        .setNegativeButton("No",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                dialog.cancel();
                            }
                        });

                // create alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();

                // show it
                alertDialog.show();
            }

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
            );
        }

        mMap.getUiSettings().setCompassEnabled(true);
        mMap.setMyLocationEnabled(true);

    }

}
