package com.example.umorning.activities;

import android.content.Context;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Vibrator;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.umorning.R;

public class AlarmRingActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_ring);

           //TODO mettere bottoni stop e snooze poi bisogna trovare il modo di fare dei metodi che lo facciano

        //servizio vibrazione
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // ogni elemento alterna vibrazione, pausa, vibrazione, pausa...
        long[] pattern = {0,500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500};
        // Il '-1' vibra una volta
        // '0' vibra all'infinito
        v.vibrate(pattern, 0);
        //servizio suoneria
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.prova, menu);
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

}
