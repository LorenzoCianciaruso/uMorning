package com.example.umorning.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.umorning.R;
import com.example.umorning.activities.AccountManagerActivity;
import com.example.umorning.activities.AlarmDetailsActivity;
import com.example.umorning.activities.BadgeAchievementsActivity;
import com.example.umorning.activities.PostMetagActivity;
import com.example.umorning.activities.UserSettingActivity;
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
        setHasOptionsMenu(true);
        super.onCreate(savedInstanceState);
        return rootView;
    }

    @Override
    public void onResume() {
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
                Intent myIntent = new Intent(getActivity(), AlarmDetailsActivity.class);
                myIntent.putExtra("alarmId", alarms.get(i).getId());
                startActivityForResult(myIntent, 0);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_accounts: {
                Intent i = new Intent(getActivity(), AccountManagerActivity.class);
                startActivity(i);
                break;
            }
            case R.id.menu_settings: {
                Intent i = new Intent(getActivity(), UserSettingActivity.class);
                startActivityForResult(i, 1);
                break;
            }
            case R.id.post_metag: {
                Intent i = new Intent(getActivity(), PostMetagActivity.class);
                startActivityForResult(i, 1);
                break;
            }
            case R.id.menu_achievements: {
                Intent i = new Intent(getActivity(), BadgeAchievementsActivity.class);
                startActivityForResult(i, 1);
                break;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}