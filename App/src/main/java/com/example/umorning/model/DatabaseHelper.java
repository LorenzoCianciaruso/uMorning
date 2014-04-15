package com.example.umorning.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.CalendarContract;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    //proprietà del DB
    private static final String LOG = "DatabaseHelper";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "uMorningDatabase";
    private static final String TABLE_ALARM = "alarms";

    // Colonne
    private static final String KEY_ALARM_ID = "id";
    private static final String KEY_DELAY = "delay";
    private static final String KEY_NAME = "name";
    private static final String KEY_ADDRESS = "address";
    private static final String KEY_CITY = "city";
    private static final String KEY_COUNTRY = "country";
    private static final String KEY_START_LATITUDE = "startLatitude";
    private static final String KEY_START_LONGITUDE = "startLongitude";
    private static final String KEY_END_LATITUDE = "endLatitude";
    private static final String KEY_END_LONGITUDE = "endLongitude";
    private static final String KEY_LOCATION_NAME = "locationName";
    private static final String KEY_DATE = "date";
    private static final String KEY_DATE_ALARM = "dateAlarm";
    private static final String KEY_ACTIVATED = "activated";
    private static final String KEY_TO_DELETE = "toDelete";
    private static final String CREATE_TABLE_ALARM = "CREATE TABLE " + TABLE_ALARM
            + "("
            + KEY_ALARM_ID + " INTEGER PRIMARY KEY,"
            + KEY_DELAY + " LONG,"
            + KEY_NAME + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_CITY + " TEXT,"
            + KEY_COUNTRY + " TEXT,"
            + KEY_START_LATITUDE + " DOUBLE,"
            + KEY_START_LONGITUDE + " DOUBLE,"
            + KEY_END_LATITUDE + " DOUBLE,"
            + KEY_END_LONGITUDE + " DOUBLE,"
            + KEY_LOCATION_NAME + " TEXT,"
            + KEY_DATE + " LONG,"
            + KEY_DATE_ALARM + " LONG,"
            + KEY_ACTIVATED + " INTEGER,"
            + KEY_TO_DELETE + " INTEGER"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ALARM);
        System.out.println("onCreate DB");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // on upgrade elimina le tabelle veccchie
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
        // crea nuove tabelle
        onCreate(sqLiteDatabase);
        System.out.println("onUpgrade DB");
    }

    // Aggiungi un allarme
    public long addAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(alarm);
        long id = db.insert(TABLE_ALARM, null, values);
        return id;
    }

    //prendi allarme per id
    public Alarm getAlarm(int alarm_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_ALARM + " WHERE "
                + KEY_ALARM_ID + " = " + alarm_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) {
            c.moveToFirst();
        }
        return fromCursorToAlarm(c);
    }

    //prendi tutti gli allarmi come lista
    public List<Alarm> getAllAlarms() {
        List<Alarm> alarms = new ArrayList<Alarm>();
        String selectQuery = "SELECT  * FROM " + TABLE_ALARM;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // aggiungili alla lista
        if (c.moveToFirst()) {
            do {
                Alarm a = fromCursorToAlarm(c);
                alarms.add(a);
            } while (c.moveToNext());
        }
        return alarms;
    }

    //aggiornare un allarme
    public long updateAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = getContentValues(alarm);

        return db.update(TABLE_ALARM, values, KEY_ALARM_ID + " = ?",
                new String[]{String.valueOf(alarm.getId())});
    }

    //cancellare un allarme
    public void deleteAlarm(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARM, KEY_ALARM_ID + " = ?",
                new String[]{String.valueOf(id)});
    }

    //trasforma un ogetto del db in un oggetto Alarm compatibile con il sistema
    private Alarm fromCursorToAlarm(Cursor c) {
        //creo i campi
        int id = (c.getInt(c.getColumnIndex(KEY_ALARM_ID)));
        long delay = (c.getLong(c.getColumnIndex(KEY_DELAY)));
        String name = (c.getString(c.getColumnIndex(KEY_NAME)));
        String address = (c.getString(c.getColumnIndex(KEY_ADDRESS)));
        String city = (c.getString(c.getColumnIndex(KEY_CITY)));
        String country = (c.getString(c.getColumnIndex(KEY_COUNTRY)));
        double startLatitude = (c.getDouble(c.getColumnIndex(KEY_START_LATITUDE)));
        double startLongitude = (c.getDouble(c.getColumnIndex(KEY_START_LONGITUDE)));
        double endLatitude = (c.getDouble(c.getColumnIndex(KEY_END_LATITUDE)));
        double endLongitude = (c.getDouble(c.getColumnIndex(KEY_END_LONGITUDE)));
        String location = (c.getString(c.getColumnIndex(KEY_LOCATION_NAME)));
        Calendar date = new GregorianCalendar();
        date.setTimeInMillis(c.getLong(c.getColumnIndex(KEY_DATE)));
        Calendar expectedTime = new GregorianCalendar();
        expectedTime.setTimeInMillis(c.getLong(c.getColumnIndex(KEY_DATE_ALARM)));
        int activation = (c.getInt(c.getColumnIndex(KEY_ACTIVATED)));
        boolean activated;
        if (activation == 0) {
            activated = false;
        } else {
            activated = true;
        }
        int deletion = (c.getInt(c.getColumnIndex(KEY_TO_DELETE)));
        boolean toDelete;
        if (deletion == 0) {
            toDelete = false;
        } else {
            toDelete = true;
        }
        //chiamo il costruttore
        return new Alarm(id, delay, name, address, city, country, startLatitude, startLongitude, endLatitude, endLongitude, location, date, expectedTime, activated, toDelete);
    }

    private ContentValues getContentValues(Alarm alarm) {
        ContentValues values = new ContentValues();

        values.put(KEY_DELAY, alarm.getDelay());
        values.put(KEY_NAME, alarm.getName());
        values.put(KEY_ADDRESS, alarm.getAddress());
        values.put(KEY_CITY, alarm.getCity());
        values.put(KEY_COUNTRY, alarm.getCountry());
        values.put(KEY_START_LATITUDE, alarm.getStartLatitude());
        values.put(KEY_START_LONGITUDE, alarm.getStartLongitude());
        values.put(KEY_END_LATITUDE, alarm.getEndLatitude());
        values.put(KEY_END_LONGITUDE, alarm.getEndLongitude());
        values.put(KEY_LOCATION_NAME, alarm.getLocationName());
        values.put(KEY_DATE, alarm.getDate().getTimeInMillis());
        values.put(KEY_DATE_ALARM, alarm.getExpectedTime().getTimeInMillis());
        if (alarm.isActivated()) {
            values.put(KEY_ACTIVATED, 1);
        } else {
            values.put(KEY_ACTIVATED, 0);
        }
        if (alarm.isToDelete()) {
            values.put(KEY_ACTIVATED, 1);
        } else {
            values.put(KEY_ACTIVATED, 0);
        }
        return values;
    }
}