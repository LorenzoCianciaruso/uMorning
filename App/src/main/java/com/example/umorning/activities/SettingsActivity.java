package com.example.umorning.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.umorning.R;

import java.util.List;

public class SettingsActivity extends Activity {

    private NumberPicker delayPicker;
    private NumberPicker refreshPicker;
    //ringtone
    private Uri ringtoneUri;
    private Ringtone ringtone;

    private TextView selectedRingtone;

    private List<String> ringtones;
    private List<Uri> ringtonesListUri;
    String ringtonePicked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        //delay
        delayPicker = (NumberPicker) findViewById(R.id.delayPicker);
        delayPicker.setMaxValue(480);
        delayPicker.setMinValue(1);
        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        long saved = prefs.getLong("DELAY", 30);
        delayPicker.setValue((int) saved);

        //refresh rate
        refreshPicker = (NumberPicker) findViewById(R.id.refreshPicker);
        refreshPicker.setMaxValue(180);
        refreshPicker.setMinValue(15);
        saved = prefs.getLong("REFRESH", 60);
        refreshPicker.setValue((int) saved);

        selectedRingtone = (TextView) findViewById(R.id.selectedRingtone);
        selectedRingtone.setClickable(true);
        ringtoneUri = Uri.parse(prefs.getString("TONE", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()));
        ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
        selectedRingtone.setText(ringtone.getTitle(this));

    }

    public void saveSettings(View view) {
        long userDelay = delayPicker.getValue();
        long refreshRate = refreshPicker.getValue();

        // salva nelle preferenze
        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("DELAY", userDelay);
        editor.putLong("REFRESH", refreshRate);
        editor.putString("TONE", ringtoneUri.toString());
        editor.commit();
        finish();
    }

    public void selectRingtone(View view) {
        String uri = null;
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        //intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
        // RingtoneManager.TYPE_ALARM);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");

        SharedPreferences settings = getSharedPreferences("uMorning", 0);
        uri = settings.getString("ringtone", "none");

        if (!uri.equals("none")) {
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
                    Uri.parse(uri));
            System.out.println();
        } else {
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
                    (Uri) null);
        }
        startActivityForResult(intent, 999);


    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {

        try {
            ringtoneUri = data.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
        } catch (NullPointerException e) {
            SharedPreferences prefs = getSharedPreferences("uMorning", 0);
            ringtoneUri = Uri.parse(prefs.getString("TONE", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()));
        }

        ringtone = RingtoneManager.getRingtone(this, ringtoneUri);
        selectedRingtone.setText(ringtone.getTitle(this));

        // salva nelle preferenze
        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("TONE", ringtoneUri.toString());
        editor.commit();

    }

}
