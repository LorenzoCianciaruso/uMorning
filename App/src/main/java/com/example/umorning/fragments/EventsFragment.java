package com.example.umorning.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.umorning.R;
import com.example.umorning.activities.AccountManagerActivity;
import com.example.umorning.activities.BadgeAchievementsActivity;
import com.example.umorning.activities.EventDetailsActivity;
import com.example.umorning.activities.PostMetagActivity;
import com.example.umorning.activities.UserSettingActivity;
import com.example.umorning.external_services.Eventbrite;
import com.example.umorning.external_services.Facebook;
import com.example.umorning.external_services.HttpRequests;
import com.example.umorning.internal_services.CalendarRetrievalEngine;
import com.example.umorning.model.Event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class EventsFragment extends Fragment {

    static private List<Event> events;
    private ListView list_of_events;
    private ArrayAdapter<String> listAdapter;
    private ProgressBar progress;
    private AsyncTaskEvent retrievingEvents;
    private EventsAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        progress = (ProgressBar) getView().findViewById(R.id.pbHeaderProgress);
        retrievingEvents = new AsyncTaskEvent();
        if (events == null) {
            if (HttpRequests.isOnline(getActivity())) {
                retrievingEvents.execute();
            } else {
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG);
            }
        } else {
            retrievingEvents.onPostExecute(events);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);
        /*
        SharedPreferences prefs = getActivity().getSharedPreferences("uMorning", 0);
        String token = prefs.getString("EventbriteToken", "NotEventbriteLogged");

        //se è loggato in eventbrite
        if (!token.equals("NotEventbriteLogged")) {
            new AsyncTaskEventbrite().execute(token);
        }

        Facebook fb = new Facebook(getActivity());
        //se è loggato in facebook
        if(fb.getSession() != null && fb.getSession().isOpened()==true){
            new AsyncTaskFacebook().execute(fb);
        }

        new AsyncTaskEvent().execute();*/
        return rootView;
    }

    private class AsyncTaskEvent extends AsyncTask<Void, Void, List<Event>> {

        @Override
        protected void onPreExecute() {
            progress.bringToFront();
            progress.setVisibility(View.VISIBLE);
        }

        @Override
        protected List<Event> doInBackground(Void... params) {
            events = new ArrayList<Event>();

            //verifico connessione internet
            if (HttpRequests.isOnline(getActivity())) {

                //new AsyncTaskEventbrite().execute(token);
                Eventbrite eve = new Eventbrite(getActivity());
                events.addAll(eve.getEventList());
                Facebook fb = new Facebook(getActivity());

                //se esiste sessione attiva
                if (fb.isLogged()) {
                    //new AsyncTaskFacebook().execute(fb);
                    events.addAll(fb.getEventList());
                }
            }
            CalendarRetrievalEngine eve = new CalendarRetrievalEngine(getActivity());
            events.addAll(eve.getEvent());
            Collections.sort(events, new Comparator<Event>() {
                @Override
                public int compare(Event e1, Event e2) {
                    return e1.getDate().compareTo(e2.getDate());
                }
            });
            return events;
        }

        @Override
        protected void onPostExecute(List<Event> params) {
            events = params;
            list_of_events = (ListView) getActivity().findViewById(R.id.listViewEvents);

            ArrayList<String> nameEvents = new ArrayList<String>();
            for (Event x : events) {
                nameEvents.add(x.getName());
            }
            /*listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_events, R.id.eventName, nameEvents);
            list_of_events.setAdapter(listAdapter);*/
            adapter = new EventsAdapter(getActivity(), events);
            list_of_events.setAdapter(adapter);

            list_of_events.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view,
                                        int i, long l) {
                    Event event = events.get(i);
                    Intent intent = new Intent(getActivity(), EventDetailsActivity.class);
                    intent.putExtra("name", event.getName());
                    intent.putExtra("place", event.getAddress());
                    intent.putExtra("dateInMillis", event.getDate().getTimeInMillis());
                    intent.putExtra("url", event.getEventURL());
                    intent.putExtra("latitude", event.getLatitude());
                    intent.putExtra("longitude", event.getLongitude());
                    intent.putExtra("organizer", event.getOrganizer());
                    startActivity(intent);
                }
            });
            progress.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        events = null;
        if (retrievingEvents != null) {
            if (!retrievingEvents.isCancelled()) {
                retrievingEvents.cancel(true);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.fragment_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.refresh: {
                events.clear();
                retrievingEvents = new AsyncTaskEvent();
                retrievingEvents.execute();
                break;
            }
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

/*
    private class AsyncTaskFacebook extends AsyncTask<Facebook, Void, List<Event>> {

        @Override
        protected List<Event> doInBackground(Facebook... params) {
            List<Event> list = params[0].getEventList();
            return list;
        }
        @Override
        protected void onPostExecute(List<Event> params) {
            events.addAll(params);
        }
    }

    private class AsyncTaskEventbrite extends AsyncTask<String, Void, List<Event>> {

        @Override
        protected List<Event> doInBackground(String... params) {
            String token = params[0];
            Eventbrite eve = new Eventbrite(token);
            eve.getEventbriteOrders();
            List<Event> eventList = eve.getEventList();
            return eventList;
        }

        @Override
        protected void onPostExecute(List<Event> params) {
            events.addAll(params);
        }
    }*/
}