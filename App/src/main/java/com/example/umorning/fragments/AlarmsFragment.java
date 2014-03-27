package com.example.umorning.fragments;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.example.umorning.activities.AlarmAddNewActivity;
import com.example.umorning.R;
import com.example.umorning.internal_services.AlarmService;

import java.util.Calendar;

public class AlarmsFragment extends Fragment {

    private TimePicker tpResult;
    private DatePicker dpResult;

    private int year;
    private int month;
    private int day;
    private int hour;
    private int minute;

    private Button btnSetAlarm;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_alarms, container, false);

        super.onCreate(savedInstanceState);
        super.onCreate(savedInstanceState);

        //prende ora della sveglia dal date picker
/*        btnSetAlarm = (Button) getView().findViewById(R.id.btnSetAlarm);
        btnSetAlarm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                tpResult = (TimePicker) getView().findViewById(R.id.timePicker);
                dpResult = (DatePicker) getView().findViewById(R.id.datePicker);
                year = dpResult.getYear();
                month = dpResult.getMonth();
                day = dpResult.getDayOfMonth();
                hour = tpResult.getCurrentHour();
                minute = tpResult.getCurrentMinute();

                //chiama un alarmservice
                Intent myIntent = new Intent(getActivity(), AlarmService.class);
                PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0, myIntent, 0);
                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(Service.ALARM_SERVICE);

                //imposta l'ora e fa partire
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());
                calendar.set(year, month, day, hour, minute);
                alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
            }
        });*/

        return rootView;
    }

    public void openAddNewActivity(View view) {
        // Do something in response to button
        /*
        Intent myIntent = new Intent(getActivity(), AlarmAddNewActivity.class);
        startActivity(myIntent);*/
        startActivity(new Intent(view.getContext(),AlarmAddNewActivity.class));
    }
}