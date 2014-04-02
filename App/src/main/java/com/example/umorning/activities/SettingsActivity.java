package com.example.umorning.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
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
        number.setValue(30);
    }

    public void saveSettings(View view){
        userDelay=number.getValue()*60*1000;
        // salva nelle preferenze
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this.getApplicationContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("DELAY", userDelay);
        editor.commit();
        System.out.println(userDelay);
    }
}
