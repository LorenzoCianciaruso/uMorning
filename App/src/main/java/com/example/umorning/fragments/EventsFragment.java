package com.example.umorning.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.umorning.R;
import com.example.umorning.external_services.Eventbrite;
import com.example.umorning.model.Event;

import java.util.ArrayList;
import java.util.List;

public class EventsFragment extends Fragment {

    /*
    // dynamic lookups improves performance.
    public static final String[] EVENT_PROJECTION = new String[]{
            CalendarContract.Calendars._ID,                           // 0
            CalendarContract.Calendars.ACCOUNT_NAME,                  // 1
            CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,         // 2
            CalendarContract.Calendars.OWNER_ACCOUNT                  // 3
    };
    */

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        super.onCreate(savedInstanceState);

        View rootView = inflater.inflate(R.layout.fragment_events, container, false);


        SharedPreferences prefs = getActivity().getSharedPreferences("uMorning", 0);
        String token = prefs.getString("EventbriteToken", "NotEventbriteLogged");

        if(token.equals("NotEventbriteLogged")){
            //TODO notlogged
        }else{
        new AsyncTaskEventbrite().execute(token);
        }

        return rootView;
    }


    private class AsyncTaskEventbrite extends AsyncTask<String, Void, List<Event>>{

        @Override
        protected List<Event> doInBackground(String... params) {

            String token = params[0];

            Eventbrite eve = new Eventbrite(token);
            eve.getEventbriteOrders();
            List<Event> eventList = eve.getEventList();
            return eventList;

        }

        @Override
        protected void onPostExecute(List<Event> params){

        }
    }

    private class AsyncTaskEventRetrive extends AsyncTask<Void, Void, List<Event> > {

        @Override
        protected List<Event> doInBackground(Void...params) {

            /*
            // Run query
            Cursor cur = null;
            ContentResolver cr = getContentResolver();
            Uri uri = CalendarContract.Calendars.CONTENT_URI;
            String selection = "((" + CalendarContract.Calendars.ACCOUNT_NAME + " = ?) AND ("
                    + CalendarContract.Calendars.ACCOUNT_TYPE + " = ?) AND ("
                    + CalendarContract.Calendars.OWNER_ACCOUNT + " = ?))";
            String[] selectionArgs = new String[]{"lory90@gmail.com", "com.google",};

            // Submit the query and get a Cursor object back.
            String selection = "(1=?)";
            String[] selectionArgs = new String[]{"1"};
            cur = cr.query(uri, EVENT_PROJECTION, selection, selectionArgs, null);


            String[] projection =
                    new String[]{
                            CalendarContract.Calendars._ID,
                            CalendarContract.Calendars.NAME,
                            CalendarContract.Calendars.ACCOUNT_NAME,
                            CalendarContract.Calendars.ACCOUNT_TYPE};
            Cursor calCursor =
                    getContentResolver().
                            query(CalendarContract.Calendars.CONTENT_URI,
                                    projection,
                                    CalendarContract.Calendars.VISIBLE + " = 1",
                                    null,
                                    CalendarContract.Calendars._ID + " ASC");
            if (calCursor.moveToFirst()) {
                do {
                    long id = calCursor.getLong(0);
                    String displayName = calCursor.getString(1);
                    System.out.println("Ecco i campi" + calCursor.getString(0) + calCursor.getString(1) + calCursor.getString(2) + calCursor.getString(3));
                } while (calCursor.moveToNext());
            }
            */
            Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();
/*
            Cursor mCursor;
            String[] projection = new String[]
                    {CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND};

            mCursor = getContentResolver().query(
                    builder.build(), projection, CalendarContract.Instances.CALENDAR_ID + " = ?",
                    new String[]{"1"}, null);
            if (mCursor.moveToFirst()) {
                do {
                    System.out.println("Ecco i papa" + mCursor.getString(0));
                } while (mCursor.moveToNext());
            }
            */
            List<Event> events = new ArrayList<Event>();

            return events;
        }

    }

}