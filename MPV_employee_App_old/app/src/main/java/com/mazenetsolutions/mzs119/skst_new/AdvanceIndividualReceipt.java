package com.mazenetsolutions.mzs119.skst_new;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenetsolutions.mzs119.skst_new.Adapter.CustomAdapterreceiptgenearte;
import com.mazenetsolutions.mzs119.skst_new.Database.Databasecustomers;
import com.mazenetsolutions.mzs119.skst_new.Database.Databaserecepit;
import com.mazenetsolutions.mzs119.skst_new.Model.Custmodel;
import com.mazenetsolutions.mzs119.skst_new.Model.Enrollmodel;
import com.mazenetsolutions.mzs119.skst_new.Utils.AppController;
import com.mazenetsolutions.mzs119.skst_new.Utils.Config;
import com.mazenetsolutions.mzs119.skst_new.Utils.ConnectionDetector;
import com.mazenetsolutions.mzs119.skst_new.Utils.NonScrollListView;

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

public class AdvanceIndividualReceipt extends AppCompatActivity {

    public ArrayList<Enrollmodel> enroll_list = new ArrayList<Enrollmodel>();
    public ArrayList<Enrollmodel> enroll_listmain = new ArrayList<Enrollmodel>();
    public ArrayList<Enrollmodel> enroll_listpost = new ArrayList<Enrollmodel>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    SimpleDateFormat df;
    Spinner spn_paytype;
    TextView txt_cusname;
    Databaserecepit dbrecepit;
    String paytpe[] = {"Cash", "D.D", "Cheque", "RTGS/NEFT", "Card"};
    private ProgressDialog pDialog;
    String cusid = "0", cusname = "", enrollid = "", groupname = "", Toatal_payable = "";
    NonScrollListView lst_re_enroll;
    Databasecustomers dbcust;
    Databaserecepit dbrec;
    String url = Config.reteriveindienroll_adv;
    ArrayList<Custmodel> customerArray = new ArrayList<>();
    ArrayList<Custmodel> customer_listmain = new ArrayList<>();
    String reteriveuser = Config.reteriveusers;
    LinearLayout cheque_lay, dd_lay, rtgs_lay;
    CustomAdapterreceiptgenearte adapterlist;
    String advance = "", isadvc = "";
    EditText edt_remark, edt_bankname, edt_bankbranch, edt_cheno, edt_ddno, edt_ddbank, edt_ddbranch, edt_rtgsno, edt_cusname, edt_receiptamnt, edt_enrol, edt_advanceavail, edt_totalamnt;
    Button btn_submit, btn_transdate, btn_chequedate, btn_dddate;
    ArrayAdapter<String> paytypeadpter, amnttypeadapter;
    DatePickerDialog fromDatePickerDialog;
    //    CustomerAdapterAdvance customeradpater;
    String cid = "", pay_mode = "", str_dddate = "", str_chedate = "", str_transdate = "", str_enrollid = "", str_amnttype = "", cadvance = "", tot = "", cname = "", Groupname = "", Ticketno = "", subid = "", mobile = "";
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advance_individual_receipt);

        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();

        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);
        df = new SimpleDateFormat("dd-MM-yyyy");

        Date date1 = new Date();
        date = df.format(date1);
        dbcust = new Databasecustomers(AdvanceIndividualReceipt.this);
        dbrec = new Databaserecepit(AdvanceIndividualReceipt.this);
        cd = new ConnectionDetector(this);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);
        lst_re_enroll = (NonScrollListView) findViewById(R.id.list_advanceindiv);
        cheque_lay = (LinearLayout) findViewById(R.id.lay_adc_che);
        dd_lay = (LinearLayout) findViewById(R.id.lay_adc_dd);
        rtgs_lay = (LinearLayout) findViewById(R.id.lay_adc_rtgs);
        btn_chequedate = (Button) findViewById(R.id.btn_adc_che_date);
        btn_dddate = (Button) findViewById(R.id.btn_adc_dd_date);
        btn_transdate = (Button) findViewById(R.id.btn_adc_rtgs_date);
        btn_submit = (Button) findViewById(R.id.btn_adc_submit);
        edt_cheno = (EditText) findViewById(R.id.edt_adc_cheno);
        edt_ddno = (EditText) findViewById(R.id.edt_adc_ddno);
        edt_bankname = (EditText) findViewById(R.id.edt_adc_chebank);
        edt_bankbranch = (EditText) findViewById(R.id.edt_adc_chebranch);
        edt_ddbank = (EditText) findViewById(R.id.edt_adc_ddbank);
        edt_ddbranch = (EditText) findViewById(R.id.edt_adc_ddbranch);
        edt_remark = (EditText) findViewById(R.id.edt_adc_remark);
        edt_rtgsno = (EditText) findViewById(R.id.edt_adc_rtgsno);
        edt_receiptamnt = (EditText) findViewById(R.id.edt_adc_receiptamnt);
        edt_totalamnt = (EditText) findViewById(R.id.edt_adc_totaladvance);
        edt_advanceavail = (EditText) findViewById(R.id.edt_adc_availadvnce);
        spn_paytype = (Spinner) findViewById(R.id.spn_adc_paytype);
        txt_cusname = (TextView) findViewById(R.id.txt_cusname);
        try {
            Intent it = getIntent();
            cusid = it.getStringExtra("cusid");
            cusname = it.getStringExtra("cusname");
            groupname = it.getStringExtra("groupname");
            enrollid = it.getStringExtra("enrollid");
//            subid = it.getStringExtra("subid");
//            mobile = it.getStringExtra("mobile");
            txt_cusname.setText("Name : " + cusname);


        } catch (Exception e) {
            e.printStackTrace();
        }




        if (cd.isConnectedToInternet()) {
            reteriveall();
            reteriveadvance();
        } else {
            reteriveloca();
            getlocaladvance(cusid, enrollid);
        }


        paytypeadpter = new ArrayAdapter<String>(AdvanceIndividualReceipt.this, android.R.layout.simple_dropdown_item_1line, paytpe);
        spn_paytype.setAdapter(paytypeadpter);


        spn_paytype.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                String selecteditem = spn_paytype.getSelectedItem().toString();
                if (selecteditem.equalsIgnoreCase("Cash")) {
                    pay_mode = "cash";
                    cheque_lay.setVisibility(View.GONE);
                    rtgs_lay.setVisibility(View.GONE);
                    dd_lay.setVisibility(View.GONE);
                    edt_remark.setVisibility(View.VISIBLE);
                    btn_submit.setVisibility(View.VISIBLE);
                } else if (selecteditem.equalsIgnoreCase("D.D")) {
                    pay_mode = "dd";
                    cheque_lay.setVisibility(View.GONE);
                    rtgs_lay.setVisibility(View.GONE);
                    dd_lay.setVisibility(View.VISIBLE);
                    btn_submit.setVisibility(View.VISIBLE);
                    edt_remark.setVisibility(View.VISIBLE);
                } else if (selecteditem.equalsIgnoreCase("Cheque")) {
                    pay_mode = "cheque";
                    cheque_lay.setVisibility(View.VISIBLE);
                    rtgs_lay.setVisibility(View.GONE);
                    dd_lay.setVisibility(View.GONE);
                    btn_submit.setVisibility(View.VISIBLE);
                    edt_remark.setVisibility(View.VISIBLE);
                } else if (selecteditem.equalsIgnoreCase("RTGS/NEFT")) {
                    pay_mode = "neft";
                    cheque_lay.setVisibility(View.GONE);
                    rtgs_lay.setVisibility(View.VISIBLE);
                    btn_submit.setVisibility(View.VISIBLE);
                    dd_lay.setVisibility(View.GONE);
                    edt_remark.setVisibility(View.VISIBLE);





                } else if (selecteditem.equalsIgnoreCase("Card")) {
                    pay_mode = "card";
                    cheque_lay.setVisibility(View.GONE);
                    rtgs_lay.setVisibility(View.VISIBLE);
                    btn_submit.setVisibility(View.VISIBLE);
                    dd_lay.setVisibility(View.GONE);
                    edt_remark.setVisibility(View.VISIBLE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        edt_receiptamnt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_totalamnt.setText(tot);
            }
        });

        btn_chequedate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                fromDatePickerDialog = new DatePickerDialog(AdvanceIndividualReceipt.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();


                        newDate.set(year, monthOfYear, dayOfMonth);
                        calendar.set(year, monthOfYear, dayOfMonth);

                        try {
                            str_chedate = df.format(newDate.getTime());
                            btn_chequedate.setText(str_chedate);
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
        btn_transdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                fromDatePickerDialog = new DatePickerDialog(AdvanceIndividualReceipt.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();


                        newDate.set(year, monthOfYear, dayOfMonth);
                        calendar.set(year, monthOfYear, dayOfMonth);

                        try {
                            str_transdate = df.format(newDate.getTime());
                            btn_transdate.setText(str_transdate);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, newCalendar.get(Calendar.YEAR),
                        newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.setTitle("RTGS date");

                fromDatePickerDialog.show();

            }
        });


        btn_dddate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar newCalendar = Calendar.getInstance();

                fromDatePickerDialog = new DatePickerDialog(AdvanceIndividualReceipt.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();


                        newDate.set(year, monthOfYear, dayOfMonth);
                        calendar.set(year, monthOfYear, dayOfMonth);

                        try {
                            str_dddate = df.format(newDate.getTime());
                            btn_dddate.setText(str_dddate);
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
//====================================================================================================================
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btn_submit.setVisibility(View.INVISIBLE);
                final String recpt_amnt = edt_receiptamnt.getText().toString();

                if (((edt_receiptamnt.getText().toString()).equalsIgnoreCase("")) || ((edt_receiptamnt.getText().toString()).equalsIgnoreCase("0"))) {
                    btn_submit.setVisibility(View.VISIBLE);
                    edt_receiptamnt.setError("Enter valid Amount");
                    edt_receiptamnt.requestFocus();
                    return;
//                    Toast.makeText(AdvanceIndividualReceipt.this, "Enter Amount", Toast.LENGTH_SHORT).show();

                } else {
                    if (pay_mode.equalsIgnoreCase("cash")) {
                        if (cd.isConnectedToInternet()) {
                            dailycollection(recpt_amnt, "", "", "", "", "", "");
                            dbrec.addadvanceviewreceipt(cusid, cusname, enrollid, "", "", str_chedate, "", "", "", "", pay_mode, edt_remark.getText().toString(), "Active", "", "", recpt_amnt, date,Config.Currenttime());
                        } else {

                            dbrec.addadvancetempreceipt(cusid, cusname, enrollid, "", "", str_chedate, "", "", "", "", pay_mode, edt_remark.getText().toString(), "Active", "", "", recpt_amnt, date,Config.Currenttime());
                            dbrec.addadvanceviewreceipt(cusid, cusname, enrollid, "", "", str_chedate, "", "", "", "", pay_mode, edt_remark.getText().toString(), "Active", "", "", recpt_amnt, date,Config.Currenttime());
                            hidePDialog();
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                    AdvanceIndividualReceipt.this, R.style.MyAlertDialogStyleApproved);
                            alertDialog.setTitle("Success");
                            alertDialog
                                    .setMessage("Receipt has been generated Successfully.");
                            alertDialog.setCancelable(false);
//                            alertDialog.setPositiveButton("Yes",
//                                    new DialogInterface.OnClickListener() {
//                                        public void onClick(DialogInterface dialog,
//                                                            int which) {
//                                            dialog.cancel();
//                                            Intent i = new Intent(AdvanceIndividualReceipt.this, PrintActivityAdvance.class);
//                                            i.putExtra("Cusname", cusname);
//                                            i.putExtra("ReceivedAmount", recpt_amnt);
//                                            i.putExtra("paymode", pay_mode);
//                                            i.putExtra("Groupname", Groupname);
//                                            i.putExtra("ticketno", Ticketno);
//                                            i.putExtra("cheno", "");
//                                            i.putExtra("chebank", "");
//                                            i.putExtra("chedate", "");
////                                            i.putExtra("subid", subid);
////                                            i.putExtra("mobile", mobile);
//                                            i.putExtra("TotalAmount", edt_totalamnt.getText().toString());
//                                            System.out.println(" tott " + edt_totalamnt.getText().toString());
//                                            i.putExtra("Receiptno", "");
//                                            startActivity(i);
//                                            finish();
//                                        }
//                                    });
                            alertDialog.setNegativeButton("ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                    Intent i = new Intent(AdvanceIndividualReceipt.this, CollectionActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    finish();
                                }
                            });
                            alertDialog.show();

                            Toast.makeText(AdvanceIndividualReceipt.this, "Saved Offline", Toast.LENGTH_SHORT).show();
                        }
                        isadvc = dbrec.findadvance(cusid, enrollid);
                        System.out.println("adv " + isadvc);
                        if (!isadvc.equalsIgnoreCase(cusid)) {
                            dbrec.insertadvance(cusid, enrollid, recpt_amnt);
                        } else {
                            dbrec.updateadvance(edt_totalamnt.getText().toString(), enrollid);
                        }

                    } else if (pay_mode.equalsIgnoreCase("cheque")) {
                        final String cheno = edt_cheno.getText().toString();
                        final String chedate = str_chedate;
                        final String chebank = edt_bankname.getText().toString();
                        String chebranch = edt_bankbranch.getText().toString();
                        if (chebank.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            edt_bankname.setError("Enter Cheque Bank Name");
                            return;
                            // Toast.makeText(IndividualReceipt.this, "Enter Cheque Bank", Toast.LENGTH_LONG).show();

                        } else if (chebranch.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            edt_bankbranch.setError("Enter Cheque Bank Branch");
                            return;
                            //Toast.makeText(IndividualReceipt.this, "Enter Cheque Bank Branch", Toast.LENGTH_LONG).show();

                        } else if (cheno.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            edt_cheno.setError("Enter Cheque Number");
                            return;
                            // Toast.makeText(IndividualReceipt.this, "Enter Cheque Number", Toast.LENGTH_LONG).show();

                        } else if (str_chedate.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            Toast.makeText(AdvanceIndividualReceipt.this, "Select Cheque Date", Toast.LENGTH_LONG).show();
                            return;

                        } else {
                            if (cd.isConnectedToInternet()) {
                                dailycollection(recpt_amnt, cheno, str_chedate, chebranch, chebank, "", "");
                                dbrec.addadvanceviewreceipt(cusid, cusname, enrollid, "", "", str_chedate, "", "", "", "", pay_mode, edt_remark.getText().toString(), "Active", "", "", recpt_amnt, date,Config.Currenttime());
                            } else {
                                dbrec.addadvancetempreceipt(cusid, cusname, enrollid, "", cheno, str_chedate, chebank, chebranch, "", "", pay_mode, edt_remark.getText().toString(), "Pending", "", "", recpt_amnt, date,Config.Currenttime());
                                dbrec.addadvanceviewreceipt(cusid, cusname, enrollid, "", "", str_chedate, "", "", "", "", pay_mode, edt_remark.getText().toString(), "Active", "", "", recpt_amnt, date,Config.Currenttime());
                                hidePDialog();
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                        AdvanceIndividualReceipt.this, R.style.MyAlertDialogStyleApproved);
                                alertDialog.setTitle("Success");
                                alertDialog
                                        .setMessage("Receipt has been generated Successfully.");
                                alertDialog.setCancelable(false);
//                                alertDialog.setPositiveButton("Yes",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog,
//                                                                int which) {
//                                                dialog.cancel();
//                                                Intent i = new Intent(AdvanceIndividualReceipt.this, PrintActivityAdvance.class);
//                                                i.putExtra("Cusname", cusname);
//                                                i.putExtra("ReceivedAmount", recpt_amnt);
//                                                i.putExtra("paymode", pay_mode);
//                                                i.putExtra("Groupname", Groupname);
//                                                i.putExtra("ticketno", Ticketno);
//                                                i.putExtra("cheno", cheno);
//                                                i.putExtra("chebank", chebank);
//                                                i.putExtra("chedate", chedate);
////                                                i.putExtra("subid", subid);
////                                                i.putExtra("mobile", mobile);
//                                                i.putExtra("TotalAmount", edt_totalamnt.getText().toString());
//                                                System.out.println(" tott " + edt_totalamnt.getText().toString());
//                                                i.putExtra("Receiptno", "");
//                                                startActivity(i);
//                                                finish();
//                                            }
//                                        });
                                alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent i = new Intent(AdvanceIndividualReceipt.this, CollectionActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        finish();
                                    }
                                });
                                alertDialog.show();
                                Toast.makeText(AdvanceIndividualReceipt.this, "Saved Offline", Toast.LENGTH_SHORT).show();
                            }
                            isadvc = dbrec.findadvance(cusid, enrollid);
                            if (!isadvc.equalsIgnoreCase(cusid)) {
                                dbrec.insertadvance(cusid, enrollid, recpt_amnt);
                            } else {
                                dbrec.updateadvance(edt_totalamnt.getText().toString(), enrollid);
                            }
                        }
                    } else if (pay_mode.equalsIgnoreCase("dd")) {
                        final String cheno = edt_ddno.getText().toString();
                        final String chebank = edt_ddbank.getText().toString();
                        String chebranch = edt_ddbranch.getText().toString();
                        if (chebank.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            edt_ddbank.setError("Enter DD Bank");
                            return;
                            // Toast.makeText(IndividualReceipt.this, "Enter DD Bank", Toast.LENGTH_LONG).show();

                        } else if (chebranch.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            edt_ddbranch.setError("Enter DD Bank Branch");
                            return;
                            //Toast.makeText(IndividualReceipt.this, "Enter DD Bank Branch", Toast.LENGTH_LONG).show();

                        } else if (cheno.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            edt_ddno.setError("Enter DD Number");
                            return;
                            // Toast.makeText(IndividualReceipt.this, "Enter DD Number", Toast.LENGTH_LONG).show();

                        } else if (str_dddate.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            Toast.makeText(AdvanceIndividualReceipt.this, "Select DD Date", Toast.LENGTH_LONG).show();
                            return;


                        } else {
                            if (cd.isConnectedToInternet()) {
                                dailycollection(recpt_amnt, cheno, str_dddate, chebranch, chebank, "", "");
                                dbrec.addadvanceviewreceipt(cusid, cusname, enrollid, "", "", str_chedate, "", "", "", "", pay_mode, edt_remark.getText().toString(), "Active", "", "", recpt_amnt, date,Config.Currenttime());
                            } else {
                                dbrec.addadvancetempreceipt(cusid, cusname, enrollid, "", cheno, str_chedate, chebank, chebranch, "", "", pay_mode, edt_remark.getText().toString(), "Active", "", "", recpt_amnt, date,Config.Currenttime());
                                dbrec.addadvanceviewreceipt(cusid, cusname, enrollid, "", "", str_chedate, "", "", "", "", pay_mode, edt_remark.getText().toString(), "Active", "", "", recpt_amnt, date,Config.Currenttime());
                                hidePDialog();
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                        AdvanceIndividualReceipt.this, R.style.MyAlertDialogStyleApproved);
                                alertDialog.setTitle("Success");
                                alertDialog
                                        .setMessage("Receipt has been generated Successfully.");
                                alertDialog.setCancelable(false);
//                                alertDialog.setPositiveButton("Yes",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog,
//                                                                int which) {
//                                                dialog.cancel();
//                                                Intent i = new Intent(AdvanceIndividualReceipt.this, PrintActivityAdvance.class);
//                                                i.putExtra("Cusname", cusname);
//                                                i.putExtra("ReceivedAmount", recpt_amnt);
//                                                i.putExtra("paymode", pay_mode);
//                                                i.putExtra("Groupname", Groupname);
//                                                i.putExtra("ticketno", Ticketno);
//                                                i.putExtra("cheno", cheno);
//                                                i.putExtra("chebank", chebank);
//                                                i.putExtra("chedate", str_chedate);
////                                                i.putExtra("subid", subid);
////                                                i.putExtra("mobile", mobile);
//                                                i.putExtra("TotalAmount", edt_totalamnt.getText().toString());
//                                                System.out.println(" tott " + edt_totalamnt.getText().toString());
//                                                i.putExtra("Receiptno", "");
//                                                startActivity(i);
//                                                finish();
//                                            }
//                                        });
                                alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent i = new Intent(AdvanceIndividualReceipt.this, CollectionActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        finish();
                                    }
                                });
                                alertDialog.show();
                                Toast.makeText(AdvanceIndividualReceipt.this, "Saved Offline", Toast.LENGTH_SHORT).show();
                            }
                            isadvc = dbrec.findadvance(cusid, enrollid);
                            if (!isadvc.equalsIgnoreCase(cusid)) {
                                dbrec.insertadvance(cusid, enrollid, recpt_amnt);
                            } else {
                                dbrec.updateadvance(edt_totalamnt.getText().toString(), enrollid);
                            }
                        }
                    } else if (pay_mode.equalsIgnoreCase("neft")) {
                        String transno = edt_rtgsno.getText().toString();
                        if (transno.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            edt_rtgsno.setError("Enter Transaction Number");
                            return;
                        } else if (str_transdate.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            Toast.makeText(AdvanceIndividualReceipt.this, "Select Transaction Date", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            if (cd.isConnectedToInternet()) {
                                dailycollection(recpt_amnt, "", "", "", "", transno, str_transdate);
                                dbrec.addadvanceviewreceipt(cusid, cusname, enrollid, "", "", str_chedate, "", "", "", "", pay_mode, edt_remark.getText().toString(), "Active", "", "", recpt_amnt, date,Config.Currenttime());
                            } else {
                                dbrec.addadvancetempreceipt(cusid, cusname, enrollid, "", "", str_chedate, "", "", transno, str_transdate, pay_mode, edt_remark.getText().toString(), "Active", "", "", recpt_amnt, date,Config.Currenttime());
                                dbrec.addadvanceviewreceipt(cusid, cusname, enrollid, "", "", str_chedate, "", "", "", "", pay_mode, edt_remark.getText().toString(), "Active", "", "", recpt_amnt, date,Config.Currenttime());
                                hidePDialog();
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                        AdvanceIndividualReceipt.this, R.style.MyAlertDialogStyleApproved);
                                alertDialog.setTitle("Success");
                                alertDialog
                                        .setMessage("Receipt has been generated Successfully.");
                                alertDialog.setCancelable(false);
//                                alertDialog.setPositiveButton("Yes",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog,
//                                                                int which) {
//                                                dialog.cancel();
//                                                Intent i = new Intent(AdvanceIndividualReceipt.this, PrintActivityAdvance.class);
//                                                i.putExtra("Cusname", cusname);
//                                                i.putExtra("ReceivedAmount", recpt_amnt);
//                                                i.putExtra("paymode", pay_mode);
//                                                i.putExtra("Groupname", Groupname);
//                                                i.putExtra("ticketno", Ticketno);
//                                                i.putExtra("cheno", "");
//                                                i.putExtra("chebank", "");
//                                                i.putExtra("chedate", "");
////                                                i.putExtra("subid", subid);
////                                                i.putExtra("mobile", mobile);
//                                                i.putExtra("TotalAmount", edt_totalamnt.getText().toString());
//                                                System.out.println(" tott " + edt_totalamnt.getText().toString());
//                                                i.putExtra("Receiptno", "");
//                                                startActivity(i);
//                                                finish();
//                                            }
//                                        });
                                alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent i = new Intent(AdvanceIndividualReceipt.this, CollectionActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        finish();
                                    }
                                });
                                alertDialog.show();
                                Toast.makeText(AdvanceIndividualReceipt.this, "Saved Offline", Toast.LENGTH_SHORT).show();
                            }
                            isadvc = dbrec.findadvance(cusid, enrollid);
                            if (!isadvc.equalsIgnoreCase(cusid)) {
                                dbrec.insertadvance(cusid, enrollid, recpt_amnt);
                            } else {
                                dbrec.updateadvance(edt_totalamnt.getText().toString(), enrollid);
                            }

                        }
                    } else if (pay_mode.equalsIgnoreCase("card")) {
                        String transno = edt_rtgsno.getText().toString();
                        if (transno.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            edt_rtgsno.setError("Enter Transaction Number");
                            return;
                        } else if (str_transdate.equalsIgnoreCase("")) {
                            btn_submit.setVisibility(View.VISIBLE);
                            Toast.makeText(AdvanceIndividualReceipt.this, "Select Transaction Date", Toast.LENGTH_LONG).show();
                            return;
                        } else {
                            if (cd.isConnectedToInternet()) {
                                dailycollection(recpt_amnt, "", "", "", "", transno, str_transdate);
                                dbrec.addadvanceviewreceipt(cusid, cusname, enrollid, "", "", str_chedate, "", "", "", "", pay_mode, edt_remark.getText().toString(), "Active", "", "", recpt_amnt, date,Config.Currenttime());
                            } else {
                                dbrec.addadvancetempreceipt(cusid, cusname, enrollid, "", "", str_chedate, "", "", transno, str_transdate, pay_mode, edt_remark.getText().toString(), "Active", "", "", recpt_amnt, date,Config.Currenttime());
                                dbrec.addadvanceviewreceipt(cusid, cusname, enrollid, "", "", str_chedate, "", "", "", "", pay_mode, edt_remark.getText().toString(), "Active", "", "", recpt_amnt, date,Config.Currenttime());
                                hidePDialog();
                                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                        AdvanceIndividualReceipt.this, R.style.MyAlertDialogStyleApproved);
                                alertDialog.setTitle("Success");
                                alertDialog
                                        .setMessage("Receipt has been generated Successfully.");
                                alertDialog.setCancelable(false);
//                                alertDialog.setPositiveButton("Yes",
//                                        new DialogInterface.OnClickListener() {
//                                            public void onClick(DialogInterface dialog,
//                                                                int which) {
//                                                dialog.cancel();
//                                                Intent i = new Intent(AdvanceIndividualReceipt.this, PrintActivityAdvance.class);
//                                                i.putExtra("Cusname", cusname);
//                                                i.putExtra("ReceivedAmount", recpt_amnt);
//                                                i.putExtra("paymode", pay_mode);
//                                                i.putExtra("Groupname", Groupname);
//                                                i.putExtra("ticketno", Ticketno);
//                                                i.putExtra("cheno", "");
//                                                i.putExtra("chebank", "");
//                                                i.putExtra("chedate", "");
////                                                i.putExtra("subid", subid);
////                                                i.putExtra("mobile", mobile);
//                                                i.putExtra("TotalAmount", edt_totalamnt.getText().toString());
//                                                System.out.println(" tott " + edt_totalamnt.getText().toString());
//                                                i.putExtra("Receiptno", "");
//                                                startActivity(i);
//                                                finish();
//                                            }
//                                        });
                                alertDialog.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        Intent i = new Intent(AdvanceIndividualReceipt.this, CollectionActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        finish();
                                    }
                                });
                                alertDialog.show();
                                Toast.makeText(AdvanceIndividualReceipt.this, "Saved Offline", Toast.LENGTH_SHORT).show();
                            }
                            isadvc = dbrec.findadvance(cusid, enrollid);
                            if (!isadvc.equalsIgnoreCase(cusid)) {
                                dbrec.insertadvance(cusid, enrollid, recpt_amnt);
                            } else {
                                dbrec.updateadvance(edt_totalamnt.getText().toString(), enrollid);
                            }

                        }
                    }

                }
            }

        });

        //=================================================================================================


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

    public void reteriveall() {

        showDialog();
        enroll_list.clear();
//        dbrecepit.deletetable();
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
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
//                            sched.setDiscountAmnt(jObj.getString("Discount_Amnt"));
//                            System.out.println("disccc " + jObj.getString("Discount_Amnt"));
//                            sched.setDiscountId(jObj.getString("Discount_Id"));
                            sched.setPayamount("0");
//                            sched.setUpcoming_dueamount(jObj.getString("next_due_amount"));
//                            sched.setUpcommimg_due_date(jObj.getString("next_due_date"));
//                            sched.setUpcommimg_installment_no(jObj.getString("next_installment_no")); 
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

//                            dbrecepit.addenroll(enroll_list);
                            adapterlist = new CustomAdapterreceiptgenearte(AdvanceIndividualReceipt.this, enroll_list);
                            adapterlist.notifyDataSetChanged();
                            lst_re_enroll.setAdapter(adapterlist);
                            Enrollmodel sched = enroll_list.get(0);
                            Groupname = sched.getGroup_Name();
                            Ticketno = sched.getGroup_Ticket_Name();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                AdvanceIndividualReceipt.this);
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
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    public void dailycollection(final String receeiptamnt, final String chequno, final String chedate, final String chebranch, final String chebank, final String transno, final String transdate) {
        showDialog();
//        System.out.println(" daily col  " + str_amnttype);
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.dailyreceipt, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("customer Activity", response.toString());
                System.out.println(" daily col  " + response.toString());

                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    final String receipt = object.getString("receipt");
                    final String receiptid = object.getString("receiptid");
                    String details = object.getString("details");

                    if (status.equalsIgnoreCase("1")) {
                        hidePDialog();
//                        dbrec.addadvancetempreceipt(cusid, cusname, enrollid, "", "", chedate, "", "", transno, transdate, pay_mode, edt_remark.getText().toString(), "Active", "", "", receeiptamnt);
                        dbrec.updateadvance(edt_totalamnt.getText().toString(), enrollid);
                        System.out.println("reid "+receiptid);
                        sendsms(receiptid);
                        System.out.println("receipt  " + receipt);
//                        builddilogsuccess();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                AdvanceIndividualReceipt.this, R.style.MyAlertDialogStyleApproved);
                        alertDialog.setTitle("Success");
                        alertDialog
                                .setMessage("Receipt has been generated Successfully");
                        alertDialog.setCancelable(false);
                        alertDialog.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int which) {
                                        dialog.cancel();
                                        String whatsappurl = null;
//                                        try {
//                                            whatsappurl = Config.mobile_receipt_print + "rno=" + receipt + "&Cust_Id=" + cusid;
//                                            System.out.println("what " + whatsappurl);
//                                            HttpAsyncTask hat = new HttpAsyncTask();
//                                            hat.execute(whatsappurl);
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }

                                        Intent i = new Intent(AdvanceIndividualReceipt.this, CollectionActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                        startActivity(i);
                                        finish();
                                    }
                                });
//                        alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                Intent i = new Intent(AdvanceIndividualReceipt.this, CollectionActivity.class);
//                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(i);
//                                finish();
//                            }
//                        });
//                        alertDialog.show();
                        alertDialog.show();

                    } else {
                        btn_submit.setVisibility(View.VISIBLE);
                        builddilogerror(details);
//                        Toast.makeText(AdvanceIndividualReceipt.this, details, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

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
                params.put("Branchid", pref.getString("empbranch", ""));
                params.put("cust_id", cusid);
                params.put("enrlid", enrollid);
                params.put("userid", pref.getString("userid", ""));
                params.put("pay_mode", pay_mode);
                params.put("recpt_amnt", receeiptamnt);
                params.put("cheque_no", chequno);
                params.put("cheque_date", chedate);
                params.put("cheque_brnch", chebranch);
                params.put("cheque_bank", chebank);
                params.put("trn_no", transno);
                params.put("trn_date", transdate);
                params.put("recdate", date);
                params.put("rectime", Config.Currenttime());
                params.put("createdby", pref.getString("username", ""));
                params.put("updatedby", pref.getString("username", ""));
                params.put("remark", edt_remark.getText().toString());
                Log.d("recept_param",params.toString());
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
                Config.send_advance_sms, new Response.Listener<String>() {
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
                System.out.println("reid "+recpid);
                return params;
            }

        };
        localreq.setRetryPolicy(new DefaultRetryPolicy(40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(localreq);
    }
    public void reteriveadvance() {

        showDialog();
        System.out.println("advance idd");
        enroll_list.clear();
        //dbrecepit.deletetable();
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.availableadvance, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                hidePDialog();


                try {


                    JSONObject object = new JSONObject(response);
                    advance = object.getString("advance");
                    edt_advanceavail.setText(advance);

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
                params.put("enrlid", enrollid);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    public void getlocaladvance(final String cusid, final String enrlid) {
        edt_advanceavail.setText(dbrec.getadvanceamnount(cusid, enrlid));
    }

    public void textwatch() {
        int edt1 = 0, edt2 = 0;
        try {
            String str1 = edt_advanceavail.getText().toString();
            if (str1.equalsIgnoreCase("")) {
                str1 = "0";
                edt1 = Integer.parseInt(str1);
            } else {
                edt1 = Integer.parseInt(str1);
            }
            String str2 = edt_receiptamnt.getText().toString();
            if (str2.equalsIgnoreCase("")) {
                str2 = "0";
                edt2 = Integer.parseInt(str2);
            } else {
                edt2 = Integer.parseInt(str2);
            }

            tot = String.valueOf((edt1) + (edt2));
            System.out.println(tot + "  hhoesogjoie");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void builddilogerror(String title) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                this, R.style.MyAlertDialogStyleDeclined);
        alertDialog.setTitle("Error");
        alertDialog
                .setMessage(title);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();
                        finish();
                    }
                });
        alertDialog.show();
    }

    private void reteriveloca() {
        showDialog();
        enroll_list.clear();
        dbrec.deletetable();
        enroll_list = dbrec.getreceiptforcustenroll(cusid, enrollid);
        dbrec.addenroll(enroll_list);
        adapterlist = new CustomAdapterreceiptgenearte(AdvanceIndividualReceipt.this, enroll_list);
        adapterlist.notifyDataSetChanged();
        //   ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
        lst_re_enroll.setAdapter(adapterlist);
        hidePDialog();

    }

    public void builddilogsuccess() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                AdvanceIndividualReceipt.this, R.style.MyAlertDialogStyleApproved);
        alertDialog.setTitle("Success");
        alertDialog
                .setMessage("Receipt has been generated Successfully");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();
                        Intent i = new Intent(AdvanceIndividualReceipt.this, CollectionActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(i);
                        finish();
                    }
                });
        alertDialog.show();
    }

    public void builddilogdecline() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                AdvanceIndividualReceipt.this, R.style.MyAlertDialogStyleDeclined);
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

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while ((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;
    }

}
