package com.example.umorning.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.umorning.AddNewAlarm;
import com.example.umorning.R;

public class AlarmsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_alarms, container, false);

        return rootView;
    }

    public void openAddNewActivity(View view) {
        // Do something in response to button
        /*
        Intent myIntent = new Intent(getActivity(), AddNewAlarm.class);
        startActivity(myIntent);*/
        startActivity(new Intent(view.getContext(),AddNewAlarm.class));
    }
}

