package com.mazenetsolutions.mzs119.skst_new;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenetsolutions.mzs119.skst_new.Adapter.CustomAdapterenrollment;
import com.mazenetsolutions.mzs119.skst_new.Database.Databasecustomers;
import com.mazenetsolutions.mzs119.skst_new.Database.Databaserecepit;
import com.mazenetsolutions.mzs119.skst_new.GPS.GPSTracker;
import com.mazenetsolutions.mzs119.skst_new.Model.Custmodel;
import com.mazenetsolutions.mzs119.skst_new.Model.Enrollmodel;
import com.mazenetsolutions.mzs119.skst_new.Utils.AppController;
import com.mazenetsolutions.mzs119.skst_new.Utils.Config;
import com.mazenetsolutions.mzs119.skst_new.Utils.ConnectionDetector;
import com.mazenetsolutions.mzs119.skst_new.Utils.LocationAddress;
import com.mazenetsolutions.mzs119.skst_new.Utils.NDSpinner;
import com.mazenetsolutions.mzs119.skst_new.Utils.NonScrollListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class IndividualReceipt extends AppCompatActivity {

    public ArrayList<Enrollmodel> enroll_list = new ArrayList<Enrollmodel>();
    public ArrayList<Enrollmodel> enroll_listmain = new ArrayList<Enrollmodel>();
    public ArrayList<Enrollmodel> enroll_listpost = new ArrayList<Enrollmodel>();
    public ArrayList<Custmodel> custpaidpend = new ArrayList<>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    String url = Config.reteriveindienroll;
    String url2 = Config.saverecepit;
    String receipt = Config.getreceiptno;
    String Enroll_update_cust = Config.Enroll_update_cust;
    EditText edt_re_amount, edt_re_remark, edt_re_rtgsno;
    TextView txt_totalamt, txt_address, txt_cusname;
    NonScrollListView lst_re_enroll;
    int payatotal = 0, amntpayable = 0;
    String cusid = "0", cusname = "", enrollid = "", groupname = "", Toatal_payable = "0";
    double peding_value=0,penalty_value=0,bonus_value=0;
    String pending_Strig,penalty_string,bonus_string;

    Button btn_autopopulate, btn_submit, btn_che_date, btn_dd_date, btn_rtgs_date;
    CustomAdapterenrollment adapterlist;
    Databaserecepit dbrecepit;
    Databasecustomers dbc;
    String recepitamt = "0", amntpayables = "";
    int Balanceamt = 0;
    NDSpinner spinner;
    String payamount = "";
    ArrayList<String> list = new ArrayList<String>();
    String paytype = "Cash";
    LinearLayout lay_re_che, lay_re_dd, lay_re_rtgs;
    EditText edt_re_cheno, edt_re_chebank, edt_re_chebranch, edt_re_ddno, edt_re_ddbank, edt_re_ddbranch;
    DatePickerDialog fromDatePickerDialog;
    SimpleDateFormat df;
    String str_chedate = "", str_dddate = "", str_rtgsdate = "", str_cheno = "", str_chebank = "", str_chebranch = "", receiptno = "", str_remark = "", str_tranno = "", recptid = "";
    int payable = 0;
    private ProgressDialog pDialog;
    String amount2 = "";
    SimpleDateFormat dateFormat;
    String date = "";
    Button btn_savelocation, btn_showlocation;
    int MY_PERMISSION_ACCESS_COURSE_LOCATION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_individual_receipt);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();


        df = new SimpleDateFormat("dd-MM-yyyy");

        dbc = new Databasecustomers(IndividualReceipt.this);
        cd = new ConnectionDetector(this);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);
        lst_re_enroll = (NonScrollListView) findViewById(R.id.lst_re_enroll_individual);

        lay_re_che = (LinearLayout) findViewById(R.id.lay_re_che_indiv);
        lay_re_dd = (LinearLayout) findViewById(R.id.lay_re_dd_indiv);
        lay_re_rtgs = (LinearLayout) findViewById(R.id.lay_re_rtgs_indiv);

        spinner = (NDSpinner) findViewById(R.id.spn_paytype_indiv);

        edt_re_amount = (EditText) findViewById(R.id.edt_amount_indiv);
        edt_re_remark = (EditText) findViewById(R.id.edt_re_remark_indiv);

        edt_re_cheno = (EditText) findViewById(R.id.edt_re_cheno_indiv);
        edt_re_chebank = (EditText) findViewById(R.id.edt_re_chebank_indiv);
        edt_re_chebranch = (EditText) findViewById(R.id.edt_re_chebranch_indiv);


        edt_re_ddno = (EditText) findViewById(R.id.edt_re_ddno_indiv);
        edt_re_ddbank = (EditText) findViewById(R.id.edt_re_ddbank_indiv);
        edt_re_ddbranch = (EditText) findViewById(R.id.edt_re_ddbranch_indiv);

        edt_re_rtgsno = (EditText) findViewById(R.id.edt_re_rtgsno_indiv);

        // btn_autopopulate = (Button) findViewById(R.id.btn_autopopulate_indiv);
        btn_submit = (Button) findViewById(R.id.btn_submit_indiv);
        btn_che_date = (Button) findViewById(R.id.btn_che_date_indiv);
        btn_dd_date = (Button) findViewById(R.id.btn_dd_date_indiv);
        btn_rtgs_date = (Button) findViewById(R.id.btn_rtgs_date_indiv);
        btn_savelocation = (Button) findViewById(R.id.btn_savelocation);
        btn_showlocation = (Button) findViewById(R.id.btn_showlocation);
        txt_address = (TextView) findViewById(R.id.txt_address);
        txt_cusname = (TextView) findViewById(R.id.txt_cusname);

        dbrecepit = new Databaserecepit(this);

        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        Date date1 = new Date();
        date = dateFormat.format(date1);

        try {
            Intent it = getIntent();
            cusid = it.getStringExtra("cusid");
            cusname = it.getStringExtra("cusname");
            txt_cusname.setText(cusname);
            groupname = it.getStringExtra("groupname");
            enrollid = it.getStringExtra("enrollid");

//
//            iy.putExtra("penalty_value", penalty_value);
//            iy.putExtra("bonus_value", bonus_value);
            pending_Strig=it.getStringExtra("pending_value");
            penalty_string=it.getStringExtra("penalty_value");
            bonus_string=it.getStringExtra("bonus_value");

            peding_value=Double.parseDouble(pending_Strig);
            penalty_value=Double.parseDouble(penalty_string);
            bonus_value=Double.parseDouble(bonus_string);


        } catch (Exception e) {
            e.printStackTrace();
        }
        if (cd.isConnectedToInternet()) {
            reteriveall();
        } else {
            reteriveloca();
        }
        btn_savelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(IndividualReceipt.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(IndividualReceipt.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                    GPSTracker gps = new GPSTracker(IndividualReceipt.this);
                    System.out.println("lat " + gps.getLatitude());
                    System.out.println("lat " + gps.getLongitude());

                    LocationAddress locationAddress = new LocationAddress();
                    locationAddress.getAddressFromLocation(gps.getLatitude(), gps.getLongitude(),
                            getApplicationContext(), new GeocoderHandler());
                } else {
                    ActivityCompat.requestPermissions(IndividualReceipt.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            MY_PERMISSION_ACCESS_COURSE_LOCATION);
                }

            }
        });
        btn_showlocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GPSTracker gps = new GPSTracker(IndividualReceipt.this);
//                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", gps.getLatitude(), gps.getLongitude());
                String uri = "http://maps.google.com/maps?q=loc:" + gps.getLatitude() + "," + gps.getLongitude() + " (" + "Customer" + ")";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                startActivity(intent);
            }
        });
        list.clear();
        list.add("Cash");
        list.add("Cheque");
        list.add("D.D");
        list.add("NEFT");
        list.add("Card");


        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(
                IndividualReceipt.this,
                android.R.layout.simple_spinner_item, list);

        dataAdapter
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(dataAdapter);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view,
                                       int i, long l) {
                paytype = list.get(i);

                if (paytype.equalsIgnoreCase("Cash")) {
                    lay_re_che.setVisibility(View.GONE);
                    lay_re_dd.setVisibility(View.GONE);
                    lay_re_rtgs.setVisibility(View.GONE);

                } else if (paytype.equalsIgnoreCase("Cheque")) {
                    lay_re_che.setVisibility(View.VISIBLE);
                    lay_re_dd.setVisibility(View.GONE);
                    lay_re_rtgs.setVisibility(View.GONE);


                } else if (paytype.equalsIgnoreCase("D.D")) {
                    lay_re_che.setVisibility(View.GONE);
                    lay_re_dd.setVisibility(View.VISIBLE);
                    lay_re_rtgs.setVisibility(View.GONE);


                } else if (paytype.equalsIgnoreCase("NEFT")) {
                    lay_re_che.setVisibility(View.GONE);
                    lay_re_dd.setVisibility(View.GONE);
                    lay_re_rtgs.setVisibility(View.VISIBLE);


                } else if (paytype.equalsIgnoreCase("Card")) {
                    lay_re_che.setVisibility(View.GONE);
                    lay_re_dd.setVisibility(View.GONE);
                    lay_re_rtgs.setVisibility(View.VISIBLE);


                } else {
                    lay_re_che.setVisibility(View.GONE);
                    lay_re_dd.setVisibility(View.GONE);
                    lay_re_rtgs.setVisibility(View.GONE);


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//============================================================================================================
        recepitamt = "0";
        Balanceamt = 0;
/*        btn_autopopulate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                recepitamt = edt_re_amount.getText().toString();

                if (recepitamt.equalsIgnoreCase("0") || recepitamt.equalsIgnoreCase("")) {
                    Toast.makeText(IndividualReceipt.this, "Enter Recepit Amount", Toast.LENGTH_LONG).show();
                } else {
                    Balanceamt = Integer.parseInt(recepitamt);

                    enroll_listmain = dbrecepit.getAllenroll();

                    for (int i = 0; i < enroll_listmain.size(); i++) {
                        Enrollmodel mod = enroll_listmain.get(i);
                        String pendingamt = mod.getPending_Amt();
                        String penaltyamt = mod.getPenalty_Amt();
                        String bonusamt = mod.getBonus_Amt();
                        String tableid = mod.getTableid();


                        if (Balanceamt > 0) {
                            payable = Integer.parseInt(pendingamt) + Integer.parseInt(penaltyamt) - Integer.parseInt(bonusamt);

                            if (Balanceamt >= payable) {

                                int pay2 = Integer.parseInt(pendingamt) - Integer.parseInt(bonusamt);
                                dbrecepit.updatepayamount(String.valueOf(payable), tableid, String.valueOf(pay2));

                                Balanceamt = Balanceamt - payable;
                            } else {
                                int payable1 = Integer.parseInt(pendingamt) + Integer.parseInt(penaltyamt);
                                // int payable2 = payable1 - Balanceamt;
                                int pay2 = Balanceamt - Integer.parseInt(penaltyamt);

                                dbrecepit.updatepayamount(String.valueOf(Balanceamt), tableid, String.valueOf(pay2));

                                Balanceamt = Balanceamt - payable1;
                                if (Balanceamt < 0) {
                                    Balanceamt = 0;
                                }
                            }

                        } else {
                            dbrecepit.updatepayamount(String.valueOf(0), tableid, String.valueOf(0));

                        }
                    }


                    enroll_list = dbrecepit.getAllenroll();
                    adapterlist = new CustomAdapterenrollment(IndividualReceipt.this, enroll_list);
                    adapterlist.notifyDataSetChanged();
                    // ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
                    lst_re_enroll.setAdapter(adapterlist);
                    Toatal_payable=dbrecepit.gettotal();


                }

            }
        }); */
//============================================================================================================
        btn_che_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                fromDatePickerDialog = new DatePickerDialog(IndividualReceipt.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();


                        newDate.set(year, monthOfYear, dayOfMonth);
                        calendar.set(year, monthOfYear, dayOfMonth);

                        try {
                            str_chedate = df.format(newDate.getTime());
                            btn_che_date.setText(str_chedate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, newCalendar.get(Calendar.YEAR),
                        newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.setTitle("Cheque date");

                fromDatePickerDialog.show();

            }
        });
        btn_rtgs_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                fromDatePickerDialog = new DatePickerDialog(IndividualReceipt.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();


                        newDate.set(year, monthOfYear, dayOfMonth);
                        calendar.set(year, monthOfYear, dayOfMonth);

                        try {
                            str_rtgsdate = df.format(newDate.getTime());
                            btn_rtgs_date.setText(str_rtgsdate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, newCalendar.get(Calendar.YEAR),
                        newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.setTitle("NEFT date");

                fromDatePickerDialog.show();

            }
        });


        btn_dd_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                fromDatePickerDialog = new DatePickerDialog(IndividualReceipt.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();


                        newDate.set(year, monthOfYear, dayOfMonth);
                        calendar.set(year, monthOfYear, dayOfMonth);

                        try {
                            str_dddate = df.format(newDate.getTime());
                            btn_dd_date.setText(str_dddate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, newCalendar.get(Calendar.YEAR),
                        newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.setTitle("DD date");

                fromDatePickerDialog.show();

            }
        });
//=============================================================================================================
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_submit.setVisibility(View.GONE);
                //FillAmount();
                // Fillindividual();
                String ticketno = "";
                String penaltyamt = "";
                String pendingamt = "";
                String paidamt = "";
                String bonusamt = "";
                String payableamount = "";
                String enrollid = "";
                String chitvalue = "";
                String cusbranch = "";
                String pendingdays = "";
                String Group = "";
                double pending_double=0;
                String Advance = "";

                //String amount = ;
                amount2 = edt_re_amount.getText().toString();

                str_remark = edt_re_remark.getText().toString();
                enroll_listpost.clear();
                enroll_listpost = dbrecepit.getAllenroll();
                for (int i = 0; i < enroll_listpost.size(); i++) {
                    Enrollmodel mod = enroll_listpost.get(i);


                }
                pendingamt = pending_Strig;
                penaltyamt=penalty_string;
                bonusamt=bonus_string;

                if(penaltyamt==null|| penaltyamt.equals("")){
                    penaltyamt="0";
                }
                if(bonusamt==null|| bonusamt.equals("")){
                    bonusamt="0";
                }
                if(!pendingamt.equals("")&&!pendingamt.equals("0")&&pendingamt!=null){

          //     //    //   pending_double=Double.parseDouble(pendingamt);

                    String v = pending_Strig.replace(",", "");
                    pending_double=Double.parseDouble(v);

                }else
                {
                    pending_double=0;
                }

                double payamount1333=pending_double+Double.parseDouble(penaltyamt)-Double.parseDouble(bonusamt);



                System.out.println("amountttt  " + amount2);
                if ((amount2.equals("")) || (amount2.equals("0"))) {
                    btn_submit.setVisibility(View.VISIBLE);
                    Toast.makeText(IndividualReceipt.this, "Enter Valid Amount", Toast.LENGTH_LONG).show();

                } else if (pendingamt.trim().equalsIgnoreCase("0") || pendingamt.trim().equalsIgnoreCase("")) {
                    Toast.makeText(IndividualReceipt.this, "No Pending Available", Toast.LENGTH_LONG).show();
                    btn_submit.setVisibility(View.VISIBLE);
                } else if(Double.parseDouble(amount2)>payamount1333){
                    Toast.makeText(IndividualReceipt.this, "Amount Exceeds", Toast.LENGTH_LONG).show();
                    btn_submit.setVisibility(View.VISIBLE);
                }
                else {


                    if (paytype.equalsIgnoreCase("Cheque")) {
                        btn_submit.setVisibility(View.VISIBLE);
                        str_chebank = edt_re_chebank.getText().toString();
                        str_chebranch = edt_re_chebranch.getText().toString();
                        str_cheno = edt_re_cheno.getText().toString();

                        if (str_chebank.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            edt_re_chebank.setError("Enter Cheque Bank Name");
                            return;
                            // Toast.makeText(IndividualReceipt.this, "Enter Cheque Bank", Toast.LENGTH_LONG).show();

                        } else if (str_chebranch.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            edt_re_chebranch.setError("Enter Cheque Bank Branch");
                            return;
                            //Toast.makeText(IndividualReceipt.this, "Enter Cheque Bank Branch", Toast.LENGTH_LONG).show();

                        } else if (str_cheno.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            edt_re_cheno.setError("Enter Cheque Number");
                            return;
                            // Toast.makeText(IndividualReceipt.this, "Enter Cheque Number", Toast.LENGTH_LONG).show();

                        } else if (str_chedate.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            Toast.makeText(IndividualReceipt.this, "Select Cheque Date", Toast.LENGTH_LONG).show();
                            return;

                        } else {
                            if (cd.isConnectedToInternet()) {
                                for (int i = 0; i < enroll_listpost.size(); i++) {
                                    Enrollmodel mod = enroll_listpost.get(i);
                                    enrollid = mod.getEnrollid();
                                    bonusamt = mod.getBonus_Amt();
                                    paidamt = mod.getPaid_Amt();
                                    pendingamt = mod.getPending_Amt();
                                    penaltyamt = mod.getPenalty_Amt();
                                    Group = mod.getGrpid();
                                    ticketno = mod.getGroup_Ticket_Name();
                                    payamount = amount2;
                                    chitvalue = mod.getScheme();
                                    cusbranch = mod.getCusbranch();
                                    pendingdays = mod.getPendingdys();
                                    payableamount = amount2;
                                    Advance = mod.getAdvanceamnt();

                                    if (payamount.equalsIgnoreCase("")) {

                                    } else if (payamount.equalsIgnoreCase("0")) {

                                    } else {
                                        try {
                                            if (Advance.isEmpty()) {
                                                Advance = "0";
                                            }
                                        } catch (Exception e) {

                                        }

                                        postentry(enrollid, bonusamt, paidamt, pendingamt, penaltyamt, Group, ticketno, payamount, str_remark, chitvalue, "cheque", cusbranch, pendingdays, "Pending", date);

                                    }
                                }
                            } else {

                                for (int i = 0; i < enroll_listpost.size(); i++) {
                                    Enrollmodel mod = enroll_listpost.get(i);
                                    enrollid = mod.getEnrollid();
                                    bonusamt = mod.getBonus_Amt();
                                    paidamt = mod.getPaid_Amt();
                                    pendingamt = mod.getPending_Amt();
                                    penaltyamt = mod.getPenalty_Amt();
                                    Group = mod.getGrpid();
                                    ticketno = mod.getGroup_Ticket_Name();
                                    payamount = amount2;
                                    chitvalue = mod.getScheme();
                                    cusbranch = mod.getCusbranch();
                                    pendingdays = mod.getPendingdys();
                                    payableamount = amount2;
                                    Advance = mod.getAdvanceamnt();
                                    if (payamount.equalsIgnoreCase("")) {

                                    } else if (payamount.equalsIgnoreCase("0")) {

                                    } else {
                                        try {
                                            if (Advance.isEmpty()) {
                                                Advance = "0";
                                            }
                                        } catch (Exception e) {

                                        }
                                        int pen = Integer.parseInt(pendingamt);
                                        if (pen <= 0) {
                                            System.out.println("pending amnty enter");
                                            int totadvance = ((Integer.parseInt(amount2)) + (Integer.parseInt(Advance)));
                                            Toatal_payable = String.valueOf(totadvance);
                                            System.out.println("pending amnty enter payablwe" + Toatal_payable);
                                        }
                                        if (pen > 0) {

                                            int totadvance = ((Integer.parseInt(amount2)) - (Integer.parseInt(pendingamt)) + (Integer.parseInt(Advance)));
                                            if (totadvance <= 0) {
                                                Toatal_payable = "0";
                                            } else {
                                                Toatal_payable = String.valueOf(totadvance);
                                            }

                                        }
                                        Toast.makeText(IndividualReceipt.this, "Saved Offline", Toast.LENGTH_SHORT).show();
                                        dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, str_cheno, pendingamt, str_chedate, str_chebank, str_chebranch, str_tranno, "", "cheque", str_remark, "Pending", Toatal_payable, date, Config.Currenttime());
                                        dbrecepit.addtempreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, str_cheno, pendingamt, str_chedate, str_chebank, str_chebranch, str_tranno, "", "cheque", str_remark, "Pending", date, Config.Currenttime());
                                        String paid = String.valueOf(Integer.parseInt(paidamt) + Integer.parseInt(payableamount));
                                        String pending = String.valueOf(Integer.parseInt(pendingamt) - Integer.parseInt(payableamount));
                                        if (Integer.parseInt(pending) < 0) {
                                            pending = "0";
                                        }
                                        dbc.updatepaid(paid, pending, cusid);
                                        dbrecepit.updateenroll(paid, pending, enrollid);
                                    }
                                }
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                        IndividualReceipt.this, R.style.MyAlertDialogStyleApproved);
                                alertDialog.setTitle("Success");
                                alertDialog
                                        .setMessage("Receipt has been generated Successfully. Do you want to print a Receipt?");
                                alertDialog.setCancelable(false);
                                alertDialog.setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                dialog.cancel();
                                                Intent i = new Intent(IndividualReceipt.this, CollectionActivity.class);
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(i);
                                                finish();
                                            }
                                        });
                                final String finalBonusamt = bonusamt;
                                final String finalTicketno = ticketno;
                                final String finalPendingamt = pendingamt;
                                final String finalPenaltyamt = penaltyamt;
                                final String finalPayableamount = payableamount;
                                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent it = new Intent(IndividualReceipt.this, PrintActivity.class);
                                        it.putExtra("Cusname", cusname);
                                        it.putExtra("Amount", amount2);
                                        it.putExtra("Groupname", groupname);
                                        it.putExtra("ticketno", finalTicketno);
                                        it.putExtra("paymode", paytype);
                                        it.putExtra("bonusamnt", finalBonusamt);
                                        it.putExtra("pendingamnt", finalPendingamt);
                                        it.putExtra("penaltyamnt", finalPenaltyamt);
                                        it.putExtra("instlmntamnt", finalPayableamount);
                                        it.putExtra("advance", Toatal_payable);
                                        it.putExtra("Receiptno", "");
                                        startActivity(it);

                                        finish();
                                    }
                                });
                                alertDialog.show();
                            }
                        }


                    } else if (paytype.equalsIgnoreCase("D.D")) {

                        str_chebank = edt_re_ddbank.getText().toString();
                        str_chebranch = edt_re_ddbranch.getText().toString();
                        str_cheno = edt_re_ddno.getText().toString();
                        str_chedate = str_dddate;
                        if (str_chebank.equalsIgnoreCase("")) {
                            edt_re_ddbank.setError("Enter DD Bank");
                            btn_submit.setVisibility(View.VISIBLE);
                            return;
                            // Toast.makeText(IndividualReceipt.this, "Enter DD Bank", Toast.LENGTH_LONG).show();

                        } else if (str_chebranch.equalsIgnoreCase("")) {
                            edt_re_ddbranch.setError("Enter DD Bank Branch");
                            btn_submit.setVisibility(View.VISIBLE);
                            return;
                            //Toast.makeText(IndividualReceipt.this, "Enter DD Bank Branch", Toast.LENGTH_LONG).show();

                        } else if (str_cheno.equalsIgnoreCase("")) {
                            edt_re_ddno.setError("Enter DD Number");
                            btn_submit.setVisibility(View.VISIBLE);
                            return;
                            // Toast.makeText(IndividualReceipt.this, "Enter DD Number", Toast.LENGTH_LONG).show();

                        } else if (str_dddate.equalsIgnoreCase("")) {
                            Toast.makeText(IndividualReceipt.this, "Select DD Date", Toast.LENGTH_LONG).show();
                            btn_submit.setVisibility(View.VISIBLE);
                            return;


                        } else {
                            if (cd.isConnectedToInternet()) {
                                for (int i = 0; i < enroll_listpost.size(); i++) {
                                    Enrollmodel mod = enroll_listpost.get(i);
                                    enrollid = mod.getEnrollid();
                                    bonusamt = mod.getBonus_Amt();
                                    paidamt = mod.getPaid_Amt();
                                    pendingamt = mod.getPending_Amt();
                                    penaltyamt = mod.getPenalty_Amt();
                                    Group = mod.getGrpid();
                                    ticketno = mod.getGroup_Ticket_Name();
                                    payamount = amount2;
                                    chitvalue = mod.getScheme();
                                    cusbranch = mod.getCusbranch();
                                    pendingdays = mod.getPendingdys();
                                    payableamount = amount2;
                                    Advance = mod.getAdvanceamnt();
                                    if (payamount.equalsIgnoreCase("")) {

                                    } else if (payamount.equalsIgnoreCase("0")) {

                                    } else {
                                        try {
                                            if (Advance.isEmpty()) {
                                                Advance = "0";
                                            }
                                        } catch (Exception e) {

                                        }
                                        postentry(enrollid, bonusamt, paidamt, pendingamt, penaltyamt, Group, ticketno, payamount, str_remark, chitvalue, "dd", cusbranch, pendingdays, "Active", date);
                                    }


                                }
                            } else {
                                for (int i = 0; i < enroll_listpost.size(); i++) {
                                    Enrollmodel mod = enroll_listpost.get(i);
                                    enrollid = mod.getEnrollid();
                                    bonusamt = mod.getBonus_Amt();
                                    paidamt = mod.getPaid_Amt();
                                    pendingamt = mod.getPending_Amt();
                                    penaltyamt = mod.getPenalty_Amt();
                                    Group = mod.getGrpid();
                                    ticketno = mod.getGroup_Ticket_Name();
                                    payamount = amount2;
                                    chitvalue = mod.getScheme();
                                    cusbranch = mod.getCusbranch();
                                    pendingdays = mod.getPendingdys();
                                    payableamount = amount2;
                                    Advance = mod.getAdvanceamnt();
                                    if (payamount.equalsIgnoreCase("")) {

                                    } else if (payamount.equalsIgnoreCase("0")) {

                                    } else {
                                        System.out.println("temppppppppppppp_tableeeeeeeeeee");
                                       /* if (((Integer.parseInt(amount2)) > (Integer.parseInt(pendingamt)))) {
                                            payatotal = ((Integer.parseInt(amount2)) - (Integer.parseInt(pendingamt)));
                                            Toatal_payable = String.valueOf(payatotal);
                                            amntpayable = ((Integer.parseInt(amount2)) - (Integer.parseInt(Toatal_payable)));
                                            amntpayables = String.valueOf(amntpayable);
                                            dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, amntpayables, ticketno, cusbranch, pendingdays, amntpayables, str_cheno, pendingamt, "", str_chebank, str_chebranch, str_tranno, str_chedate, "dd", str_remark, "Active", Toatal_payable);
                                            dbrecepit.addtempreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, amntpayables, ticketno, cusbranch, pendingdays, amntpayables, str_cheno, pendingamt, "", str_chebank, str_chebranch, str_tranno, str_chedate, "dd", str_remark, "Active", Toatal_payable);
                                        } else { */
                                        try {
                                            if (Advance.isEmpty()) {
                                                Advance = "0";
                                            }
                                        } catch (Exception e) {

                                        }
                                        int pen = Integer.parseInt(pendingamt);
                                        if (pen <= 0) {
                                            System.out.println("pending amnty enter");
                                            int totadvance = ((Integer.parseInt(amount2)) + (Integer.parseInt(Advance)));
                                            Toatal_payable = String.valueOf(totadvance);
                                            System.out.println("pending amnty enter payablwe" + Toatal_payable);
                                        }
                                        if (pen > 0) {

                                            int totadvance = ((Integer.parseInt(amount2)) - (Integer.parseInt(pendingamt)) + (Integer.parseInt(Advance)));
                                            if (totadvance <= 0) {
                                                Toatal_payable = "0";
                                            } else {
                                                Toatal_payable = String.valueOf(totadvance);
                                            }

                                        }
                                        Toast.makeText(IndividualReceipt.this, "Saved Offline", Toast.LENGTH_SHORT).show();
                                        dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, str_cheno, pendingamt, "", str_chebank, str_chebranch, str_tranno, str_chedate, "dd", str_remark, "Active", Toatal_payable, date, Config.Currenttime());
                                        dbrecepit.addtempreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, str_cheno, pendingamt, "", str_chebank, str_chebranch, str_tranno, str_chedate, "dd", str_remark, "Active", date, Config.Currenttime());
                                        String paid = String.valueOf(Integer.parseInt(paidamt) + Integer.parseInt(payableamount));
                                        String pending = String.valueOf(Integer.parseInt(pendingamt) - Integer.parseInt(payableamount));
                                        if (Integer.parseInt(pending) < 0) {
                                            pending = "0";
                                        }
                                        dbc.updatepaid(paid, pending, cusid);
                                        dbrecepit.updateenroll(paid, pending, enrollid);
                                    }
                                }
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                        IndividualReceipt.this, R.style.MyAlertDialogStyleApproved);
                                alertDialog.setTitle("Success");
                                alertDialog
                                        .setMessage("Receipt has been generated Successfully. Do you want to print a Receipt?");
                                alertDialog.setCancelable(false);
                                alertDialog.setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                dialog.cancel();
                                                Intent i = new Intent(IndividualReceipt.this, CollectionActivity.class);
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(i);
                                                finish();
                                            }
                                        });
                                final String finalBonusamt = bonusamt;
                                final String finalTicketno = ticketno;
                                final String finalPendingamt = pendingamt;
                                final String finalPenaltyamt = penaltyamt;
                                final String finalPayableamount = payableamount;
                                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent it = new Intent(IndividualReceipt.this, PrintActivity.class);
                                        it.putExtra("Cusname", cusname);
                                        it.putExtra("Amount", amount2);
                                        it.putExtra("Groupname", groupname);
                                        it.putExtra("ticketno", finalTicketno);
                                        it.putExtra("paymode", paytype);
                                        it.putExtra("bonusamnt", finalBonusamt);
                                        it.putExtra("pendingamnt", finalPendingamt);
                                        it.putExtra("penaltyamnt", finalPenaltyamt);
                                        it.putExtra("instlmntamnt", finalPayableamount);
                                        it.putExtra("advance", Toatal_payable);
                                        it.putExtra("Receiptno", "");
                                        startActivity(it);

                                        finish();
                                    }
                                });
                                alertDialog.show();
                            }
                        }







                    } else if (paytype.equalsIgnoreCase("NEFT")) {


                        str_tranno = edt_re_rtgsno.getText().toString();


                        if (str_rtgsdate.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            Toast.makeText(IndividualReceipt.this, "Select Transaction Date", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            if (cd.isConnectedToInternet()) {
                                for (int i = 0; i < enroll_listpost.size(); i++) {
                                    Enrollmodel mod = enroll_listpost.get(i);
                                    enrollid = mod.getEnrollid();
                                    bonusamt = mod.getBonus_Amt();
                                    paidamt = mod.getPaid_Amt();
                                    pendingamt = mod.getPending_Amt();
                                    penaltyamt = mod.getPenalty_Amt();
                                    Group = mod.getGrpid();
                                    ticketno = mod.getGroup_Ticket_Name();
                                    payamount = amount2;
                                    chitvalue = mod.getScheme();
                                    cusbranch = mod.getCusbranch();
                                    pendingdays = mod.getPendingdys();
                                    payableamount = amount2;
                                    Advance = mod.getAdvanceamnt();
                                    if (payamount.equalsIgnoreCase("")) {

                                    } else if (payamount.equalsIgnoreCase("0")) {

                                    } else {
                                        try {
                                            if (Advance.isEmpty()) {
                                                Advance = "0";
                                            }
                                        } catch (Exception e) {

                                        }
                                        postentry(enrollid, bonusamt, paidamt, pendingamt, penaltyamt, Group, ticketno, payamount, str_remark, chitvalue, "neft", cusbranch, pendingdays, "Active", date);

                                    }

                                }
                            } else {
                                for (int i = 0; i < enroll_listpost.size(); i++) {
                                    Enrollmodel mod = enroll_listpost.get(i);
                                    enrollid = mod.getEnrollid();
                                    bonusamt = mod.getBonus_Amt();
                                    paidamt = mod.getPaid_Amt();
                                    pendingamt = mod.getPending_Amt();
                                    penaltyamt = mod.getPenalty_Amt();
                                    Group = mod.getGrpid();
                                    ticketno = mod.getGroup_Ticket_Name();
                                    payamount = amount2;
                                    chitvalue = mod.getScheme();
                                    cusbranch = mod.getCusbranch();
                                    pendingdays = mod.getPendingdys();
                                    payableamount = amount2;
                                    Advance = mod.getAdvanceamnt();
                                    if (payamount.equalsIgnoreCase("")) {

                                    } else if (payamount.equalsIgnoreCase("0")) {

                                    } else {
                                        System.out.println("temppppppppppppp_tableeeeeeeeeee");
                                        try {
                                            if (Advance.isEmpty()) {
                                                Advance = "0";
                                            }
                                        } catch (Exception e) {

                                        }
                                        int pen = Integer.parseInt(pendingamt);
                                        if (pen <= 0) {
                                            System.out.println("pending amnty enter");
                                            int totadvance = ((Integer.parseInt(amount2)) + (Integer.parseInt(Advance)));
                                            Toatal_payable = String.valueOf(totadvance);
                                            System.out.println("pending amnty enter payablwe" + Toatal_payable);
                                        }
                                        if (pen > 0) {

                                            int totadvance = ((Integer.parseInt(amount2)) - (Integer.parseInt(pendingamt)) + (Integer.parseInt(Advance)));
                                            if (totadvance <= 0) {
                                                Toatal_payable = "0";
                                            } else {
                                                Toatal_payable = String.valueOf(totadvance);
                                            }

                                        }
                                        Toast.makeText(IndividualReceipt.this, "Saved Offline", Toast.LENGTH_SHORT).show();
                                        dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, "", pendingamt, "", "", "", str_tranno, str_rtgsdate, "neft", str_remark, "Active", Toatal_payable, date, Config.Currenttime());
                                        dbrecepit.addtempreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, "", pendingamt, "", "", "", str_tranno, str_rtgsdate, "neft", str_remark, "Active", date, Config.Currenttime());
                                        String paid = String.valueOf(Integer.parseInt(paidamt) + Integer.parseInt(payableamount));
                                        String pending = String.valueOf(Integer.parseInt(pendingamt) - Integer.parseInt(payableamount));
                                        if (Integer.parseInt(pending) < 0) {
                                            pending = "0";
                                        }
                                        dbc.updatepaid(paid, pending, cusid);
                                        dbrecepit.updateenroll(paid, pending, enrollid);
                                    }
                                }
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                        IndividualReceipt.this, R.style.MyAlertDialogStyleApproved);
                                alertDialog.setTitle("Success");
                                alertDialog
                                        .setMessage("Receipt has been generated Successfully. Do you want to print a Receipt?");
                                alertDialog.setCancelable(false);
                                alertDialog.setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                dialog.cancel();
                                                Intent i = new Intent(IndividualReceipt.this, CollectionActivity.class);
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(i);
                                                finish();
                                            }
                                        });
                                final String finalBonusamt = bonusamt;
                                final String finalTicketno = ticketno;
                                final String finalPendingamt = pendingamt;
                                final String finalPenaltyamt = penaltyamt;
                                final String finalPayableamount = payableamount;
                                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent it = new Intent(IndividualReceipt.this, PrintActivity.class);
                                        it.putExtra("Cusname", cusname);
                                        it.putExtra("Amount", amount2);
                                        it.putExtra("Groupname", groupname);
                                        it.putExtra("ticketno", finalTicketno);
                                        it.putExtra("paymode", paytype);
                                        it.putExtra("bonusamnt", finalBonusamt);
                                        it.putExtra("pendingamnt", finalPendingamt);
                                        it.putExtra("penaltyamnt", finalPenaltyamt);
                                        it.putExtra("instlmntamnt", finalPayableamount);
                                        it.putExtra("advance", Toatal_payable);
                                        it.putExtra("Receiptno", "");
                                        startActivity(it);

                                        finish();
                                    }
                                });
                                alertDialog.show();
                            }

                            // updateenroll();

                        }

                    } else if (paytype.equalsIgnoreCase("Card")) {


                        str_tranno = edt_re_rtgsno.getText().toString();


                        if (str_rtgsdate.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            Toast.makeText(IndividualReceipt.this, "Select Transaction Date", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            if (cd.isConnectedToInternet()) {
                                for (int i = 0; i < enroll_listpost.size(); i++) {
                                    Enrollmodel mod = enroll_listpost.get(i);
                                    enrollid = mod.getEnrollid();
                                    bonusamt = mod.getBonus_Amt();
                                    paidamt = mod.getPaid_Amt();
                                    pendingamt = mod.getPending_Amt();
                                    penaltyamt = mod.getPenalty_Amt();
                                    Group = mod.getGrpid();
                                    ticketno = mod.getGroup_Ticket_Name();
                                    payamount = amount2;
                                    chitvalue = mod.getScheme();
                                    cusbranch = mod.getCusbranch();
                                    pendingdays = mod.getPendingdys();
                                    payableamount = amount2;
                                    Advance = mod.getAdvanceamnt();
                                    if (payamount.equalsIgnoreCase("")) {

                                    } else if (payamount.equalsIgnoreCase("0")) {

                                    } else {
                                        try {
                                            if (Advance.isEmpty()) {
                                                Advance = "0";
                                            }
                                        } catch (Exception e) {

                                        }

                                        postentry(enrollid, bonusamt, paidamt, pendingamt, penaltyamt, Group, ticketno, payamount, str_remark, chitvalue, "card", cusbranch, pendingdays, "Active", date);
                                    }


                                }
                            } else {
                                for (int i = 0; i < enroll_listpost.size(); i++) {
                                    Enrollmodel mod = enroll_listpost.get(i);
                                    enrollid = mod.getEnrollid();
                                    bonusamt = mod.getBonus_Amt();
                                    paidamt = mod.getPaid_Amt();
                                    pendingamt = mod.getPending_Amt();
                                    penaltyamt = mod.getPenalty_Amt();
                                    Group = mod.getGrpid();
                                    ticketno = mod.getGroup_Ticket_Name();
                                    payamount = amount2;
                                    chitvalue = mod.getScheme();
                                    cusbranch = mod.getCusbranch();
                                    pendingdays = mod.getPendingdys();
                                    payableamount = amount2;
                                    Advance = mod.getAdvanceamnt();
                                    if (payamount.equalsIgnoreCase("")) {

                                    } else if (payamount.equalsIgnoreCase("0")) {

                                    } else {
                                        System.out.println("temppppppppppppp_tableeeeeeeeeee");
                                        try {
                                            if (Advance.isEmpty()) {
                                                Advance = "0";
                                            }
                                        } catch (Exception e) {

                                        }
                                        int pen = Integer.parseInt(pendingamt);
                                        if (pen <= 0) {
                                            System.out.println("pending amnty enter");
                                            int totadvance = ((Integer.parseInt(amount2)) + (Integer.parseInt(Advance)));
                                            Toatal_payable = String.valueOf(totadvance);
                                            System.out.println("pending amnty enter payablwe" + Toatal_payable);
                                        }
                                        if (pen > 0) {

                                            int totadvance = ((Integer.parseInt(amount2)) - (Integer.parseInt(pendingamt)) + (Integer.parseInt(Advance)));
                                            if (totadvance <= 0) {
                                                Toatal_payable = "0";
                                            } else {
                                                Toatal_payable = String.valueOf(totadvance);
                                            }

                                        }
                                        Toast.makeText(IndividualReceipt.this, "Saved Offline", Toast.LENGTH_SHORT).show();
                                        dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, "", pendingamt, "", "", "", str_tranno, str_rtgsdate, "card", str_remark, "Active", Toatal_payable, date, Config.Currenttime());
                                        dbrecepit.addtempreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, "", pendingamt, "", "", "", str_tranno, str_rtgsdate, "card", str_remark, "Active", date, Config.Currenttime());
                                        String paid = String.valueOf(Integer.parseInt(paidamt) + Integer.parseInt(payableamount));
                                        String pending = String.valueOf(Integer.parseInt(pendingamt) - Integer.parseInt(payableamount));
                                        if (Integer.parseInt(pending) < 0) {
                                            pending = "0";
                                        }
                                        dbc.updatepaid(paid, pending, cusid);
                                        dbrecepit.updateenroll(paid, pending, enrollid);
                                    }
                                }
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                        IndividualReceipt.this, R.style.MyAlertDialogStyleApproved);
                                alertDialog.setTitle("Success");
                                alertDialog
                                        .setMessage("Receipt has been generated Successfully. Do you want to print a Receipt?");
                                alertDialog.setCancelable(false);
                                alertDialog.setNegativeButton("No",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog,
                                                                int which) {
                                                dialog.cancel();
                                                Intent i = new Intent(IndividualReceipt.this, CollectionActivity.class);
                                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(i);
                                                finish();
                                            }
                                        });
                                final String finalBonusamt = bonusamt;
                                final String finalTicketno = ticketno;
                                final String finalPendingamt = pendingamt;
                                final String finalPenaltyamt = penaltyamt;
                                final String finalPayableamount = payableamount;
                                alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent it = new Intent(IndividualReceipt.this, PrintActivity.class);
                                        it.putExtra("Cusname", cusname);
                                        it.putExtra("Amount", amount2);
                                        it.putExtra("Groupname", groupname);
                                        it.putExtra("ticketno", finalTicketno);
                                        it.putExtra("paymode", paytype);
                                        it.putExtra("bonusamnt", finalBonusamt);
                                        it.putExtra("pendingamnt", finalPendingamt);
                                        it.putExtra("penaltyamnt", finalPenaltyamt);
                                        it.putExtra("instlmntamnt", finalPayableamount);
                                        it.putExtra("advance", Toatal_payable);
                                        it.putExtra("Receiptno", "");
                                        startActivity(it);

                                        finish();
                                    }
                                });
                                alertDialog.show();
                            }
                        }
                        //updateenroll();


                    } else {
                        if (cd.isConnectedToInternet()) {

                            for (int i = 0; i < enroll_listpost.size(); i++) {
                                Enrollmodel mod = enroll_listpost.get(i);
                                enrollid = mod.getEnrollid();
                                bonusamt = mod.getBonus_Amt();
                                paidamt = mod.getPaid_Amt();
                                pendingamt = mod.getPending_Amt();
                                penaltyamt = mod.getPenalty_Amt();
                                Group = mod.getGrpid();
                                ticketno = mod.getGroup_Ticket_Name();
                                payamount = amount2;
                                chitvalue = mod.getScheme();
                                cusbranch = mod.getCusbranch();
                                pendingdays = mod.getPendingdys();
                                payableamount = amount2;
                                Advance = mod.getAdvanceamnt();
                                if (payamount.equalsIgnoreCase("")) {

                                } else if (payamount.equalsIgnoreCase("0")) {

                                } else {
                                    System.out.println("internetttttttttttttttttttt");
                                    try {
                                        if (Advance.isEmpty()) {
                                            Advance = "0";
                                        }
                                    } catch (Exception e) {

                                    }

                                    postentry(enrollid, bonusamt, paidamt, pendingamt, penaltyamt, Group, ticketno, payamount, str_remark, chitvalue, "cash", cusbranch, pendingdays, "Active", date);

                                }
                            }


                        } else {
                            for (int i = 0; i < enroll_listpost.size(); i++) {
                                Enrollmodel mod = enroll_listpost.get(i);
                                enrollid = mod.getEnrollid();
                                bonusamt = mod.getBonus_Amt();
                                paidamt = mod.getPaid_Amt();
                                pendingamt = mod.getPending_Amt();
                                penaltyamt = mod.getPenalty_Amt();
                                Group = mod.getGrpid();
                                ticketno = mod.getGroup_Ticket_Name();
                                payamount = amount2;
                                chitvalue = mod.getScheme();
                                cusbranch = mod.getCusbranch();
                                pendingdays = mod.getPendingdys();
                                payableamount = amount2;
                                Advance = mod.getAdvanceamnt();
                                if (payamount.equalsIgnoreCase("")) {

                                } else if (payamount.equalsIgnoreCase("0")) {

                                } else {
                                    System.out.println("temppppppppppppp_tableeeeeeeeeee");
                                    try {
                                        if (Advance.isEmpty()) {
                                            Advance = "0";
                                        }
                                    } catch (Exception e) {

                                    }
                                    // payatotal = ((Integer.parseInt(amount2)) - (Integer.parseInt(pendingamt)));
                                    System.out.println("pending amnty " + pendingamt);
                                    System.out.println("pending amnty " + Integer.parseInt(pendingamt));
                                    int pen = Integer.parseInt(pendingamt);
                                    if (pen <= 0) {
                                        System.out.println("pending amnty enter");
                                        int totadvance = ((Integer.parseInt(amount2)) + (Integer.parseInt(Advance)));
                                        Toatal_payable = String.valueOf(totadvance);
                                        System.out.println("pending amnty enter payablwe" + Toatal_payable);
                                    }
                                    if (pen > 0) {

                                        int totadvance = ((Integer.parseInt(amount2)) - (Integer.parseInt(pendingamt)) + (Integer.parseInt(Advance)));
                                        if (totadvance <= 0) {
                                            Toatal_payable = "0";
                                        } else {
                                            Toatal_payable = String.valueOf(totadvance);
                                        }

                                    }
                                    //String.valueOf(payatotal);
                                    // amntpayable = ((Integer.parseInt(amount2)) - (Integer.parseInt(Toatal_payable)));
                                    //  amntpayables = String.valueOf(amntpayable);

                                    Toast.makeText(IndividualReceipt.this, "Saved Offline", Toast.LENGTH_SHORT).show();
                                    dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, "", pendingamt, "", "", "", "", "", "cash", str_remark, "Active", Toatal_payable, date, Config.Currenttime());
                                    dbrecepit.addtempreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, cusbranch, pendingdays, payableamount, "", pendingamt, "", "", "", "", "", "cash", str_remark, "Active", date, Config.Currenttime());
                                    String paid = String.valueOf(Integer.parseInt(paidamt) + Integer.parseInt(payableamount));
                                    String pending = String.valueOf(Integer.parseInt(pendingamt) - Integer.parseInt(payableamount));
                                    if (Integer.parseInt(pending) < 0) {
                                        pending = "0";
                                    }
                                    dbc.updatepaid(paid, pending, cusid);
                                    dbrecepit.updateenroll(paid, pending, enrollid);
                                }
                            }
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                    IndividualReceipt.this, R.style.MyAlertDialogStyleApproved);
                            alertDialog.setTitle("Success");
                            alertDialog
                                    .setMessage("Receipt has been generated Successfully.");
                            alertDialog.setCancelable(false);
                            alertDialog.setNegativeButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.cancel();
                                            Intent i = new Intent(IndividualReceipt.this, CollectionActivity.class);
                                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                            startActivity(i);
                                            finish();
                                        }
                                    });
                            final String finalBonusamt = bonusamt;
                            final String finalTicketno = ticketno;
                            final String finalPendingamt = pendingamt;
                            final String finalPenaltyamt = penaltyamt;
                            final String finalPayableamount = payableamount;
//                            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialog, int which) {
//                                    dialog.dismiss();
//                                    Intent it = new Intent(IndividualReceipt.this, PrintActivity.class);
//                                    it.putExtra("Cusname", cusname);
//                                    it.putExtra("Amount", amount2);
//                                    it.putExtra("Groupname", groupname);
//                                    it.putExtra("ticketno", finalTicketno);
//                                    it.putExtra("paymode", paytype);
//                                    it.putExtra("bonusamnt", finalBonusamt);
//                                    it.putExtra("pendingamnt", finalPendingamt);
//                                    it.putExtra("penaltyamnt", finalPenaltyamt);
//                                    it.putExtra("instlmntamnt", finalPayableamount);
//                                    it.putExtra("advance", Toatal_payable);
//                                    it.putExtra("Receiptno", "");
//                                    startActivity(it);
//
//                                    finish();
//                                }
//                            });
                            alertDialog.show();

                        }

                    }
                    System.out.println("pendingggggg" + pendingamt);
                    System.out.println("paayyyamounttttt" + paidamt);
//                    int pend = (Integer.parseInt(pendingamt)) - (Integer.parseInt(payamount));
//                    String pendi = Integer.toString(pend);
//                    int pai = (Integer.parseInt(paidamt)) + (Integer.parseInt(payamount));
//                    String paid = Integer.toString(pai);
//
//                    //onBackPressed();
//                    if (!cd.isConnectedToInternet()) {
//                        String Enrl_Paid = "", Enrl_Pending = "", Enrl_advance = "", Total_Enrl_Paid = "", Total_Enrl_Pending = "", Total_Enrl_advance = "";
//                           String receiptadv = dbrecepit.gettotaladvancereceipt(cusid, enrollid);
//                          System.out.println("receiptadvvv "+receiptadv);
//                          int receiptadvt = ((Integer.parseInt(Toatal_payable) + (Integer.parseInt(receiptadv))));
//                         String receiptadvtot = Integer.toString(receiptadvt);
//                         System.out.println("receiptadvtot "+receiptadv);
//                        dbrecepit.updatepaidamnt(cusid, enrollid, pendi, paid, Toatal_payable);
//                        custpaidpend = dbc.getpaidpending(cusid);
//                        for (int i = 0; i < custpaidpend.size(); i++) {
//                            Custmodel cmo = custpaidpend.get(i);
//                            Enrl_Paid = cmo.getEnrlpaid();
//                            Enrl_Pending = cmo.getTotalenrlpending();
//                            Enrl_advance = cmo.getAdvanceamt();
//
//                        }
//                        int Total_Enrl_Pendin = (Integer.parseInt(Enrl_Pending)) - (Integer.parseInt(payamount));
//                        int Total_Enrl_Pai = (Integer.parseInt(Enrl_Paid)) + (Integer.parseInt(payamount));
//                        Total_Enrl_Pending = Integer.toString(Total_Enrl_Pendin);
//                        Total_Enrl_Paid = Integer.toString(Total_Enrl_Pai);
//                        //Total_Enrl_advance=Toatal_payable;
//                        String advancetotal = dbc.gettotaladvance(cusid);
//                          System.out.println("advancetotal cust "+receiptadv);
//                          int advancetot = (Integer.parseInt(Toatal_payable) + (Integer.parseInt(Enrl_advance)));
//                         String advancxeupdate = String.valueOf(advancetot);
//                          System.out.println("advancxeupdate custy "+receiptadv);
//                        dbc.updateamnt(cusid, Total_Enrl_Paid, Total_Enrl_Pending);
//
//                    } else {
//
//                    }
                    // } else {
                    //  Toast.makeText(IndividualReceipt.this, "Amount Mismatch", Toast.LENGTH_LONG).show();


                }

            }
        });
//==============================================================================================================
    }


    private void reteriveloca() {
        showDialog();
        enroll_list.clear();
        dbrecepit.deletetable();
        enroll_list = dbrecepit.getreceiptforcustenroll(cusid, enrollid);
        dbrecepit.addenroll(enroll_list);
        adapterlist = new CustomAdapterenrollment(IndividualReceipt.this, enroll_list);
        adapterlist.notifyDataSetChanged();
        //   ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
        lst_re_enroll.setAdapter(adapterlist);
        hidePDialog();

    }

    public void reteriveall() {

        showDialog();
        enroll_list.clear();
        // dbrecepit.deletetable();
        StringRequest internetreq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("retrive_enroll_response " + response);
                hidePDialog();


                try {

                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("customer");

                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);

                            Enrollmodel sched = new Enrollmodel();
                            sched.setEnrollid(jObj.getString("Id"));
                            sched.setScheme(jObj.getString("Chit_value"));
                            sched.setPending_Amt(jObj.getString("Pending_Amt"));
                            sched.setPaid_Amt(jObj.getString("Paid_Amt"));
                            sched.setPenalty_Amt(jObj.getString("Penalty_Amt"));
                            sched.setBonus_Amt(jObj.getString("Bonus_Amt"));
                            sched.setGroup_Name(jObj.getString("Group_Name"));
                            sched.setGroup_Ticket_Name(jObj.getString("Group_Ticket_Name"));
                            sched.setCusbranch(jObj.getString("Branch_Id"));
                            sched.setPendingdys(jObj.getString("Pending_Days"));
                            sched.setAdvanceamnt(jObj.getString("Advance_Amt"));
                            sched.setAdvanceAvail(jObj.getString("advance_available"));
                            sched.setGrpid(jObj.getString("grpid"));
                            sched.setPayamount("0");
                            sched.setUpcoming_dueamount("");
                            sched.setUpcommimg_due_date("");
                            sched.setUpcommimg_installment_no("");

                            enroll_list.add(sched);

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (enroll_list.size() > 0) {
                        try {
                            dbrecepit.deletetable();
                        } catch (Exception e) {

                        }

                        try {

                            dbrecepit.addenroll(enroll_list);
                            adapterlist = new CustomAdapterenrollment(IndividualReceipt.this, enroll_list);
                            adapterlist.notifyDataSetChanged();
                            //ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
                            lst_re_enroll.setAdapter(adapterlist);

                        } catch (Exception e) {

                            e.printStackTrace();

                        }

                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                IndividualReceipt.this);
                        alertDialog.setTitle("Information");
                        alertDialog
                                .setMessage("No Data from Server. contact Admin");

                        alertDialog.setPositiveButton("ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();
                                    }
                                });
                        alertDialog.setNegativeButton("Close",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        onBackPressed();
                                    }
                                });
                        alertDialog.show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Cust_Id", cusid);
                params.put("enrollid", enrollid);
               System.out.print("Retrive enroll param"+ params);
                return params;
            }

        };
        internetreq.setRetryPolicy(new DefaultRetryPolicy(80000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(internetreq);
    }

    private void hidePDialog() {
        if (pDialog.isShowing()) {
            pDialog.dismiss();

        }
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
        pDialog.setContentView(R.layout.my_progress);

    }

    //============================================================================================================================
    public void postentry(final String enrollidd, final String bonusamt, final String paidamt, final String pendingamt, final String penaltyamt, final String Group, final String ticketno, final String payableamount, final String Remark, final String chitvalue, final String paytype, final String Cus_Branch, final String Pending_Days, final String status, final String date) {

        showDialog();
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response);
                System.out.println("coleeeeeeeeeee" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    receiptno = obj.getString("receiptno");
                    recptid = obj.getString("receiptid");
                    String status = obj.getString("status");
                    String receivedamnt = obj.getString("receivedamnt");
                    final String Pending_Amt = obj.getString("Pending_Amt");
                    String Total_Enrl_Paid = obj.getString("Total_Enrl_Paid");
                    String Total_Enrl_Pending = obj.getString("Total_Enrl_Pending");
                    final String advanceamnt;
                    String advanceamnt1 = "0";
                    try {
                        advanceamnt1 = obj.getString("advanceamnt");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        advanceamnt1 = "0";
                        e.printStackTrace();
                    }
//                    System.out.println("advancee amnt postentry" + advanceamnt);
                    advanceamnt = advanceamnt1;
                    int pend = (Integer.parseInt(pendingamt)) - (Integer.parseInt(payamount));
                    String pendi = Integer.toString(pend);
                    int pai = (Integer.parseInt(paidamt)) + (Integer.parseInt(payamount));
                    String paid = Integer.toString(pai);

//                    System.out.println("pending amnt postentry" + pendi);
//                    System.out.println("paid amnt postentry" + paid);
                    dbrecepit.updatepaidamnt(cusid, enrollid, pendi, paid, advanceamnt);
//                    System.out.println("Collection gbewingb" + status);
//                    System.out.println("receivedddddddddddn gbewrgvuivivbnrbgnringb" + receivedamnt);
//                    System.out.println("Pending_Amt gbewrgvuivivbnrbgnringb" + Pending_Amt);
//                    System.out.println("Total_Enrl_Paid gbewrgvuivivbnrbgnringb" + Total_Enrl_Paid);
//                    System.out.println("Total_Enrl_Pending gbewrgvuivivbnrbgnringb" + Total_Enrl_Pending);

                    if (status.equals("1")) {
                        hidePDialog();
                        System.out.println("Collection Activity + receir");
                        String advancetotal = dbc.gettotaladvance(cusid);
                        int advancetot = ((convertStringtoInt(advanceamnt) + (convertStringtoInt(advancetotal))));
                        String advancxeupdate = String.valueOf(advancetot);
                        dbc.updateamnt(cusid, Total_Enrl_Paid, Total_Enrl_Pending);
                        dbrecepit.updateenroll(paid, Total_Enrl_Pending, enrollid);
                        editor.putString("receiptamnt", receivedamnt);
                        editor.commit();
                        if (paytype.equalsIgnoreCase("cash")) {
                            dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, Cus_Branch, Pending_Days, payableamount, "", pendingamt, "", "", "", "", "", "cash", str_remark, "Active", advanceamnt, date, Config.Currenttime());
                        } else if (paytype.equalsIgnoreCase("cheque")) {
                            dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, Cus_Branch, Pending_Days, payableamount, str_cheno, pendingamt, str_chedate, str_chebank, str_chebranch, str_tranno, "", "cheque", str_remark, "Pending", advanceamnt, date, Config.Currenttime());
                        } else if (paytype.equalsIgnoreCase("dd")) {
                            dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, Cus_Branch, Pending_Days, payableamount, str_cheno, pendingamt, "", str_chebank, str_chebranch, str_tranno, str_chedate, "dd", str_remark, "Active", advanceamnt, date, Config.Currenttime());
                        } else if (paytype.equalsIgnoreCase("neft")) {
                            dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, Cus_Branch, Pending_Days, payableamount, "", pendingamt, "", "", "", str_tranno, str_rtgsdate, "neft", str_remark, "Active", advanceamnt, date, Config.Currenttime());
                        } else if (paytype.equalsIgnoreCase("card")) {
                            dbrecepit.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, payamount, ticketno, Cus_Branch, Pending_Days, payableamount, "", pendingamt, "", "", "", str_tranno, str_rtgsdate, "card", str_remark, "Active", advanceamnt, date, Config.Currenttime());
                        }
                        sendsms(recptid);
//                        Intent i = new Intent(IndividualReceipt.this, CollectionActivity.class);
//                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                        startActivity(i);
//                        finish();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                IndividualReceipt.this, R.style.MyAlertDialogStyleApproved);
                        alertDialog.setTitle("Success");
                        alertDialog
//                                .setMessage("Receipt has been generated Successfully. Do you want to send invoice through WhatsApp ?");
                                .setMessage("Receipt has been generated Successfully.");
                        alertDialog.setCancelable(false);
//                        alertDialog.setNegativeButton("Yes",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,
//                                                        int which) {
//                                        dialog.cancel();
//                                        String whatsappurl = null;
//                                        try {
//                                            whatsappurl = Config.mobile_receipt_print + "rno=" + receiptno + "&Cust_Id=" + cusid;
////                                           sendwhatsapp();
//                                            System.out.println("what " + whatsappurl);
//                                            HttpAsyncTask hat = new HttpAsyncTask();
//                                            hat.execute(whatsappurl);
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        Intent i = new Intent(IndividualReceipt.this, CollectionActivity.class);
//                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        startActivity(i);
//                                        finish();
//                                    }
//                                });
                        alertDialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                Intent i = new Intent(IndividualReceipt.this, CollectionActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                                finish();
                            }
                        });
                        alertDialog.show();

                    } else {
                        btn_submit.setVisibility(View.VISIBLE);
                        builddilogdecline();
                    }
                    hidePDialog();


                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                builddilogdecline();
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidePDialog();
                btn_submit.setVisibility(View.VISIBLE);

            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                double input=Double.parseDouble(payableamount);

              double pnlty_amt;
                if(penaltyamt.equalsIgnoreCase("")){
                    pnlty_amt=0;
                }else {

                    pnlty_amt=Double.parseDouble(penaltyamt);

                }

                double result=input-pnlty_amt;

                String result_payable=String.valueOf(result);

                Map<String, String> params = new HashMap<String, String>();
                params.put("Cust_Id", cusid);
                params.put("enrollid", enrollidd);
                params.put("bonusamt", bonusamt);
                params.put("paidamt", paidamt);
                params.put("pendingamt", pendingamt);
                params.put("penaltyamt", penaltyamt);
                params.put("Groupid", Group);
                params.put("ticketno", ticketno);
                params.put("payamount", payableamount);
                params.put("Emp_Id", pref.getString("userid", "0"));
                params.put("Created_By", pref.getString("username", "DEMO"));
                params.put("Remark", Remark);
                params.put("Customer_Name", cusname);
                params.put("Chit_Value", chitvalue);
                params.put("Payment_Type", paytype);
                params.put("Cheque_No", str_cheno);
                params.put("Cheque_Date", str_chedate);
                params.put("Bank_Name", str_chebank);
                params.put("Branch_Name", str_chebranch);
                params.put("Trn_No", str_tranno);
                params.put("Trn_Date", str_rtgsdate);
                params.put("Cus_Branch", Cus_Branch);
                params.put("Pending_Days", Pending_Days);
                params.put("Emp_Branch", pref.getString("empbranch", "3"));
                params.put("status", status);
                params.put("recdate", date);
                params.put("rectime", Config.Currenttime());

                System.out.println("Submit Data"+params+"    penaltl"+penaltyamt+"  bonus "+bonusamt);
                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(90000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    private void sendsms(final String recpid) {
        StringRequest localreq = new StringRequest(Request.Method.POST,
                Config.send_receipt_sms, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                hidePDialog();

            }


        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("recpid", recpid);
                return params;
            }

        };
        localreq.setRetryPolicy(new DefaultRetryPolicy(4000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(localreq);
    }

    public void addtoviewdb() {

    }

    private void Fillindividual() {
        recepitamt = edt_re_amount.getText().toString();
        String pendingamt = "", tableid = "";
        if (recepitamt.equalsIgnoreCase("0") || recepitamt.equalsIgnoreCase("")) {
            Toast.makeText(IndividualReceipt.this, "Enter Receipt Amount", Toast.LENGTH_LONG).show();
        } else {
            Balanceamt = Integer.parseInt(recepitamt);

            enroll_listmain = dbrecepit.getAllenroll();

            for (int i = 0; i < enroll_listmain.size(); i++) {
                Enrollmodel mod = enroll_listmain.get(i);
                pendingamt = mod.getPending_Amt();
                String penaltyamt = mod.getPenalty_Amt();
                String bonusamt = mod.getBonus_Amt();
                tableid = mod.getTableid();
            }
            if (Balanceamt > 0) {
                dbrecepit.updatepayamount(String.valueOf(Balanceamt), tableid, String.valueOf(pendingamt));
            }
            enroll_list = dbrecepit.getAllenroll();
            adapterlist = new CustomAdapterenrollment(IndividualReceipt.this, enroll_list);
            adapterlist.notifyDataSetChanged();
            // ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
            lst_re_enroll.setAdapter(adapterlist);
            Toatal_payable = dbrecepit.gettotal();
        }
    }

    private void FillAmount() {
        recepitamt = edt_re_amount.getText().toString();

        if (recepitamt.equalsIgnoreCase("0") || recepitamt.equalsIgnoreCase("")) {
            Toast.makeText(IndividualReceipt.this, "Enter Receipt Amount", Toast.LENGTH_LONG).show();
        } else {
            Balanceamt = Integer.parseInt(recepitamt);

            enroll_listmain = dbrecepit.getAllenroll();

            for (int i = 0; i < enroll_listmain.size(); i++) {
                Enrollmodel mod = enroll_listmain.get(i);
                String pendingamt = mod.getPending_Amt();
                String penaltyamt = mod.getPenalty_Amt();
                String bonusamt = mod.getBonus_Amt();
                String tableid = mod.getTableid();


                if (Balanceamt > 0) {
                    payable = Integer.parseInt(pendingamt);//+ Integer.parseInt(penaltyamt) - Integer.parseInt(bonusamt);

                    if (Balanceamt >= payable) {

                        int pay2 = Integer.parseInt(pendingamt) - Integer.parseInt(bonusamt);
                        dbrecepit.updatepayamount(String.valueOf(payable), tableid, String.valueOf(pay2));

                        Balanceamt = Balanceamt - payable;
                    } else {
                        int payable1 = Integer.parseInt(pendingamt) + Integer.parseInt(penaltyamt);
                        // int payable2 = payable1 - Balanceamt;
                        int pay2 = Balanceamt - Integer.parseInt(penaltyamt);

                        dbrecepit.updatepayamount(String.valueOf(Balanceamt), tableid, String.valueOf(pay2));

                        Balanceamt = Balanceamt - payable1;
                        if (Balanceamt < 0) {
                            Balanceamt = 0;
                        }
                    }
                    // dbrecepit.updatepayamount(String.valueOf(payable), tableid, String.valueOf(pendingamt));
                } else {
                    dbrecepit.updatepayamount(String.valueOf(0), tableid, String.valueOf(0));
                }


            }


            enroll_list = dbrecepit.getAllenroll();
            adapterlist = new CustomAdapterenrollment(IndividualReceipt.this, enroll_list);
            adapterlist.notifyDataSetChanged();
            // ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
            lst_re_enroll.setAdapter(adapterlist);
            Toatal_payable = dbrecepit.gettotal();


        }
    }

    private class GeocoderHandler extends Handler {
        @Override
        public void handleMessage(Message message) {
            String locationAddress;
            switch (message.what) {
                case 1:
                    Bundle bundle = message.getData();
                    locationAddress = bundle.getString("address");
                    break;
                default:
                    locationAddress = null;
            }
            txt_address.setText(locationAddress);
        }
    }

    public void builddilogsuccess() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                IndividualReceipt.this, R.style.MyAlertDialogStyleApproved);
        alertDialog.setTitle("Success");
        alertDialog
                .setMessage("Receipt has been generated Successfully");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();
                        Intent i = new Intent(IndividualReceipt.this, CollectionActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void builddilogdecline() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                IndividualReceipt.this, R.style.MyAlertDialogStyleDeclined);
        alertDialog.setTitle("Error");
        alertDialog
                .setMessage("Receipt Entry Not Saved. Try again");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... urls) {

            return httpRequestResponse(urls[0]);
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {

        }
    }

    //For HttpAsync Functions: sending requests and receiving responses
    public static String httpRequestResponse(String url) {
        InputStream inputStream = null;
        String result = "";
        try {
            // create HttpClient
            HttpClient httpclient = new DefaultHttpClient();

            // make GET request to the given URL
            HttpResponse httpResponse = httpclient.execute(new HttpGet(url));

            // receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // convert InputStream to string
            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "InputStream did not work";

        } catch (Exception e) {
            Log.d("InputStream", e.getLocalizedMessage());
        }

        return result;
    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

    public static String makeStringZeroifNull(String string) {
        try {
            if (string.isEmpty() || string.equalsIgnoreCase("") || string.equalsIgnoreCase("null")) {
                string = "0";
                return string;
            }
        } catch (Exception e) {
            string = "0";

        }
        return string;

    }

    public static int convertStringtoInt(String string) {
        int formattedInt = 0;
        string = makeStringZeroifNull(string);
        string = string.replaceAll(",", "");
        string = string.replaceAll(" ", "");
        try {
            formattedInt = Integer.parseInt(string);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return formattedInt;
    }
    public void sendwhatsapp() {


        System.out.println("advance idd " + enrollid);
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.mobile_receipt_print, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("rno", receiptno);
                params.put("Cust_Id", cusid);
                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
}
