package com.example.umorning.activities;

import android.app.Activity;
import android.os.Bundle;

public class SettingsActivity extends Activity {
    Long userDelay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //TODO fare la grafica
        //setContentView(R.layout.activity_account_manager);

    }

    public Long getUserDelay() {
        return userDelay;
    }

}
