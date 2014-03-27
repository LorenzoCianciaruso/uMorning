package com.example.umorning.internal_services;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import com.example.umorning.model.Event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EventService {

    private Context cxt;

    public EventService(Context cxt){
        this.cxt=cxt;
    }
    public List<Event> getEvent(){

            /*
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
                Date date = new Date();
                try {
                    long l = Long.parseLong(mCursor.getString(mCursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)));
                    date.setTime(l);
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
            System.out.println ("Nome"+e.getName()+"loc "+e.getLocationName()+"org "+e.getOrganizerName()+"date "+e.getDate());

        }
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
