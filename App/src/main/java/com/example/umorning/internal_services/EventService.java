package com.example.umorning.internal_services;

import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import com.example.umorning.model.Event;
import java.util.ArrayList;
import java.util.List;

public class EventService {
    public List<Event> getEvent(){

            /*
            // Run queryjiofvsuhbfvuabrfbhiufdbui

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

        Uri.Builder builder = CalendarContract.Instances.CONTENT_URI.buildUpon();

        Cursor mCursor;
        String[] projection = new String[]
                {CalendarContract.Events.TITLE, CalendarContract.Events.DTSTART, CalendarContract.Events.DTEND};

        mCursor = getContentResolver().query(
                builder.build(), projection, CalendarContract.Instances.CALENDAR_ID + " = ?",
                new String[]{"1"}, null);
        List<Event> events = new ArrayList<Event>();
        if (mCursor.moveToFirst()) {
            do {
                Event event = new Event(CalendarContract.Events.TITLE, CalendarContract.Events.ORIGINAL_ID, CalendarContract.Events.ORGANIZER, CalendarContract.Events.EVENT_LOCATION);
                events.add(event);
            } while (mCursor.moveToNext());
        }
        for (int i=0;i<events.size();i++){
            Event e = events.get(i);
            System.out.println (e.getName()+e.getLocationName()+e.getOrganizerName()+e.getId());
        }
        return events;
    */return null;}
}
