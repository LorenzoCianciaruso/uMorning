package com.example.umorning.internal_services;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.widget.Toast;

import com.example.umorning.external_services.GoogleTrafficRequest;
import com.example.umorning.model.Alarm;
import com.example.umorning.model.DatabaseHelper;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


public class UpdateAlarmService extends Service {

    @Override
    public void onCreate() {
    }

    @Override
    public void onStart (Intent receivedIntent, int boh) {
        super.onStart(receivedIntent, boh);
        System.out.println ("BLIBBuferuyerfiusef");
        //apri l'applicazione e aggiorna le sveglie
        GpsLocalizationService gps = new GpsLocalizationService(this);
        DatabaseHelper db = new DatabaseHelper(getApplicationContext());
        List<Alarm> alarms = db.getAllAlarms();

        for (Alarm a : alarms) {
            if (a.isToDelete()) {
                db.deleteAlarm(a.getId());
            } else if (gps.canGetLocation && a.isActivated()) {
                //copio l'allarme in locale
                int id = a.getId();
                long delay = a.getDelay();
                String name = a.getName();
                String address = a.getAddress();
                String city = a.getCity();
                String country = a.getCountry();
                double startLatitude = a.getStartLatitude();
                double startLongitude = a.getStartLongitude();
                double endLatitude = a.getEndLatitude();
                double endLongitude = a.getEndLongitude();
                Calendar date = a.getDate();
                boolean activated = a.isActivated();
                boolean toDelete = a.isToDelete();

                //aggiornamento
                //richiesta traffico
                GoogleTrafficRequest trafficRequest = new GoogleTrafficRequest(startLatitude, startLongitude, endLatitude, endLongitude);
                long trafficMillis = trafficRequest.getTripDurationInMillis();

                //ottengo l'ora della sveglia sottraendo traffico e tempo per prepararsi
                Calendar expectedTime = new GregorianCalendar();
                expectedTime.setTimeInMillis(date.getTimeInMillis() - trafficMillis - (delay * 1000 * 60));

                //considera il refresh rate
                SharedPreferences prefs = getSharedPreferences("uMorning", 0);
                long refreshRate = prefs.getLong("REFRESH", 60);
                //metti la sveglia se è in questo segmento temporale
                if (expectedTime.getTimeInMillis() < (System.currentTimeMillis() + (refreshRate * 60 * 1000)) && activated) {
                    //chiama un alarmservice
                    Intent myIntent = new Intent(this, AlarmBroadcastReceiver.class);
                    myIntent.putExtra("alarmId", id);
                    PendingIntent intent = PendingIntent.getService(this, 0, myIntent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
                    //imposta l'ora e fa partire
                    alarmManager.set(AlarmManager.RTC_WAKEUP, expectedTime.getTimeInMillis(), intent);
                }
                //aggiorna l'allarme e lo salva nel DB
                Alarm updated = new Alarm(id, delay, name, address, city, country, startLatitude, startLongitude, endLatitude, endLongitude, date, expectedTime, activated, toDelete);
                db.updateAlarm(updated);
            }
        }
        //chiama un nuovo update al tempo impostato dall'utente
        Intent updateIntent = new Intent(getApplicationContext(), UpdateAlarmService.class);
        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        long refreshRate = prefs.getLong("REFRESH", 60);
        PendingIntent intent = PendingIntent.getService(this, 0, updateIntent, 0);
        AlarmManager alarmManager = (AlarmManager) getSystemService(Service.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, (System.currentTimeMillis() + (refreshRate * 60 * 1000)), intent);

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
