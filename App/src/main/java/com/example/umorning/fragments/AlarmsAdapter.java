package com.example.umorning.fragments;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.umorning.R;
import com.example.umorning.model.Alarm;

import java.util.List;

public class AlarmsAdapter extends BaseAdapter {

    private Activity activity;
    private List<Alarm> alarms;
    private static LayoutInflater inflater=null;

    AlarmsAdapter(Activity activity, List<Alarm> alarms){
        this.activity=activity;
        this.alarms=alarms;
        inflater = (LayoutInflater)activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
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
        View row=convertView;
        if(convertView==null) {
            row = inflater.inflate(R.layout.list_item_alarms, null);
        }

        TextView title = (TextView)row.findViewById(R.id.alarmTitle);
        TextView time = (TextView)row.findViewById(R.id.alarmTime);

        Alarm a;
        a=alarms.get(position);

        title.setText(a.getName());
        time.setText(String.valueOf(a.getId()));

        return row;
    }
}
