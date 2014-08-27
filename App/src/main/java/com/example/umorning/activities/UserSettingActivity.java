package com.example.umorning.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.RingtonePreference;
import android.view.MenuItem;

import com.example.umorning.R;
import com.example.umorning.model.Badge;
import com.example.umorning.model.DatabaseHelper;

public class UserSettingActivity extends PreferenceActivity {

    private Uri ringtoneUri;
    private Ringtone ringtone;
    RingtonePreference ringtonePicker;
    NumberPickerPreference refreshPicker;
    NumberPickerPreference delayPicker;
    private SharedPreferences sp;
    private DatabaseHelper db = new DatabaseHelper(this);

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setPreferenceScreen(null);
        addPreferencesFromResource(R.xml.settings);

        getActionBar().setDisplayHomeAsUpEnabled(true);
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setTitle("Settings");

        refreshPicker = (NumberPickerPreference) findPreference("refresh_picker");
        delayPicker = (NumberPickerPreference) findPreference("delay_picker");


        refreshPicker.setSummary("" + refreshPicker.getValue() + " minutes");
        delayPicker.setSummary("" + delayPicker.getValue() + " minutes");

        ringtonePicker = (RingtonePreference) findPreference("ringtone_picker");
        ringtonePicker.setShowDefault(true);

        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        ringtoneUri = Uri.parse(prefs.getString("TONE", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()));
        ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
        ringtonePicker.setSummary(ringtone.getTitle(this));

        sp = PreferenceManager.getDefaultSharedPreferences(this);
        ringtonePicker.getOnPreferenceClickListener();

        if (db.aquireBadge(Badge.SETTINGS)) {
            Badge badge = db.getBadge(Badge.SETTINGS);
            Intent myIntent = new Intent(this, BadgeAcquisitionActivity.class);
            myIntent.putExtra("badgeAquired", badge);
            startActivity(myIntent);
        }

        /*int titleId = Resources.getSystem().getIdentifier("action_bar_title", "id", "android");
        TextView yourTextView = (TextView) findViewById(titleId);
        yourTextView.setClickable(true);
        yourTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveAll();
                finish();
            }
        });*/

    }

    @Override
    protected void onResume() {
        super.onResume();
        //saveAll();
    }

       public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            saveAll();
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        saveAll();
    }

    private void saveAll() {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        ringtoneUri = Uri.parse(prefs.getString("ringtone_picker", "<unset>"));
        ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
        ringtonePicker.setSummary(ringtone.getTitle(this));

        prefs = getSharedPreferences("uMorning", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("DELAY", delayPicker.getValue());
        editor.putLong("REFRESH", refreshPicker.getValue());
        editor.putString("TONE", ringtoneUri.toString());
        editor.commit();

        if (delayPicker.getValue() <= 15) {
            if (db.aquireBadge(Badge.SHORT_PREP_TIME)) {
                Badge badge = db.getBadge(Badge.SHORT_PREP_TIME);
                Intent myIntent = new Intent(this, BadgeAcquisitionActivity.class);
                myIntent.putExtra("badgeAquired", badge);
                startActivity(myIntent);
            }
        }
        if (delayPicker.getValue() >= 120) {
            if (db.aquireBadge(Badge.LONG_PREP_TIME)) {
                Badge badge = db.getBadge(Badge.LONG_PREP_TIME);
                Intent myIntent = new Intent(this, BadgeAcquisitionActivity.class);
                myIntent.putExtra("badgeAquired", badge);
                startActivity(myIntent);
            }
        }

        if (db.aquireBadge(Badge.RINGTONE)) {
            Badge badge = db.getBadge(Badge.RINGTONE);
            Intent myIntent = new Intent(this, BadgeAcquisitionActivity.class);
            myIntent.putExtra("badgeAquired", badge);
            startActivity(myIntent);
        }
    }
}
