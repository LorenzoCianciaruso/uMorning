package com.example.umorning.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.umorning.R;
import com.example.umorning.model.Alarm;
import com.example.umorning.model.DatabaseHelper;

import java.util.ArrayList;
import java.util.List;

public class AlarmsFragment extends Fragment {

    private ListView list_of_alarms;
    private ArrayAdapter<String> listAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_alarms, container, false);

        super.onCreate(savedInstanceState);

        // Database
        DatabaseHelper db=new DatabaseHelper(getActivity().getApplicationContext());
        list_of_alarms = (ListView) rootView.findViewById(R.id.listView);

        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        System.out.println("XXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXXX");
        ArrayList<String> nameAlarms = new ArrayList<String>();
        List<Alarm> alarms=new ArrayList<Alarm>();
        alarms=db.getAllAlarms();

        for(Alarm x: alarms){
            System.out.println(x.getName());
            nameAlarms.add(x.getName());
        }

        listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item, nameAlarms);
        list_of_alarms.setAdapter(listAdapter);



        return rootView;
    }


}