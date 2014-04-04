package com.example.umorning.fragments;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.umorning.R;
import com.example.umorning.external_services.Eventbrite;
import com.example.umorning.external_services.Facebook;
import com.example.umorning.external_services.HttpRequest;
import com.example.umorning.internal_services.EventService;
import com.example.umorning.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {

    private List<Event> events;
    private ListView list_of_events;
    private ArrayAdapter<String> listAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //new AsyncTaskEvent().execute();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);

        new AsyncTaskEvent().execute();
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
        protected List<Event> doInBackground(Void... params) {
            events=new ArrayList<Event>();
            SharedPreferences prefs = getActivity().getSharedPreferences("uMorning", 0);
            String token = prefs.getString("EventbriteToken", "NotEventbriteLogged");

            //verifico connessione internet
            if(HttpRequest.isOnline(getActivity())) {
                //se è loggato in eventbrite
                if (!token.equals("NotEventbriteLogged")) {
                    //new AsyncTaskEventbrite().execute(token);
                    Eventbrite eve = new Eventbrite(token);
                    eve.getEventbriteOrders();
                    events.addAll(eve.getEventList());
                }
                Facebook fb = new Facebook(getActivity());
                //se è loggato in facebook
                if (fb.getSession() != null && fb.getSession().isOpened() == true) {
                    //new AsyncTaskFacebook().execute(fb);
                    events.addAll(fb.getEventList());
                }
            }else{
                Toast.makeText(getActivity().getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
            EventService eve = new EventService(getActivity());
            events.addAll(eve.getEvent());
            return events;
        }

        @Override
        protected void onPostExecute(List<Event> params) {
            events=params;

            list_of_events = (ListView) getActivity().findViewById(R.id.listViewEvents);

            ArrayList<String> nameEvents = new ArrayList<String>();
            for(Event x: events){
                System.out.println("AAAAA");
                System.out.println(x.getName());
                nameEvents.add(x.getName());
            }
            listAdapter = new ArrayAdapter<String>(getActivity(), R.layout.list_item_events, R.id.eventName,nameEvents);
            list_of_events.setAdapter(listAdapter);
        }
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