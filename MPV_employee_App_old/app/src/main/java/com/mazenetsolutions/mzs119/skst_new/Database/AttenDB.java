package com.mazenetsolutions.mzs119.skst_new.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mazenetsolutions.mzs119.skst_new.Model.GPSModel;

import java.util.ArrayList;



/**
 * Created by admin on 03/10/2016.
 */
public class AttenDB extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "attedence";

    private static final String TABLE_CONTACTS = "atte";

    private static final String KEY_ID = "id";
    private static final String KEY_NAME = "name";
    private static final String KEY_lat = "lattitude";
    private static final String KEY_long = "longitude";
    private static final String KEY_time = "time";
    private static final String KEY_type = "atttype";
    private static final String KEY_isposted = "Isposted";
    private static final String KEY_date = "dattt";


    public AttenDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_lat + " TEXT," + KEY_long + " TEXT," + KEY_time
                + " TEXT," + KEY_type + " TEXT," + KEY_isposted + " TEXT," + KEY_date + " TEXT" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);

        onCreate(db);
    }

    public void deletetable() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_CONTACTS, null, null);
        db.close();
    }

    public void addattedence(ArrayList<GPSModel> sched) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < sched.size(); i++) {
            ContentValues values = new ContentValues();
            GPSModel contact = new GPSModel();
            contact = sched.get(i);
            values.put(KEY_NAME, contact.getName());
            values.put(KEY_lat, contact.getLat());
            values.put(KEY_long, contact.getLon());
            values.put(KEY_time, contact.getTime());
            values.put(KEY_type, contact.getType());
            values.put(KEY_date, contact.getDatee());

            values.put(KEY_isposted, "No");
            db.insert(TABLE_CONTACTS, null, values);
        }
        db.close();
    }


    public ArrayList<GPSModel> getAllattt() {
        ArrayList<GPSModel> contactList = new ArrayList<GPSModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                GPSModel contact = new GPSModel();
                contact.setName(cursor.getString(1));
                contact.setLat(cursor.getString(2));
                contact.setLon(cursor.getString(3));
                contact.setTime(cursor.getString(4));
                contact.setType(cursor.getString(5));
                contact.setDatee(cursor.getString(7));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        return contactList;
    }

    public ArrayList<GPSModel> getnotposted() {
        ArrayList<GPSModel> contactList = new ArrayList<GPSModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE " + KEY_isposted + " = 'No'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                GPSModel contact = new GPSModel();
                contact.setName(cursor.getString(1));
                contact.setLat(cursor.getString(2));
                contact.setLon(cursor.getString(3));
                contact.setTime(cursor.getString(4));
                contact.setType(cursor.getString(5));
                contact.setId(cursor.getString(0));
                contact.setDatee(cursor.getString(7));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();
        return contactList;
    }


    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int value = cursor.getCount();
        db.close();

        return value;
    }

    public void upcontact(String id) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL("UPDATE " + TABLE_CONTACTS + " SET " + KEY_isposted
                    + " ='" + "yes" + "' WHERE " + KEY_ID + " = '" + id
                    + "'");
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

