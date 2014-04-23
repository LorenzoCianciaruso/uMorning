package com.example.umorning.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.umorning.R;
import com.example.umorning.model.Alarm;

import java.text.SimpleDateFormat;
import java.util.List;

public class AlarmsAdapter extends BaseAdapter {

    private Activity activity;
    private List<Alarm> alarms;
    private static LayoutInflater inflater = null;

    AlarmsAdapter(Activity activity, List<Alarm> alarms) {
        this.activity = activity;
        this.alarms = alarms;
        inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return alarms.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        if (convertView == null) {
            row = inflater.inflate(R.layout.list_item_alarms, null);
        }
        ImageView icon = (ImageView) row.findViewById(R.id.alarmIcon);
        TextView time = (TextView) row.findViewById(R.id.alarmTime);
        TextView title = (TextView) row.findViewById(R.id.alarmTitle);

        Alarm a;
        a = alarms.get(position);
        //Ora dell'allarme
        SimpleDateFormat expectedTimeAlarm = new SimpleDateFormat("HH:mm");
        String formattedExpectedTime = expectedTimeAlarm.format(a.getExpectedTime().getTime());
        SimpleDateFormat timeAlarm = new SimpleDateFormat("HH:mm");
        String formattedTime = timeAlarm.format(a.getDate().getTime());
        time.setText(formattedExpectedTime + " - " + formattedTime);
        //Data e titolo dell'allarme
        SimpleDateFormat dateAlarm = new SimpleDateFormat("(d/LL/yyyy)");
        String formattedDate = dateAlarm.format(a.getDate().getTime());
        title.setText(formattedDate + " " + a.getName());
        //Allarme attivato/disattivato (icona)
        Integer[] paths = {R.drawable.alarm_icon, R.drawable.alarm_icon_deactivated};
        Color c = new Color();
        if (a.isActivated())
            icon.setBackgroundResource(paths[0]);
        else {
            icon.setBackgroundResource(paths[1]);
            time.setTextColor(c.parseColor("#a4a4a7"));
            title.setTextColor(c.parseColor("#a4a4a7"));
        }
        return row;
    }
}
