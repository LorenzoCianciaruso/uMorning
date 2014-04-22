package com.example.umorning.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.ContactsContract;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.example.umorning.R;
import com.example.umorning.model.Alarm;
import com.example.umorning.model.DatabaseHelper;

import java.text.SimpleDateFormat;

public class AlarmRingActivity extends Activity {

    private Vibrator v;
    private Ringtone r;
    private int id;
    private TextView nameView;
    private TextView addressView;
    private TextView timeView;
    private Alarm alarm;
    private DatabaseHelper dbHelp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getActionBar().hide();
        setContentView(R.layout.activity_alarm_ring);
        id = getIntent().getIntExtra("alarmId", 0);

        dbHelp = new DatabaseHelper(this);
        alarm = dbHelp.getAlarm(id);

        nameView = (TextView) findViewById(R.id.name);
        addressView = (TextView) findViewById(R.id.address);
        timeView = (TextView) findViewById(R.id.time);
        nameView.setText(alarm.getName());
        addressView.setText(alarm.getAddress());
        SimpleDateFormat df = new SimpleDateFormat("c d LLLL yyyy HH:mm");
        String time = df.format(alarm.getDate().getTime());
        timeView.setText(time);

        System.out.println("AAAAAAAAAAAA "+alarm.getAddress() + " " + time + " " + alarm.getName() );
        //servizio vibrazione
        v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // ogni elemento alterna vibrazione, pausa, vibrazione, pausa...
        long[] pattern = {0, 500, 110, 500, 110, 450, 110, 200, 110, 170, 40, 450, 110, 200, 110, 170, 40, 500};
        // '-1' vibra una volta '0' vibra all'infinito
        v.vibrate(pattern, 0);
        //servizio suoneria
        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        Uri tone = Uri.parse(prefs.getString("TONE", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()));
        r = RingtoneManager.getRingtone(getApplicationContext(), tone);
        r.play();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void stopAlarm(View view) {
        alarm.setActivated(false);
        dbHelp.updateAlarm(alarm);
        v.cancel();
        r.stop();
        finish();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}