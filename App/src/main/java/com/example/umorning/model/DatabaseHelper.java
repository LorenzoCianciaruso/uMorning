package com.example.umorning.model;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
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
    private static final String TABLE_BADGE = "badges";
    private static final String TABLE_REPORT = "reports";


    // colonne alarm
    private static final String KEY_ALARM_ID = "id";
    private static final String KEY_ALARM_DELAY = "delay";
    private static final String KEY_ALARM_NAME = "name";
    private static final String KEY_ALARM_ADDRESS = "address";
    private static final String KEY_ALARM_CITY = "city";
    private static final String KEY_ALARM_COUNTRY = "country";
    private static final String KEY_ALARM_START_LATITUDE = "startLatitude";
    private static final String KEY_ALARM_START_LONGITUDE = "startLongitude";
    private static final String KEY_ALARM_END_LATITUDE = "endLatitude";
    private static final String KEY_ALARM_END_LONGITUDE = "endLongitude";
    private static final String KEY_ALARM_DATE = "date";
    private static final String KEY_ALARM_DATE_ALARM = "dateAlarm";
    private static final String KEY_ALARM_ACTIVATED = "activated";

    // colonne badge
    private static final String KEY_BADGE_ID = "idBadge";
    private static final String KEY_BADGE_NAME = "name";
    private static final String KEY_BADGE_DESCRIPTION = "description";
    private static final String KEY_BADGE_ICON_AQUIRED = "iconAquired";
    private static final String KEY_BADGE_ICON_PENDING = "iconPending";
    private static final String KEY_BADGE_ACQUIRED = "acquired";

    //colonne report
    private static final String KEY_REPORT_ID = "idReport";
    private static final String KEY_REPORT_DELAY = "delay";
    private static final String KEY_REPORT_START_LATITUDE = "startLatitude";
    private static final String KEY_REPORT_START_LONGITUDE = "startLongitude";
    private static final String KEY_REPORT_END_LATITUDE = "endLatitude";
    private static final String KEY_REPORT_END_LONGITUDE = "endLongitude";
    private static final String KEY_REPORT_DATE = "date";
    private static final String KEY_REPORT_EXPECTEDTIME = "expectedTime";


    private static final String CREATE_TABLE_ALARM = "CREATE TABLE " + TABLE_ALARM
            + "("
            + KEY_ALARM_ID + " INTEGER PRIMARY KEY,"
            + KEY_ALARM_DELAY + " LONG,"
            + KEY_ALARM_NAME + " TEXT,"
            + KEY_ALARM_ADDRESS + " TEXT,"
            + KEY_ALARM_CITY + " TEXT,"
            + KEY_ALARM_COUNTRY + " TEXT,"
            + KEY_ALARM_START_LATITUDE + " DOUBLE,"
            + KEY_ALARM_START_LONGITUDE + " DOUBLE,"
            + KEY_ALARM_END_LATITUDE + " DOUBLE,"
            + KEY_ALARM_END_LONGITUDE + " DOUBLE,"
            + KEY_ALARM_DATE + " LONG,"
            + KEY_ALARM_DATE_ALARM + " LONG,"
            + KEY_ALARM_ACTIVATED + " INTEGER"
            + ")";

    private static final String CREATE_TABLE_BADGE = "CREATE TABLE " + TABLE_BADGE
            + "("
            + KEY_BADGE_ID + " INTEGER,"
            + KEY_BADGE_NAME + " TEXT,"
            + KEY_BADGE_DESCRIPTION + " TEXT,"
            + KEY_BADGE_ICON_AQUIRED + " TEXT,"
            + KEY_BADGE_ICON_PENDING + " TEXT,"
            + KEY_BADGE_ACQUIRED + " INTEGER"
            + ")";

    private static final String CREATE_TABLE_REPORT = "CREATE TABLE " + TABLE_REPORT
            + "("
            + KEY_REPORT_ID + " INTEGER PRIMARY KEY,"
            + KEY_REPORT_DELAY + " LONG,"
            + KEY_REPORT_START_LATITUDE + " DOUBLE,"
            + KEY_REPORT_START_LONGITUDE + " DOUBLE,"
            + KEY_REPORT_END_LATITUDE + " DOUBLE,"
            + KEY_REPORT_END_LONGITUDE + " DOUBLE,"
            + KEY_REPORT_DATE + " LONG,"
            + KEY_REPORT_EXPECTEDTIME + " LONG"
            + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_TABLE_ALARM);
        sqLiteDatabase.execSQL(CREATE_TABLE_BADGE);
        sqLiteDatabase.execSQL(CREATE_TABLE_REPORT);
        List<Badge> toAdd = Badge.createBadges();
        for (Badge b : toAdd){
            //TODO così com'è addBadge() non può stare nell'oncreate, chiamata ricorsiva getWritableDatabase
            //this.addBadge(b)
            this.addBadge(b,sqLiteDatabase);
        }
        //TODO stessa cosa di sopra
        //this.aquireBadge(Badge.FIRST_USAGE);
        this.aquireBadge(Badge.FIRST_USAGE, sqLiteDatabase);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {
        // on upgrade elimina le tabelle veccchie
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_ALARM);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_BADGE);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_REPORT);

        // crea nuove tabelle
        onCreate(sqLiteDatabase);
    }

    //*************SVEGLIE*******************
    //***************************************
    // Aggiungi un allarme
    public long addAlarm(Alarm alarm) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(alarm);
        long num = db.insert(TABLE_ALARM, null, values);
        switch ((int)num){
            case (5):
                this.aquireBadge(Badge.FIVE_ALARMS, db);
                break;
            case (100):
                this.aquireBadge(Badge.HUNDRED_ALARMS, db);
                break;
        }
        return num;
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
        if (c!=null && c.moveToFirst()) {
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
        long delay = (c.getLong(c.getColumnIndex(KEY_ALARM_DELAY)));
        String name = (c.getString(c.getColumnIndex(KEY_ALARM_NAME)));
        String address = (c.getString(c.getColumnIndex(KEY_ALARM_ADDRESS)));
        String city = (c.getString(c.getColumnIndex(KEY_ALARM_CITY)));
        String country = (c.getString(c.getColumnIndex(KEY_ALARM_COUNTRY)));
        double startLatitude = (c.getDouble(c.getColumnIndex(KEY_ALARM_START_LATITUDE)));
        double startLongitude = (c.getDouble(c.getColumnIndex(KEY_ALARM_START_LONGITUDE)));
        double endLatitude = (c.getDouble(c.getColumnIndex(KEY_ALARM_END_LATITUDE)));
        double endLongitude = (c.getDouble(c.getColumnIndex(KEY_ALARM_END_LONGITUDE)));
        Calendar date = new GregorianCalendar();
        date.setTimeInMillis(c.getLong(c.getColumnIndex(KEY_ALARM_DATE)));
        Calendar expectedTime = new GregorianCalendar();
        expectedTime.setTimeInMillis(c.getLong(c.getColumnIndex(KEY_ALARM_DATE_ALARM)));
        int activation = (c.getInt(c.getColumnIndex(KEY_ALARM_ACTIVATED)));
        boolean activated;
        if (activation == 0) {
            activated = false;
        } else {
            activated = true;
        }
        //chiamo il costruttore
        return new Alarm(id, delay, name, address, city, country, startLatitude, startLongitude, endLatitude, endLongitude, date, expectedTime, activated);
    }

    private ContentValues getContentValues(Alarm alarm) {
        ContentValues values = new ContentValues();

        values.put(KEY_ALARM_DELAY, alarm.getDelay());
        values.put(KEY_ALARM_NAME, alarm.getName());
        values.put(KEY_ALARM_ADDRESS, alarm.getAddress());
        values.put(KEY_ALARM_CITY, alarm.getCity());
        values.put(KEY_ALARM_COUNTRY, alarm.getCountry());
        values.put(KEY_ALARM_START_LATITUDE, alarm.getStartLatitude());
        values.put(KEY_ALARM_START_LONGITUDE, alarm.getStartLongitude());
        values.put(KEY_ALARM_END_LATITUDE, alarm.getEndLatitude());
        values.put(KEY_ALARM_END_LONGITUDE, alarm.getEndLongitude());
        values.put(KEY_ALARM_DATE, alarm.getDate().getTimeInMillis());
        values.put(KEY_ALARM_DATE_ALARM, alarm.getExpectedTime().getTimeInMillis());
        if (alarm.isActivated()) {
            values.put(KEY_ALARM_ACTIVATED, 1);
        } else {
            values.put(KEY_ALARM_ACTIVATED, 0);
        }
        return values;
    }

    //***************BADGE*******************
    //***************************************
    //prendi tutti i badge come lista

    public List<Badge> getAllBadges() {
        List<Badge> badges = new ArrayList<Badge>();
        String selectQuery = "SELECT  * FROM " + TABLE_BADGE;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // aggiungili alla lista
        if (c!=null && c.moveToFirst()) {
            do {
                Badge b = fromCursorToBadge(c);
                badges.add(b);
            } while (c.moveToNext());
        }
        return badges;
    }

    //prendi badge per id
    public Badge getBadge(int badge_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_BADGE + " WHERE "
                + KEY_BADGE_ID + " = " + badge_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) {
            c.moveToFirst();
        }
        return fromCursorToBadge(c);
    }

    //acquisisci un badge per id
    public void aquireBadge(int badge_id, SQLiteDatabase db) {
        if(db==null) {
            db = this.getReadableDatabase();
        }
        String selectQuery = "SELECT  * FROM " + TABLE_BADGE + " WHERE "
                + KEY_BADGE_ID + " = " + badge_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) {
            c.moveToFirst();
        }
        Badge toUpdate = fromCursorToBadge(c);
        if (!toUpdate.isAcquired()){
            toUpdate.acquire();
            ContentValues values = getContentValues(toUpdate);
            db.update(TABLE_BADGE, values, KEY_BADGE_ID + " = ?",
                    new String[]{String.valueOf(toUpdate.getId())});
            //TODO pop up che comuniche che è stato preso un badge
        }

    }

    // Aggiungi un badge
    public long addBadge(Badge badge, SQLiteDatabase db) {
        //SQLiteDatabase db = this.getWritableDatabase();
        if(db==null) {
            db = this.getWritableDatabase();
        }
        ContentValues values = getContentValues(badge);
        return db.insert(TABLE_BADGE, null, values);
    }

    //trasforma un oggetto del db in un oggetto Badge compatibile con il sistema
    private Badge fromCursorToBadge(Cursor c) {
        //creo i campi
        int id = (c.getInt(c.getColumnIndex(KEY_BADGE_ID)));
        String name = (c.getString(c.getColumnIndex(KEY_BADGE_NAME)));
        String description = (c.getString(c.getColumnIndex(KEY_BADGE_DESCRIPTION)));
        String iconAcquired = (c.getString(c.getColumnIndex(KEY_BADGE_ICON_AQUIRED)));
        String iconPending = (c.getString(c.getColumnIndex(KEY_BADGE_ICON_PENDING)));
        int aquisition = (c.getInt(c.getColumnIndex(KEY_BADGE_ICON_AQUIRED)));
        boolean acquired;
        if (aquisition == 0) {
            acquired = false;
        } else {
            acquired = true;
        }
        //chiamo il costruttore
        return new Badge(id, name, description, iconAcquired, iconPending, acquired);
    }


    private ContentValues getContentValues(Badge badge) {
        ContentValues values = new ContentValues();
        values.put(KEY_BADGE_ID, badge.getId());
        values.put(KEY_BADGE_NAME, badge.getName());
        values.put(KEY_BADGE_DESCRIPTION, badge.getDescription());
        values.put(KEY_BADGE_ICON_AQUIRED, badge.getIconAcquired());
        values.put(KEY_BADGE_ICON_PENDING, badge.getIconPending());
        if (badge.isAcquired()) {
            values.put(KEY_BADGE_ACQUIRED, 1);
        } else {
            values.put(KEY_BADGE_ACQUIRED, 0);
        }
        return values;
    }


    //***************REPORT******************
    //***************************************
    //prendi tutti i report come lista

    public List<Report> getAllReport() {
        List<Report> reports = new ArrayList<Report>();
        String selectQuery = "SELECT  * FROM " + TABLE_REPORT;
        Log.e(LOG, selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);

        // aggiungili alla lista
        if (c!=null && c.moveToFirst()) {
            do {
                Report r = fromCursorToReport(c);
                reports.add(r);
            } while (c.moveToNext());
        }
        return reports;
    }

    //prendi report per id
    public Report getReport(int report_id) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_REPORT + " WHERE "
                + KEY_REPORT_ID + " = " + report_id;
        Log.e(LOG, selectQuery);
        Cursor c = db.rawQuery(selectQuery, null);
        if (c != null) {
            c.moveToFirst();
        }
        return fromCursorToReport(c);
    }

    // Aggiungi un report
    public long addReport(Report report) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = getContentValues(report);
        return db.insert(TABLE_REPORT, null, values);
    }

    //trasforma un oggetto del db in un oggetto Report compatibile con il sistema
    private Report fromCursorToReport(Cursor c) {
        //creo i campi
        int id = (c.getInt(c.getColumnIndex(KEY_REPORT_ID)));
        long delay = (c.getLong(c.getColumnIndex(KEY_REPORT_DELAY)));
        double startLatitude = (c.getDouble(c.getColumnIndex(KEY_REPORT_START_LATITUDE)));
        double startLongitude = (c.getDouble(c.getColumnIndex(KEY_REPORT_START_LONGITUDE)));
        double endLatitude = (c.getDouble(c.getColumnIndex(KEY_REPORT_END_LATITUDE)));
        double endLongitude = (c.getDouble(c.getColumnIndex(KEY_REPORT_END_LONGITUDE)));
        Calendar date = new GregorianCalendar();
        date.setTimeInMillis(c.getLong(c.getColumnIndex(KEY_REPORT_DATE)));
        Calendar expectedTime = new GregorianCalendar();
        expectedTime.setTimeInMillis(c.getLong(c.getColumnIndex(KEY_REPORT_EXPECTEDTIME)));

        //TODO real weather
        //chiamo il costruttore
        return new Report(id, delay, startLatitude,  startLongitude,  endLatitude,  endLongitude,  date,  expectedTime);
    }


    private ContentValues getContentValues(Report report) {
        ContentValues values = new ContentValues();
        values.put(KEY_REPORT_ID, report.getId());
        values.put(KEY_REPORT_DELAY, report.getDelay());
        values.put(KEY_REPORT_START_LATITUDE, report.getStartLatitude());
        values.put(KEY_REPORT_START_LONGITUDE, report.getStartLongitude());
        values.put(KEY_REPORT_END_LATITUDE, report.getEndLatitude());
        values.put(KEY_REPORT_END_LONGITUDE, report.getEndLongitude());
        values.put(KEY_REPORT_DATE, report.getDate().getTimeInMillis());
        values.put(KEY_REPORT_EXPECTEDTIME, report.getExpectedTime().getTimeInMillis());
        return values;

        //TODO real weather

    }
}