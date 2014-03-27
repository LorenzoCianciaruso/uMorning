package com.example.umorning.internal_services;

import android.app.Application;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import android.provider.CalendarContract;
import com.example.umorning.model.Event;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class EventService {

    private Context cxt;

    public EventService(Context cxt){
        this.cxt=cxt;
    }
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

                //Event event = new Event(mCursor.getString(mCursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE)) ,mCursor.getString(mCursor.getColumnIndexOrThrow(CalendarContract.Events.ORIGINAL_ID)),null,null);
                        //,null, mCursor.getString(mCursor.getColumnIndex(CalendarContract.Events.ORGANIZER)),mCursor.getString(mCursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION)));
                        //mCursor.getString(mCursor.getColumnIndex(CalendarContract.Events.ORIGINAL_ID)), mCursor.getString(mCursor.getColumnIndex(CalendarContract.Events.ORGANIZER)), mCursor.getString(mCursor.getColumnIndex(CalendarContract.Events.EVENT_LOCATION)));
                //events.add(event);
                long l = Long.parseLong(mCursor.getString(mCursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)));
                System.out.println("time: "+getDate(l, "dd/MM/yyyy HH:mm:ss"));
                System.out.println("Org "+ mCursor.getString(mCursor.getColumnIndexOrThrow(CalendarContract.Events.DTSTART)));
                System.out.println("title: "+mCursor.getString(mCursor.getColumnIndexOrThrow(CalendarContract.Events.TITLE)));
            } while (mCursor.moveToNext());
        }
        for (int i=0;i<events.size();i++){
            Event e = events.get(i);
            System.out.println ("AAAAAAAAAAAAAAAAA"+e.getName()+e.getLocationName()+e.getOrganizerName()+e.getId());

        }
        return events;
    }

    public static String getDate(long milliSeconds, String dateFormat)
    {
        // Create a DateFormatter object for displaying date in specified format.
        DateFormat formatter = new SimpleDateFormat(dateFormat);

        // Create a calendar object that will convert the date and time value in milliseconds to date.
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(milliSeconds);
        return formatter.format(calendar.getTime());
    }
}
