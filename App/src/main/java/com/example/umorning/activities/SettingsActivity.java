package com.example.umorning.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import com.example.umorning.R;

public class SettingsActivity extends Activity {

    private NumberPicker delayPicker;
    private NumberPicker refreshPicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //delay
        delayPicker = (NumberPicker) findViewById(R.id.refreshPicker);
        delayPicker.setMaxValue(480);
        delayPicker.setMinValue(1);
        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        long saved = prefs.getLong("DELAY", 30);
        delayPicker.setValue((int) saved);

        //refresh rate
        refreshPicker = (NumberPicker) findViewById(R.id.numberPicker);
        refreshPicker.setMaxValue(180);
        refreshPicker.setMinValue(15);
        saved = prefs.getLong("REFRESH", 60);
        refreshPicker.setValue((int) saved);

        //ringtone
        RingtoneManager ringtoneMgr = new RingtoneManager(this);
        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM);
        Cursor alarmsCursor = ringtoneMgr.getCursor();
        int alarmsCount = alarmsCursor.getCount();
        if (alarmsCursor.moveToFirst()) {
            Uri[] alarms = new Uri[alarmsCount];
            while (!alarmsCursor.isAfterLast() && alarmsCursor.moveToNext()) {
                int currentPosition = alarmsCursor.getPosition();
                alarms[currentPosition] = ringtoneMgr.getRingtoneUri(currentPosition);
            }
            alarmsCursor.close();
        }
    }

    public void saveSettings(View view) {
        long userDelay = delayPicker.getValue();
        long refreshRate = refreshPicker.getValue();
        // salva nelle preferenze
        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("DELAY", userDelay);
        editor.putLong("REFRESH", refreshRate);
        editor.commit();
    }
}
