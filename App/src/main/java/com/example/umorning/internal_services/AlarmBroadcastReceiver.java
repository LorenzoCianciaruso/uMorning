package com.example.umorning.internal_services;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.example.umorning.activities.AlarmRingActivity;


public class AlarmBroadcastReceiver extends Service {
    @Override
    public void onCreate() {

    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        int id = intent.getIntExtra("alarmId", 0);

        //riapri l'applicazione andando su alarm activity che suona e apre una finestra
        Intent ringIntent = new Intent(getBaseContext(), AlarmRingActivity.class);
        ringIntent.putExtra("alarmId",id);
        ringIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(ringIntent);

    }

    @Override
     public IBinder onBind(Intent intent) {
        Toast.makeText(this, "AlarmService.onBind()", Toast.LENGTH_LONG).show();
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "AlarmService.onDestroy()", Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Toast.makeText(this, "AlarmService.onUnbind()", Toast.LENGTH_LONG).show();
        return super.onUnbind(intent);
    }
}