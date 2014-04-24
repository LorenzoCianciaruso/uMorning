package com.example.umorning.internal_services;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;

import com.example.umorning.external_services.GoogleGeocode;
import com.example.umorning.model.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class CalendarRetrievalEngine {

    private Context cxt;

    public CalendarRetrievalEngine(Context cxt){
        this.cxt=cxt;
    }
    public List<Event> getEvent(){


        String[] projection = new String[] { "calendar_id", "title", "description",
                "dtstart", "dtend", "eventLocation" };

        ContentResolver cr = cxt.getApplicationContext().getContentResolver();

        Cursor mCursor = cr.query(Uri.parse("content://com.android.calendar/events"),
                projection,
                null,
                null,
                null);
        List<Event> events = new ArrayList<Event>();

        if (mCursor.moveToFirst()) {
            do {
                //prendi la data
                Calendar date = new GregorianCalendar();
                try {
                    long dateInMillis = Long.parseLong(mCursor.getString(mCursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)));
                    date.setTimeInMillis(dateInMillis);
            }
                catch (IllegalArgumentException e){
                    ;
                }
                //prendi il nome
                String title = new String();
                try {
                    title = mCursor.getString(mCursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE));
                }
                catch (IllegalArgumentException e){
                    ;
                }
                //prendi la location
                String location = new String();
                double latitude = 0;
                double longitude = 0;
                try {
                    location = mCursor.getString(mCursor.getColumnIndexOrThrow(CalendarContract.Events.EVENT_LOCATION));
                }
                catch (IllegalArgumentException e){
                    ;
                }

                //prendi organizzatore
                String organizer = new String();
                try {
                    organizer = mCursor.getString(mCursor.getColumnIndexOrThrow(CalendarContract.Events.ORGANIZER));
                }
                catch (IllegalArgumentException e){
                    ;
                }
                Event e = new Event(title, organizer,location, null ,null, latitude,longitude,null,date,null,0);
                if (e.validEvent()){
                    if (e.futureEvent()) {
                        location = e.getAddress();
                        if(location == null){
                            location = "";
                        }
                        GoogleGeocode gg = new GoogleGeocode(location);
                        Event event = new Event(e.getName(), e.getOrganizer(), e.getAddress(), null, null, gg.getLatitude(), gg.getLongitude(), null, e.getDate(),null, 0);
                        events.add(event);

                    }
                }
            } while (mCursor.moveToNext());
        }
        return events;
    }
}
