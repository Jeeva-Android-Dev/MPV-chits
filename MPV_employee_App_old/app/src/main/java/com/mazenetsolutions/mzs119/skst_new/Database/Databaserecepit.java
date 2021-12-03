package com.mazenetsolutions.mzs119.skst_new.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.mazenetsolutions.mzs119.skst_new.Model.Enrollmodel;
import com.mazenetsolutions.mzs119.skst_new.Model.TempEnrollModel;
import com.mazenetsolutions.mzs119.skst_new.Model.locadvancemodel;

import java.util.ArrayList;


public class Databaserecepit extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "chit_recepit";

    private static final String DB_RECEIPT = "fullreceipt";
    private static final String TABLE_CONTACTS = "recepit";
    private static final String KEY_ID = "id";
    private static final String KEY_enrollid = "enrollid";
    private static final String KEY_Scheme = "Scheme";
    private static final String KEY_Pending_Amt = "Pending_Amt";
    private static final String KEY_Penalty_Amt = "Penalty_Amt";
    private static final String KEY_Bonus_Amt = "Bonus_Amt";
    private static final String KEY_Paid_Amt = "Paid_Amt";
    private static final String KEY_Group_Name = "Group_Name";
    private static final String KEY_payamount = "payamount";
    private static final String KEY_Group_Ticket_Name = "Group_Ticket_Name";
    private static final String KEY_cusbranch = "cusbranch";
    private static final String KEY_pendingdays = "pendingdays";
    private static final String KEY_insamt = "insamt";
    private static final String KEY_CUSID = "cusid";
    private static final String KEY_Advance = "advance_amount";
    private static final String KEY_grpid = "grpid";
    //------------------------------------------------------------------------
    private static final String TABLE_advance = "advancetable";
    private static final String KEY_advid = "aid";
    private static final String KEY_advenrlid = "aenrlid";
    private static final String KEY_advcusid = "acusid";
    private static final String KEY_advamnt = "aamnt";
    //------------------------------------------------------------------------
    private static final String TABLE_VIEW_RECEIPT = "Vrecepit";
    private static final String TABLE_ADVTEMP_RECEIPT = "Vadvrecepit";
    private static final String TABLE_ADVVIEW_RECEIPT = "Vadvviewrecepit";
    private static final String KEY_TEMP_ID = "Tid";
    private static final String TABLE_TEMP_RECEIPT = "Trecepit";
    private static final String KEY_TEMP_enrollid = "Tenrollid";
    private static final String KEY_TEMP_insamt = "Tinsamnt";
    private static final String KEY_TEMP_Pending_Amt = "TPending_Amt";
    private static final String KEY_TEMP_Penalty_Amt = "TPenalty_Amt";
    private static final String KEY_TEMP_Bonus_Amt = "TBonus_Amt";
    private static final String KEY_TEMP_Paid_Amt = "TPaid_Amt";
    private static final String KEY_TEMP_Group_Name = "TGroup_Name";
    private static final String KEY_TEMP_payamount = "Tpayamount";
    private static final String KEY_TEMP_Group_Ticket_Name = "TGroup_Ticket_Name";
    private static final String KEY_TEMP_cusbranch = "Tcusbranch";
    private static final String KEY_TEMP_pendingdays = "Tpendingdays";
    private static final String KEY_TEMP_PAYTYPE = "paytype";
    private static final String KEY_TEMP_tranno = "Ttransno";
    private static final String KEY_TEMP_chebranch = "Tcheqbranch";
    private static final String KEY_TEMP_chebank = "Tcheqbank";
    private static final String KEY_TEMP_chedate = "Tcheqdate";
    private static final String KEY_TEMP_cheno = "Tcheqno";
    private static final String KEY_TEMP_Cusname = "Tcusname";
    private static final String KEY_TEMP_Cusid = "Tcusid";
    private static final String KEY_TEMP_rtgsdate = "Ttransdate";
    private static final String KEY_TEMP_Scheme = "Tscheme";
    private static final String KEY_TEMP_REMARK = "Tremark";
    private static final String KEY_TEMP_STATUS = "Tstatus";
    private static final String KEY_TEMP_Advance = "Tadvance_amount";
    private static final String KEY_TEMP_CRETED = "Tadvance_created";
    private static final String KEY_TEMP_UPDATED = "Tadvance_updated";
    private static final String KEY_TEMP_RECPTAMNT = "Tadvance_recptamnt";
    private static final String KEY_VIEW_Date = "VDate";
    private static final String KEY_VIEW_Time = "VTime";

    public void updatepayamount(String amount, String id, String amount1) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();
            String updatequery = "UPDATE " + TABLE_CONTACTS + " SET " + KEY_payamount + " = '" + amount + "' , " + KEY_insamt + " = '" + amount1 + "' WHERE " + KEY_ID + " = '" + id + "'";
            db.execSQL(updatequery);
            System.out.println("amonut balanceamnt" + amount + "tableid" + id + " insamnt pay2 " + amount1);
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Databaserecepit(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CONTACTS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_enrollid + " TEXT,"
                + KEY_Scheme + " TEXT,"
                + KEY_Pending_Amt + " TEXT,"
                + KEY_Penalty_Amt + " TEXT,"
                + KEY_Bonus_Amt + " TEXT,"
                + KEY_Paid_Amt + " TEXT,"
                + KEY_Group_Name + " TEXT,"
                + KEY_payamount + " TEXT,"
                + KEY_Group_Ticket_Name + " TEXT,"
                + KEY_cusbranch + " TEXT,"
                + KEY_pendingdays + " TEXT,"
                + KEY_insamt + " TEXT,"
                + KEY_Advance + " TEXT,"
                + KEY_grpid + " TEXT"
                + ")";
        String CREATE_ADVANCE_TABLE = "CREATE TABLE " + TABLE_advance + "("
                + KEY_advid + " INTEGER PRIMARY KEY,"
                + KEY_advenrlid + " TEXT,"
                + KEY_advcusid + " TEXT,"
                + KEY_advamnt + " TEXT"
                + ")";
        String CREATE_DB_RECEIPT = "CREATE TABLE " + DB_RECEIPT + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_enrollid + " TEXT,"
                + KEY_Scheme + " TEXT,"
                + KEY_Pending_Amt + " TEXT,"
                + KEY_Penalty_Amt + " TEXT,"
                + KEY_Bonus_Amt + " TEXT,"
                + KEY_Paid_Amt + " TEXT,"
                + KEY_Group_Name + " TEXT,"
                + KEY_payamount + " TEXT,"
                + KEY_Group_Ticket_Name + " TEXT,"
                + KEY_cusbranch + " TEXT,"
                + KEY_pendingdays + " TEXT,"
                + KEY_insamt + " TEXT,"
                + KEY_CUSID + " TEXT,"
                + KEY_Advance + " TEXT,"
                + KEY_grpid + " TEXT"
                + ")";
        String CREATE_TEMP_RECEIPT = "CREATE TABLE " + TABLE_TEMP_RECEIPT + "("
                + KEY_TEMP_ID + " INTEGER PRIMARY KEY,"
                + KEY_TEMP_Cusid + " TEXT,"
                + KEY_TEMP_Cusname + " TEXT,"
                + KEY_TEMP_cheno + " TEXT,"
                + KEY_TEMP_chedate + " TEXT,"
                + KEY_TEMP_chebank + " TEXT,"
                + KEY_TEMP_chebranch + " TEXT,"
                + KEY_TEMP_tranno + " TEXT,"
                + KEY_TEMP_rtgsdate + " TEXT,"
                + KEY_TEMP_enrollid + " TEXT,"
                + KEY_TEMP_Scheme + " TEXT,"
                + KEY_TEMP_Pending_Amt + " TEXT,"
                + KEY_TEMP_Penalty_Amt + " TEXT,"
                + KEY_TEMP_Bonus_Amt + " TEXT,"
                + KEY_TEMP_Paid_Amt + " TEXT,"
                + KEY_TEMP_Group_Name + " TEXT,"
                + KEY_TEMP_payamount + " TEXT,"
                + KEY_TEMP_Group_Ticket_Name + " TEXT,"
                + KEY_TEMP_cusbranch + " TEXT,"
                + KEY_TEMP_pendingdays + " TEXT,"
                + KEY_TEMP_insamt + " TEXT,"
                + KEY_TEMP_PAYTYPE + " TEXT,"
                + KEY_TEMP_REMARK + " TEXT,"
                + KEY_TEMP_STATUS + " TEXT,"
                + KEY_TEMP_Advance + " TEXT,"
                + KEY_VIEW_Date + " TEXT,"
                + KEY_VIEW_Time + " TEXT"
                + ")";
        String CREATE_ADVTEMP_RECEIPT = "CREATE TABLE " + TABLE_ADVTEMP_RECEIPT + "("
                + KEY_TEMP_ID + " INTEGER PRIMARY KEY,"
                + KEY_TEMP_Cusid + " TEXT,"
                + KEY_TEMP_Cusname + " TEXT,"
                + KEY_TEMP_cheno + " TEXT,"
                + KEY_TEMP_chedate + " TEXT,"
                + KEY_TEMP_chebank + " TEXT,"
                + KEY_TEMP_chebranch + " TEXT,"
                + KEY_TEMP_tranno + " TEXT,"
                + KEY_TEMP_rtgsdate + " TEXT,"
                + KEY_TEMP_enrollid + " TEXT,"
                + KEY_TEMP_cusbranch + " TEXT,"
                + KEY_TEMP_PAYTYPE + " TEXT,"
                + KEY_TEMP_REMARK + " TEXT,"
                + KEY_TEMP_STATUS + " TEXT,"
                + KEY_TEMP_CRETED + " TEXT,"
                + KEY_TEMP_UPDATED + " TEXT,"
                + KEY_TEMP_RECPTAMNT + " TEXT,"
                + KEY_VIEW_Date + " TEXT,"
                + KEY_VIEW_Time + " TEXT"
                + ")";
        String CREATE_ADVVIEW_RECEIPT = "CREATE TABLE " + TABLE_ADVVIEW_RECEIPT + "("
                + KEY_TEMP_ID + " INTEGER PRIMARY KEY,"
                + KEY_TEMP_Cusid + " TEXT,"
                + KEY_TEMP_Cusname + " TEXT,"
                + KEY_TEMP_cheno + " TEXT,"
                + KEY_TEMP_chedate + " TEXT,"
                + KEY_TEMP_chebank + " TEXT,"
                + KEY_TEMP_chebranch + " TEXT,"
                + KEY_TEMP_tranno + " TEXT,"
                + KEY_TEMP_rtgsdate + " TEXT,"
                + KEY_TEMP_enrollid + " TEXT,"
                + KEY_TEMP_cusbranch + " TEXT,"
                + KEY_TEMP_PAYTYPE + " TEXT,"
                + KEY_TEMP_REMARK + " TEXT,"
                + KEY_TEMP_STATUS + " TEXT,"
                + KEY_TEMP_CRETED + " TEXT,"
                + KEY_TEMP_UPDATED + " TEXT,"
                + KEY_TEMP_RECPTAMNT + " TEXT,"
                + KEY_VIEW_Date + " TEXT,"
                + KEY_VIEW_Time + " TEXT"
                + ")";
        String CREATE_VIEW_RECEIPT = "CREATE TABLE " + TABLE_VIEW_RECEIPT + "("
                + KEY_TEMP_ID + " INTEGER PRIMARY KEY,"
                + KEY_TEMP_Cusid + " TEXT,"
                + KEY_TEMP_Cusname + " TEXT,"
                + KEY_TEMP_cheno + " TEXT,"
                + KEY_TEMP_chedate + " TEXT,"
                + KEY_TEMP_chebank + " TEXT,"
                + KEY_TEMP_chebranch + " TEXT,"
                + KEY_TEMP_tranno + " TEXT,"
                + KEY_TEMP_rtgsdate + " TEXT,"
                + KEY_TEMP_enrollid + " TEXT,"
                + KEY_TEMP_Scheme + " TEXT,"
                + KEY_TEMP_Pending_Amt + " TEXT,"
                + KEY_TEMP_Penalty_Amt + " TEXT,"
                + KEY_TEMP_Bonus_Amt + " TEXT,"
                + KEY_TEMP_Paid_Amt + " TEXT,"
                + KEY_TEMP_Group_Name + " TEXT,"
                + KEY_TEMP_payamount + " TEXT,"
                + KEY_TEMP_Group_Ticket_Name + " TEXT,"
                + KEY_TEMP_cusbranch + " TEXT,"
                + KEY_TEMP_pendingdays + " TEXT,"
                + KEY_TEMP_insamt + " TEXT,"
                + KEY_TEMP_PAYTYPE + " TEXT,"
                + KEY_TEMP_REMARK + " TEXT,"
                + KEY_TEMP_STATUS + " TEXT,"
                + KEY_TEMP_Advance + " TEXT,"
                + KEY_VIEW_Date + " TEXT,"
                + KEY_VIEW_Time + " TEXT"
                + ")";

        db.execSQL(CREATE_CONTACTS_TABLE);
        db.execSQL(CREATE_TEMP_RECEIPT);
        db.execSQL(CREATE_DB_RECEIPT);
        db.execSQL(CREATE_VIEW_RECEIPT);
        db.execSQL(CREATE_ADVANCE_TABLE);
        db.execSQL(CREATE_ADVTEMP_RECEIPT);
        db.execSQL(CREATE_ADVVIEW_RECEIPT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TEMP_RECEIPT);
        db.execSQL("DROP TABLE IF EXISTS " + DB_RECEIPT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_VIEW_RECEIPT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_advance);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADVTEMP_RECEIPT);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ADVVIEW_RECEIPT);
        onCreate(db);
    }

    public void deletetable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, null, null);
        db.close();
    }

    public void deletetableadvview() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADVVIEW_RECEIPT, null, null);
        db.close();
    }

    public void deletetableaLLREC() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DB_RECEIPT, null, null);
        db.close();
    }

    public void deletetableadvtemp() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ADVTEMP_RECEIPT, null, null);
        db.close();
    }

    public void deletetableview() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_VIEW_RECEIPT, null, null);
        db.close();
    }

    public void deletetableadvance() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_advance, null, null);
        db.close();
    }

    public void deletetabletemp() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TEMP_RECEIPT, null, null);
        db.close();
    }

    public void updatepaidamnt(String cusid, String enrollid, String pending, String paid, String advance) {
        SQLiteDatabase db = this.getWritableDatabase();
        String updateqry = "UPDATE " + DB_RECEIPT + " SET " + KEY_Paid_Amt + " = " + paid + "," + KEY_Pending_Amt + " = " + pending + "," + KEY_Advance + " = '" + advance + "' WHERE " + KEY_CUSID + " = " + "'" + cusid + "'" + " AND " + KEY_enrollid + " = " + "'" + enrollid + "'";
        db.execSQL(updateqry);
        System.out.println("query ooduthuUU");
        db.close();
    }

    public void addallreceipt(ArrayList<Enrollmodel> sched) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < sched.size(); i++) {

            ContentValues values = new ContentValues();
            Enrollmodel contact = sched.get(i);

            values.put(KEY_CUSID, contact.getCusid());
            values.put(KEY_enrollid, contact.getEnrollid());
            values.put(KEY_Scheme, contact.getScheme());
            values.put(KEY_Pending_Amt, contact.getPending_Amt());
            values.put(KEY_Penalty_Amt, contact.getPenalty_Amt());
            values.put(KEY_Bonus_Amt, contact.getBonus_Amt());
            values.put(KEY_Paid_Amt, contact.getPaid_Amt());
            values.put(KEY_Group_Name, contact.getGroup_Name());
            values.put(KEY_payamount, contact.getPayamount());
            values.put(KEY_Group_Ticket_Name, contact.getGroup_Ticket_Name());
            values.put(KEY_cusbranch, contact.getCusbranch());
            values.put(KEY_pendingdays, contact.getPendingdys());
            values.put(KEY_insamt, "0");
            values.put(KEY_Advance, contact.getAdvanceamnt());
            values.put(KEY_grpid, contact.getGrpid());
            db.insert(DB_RECEIPT, null, values);
        }
        db.close();
    }

    public ArrayList<Enrollmodel> getreceiptforcust(String cusid) {
        ArrayList<Enrollmodel> contactList = new ArrayList<Enrollmodel>();
        String selectQuery = "SELECT  * FROM " + DB_RECEIPT + " WHERE " + KEY_CUSID + " = " + "'" + cusid + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Enrollmodel contact = new Enrollmodel();
                contact.setTableid(cursor.getString(0));
                contact.setEnrollid(cursor.getString(1));
                contact.setScheme(cursor.getString(2));
                contact.setPending_Amt(cursor.getString(3));
                contact.setPenalty_Amt(cursor.getString(4));
                contact.setBonus_Amt(cursor.getString(5));
                contact.setPaid_Amt(cursor.getString(6));
                contact.setGroup_Name(cursor.getString(7));
                contact.setPayamount(cursor.getString(8));
                contact.setGroup_Ticket_Name(cursor.getString(9));
                contact.setCusbranch(cursor.getString(10));
                contact.setPendingdys(cursor.getString(11));
                contact.setInsamt(cursor.getString(12));
                contact.setCusid(cursor.getString(13));
                contact.setAdvanceamnt(cursor.getString(14));
                contact.setGrpid(cursor.getString(15));
                contactList.add(contact);


            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public ArrayList<Enrollmodel> getreceiptforcustenroll(String cusid, String enroll) {
        ArrayList<Enrollmodel> contactList = new ArrayList<Enrollmodel>();
        String selectQuery = "SELECT  * FROM " + DB_RECEIPT + " WHERE " + KEY_CUSID + " = " + "'" + cusid + "' AND " + KEY_enrollid + " = " + "'" + enroll + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Enrollmodel contact = new Enrollmodel();
                contact.setTableid(cursor.getString(0));
                contact.setEnrollid(cursor.getString(1));
                contact.setScheme(cursor.getString(2));
                contact.setPending_Amt(cursor.getString(3));
                contact.setPenalty_Amt(cursor.getString(4));
                contact.setBonus_Amt(cursor.getString(5));
                contact.setPaid_Amt(cursor.getString(6));
                contact.setGroup_Name(cursor.getString(7));
                contact.setPayamount(cursor.getString(8));
                contact.setGroup_Ticket_Name(cursor.getString(9));
                contact.setCusbranch(cursor.getString(10));
                contact.setPendingdys(cursor.getString(11));
                contact.setInsamt(cursor.getString(12));
                contact.setCusid(cursor.getString(13));
                contact.setAdvanceamnt(cursor.getString(14));
                contact.setGrpid(cursor.getString(15));
                contactList.add(contact);


            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public void addenroll(ArrayList<Enrollmodel> sched) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < sched.size(); i++) {
            ContentValues values = new ContentValues();

            Enrollmodel contact = sched.get(i);

            values.put(KEY_enrollid, contact.getEnrollid());
            values.put(KEY_Scheme, contact.getScheme());
            values.put(KEY_Pending_Amt, contact.getPending_Amt());
            values.put(KEY_Penalty_Amt, contact.getPenalty_Amt());
            values.put(KEY_Bonus_Amt, contact.getBonus_Amt());
            values.put(KEY_Paid_Amt, contact.getPaid_Amt());
            values.put(KEY_Group_Name, contact.getGroup_Name());
            values.put(KEY_payamount, contact.getPayamount());
            values.put(KEY_Group_Ticket_Name, contact.getGroup_Ticket_Name());
            values.put(KEY_cusbranch, contact.getCusbranch());
            values.put(KEY_pendingdays, contact.getPendingdys());
            values.put(KEY_insamt, "0");
            values.put(KEY_Advance, contact.getAdvanceamnt());
            values.put(KEY_grpid, contact.getGrpid());
            db.insert(TABLE_CONTACTS, null, values);
        }
        db.close();
    }

    public void addalladvance(ArrayList<locadvancemodel> sched) {
        SQLiteDatabase db = this.getWritableDatabase();
        for (int i = 0; i < sched.size(); i++) {
            ContentValues values = new ContentValues();

            locadvancemodel contact = sched.get(i);
            values.put(KEY_advenrlid, contact.getEnrlid());
            values.put(KEY_advcusid, contact.getCusid());
            values.put(KEY_advamnt, contact.getAmnt());
            db.insert(TABLE_advance, null, values);
        }
        db.close();
    }

    public void insertadvance(String cusid, String enrlid, String amnt) {
        SQLiteDatabase db = this.getWritableDatabase();
        String insertqury = "INSERT INTO " + TABLE_advance + "(" + KEY_advcusid + "," + KEY_advenrlid + "," + KEY_advamnt + ") VALUES (" + cusid + "," + enrlid + "," + amnt + ")";
        try {
            System.out.println("inser " + insertqury);
            db.execSQL(insertqury);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        db.close();
    }

    public void addviewreceipt(String cusid, String Cusname, String enrollid, String scheme, String penaltyamnt, String bonusamnt, String paidamnt, String groupname, String payamout, String grp_tic_name, String cusbranch, String pend_days, String insamnt, String cheqno, String pendamnt, String cheqdate, String Cheqbank, String Cheqbranch, String tranno, String transdate, String paytype, String remark, String status, String advance, String date,String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        System.out.println("add viewing");
        ContentValues values = new ContentValues();
        values.put(KEY_TEMP_enrollid, enrollid);
        values.put(KEY_TEMP_Scheme, scheme);
        values.put(KEY_TEMP_Pending_Amt, pendamnt);
        values.put(KEY_TEMP_Penalty_Amt, penaltyamnt);
        values.put(KEY_TEMP_Bonus_Amt, bonusamnt);
        values.put(KEY_TEMP_Paid_Amt, paidamnt);
        values.put(KEY_TEMP_Group_Name, groupname);
        values.put(KEY_TEMP_payamount, payamout);
        values.put(KEY_TEMP_Group_Ticket_Name, grp_tic_name);
        values.put(KEY_TEMP_cusbranch, cusbranch);
        values.put(KEY_TEMP_pendingdays, pend_days);
        values.put(KEY_TEMP_insamt, insamnt);
        values.put(KEY_TEMP_Cusid, cusid);
        values.put(KEY_TEMP_Cusname, Cusname);
        values.put(KEY_TEMP_cheno, cheqno);
        values.put(KEY_TEMP_chedate, cheqdate);
        values.put(KEY_TEMP_chebank, Cheqbank);
        values.put(KEY_TEMP_chebranch, Cheqbranch);
        values.put(KEY_TEMP_tranno, tranno);
        values.put(KEY_TEMP_rtgsdate, transdate);
        values.put(KEY_TEMP_PAYTYPE, paytype);
        values.put(KEY_TEMP_REMARK, remark);
        values.put(KEY_TEMP_STATUS, status);
        values.put(KEY_TEMP_Advance, advance);
        values.put(KEY_VIEW_Date, date);
        values.put(KEY_VIEW_Time, time);
        System.out.println("add viewing" + " datev " + date);
        db.insert(TABLE_VIEW_RECEIPT, null, values);


        db.close();
    }

    public void addtempreceipt(String cusid, String Cusname, String enrollid, String scheme, String penaltyamnt, String bonusamnt, String paidamnt, String groupname, String payamout, String grp_tic_name, String cusbranch, String pend_days, String insamnt, String cheqno, String pendamnt, String cheqdate, String Cheqbank, String Cheqbranch, String tranno, String transdate, String paytype, String remark, String status, String date,String time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TEMP_enrollid, enrollid);
        values.put(KEY_TEMP_Scheme, scheme);
        values.put(KEY_TEMP_Pending_Amt, pendamnt);
        values.put(KEY_TEMP_Penalty_Amt, penaltyamnt);
        values.put(KEY_TEMP_Bonus_Amt, bonusamnt);
        values.put(KEY_TEMP_Paid_Amt, paidamnt);
        values.put(KEY_TEMP_Group_Name, groupname);
        values.put(KEY_TEMP_payamount, payamout);
        values.put(KEY_TEMP_Group_Ticket_Name, grp_tic_name);
        values.put(KEY_TEMP_cusbranch, cusbranch);
        values.put(KEY_TEMP_pendingdays, pend_days);
        values.put(KEY_TEMP_insamt, insamnt);
        values.put(KEY_TEMP_Cusid, cusid);
        values.put(KEY_TEMP_Cusname, Cusname);
        values.put(KEY_TEMP_cheno, cheqno);
        values.put(KEY_TEMP_chedate, cheqdate);
        values.put(KEY_TEMP_chebank, Cheqbank);
        values.put(KEY_TEMP_chebranch, Cheqbranch);
        values.put(KEY_TEMP_tranno, tranno);
        values.put(KEY_TEMP_rtgsdate, transdate);
        System.out.println("============database============");
        System.out.println(cheqno);
        System.out.println(cheqdate);
        System.out.println(Cheqbank);
        System.out.println(Cheqbranch);
        System.out.println(tranno);
        System.out.println(transdate);
        System.out.println("============database============");
        values.put(KEY_TEMP_PAYTYPE, paytype);
        values.put(KEY_TEMP_REMARK, remark);
        values.put(KEY_TEMP_STATUS, status);
        values.put(KEY_VIEW_Date, date);
        values.put(KEY_VIEW_Time, time);
        db.insert(TABLE_TEMP_RECEIPT, null, values);

        db.close();
    }

    public ArrayList<TempEnrollModel> getAllAdvanceTempenroll() {
        ArrayList<TempEnrollModel> contactList = new ArrayList<TempEnrollModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_ADVTEMP_RECEIPT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TempEnrollModel contact = new TempEnrollModel();
                contact.setTableid(cursor.getString(0));
                contact.setCusid(cursor.getString(1));
                contact.setCusname(cursor.getString(2));
                contact.setChequeNo(cursor.getString(3));
                contact.setChequeDate(cursor.getString(4));
                contact.setChequeBank(cursor.getString(5));
                contact.setChequeBranch(cursor.getString(6));
                contact.setTransNo(cursor.getString(7));
                contact.setTransDate(cursor.getString(8));
                contact.setEnrollid(cursor.getString(9));
                contact.setCusbranch(cursor.getString(10));
                contact.setPaytype(cursor.getString(11));
                contact.setRemark(cursor.getString(12));
                contact.setStatus(cursor.getString(13));
                contact.setCreated(cursor.getString(14));
                contact.setUpdated(cursor.getString(15));
                contact.setAmount(cursor.getString(16));
                contact.setRecdate(cursor.getString(17));
                contact.setRectime(cursor.getString(18));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public void addadvancetempreceipt(String cusid, String Cusname, String enrollid, String cusbranch, String cheqno, String cheqdate, String Cheqbank, String Cheqbranch, String tranno, String transdate, String paytype, String remark, String status, String creted, String updated, String amnt, String date,String time) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TEMP_enrollid, enrollid);
        values.put(KEY_TEMP_cusbranch, cusbranch);
        values.put(KEY_TEMP_Cusid, cusid);
        values.put(KEY_TEMP_Cusname, Cusname);
        values.put(KEY_TEMP_cheno, cheqno);
        values.put(KEY_TEMP_chedate, cheqdate);
        values.put(KEY_TEMP_chebank, Cheqbank);
        values.put(KEY_TEMP_chebranch, Cheqbranch);
        values.put(KEY_TEMP_tranno, tranno);
        values.put(KEY_TEMP_rtgsdate, transdate);
        values.put(KEY_TEMP_PAYTYPE, paytype);
        values.put(KEY_TEMP_REMARK, remark);
        values.put(KEY_TEMP_STATUS, status);
        values.put(KEY_TEMP_CRETED, creted);
        values.put(KEY_TEMP_UPDATED, updated);
        values.put(KEY_TEMP_RECPTAMNT, amnt);
        values.put(KEY_VIEW_Date, date);
        values.put(KEY_VIEW_Time, time);
        db.insert(TABLE_ADVTEMP_RECEIPT, null, values);
        db.close();
    }

    public void addadvanceviewreceipt(String cusid, String Cusname, String enrollid, String cusbranch, String cheqno, String cheqdate, String Cheqbank, String Cheqbranch, String tranno, String transdate, String paytype, String remark, String status, String creted, String updated, String amnt, String date,String time) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TEMP_enrollid, enrollid);
        values.put(KEY_TEMP_cusbranch, cusbranch);
        values.put(KEY_TEMP_Cusid, cusid);
        values.put(KEY_TEMP_Cusname, Cusname);
        values.put(KEY_TEMP_cheno, cheqno);
        values.put(KEY_TEMP_chedate, cheqdate);
        values.put(KEY_TEMP_chebank, Cheqbank);
        values.put(KEY_TEMP_chebranch, Cheqbranch);
        values.put(KEY_TEMP_tranno, tranno);
        values.put(KEY_TEMP_rtgsdate, transdate);
        values.put(KEY_TEMP_PAYTYPE, paytype);
        values.put(KEY_TEMP_REMARK, remark);
        values.put(KEY_TEMP_STATUS, status);
        values.put(KEY_TEMP_CRETED, creted);
        values.put(KEY_TEMP_UPDATED, updated);
        values.put(KEY_TEMP_RECPTAMNT, amnt);
        values.put(KEY_VIEW_Date, date);
        values.put(KEY_VIEW_Time, time);
        db.insert(TABLE_ADVVIEW_RECEIPT, null, values);
        db.close();
    }

    public ArrayList<TempEnrollModel> getAdvanceViewenroll(String date) {
        ArrayList<TempEnrollModel> contactList = new ArrayList<TempEnrollModel>();
        String selectQuery = "SELECT * FROM " + TABLE_ADVVIEW_RECEIPT + " WHERE " + KEY_VIEW_Date + " = '" + date + "' ";
        System.out.println("advance " + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TempEnrollModel contact = new TempEnrollModel();
                contact.setTableid(cursor.getString(0));
                contact.setCusid(cursor.getString(1));
                contact.setCusname(cursor.getString(2));
                contact.setChequeNo(cursor.getString(3));
                contact.setChequeDate(cursor.getString(4));
                contact.setChequeBank(cursor.getString(5));
                contact.setChequeBranch(cursor.getString(6));
                contact.setTransNo(cursor.getString(7));
                contact.setTransDate(cursor.getString(8));
                contact.setEnrollid(cursor.getString(9));
                contact.setCusbranch(cursor.getString(10));
                contact.setPaytype(cursor.getString(11));
                contact.setRemark(cursor.getString(12));
                contact.setStatus(cursor.getString(13));
                contact.setCreated(cursor.getString(14));
                contact.setUpdated(cursor.getString(15));
                contact.setPayamount(cursor.getString(16));
                contact.setRecdate(cursor.getString(17));
                contact.setRectime(cursor.getString(18));

                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public ArrayList<TempEnrollModel> getAllViewenroll(String date) {
        ArrayList<TempEnrollModel> contactList = new ArrayList<TempEnrollModel>();
        System.out.println(date + "date no");
        String selectQuery = "SELECT  * FROM " + TABLE_VIEW_RECEIPT + " WHERE " + KEY_VIEW_Date + " = '" + date + "'";
        System.out.println("re " + selectQuery);
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TempEnrollModel contact = new TempEnrollModel();
                contact.setTableid(cursor.getString(0));
                contact.setCusid(cursor.getString(1));
                contact.setCusname(cursor.getString(2));
                contact.setChequeNo(cursor.getString(3));
                contact.setChequeDate(cursor.getString(4));
                contact.setChequeBank(cursor.getString(5));
                contact.setChequeBranch(cursor.getString(6));
                contact.setTransNo(cursor.getString(7));
                contact.setTransDate(cursor.getString(8));
                contact.setEnrollid(cursor.getString(9));
                contact.setScheme(cursor.getString(10));
                contact.setPending_Amt(cursor.getString(11));
                contact.setPenalty_Amt(cursor.getString(12));
                contact.setBonus_Amt(cursor.getString(13));
                contact.setPaid_Amt(cursor.getString(14));
                contact.setGroup_Name(cursor.getString(15));
                contact.setPayamount(cursor.getString(16));
                contact.setGroup_Ticket_Name(cursor.getString(17));
                contact.setCusbranch(cursor.getString(18));
                contact.setPendingdys(cursor.getString(19));
                contact.setInsamt(cursor.getString(20));
                contact.setPaytype(cursor.getString(21));
                contact.setRemark(cursor.getString(22));
                contact.setStatus(cursor.getString(23));
                contact.setAdvance(cursor.getString(24));
                contact.setRecdate(cursor.getString(25));
                contact.setRectime(cursor.getString(26));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public ArrayList<TempEnrollModel> getAllTempenroll() {
        ArrayList<TempEnrollModel> contactList = new ArrayList<TempEnrollModel>();
        String selectQuery = "SELECT  * FROM " + TABLE_TEMP_RECEIPT;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                TempEnrollModel contact = new TempEnrollModel();
                contact.setTableid(cursor.getString(0));
                contact.setCusid(cursor.getString(1));
                contact.setCusname(cursor.getString(2));
                contact.setChequeNo(cursor.getString(3));
                contact.setChequeDate(cursor.getString(4));
                contact.setChequeBank(cursor.getString(5));
                contact.setChequeBranch(cursor.getString(6));
                contact.setTransNo(cursor.getString(7));
                contact.setTransDate(cursor.getString(8));
                contact.setEnrollid(cursor.getString(9));
                contact.setScheme(cursor.getString(10));
                contact.setPending_Amt(cursor.getString(11));
                contact.setPenalty_Amt(cursor.getString(12));
                contact.setBonus_Amt(cursor.getString(13));
                contact.setPaid_Amt(cursor.getString(14));
                contact.setGroup_Name(cursor.getString(15));
                contact.setPayamount(cursor.getString(16));
                contact.setGroup_Ticket_Name(cursor.getString(17));
                contact.setCusbranch(cursor.getString(18));
                contact.setPendingdys(cursor.getString(19));
                contact.setInsamt(cursor.getString(20));
                contact.setPaytype(cursor.getString(21));
                contact.setRemark(cursor.getString(22));
                contact.setStatus(cursor.getString(23));
                contact.setAdvance(cursor.getString(24));
                contact.setRecdate(cursor.getString(25));
                contact.setRectime(cursor.getString(26));
                contactList.add(contact);
            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public ArrayList<Enrollmodel> getAllenroll() {
        ArrayList<Enrollmodel> contactList = new ArrayList<Enrollmodel>();
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                Enrollmodel contact = new Enrollmodel();
                contact.setTableid(cursor.getString(0));
                contact.setEnrollid(cursor.getString(1));
                contact.setScheme(cursor.getString(2));
                contact.setPending_Amt(cursor.getString(3));
                contact.setPenalty_Amt(cursor.getString(4));
                contact.setBonus_Amt(cursor.getString(5));
                contact.setPaid_Amt(cursor.getString(6));
                contact.setGroup_Name(cursor.getString(7));
                contact.setPayamount(cursor.getString(8));
                contact.setGroup_Ticket_Name(cursor.getString(9));
                contact.setCusbranch(cursor.getString(10));
                contact.setPendingdys(cursor.getString(11));
                contact.setInsamt(cursor.getString(12));
                contact.setAdvanceamnt(cursor.getString(13));
                contact.setGrpid(cursor.getString(14));

                contactList.add(contact);


            } while (cursor.moveToNext());
        }
        db.close();

        return contactList;
    }

    public String gettotal() {
        String selectQuery = "SELECT  * FROM " + TABLE_CONTACTS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int total = 0;
        if (cursor.moveToFirst()) {
            do {
                try {
                    total = total + Integer.parseInt(cursor.getString(8));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


            } while (cursor.moveToNext());
        }
        db.close();

        return String.valueOf(total);
    }

    public String gettotaladvancereceipt(String cusid, String enroll) {
        String selectQuery = "SELECT  * FROM " + DB_RECEIPT + " WHERE " + KEY_CUSID + " = " + "'" + cusid + "' AND " + KEY_enrollid + " = " + "'" + enroll + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int total = 0;
        if (cursor.moveToFirst()) {
            do {
                try {
                    total = total + Integer.parseInt(cursor.getString(14));
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }


            } while (cursor.moveToNext());
        }
        db.close();

        return String.valueOf(total);
    }

    public String getadvanceamnount(String cusid, String enroll) {
        String selectQuery = "SELECT  * FROM " + TABLE_advance + " WHERE " + KEY_advcusid + " = " + "'" + cusid + "' AND " + KEY_advenrlid + " = " + "'" + enroll + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        int total = 0;
        if (cursor.moveToFirst()) {
            do {
                total = Integer.parseInt(cursor.getString(3));

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

    public String findadvance(String cusid, String enroll) {
        String selectQuery = "SELECT  * FROM " + TABLE_advance + " WHERE " + KEY_advcusid + " = " + "'" + cusid + "' AND " + KEY_advenrlid + " = " + "'" + enroll + "'";
        String enrollid = "";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(selectQuery, null);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cursor.moveToFirst()) {
            do {
                enrollid = (cursor.getString(2));

            } while (cursor.moveToNext());

        }
        db.close();

        return String.valueOf(enrollid);
    }

    public int getoflinedbCount() {
        String countQuery = "SELECT  * FROM " + DB_RECEIPT;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        int value = cursor.getCount();
        db.close();

        return value;
    }

    public void updateadvance(String amount, String enrlid) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            try {
                db.execSQL("UPDATE " + TABLE_advance + " SET " + KEY_advamnt + " = '" + amount + "'" + "  WHERE " + KEY_advenrlid + " = '" + enrlid + "'");
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("runing query");
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void updateenroll(String paid, String pending, String enrlid) {

        try {
            SQLiteDatabase db = this.getWritableDatabase();

            try {
                String updatesql = "UPDATE " + DB_RECEIPT + " SET " + KEY_Paid_Amt + " = '" + paid + "'," + KEY_Pending_Amt + " = '" + pending + "'" + "  WHERE " + KEY_enrollid + " = '" + enrlid + "'";
                System.out.println(updatesql);
                db.execSQL(updatesql);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            System.out.println("runing query");
            db.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
