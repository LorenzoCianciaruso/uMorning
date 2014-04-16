package com.example.umorning.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.umorning.R;
import com.example.umorning.model.Alarm;
import com.example.umorning.model.DatabaseHelper;

public class AlarmRingActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);

        int id = getIntent().getIntExtra("alarmId", 0);

        DatabaseHelper db = new DatabaseHelper(this);
        Alarm alarm = db.getAlarm(id);

       // TextView text = (TextView)findViewById(R.id.text);

        //text.setText("alzati sta suonando la sveglia "+alarm.getName()+" con id "+ alarm.getId());

        //TODO mettere bottoni stop e snooze poi bisogna trovare il modo di fare dei metodi che lo facciano
        //servizio vibrazione
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // ogni elemento alterna vibrazione, pausa, vibrazione, pausa...
        long[] pattern = {0,500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500};
        // '-1' vibra una volta '0' vibra all'infinito
        v.vibrate(pattern, 0);
        //servizio suoneria
        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        Uri tone = Uri.parse( prefs.getString("TONE", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()));
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), tone);
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

}