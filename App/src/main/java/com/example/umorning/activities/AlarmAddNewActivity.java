package com.example.umorning.activities;


import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.TimePicker;

import com.example.umorning.R;

public class AlarmAddNewActivity extends Activity {
    TimePicker timepicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_alarm);
        timepicker = (TimePicker) findViewById(R.id.timePicker1);
        timepicker.setIs24HourView(true);

        //TODO il codice per creare la sveglia lo trovi in alarm fragment ma va messo qui
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.add_new_alarm, menu);
        return true;
    }
    public void saveAlarm(View view) {
        // Do something in response to button
		/*Intent myIntent = new Intent(MainActivity.this, AlarmAddNewActivity.class);
		startActivity(myIntent);*/
        //Intent myIntent = new Intent(AlarmAddNewActivity.this, MainActivity.class);
        //startActivity(myIntent);
        //myIntent.putExtra("index", 2);
        //startActivity(myIntent);
        //overridePendingTransition(R.anim.fadeout, R.anim.fadein);
       finish();
       overridePendingTransition(R.anim.fadein,R.anim.fadeout);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
    }
}