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
// TODO Auto-generated method stub
        Toast.makeText(this, "Bregu ammazzati", Toast.LENGTH_LONG).show();
        // Get instance of Vibrator from current Context
        Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        // Start without a delay
        // Each element then alternates between vibrate, sleep, vibrate, sleep...
        long[] pattern = {0,500,110,500,110,450,110,200,110,170,40,450,110,200,110,170,40,500};
        // The '-1' here means to vibrate once
        // '0' would make the pattern vibrate indefinitely
        v.vibrate(pattern, -1);
        Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM);
        Ringtone r = RingtoneManager.getRingtone(getApplicationContext(), notification);
        r.play();
    }


    @Override
    public IBinder onBind(Intent intent) {
// TODO Auto-generated method stub
        Toast.makeText(this, "MyAlarmService.onBind()", Toast.LENGTH_LONG).show();
        return null;
    }


    @Override
    public void onDestroy() {
// TODO Auto-generated method stub
        super.onDestroy();
        Toast.makeText(this, "MyAlarmService.onDestroy()", Toast.LENGTH_LONG).show();
    }


    @Override
    public void onStart(Intent intent, int startId) {
// TODO Auto-generated method stub
        super.onStart(intent, startId);
        //Toast.makeText(this, "MyAlarmService.onStart()", Toast.LENGTH_LONG).show();
    }


    @Override
    public boolean onUnbind(Intent intent) {
// TODO Auto-generated method stub
        Toast.makeText(this, "MyAlarmService.onUnbind()", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }
}