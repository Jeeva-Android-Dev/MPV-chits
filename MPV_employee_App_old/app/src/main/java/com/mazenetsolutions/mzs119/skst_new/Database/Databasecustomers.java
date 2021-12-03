package com.mazenetsolutions.mzs119.skst_new.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mazenetsolutions.mzs119.skst_new.Model.Custmodel;

import java.util.ArrayList;


public class Databasecustomers extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 3;

    private static final String DATABASE_NAME = "chit_customers.db";

    private static final String TABLE_CONTACTS = "customers";
    private static final String KEY_ID = "id";
    private static final String KEY_Cusid = "cust_id";
    private static final String KEY_customer_id = "customer_id";
    private static final String KEY_NAME = "name";
    private static final String KEY_MOBILE = "mobile";
    private static final String KEY_advanceamt = "advanceamt";
    private static final String KEY_pendingamt = "pendingamt";
    private static final String KEY_totalenrlpending = "enrol_pending";
    private static final String KEY_enrlpaid = "enrol_paid";
    private static final String KEY_level = "level";
    private static final String KEY_bonusamt = "bonusamt";
    private static final String KEY_penaltyamt = "penaltyamt";
    private static final String KEY_pendingdays = "pendingdays";
    private static final String KEY_lastdate = "lasdate";
    private static final String KEY_tomorowdate = "tomdate";


    public Databasecustomers(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_Cusid + " TEXT,"
                + KEY_customer_id + " TEXT,"
                + KEY_NAME + " TEXT,"
                + KEY_MOBILE + " TEXT,"
                + KEY_advanceamt + " TEXT,"
                + KEY_pendingamt + " TEXT,"
                + KEY_totalenrlpending + " TEXT,"
                + KEY_enrlpaid + " TEXT,"
                + KEY_level + " TEXT,"
                + KEY_bonusamt + " TEXT,"
                + KEY_penaltyamt + " TEXT,"
                + KEY_pendingdays + " TEXT,"
                + KEY_lastdate + " TEXT,"
                + KEY_tomorowdate + " TEXT"
                + ")";
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

    public void addcustomer(ArrayList<Custmodel> sched) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < sched.size(); i++) {
            ContentValues values = new ContentValues();

            Custmodel contact = sched.get(i);
            values.put(KEY_Cusid, contact.getCusid());
            values.put(KEY_customer_id, contact.getCustomer_id());
            values.put(KEY_NAME, contact.getNAME());
            values.put(KEY_MOBILE, contact.getMOBILE());
            values.put(KEY_advanceamt, contact.getAdvanceamt());
            values.put(KEY_pendingamt, contact.getPendingamt());
            values.put(KEY_totalenrlpending, contact.getTotalenrlpending());
            values.put(KEY_enrlpaid, contact.getEnrlpaid());
            values.put(KEY_level, contact.getLevel());
            values.put(KEY_bonusamt, contact.getBonusamt());
            values.put(KEY_penaltyamt, contact.getPenaltyamt());
            values.put(KEY_pendingdays, contact.getPendingdays());
            values.put(KEY_lastdate, contact.getLastdate());
            values.put(KEY_tomorowdate, contact.getTomorowdate());
            db.insert(TABLE_CONTACTS, null, values);
        }
        db.close();
    }

    public void updateamnt(String cusid, String enrlpaid, String enrlpending) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateqry = "UPDATE " + TABLE_CONTACTS + " SET " + KEY_enrlpaid + " = " + enrlpaid + ","+ KEY_totalenrlpending + " = " + enrlpending + " WHERE " + KEY_Cusid + " = " + cusid;
        db.execSQL(updateqry);
        System.out.println("query ooduthu "+updateqry);
        db.close();
    }

    public ArrayList<Custmodel> getpaidpending(String cusid) {
        ArrayList<Custmodel> contactList = new ArrayList<Custmodel>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE " + KEY_Cusid + " = " + "'" + cusid + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Custmodel contact = new Custmodel();

                contact.setTotalenrlpending(cursor.getString(7));
                contact.setEnrlpaid(cursor.getString(8));
                contact.setAdvanceamt(cursor.getString(5));

                contactList.add(contact);

            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public ArrayList<Custmodel> getAllContacts() {
        ArrayList<Custmodel> contactList = new ArrayList<Custmodel>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Custmodel contact = new Custmodel();
                contact.setCusid(cursor.getString(1));
                contact.setCustomer_id(cursor.getString(2));
                contact.setNAME(cursor.getString(3));
                contact.setMOBILE(cursor.getString(4));
                contact.setAdvanceamt(cursor.getString(5));
                contact.setPendingamt(cursor.getString(6));
                contact.setTotalenrlpending(cursor.getString(7));
                contact.setEnrlpaid(cursor.getString(8));
                contact.setLevel(cursor.getString(9));
                contact.setBonusamt(cursor.getString(10));
                contact.setPenaltyamt(cursor.getString(11));
                contact.setPendingdays(cursor.getString(12));
                contact.setLastdate(cursor.getString(13));
                contact.setTomorowdate(cursor.getString(14));
//                System.out.print(cursor.getString(9) + "\n");

                contactList.add(contact);

            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public ArrayList<Custmodel> getlevelContacts(String level) {
        ArrayList<Custmodel> contactList = new ArrayList<Custmodel>();
        String selectQuery = "SELECT * FROM " + TABLE_CONTACTS;

        if (level.equalsIgnoreCase("1")) {
            selectQuery = "SELECT * FROM " + TABLE_CONTACTS + " WHERE " + KEY_level + " = 'Awaiting Payment' AND " + KEY_pendingamt + " !='0'";


        } else if (level.equalsIgnoreCase("2")) {
            selectQuery = "SELECT * FROM " + TABLE_CONTACTS + " WHERE (" + KEY_level + " = 'Customer Letter' OR " + KEY_level + " = 'Customer, Guarantor Letter') AND " + KEY_pendingamt + " !='0'";


        } else if (level.equalsIgnoreCase("3")) {
            selectQuery = "SELECT * FROM " + TABLE_CONTACTS + " WHERE (" + KEY_level + " = 'Legal Notice' OR " + KEY_level + " = 'Arbitration / Court') AND " + KEY_pendingamt + " !='0'";


        } else {
            selectQuery = "SELECT * FROM " + TABLE_CONTACTS + " WHERE " + KEY_level + " = 'Awaiting Payment' AND " + KEY_pendingamt + " !='0'";


        }

        System.out.print(selectQuery + "\n");

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Custmodel contact = new Custmodel();
                contact.setCusid(cursor.getString(1));
                contact.setCustomer_id(cursor.getString(2));
                contact.setNAME(cursor.getString(3));
                contact.setMOBILE(cursor.getString(4));
                contact.setAdvanceamt(cursor.getString(5));
                contact.setPendingamt(cursor.getString(6));
                contact.setTotalenrlpending(cursor.getString(7));
                contact.setEnrlpaid(cursor.getString(8));
                contact.setLevel(cursor.getString(9));
                contact.setBonusamt(cursor.getString(10));
                contact.setPenaltyamt(cursor.getString(11));
                contact.setPendingdays(cursor.getString(12));
                contact.setLastdate(cursor.getString(13));
                contact.setTomorowdate(cursor.getString(14));
                System.out.print(cursor.getString(9) + "\n");
                contactList.add(contact);

            } while (cursor.moveToNext());
        } else {
            System.out.print("fails" + "\n");

        }
        db.close();

        return contactList;
    }

    /*public String getgroupContacts(String type) {
        ArrayList<ListModel> contactList = new ArrayList<ListModel>();

        String selectQuery = "SELECT " + KEY_CASHIN + " FROM " + TABLE_CONTACTS
                + " WHERE " + KEY_NAME + " = '" + type + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        cursor.moveToFirst();

        String value = cursor.getString(0);
        db.close();

        return value;
    }
*/
    public String gettotaladvance(String cusid) {

        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS + " WHERE " + KEY_Cusid + " = " + cusid;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int total = 0;

        if (cursor.moveToFirst()) {
            do {
                try {
                    total = total + Integer.parseInt(cursor.getString(5));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        db.close();
        return String.valueOf(total);

    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_CONTACTS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int value = cursor.getCount();
        db.close();

        return value;
    }
    public void updatepaid(String paid,String pending, String cusid) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            db.execSQL("UPDATE " + TABLE_CONTACTS + " SET " + KEY_enrlpaid + " = '" + paid + "',"+ KEY_totalenrlpending + " = '" + pending + "'" + "  WHERE " + KEY_Cusid + " = '" + cusid + "'");
            System.out.println("runing query");
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
