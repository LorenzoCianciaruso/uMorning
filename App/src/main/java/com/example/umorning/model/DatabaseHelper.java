package com.example.umorning.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
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
    private static final String KEY_ACTIVATED = "activated";
    private static final String CREATE_TABLE_ALARM="CREATE TABLE " + TABLE_ALARM
            + "("
            + KEY_ALARM_ID + " INTEGER PRIMARY KEY,"
            + KEY_DELAY + " INTEGER,"
            + KEY_NAME + " TEXT,"
            + KEY_ADDRESS + " TEXT,"
            + KEY_CITY + " TEXT,"
            + KEY_COUNTRY + " TEXT,"
            + KEY_START_LATITUDE + " TEXT,"
            + KEY_START_LONGITUDE + " TEXT,"
            + KEY_END_LATITUDE + " TEXT,"
            + KEY_END_LONGITUDE + " TEXT,"
            + KEY_LOCATION_NAME + " TEXT,"
            + KEY_DATE + " DATETIME,"
            + KEY_ACTIVATED + " BOOLEAN"
            + ")";

    //private final SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-ddTHH:mm:ss.sss");

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ALARM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // on upgrade elimina le tabelle veccchie
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
        // crea nuove tabelle
        onCreate(sqLiteDatabase);
    }

    // Aggiungi un allarme
    public long addAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
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
        // values.put(KEY_DATE, parser.format(alarm.getDate()));
        values.put(KEY_ACTIVATED, alarm.isActivated());

        long alarm_id = db.insert(TABLE_ALARM, null, values);
        return alarm_id;
    }

    //prendi allarme per id
    public Alarm getAlarm(long alarm_id){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_ALARM + " WHERE "
                + KEY_ALARM_ID + " = " + alarm_id;

        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);

        Alarm a=new Alarm();
        if (c != null) {
            c.moveToFirst();
            a.setId(c.getInt(c.getColumnIndex(KEY_ALARM_ID)));
            a.setDelay(c.getInt(c.getColumnIndex(KEY_DELAY)));
            a.setName(c.getString(c.getColumnIndex(KEY_NAME)));
            a.setAddress(c.getString(c.getColumnIndex(KEY_ADDRESS)));
            a.setCity(c.getString(c.getColumnIndex(KEY_CITY)));
            a.setCountry(c.getString(c.getColumnIndex(KEY_COUNTRY)));
            a.setStartLatitude(c.getString(c.getColumnIndex(KEY_START_LATITUDE)));
            a.setStartLongitude(c.getString(c.getColumnIndex(KEY_START_LONGITUDE)));
            a.setEndLatitude(c.getString(c.getColumnIndex(KEY_END_LATITUDE)));
            a.setEndLongitude(c.getString(c.getColumnIndex(KEY_END_LONGITUDE)));
            //a.setDate(c.getString(c.getColumnIndex(KEY_DATEY)));
            //a.setActivated(c.getString(c.getColumnIndex(KEY_ACTIVATED)));
        }

        return a;
    }

    //prendi tutti gli allarmi come lista
    public List<Alarm> getAllAlarms(){
        List<Alarm> alarms=new ArrayList<Alarm>();
        String selectQuery = "SELECT  * FROM " + TABLE_ALARM;
        Log.e(LOG, selectQuery);

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // aggiungili alla lista
        if (c.moveToFirst()) {
            do {
                Alarm a=new Alarm();
                a.setId(c.getInt(c.getColumnIndex(KEY_ALARM_ID)));
                a.setDelay(c.getInt(c.getColumnIndex(KEY_DELAY)));
                a.setName(c.getString(c.getColumnIndex(KEY_NAME)));
                a.setAddress(c.getString(c.getColumnIndex(KEY_ADDRESS)));
                a.setCity(c.getString(c.getColumnIndex(KEY_CITY)));
                a.setCountry(c.getString(c.getColumnIndex(KEY_COUNTRY)));
                a.setStartLatitude(c.getString(c.getColumnIndex(KEY_START_LATITUDE)));
                a.setStartLongitude(c.getString(c.getColumnIndex(KEY_START_LONGITUDE)));
                a.setEndLatitude(c.getString(c.getColumnIndex(KEY_END_LATITUDE)));
                a.setEndLongitude(c.getString(c.getColumnIndex(KEY_END_LONGITUDE)));
                //a.setDate(c.getString(c.getColumnIndex(KEY_DELAY)));
                //a.setActivated(c.getString(c.getColumnIndex(KEY_DELAY)));

                alarms.add(a);
            } while (c.moveToNext());
        }
        return alarms;
    }

    //aggiornare un allarme
    public long updateAlarm(long id, Alarm alarm) {

        this.deleteAlarm(id);
        return this.addAlarm(alarm);
        /*
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, alarm.getName());

        return db.update(TABLE_ALARM, values, KEY_ALARM_ID + " = ?",
                new String[] { String.valueOf(alarm.getId()) });*/
    }

    //cancellare un allarme
    public void deleteAlarm(Long id){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ALARM, KEY_ALARM_ID + " = ?",
                new String[] { String.valueOf(id) });
    }
}