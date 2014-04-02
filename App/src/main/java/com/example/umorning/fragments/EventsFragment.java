package com.example.umorning.fragments;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.umorning.R;
import com.example.umorning.external_services.Eventbrite;
import com.example.umorning.external_services.Facebook;
import com.example.umorning.internal_services.EventService;
import com.example.umorning.model.Event;

import java.util.List;

public class EventsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);


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

        new AsyncTaskEvent().execute();
        return rootView;
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

        }
    }

    private class AsyncTaskEvent extends AsyncTask<Void, Void, List<Event>> {

        @Override
        protected List<Event> doInBackground(Void... params) {
            EventService eve = new EventService(getActivity());
            List<Event> eventList = eve.getEvent();
            return eventList;
        }

        @Override
        protected void onPostExecute(List<Event> params) {
        }
    }

    private class AsyncTaskFacebook extends AsyncTask<Facebook, Void, List<Event>> {

        @Override
        protected List<Event> doInBackground(Facebook... params) {
            List<Event> list = params[0].getEventList();
            return list;
        }

        @Override
        protected void onPostExecute(List<Event> params) {
        }
    }
}