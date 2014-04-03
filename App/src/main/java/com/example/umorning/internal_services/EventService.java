package com.example.umorning.internal_services;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import com.example.umorning.model.Event;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class EventService {

    private Context cxt;

    public EventService(Context cxt){
        this.cxt=cxt;
    }
    public List<Event> getEvent(){


        String[] projection = new String[] { "calendar_id", "title", "description",
                "dtstart", "dtend", "eventLocation" };

        ContentResolver cr= cxt.getApplicationContext().getContentResolver();

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
                Event e = new Event(title,organizer,location,date);
                if (e.validEvent()){
                    if (e.futureEvent()) {
                        events.add(e);
                    }
                }
            } while (mCursor.moveToNext());
        }
        for (int i=0;i<events.size();i++){
            Event e = events.get(i);
            System.out.println ("Nome "+e.getName()+" loc "+e.getLocationName()+" org "+e.getOrganizerName()+" date "+e.getDate());
        }
        return events;
    }
}
