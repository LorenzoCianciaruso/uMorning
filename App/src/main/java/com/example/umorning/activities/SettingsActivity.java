package com.example.umorning.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;

import com.example.umorning.R;

public class SettingsActivity extends Activity {

    long userDelay;
    private NumberPicker number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        number=(NumberPicker) findViewById(R.id.numberPicker);
        number.setMaxValue(480);
        number.setMinValue(1);
        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        long saved = prefs.getLong("DELAY",30);
        number.setValue((int) saved);
    }

    public void saveSettings(View view){
        userDelay=number.getValue();
        // salva nelle preferenze
        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("DELAY", userDelay);
        editor.commit();
        System.out.println(userDelay);
    }
}
