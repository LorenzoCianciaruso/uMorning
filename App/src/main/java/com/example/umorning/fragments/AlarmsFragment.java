package com.example.umorning.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.umorning.R;
import com.example.umorning.model.Alarm;
import com.example.umorning.model.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AlarmsFragment extends Fragment {

    ListView list;
    AlarmsAdapter adapter;
    private List<Alarm> alarms;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_alarms, container,
                false);

        super.onCreate(savedInstanceState);

        DatabaseHelper db = new DatabaseHelper(getActivity()
                .getApplicationContext());
        alarms = new ArrayList<Alarm>();
        alarms = db.getAllAlarms();

        System.out.println("ZZZZZZZZZZZZZZ: ");

        list = (ListView) rootView.findViewById(R.id.listView);
        adapter = new AlarmsAdapter(getActivity(), alarms);
        list.setAdapter(adapter);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {

            }
        });

        return rootView;
    }

}