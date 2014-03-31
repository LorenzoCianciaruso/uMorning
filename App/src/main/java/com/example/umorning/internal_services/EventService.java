package com.example.umorning.internal_services;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import com.example.umorning.model.Event;

import java.util.ArrayList;
import java.util.Date;
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
                GregorianCalendar date = new GregorianCalendar();
                try {

                    long l = Long.parseLong(mCursor.getString(mCursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)));
                    Date millisDate = new Date();
                    millisDate.setTime(l);
                    date.setTime(millisDate);
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
                if (e.checkFields()){
                    events.add(e);
                }
            } while (mCursor.moveToNext());
        }
        for (int i=0;i<events.size();i++){
            Event e = events.get(i);
            System.out.println ("Nome "+e.getName()+" loc "+e.getLocationName()+" org "+e.getOrganizerName()+" date "+e.getDate());

        }
        SharedPreferences prefs = cxt.getSharedPreferences("com.example.uMorning", Context.MODE_PRIVATE);
        prefs.edit().putString("prova", events.toString()).commit();
        String a =  prefs.getString("prova", "vaffanculo");
        System.out.println("shared "+a);
        return events;
    }

    /*public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        DateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }*/

}
