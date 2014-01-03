package com.example.app;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.IBinder;
import android.os.Vibrator;
import android.widget.Toast;

public class MyAlarmService extends Service {

    @Override
    public void onCreate() {

        //popup
        Toast.makeText(this, "Bregu ammazzati", Toast.LENGTH_LONG).show();
        //servizio vibrazione
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // inizia subito
        // ogni elemento alterna vibrazione, pausa, vibrazione, pausa...
        long[] pattern = {0,500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500};
        // Il '-1' vibra una volta
        // '0' vibra all'infinito
        v.vibrate(pattern, -1);
        //servizio suoneria
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
        //riapri il main
        //TODO aprire qualcosa che faccia snooze e stop
        Intent reopen = new Intent(this, MainActivity.class);
        startActivity(reopen);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        //Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }
}