package com.example.umorning.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.umorning.R;
import com.example.umorning.activities.AlarmEditActivity;
import com.example.umorning.model.Alarm;
import com.example.umorning.model.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AlarmsFragment extends Fragment {

    private ListView list;
    private AlarmsAdapter adapter;
    private List<Alarm> alarms;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_alarms, container,
                false);

        super.onCreate(savedInstanceState);
        return rootView;
    }

    @Override
    public void onResume(){
        super.onResume();
        DatabaseHelper db = new DatabaseHelper(getActivity()
                .getApplicationContext());
        alarms = new ArrayList<Alarm>();
        alarms = db.getAllAlarms();

        list = (ListView) getView().findViewById(R.id.listView);
        adapter = new AlarmsAdapter(getActivity(), alarms);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                Intent myIntent = new Intent(getActivity(), AlarmEditActivity.class);
                myIntent.putExtra("alarmId", alarms.get(i).getId());
                startActivityForResult(myIntent,0);

            }
        });
    }

}