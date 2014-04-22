package com.example.umorning.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.example.umorning.R;

import java.util.ArrayList;
import java.util.List;

public class SettingsActivity extends Activity {

    private NumberPicker delayPicker;
    private NumberPicker refreshPicker;

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

        /*
        //ringtone
        ringtonesListUri = new ArrayList<Uri>();
        ringtones = new ArrayList<String>();
        selectedRingtone = (TextView) findViewById(R.id.selectedRingtone);
        RingtoneManager ringtoneMgr = new RingtoneManager(this);
        ringtoneMgr.setType(RingtoneManager.TYPE_ALARM);

        Uri uriDefault = Uri.parse( prefs.getString("TONE", RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM).toString()));
        Ringtone defaultRingtone = RingtoneManager.getRingtone(this, uriDefault);
        String ringtoneName = defaultRingtone.getTitle(this);
        selectedRingtone.setText(ringtoneName);
        selectedRingtone.setClickable(true);

        Cursor alarmsCursor = ringtoneMgr.getCursor();
        int alarmsCount = alarmsCursor.getCount();
        if (alarmsCursor.moveToFirst()) {
            Uri[] alarms = new Uri[alarmsCount];
            while (!alarmsCursor.isAfterLast() && alarmsCursor.moveToNext()) {
                int i = alarmsCursor.getPosition();
                alarms[i] = ringtoneMgr.getRingtoneUri(i);
                ringtonesListUri.add(alarms[i]);
                Ringtone ringtone = RingtoneManager.getRingtone(this, alarms[i]);
                ringtones.add(ringtone.getTitle(this));
            }
            alarmsCursor.close();

        }

    }

    public class SelectRingtone extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            CharSequence[] cs = ringtones.toArray(new CharSequence[ringtones.size()]);
            builder.setTitle("Select Ringtone").setItems(cs, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ringtonePicked = ringtones.get(which);
                    selectedRingtone.setText(ringtonePicked);
                }
            });
            return builder.create();
        }
    }*/

        String uri = null;
        Intent intent = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TYPE,
                RingtoneManager.TYPE_NOTIFICATION);
        intent.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE, "Select Tone");

        SharedPreferences settings = getSharedPreferences("uMorning", 0);
        uri = settings.getString("ringtone", "none");

        if (!uri.equals("none")) {
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
                    Uri.parse(uri));
        } else {
            intent.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
                    (Uri) null);
        }
        startActivityForResult(intent, 999);

    }

    public void saveSettings(View view) {
        long userDelay = delayPicker.getValue();
        long refreshRate = refreshPicker.getValue();

        // salva nelle preferenze
        SharedPreferences prefs = getSharedPreferences("uMorning", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong("DELAY", userDelay);
        editor.putLong("REFRESH", refreshRate);
        Uri uri=fromTitleToUri(ringtonePicked);
        editor.putString("TONE", uri.toString());
        editor.commit();
        finish();
    }

    /*public void selectRingtone(View view) {
        SelectRingtone r = new SelectRingtone();
        r.show(getFragmentManager(), "ringtone");
    }*/

    private Uri fromTitleToUri(String title) {
        for (Uri u : ringtonesListUri)
            if (RingtoneManager.getRingtone(this, u).getTitle(this).equals(title))
                return u;
        return null;
    }
}
