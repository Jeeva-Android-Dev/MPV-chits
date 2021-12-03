package com.mazenetsolutions.mzs119.skst_new;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
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
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AdvanceAdjustment extends AppCompatActivity {

    public ArrayList<Enrollmodel> enroll_list = new ArrayList<Enrollmodel>();


    public ArrayList<Enrollmodel> enroll_listmain = new ArrayList<Enrollmodel>();
    public ArrayList<Enrollmodel> enroll_listpost = new ArrayList<Enrollmodel>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    SimpleDateFormat df;
    Spinner spn_paytype;
    TextView txt_cusname;
    String receiptno,recptid;
    Databaserecepit dbrecepit;
    String paytpe[] = {"Cash", "D.D", "Cheque", "RTGS/NEFT", "Card"};
    private ProgressDialog pDialog;
    String cusid = "0", cusname = "", enrollid = "", groupname = "", Toatal_payable = "";
    NonScrollListView lst_re_enroll;
    Databasecustomers dbc;
    Databaserecepit dbrec;
    String url = Config.reteriveindienroll;
    ArrayList<Custmodel> customerArray = new ArrayList<>();
    ArrayList<Custmodel> customer_listmain = new ArrayList<>();
    String reteriveuser = Config.reteriveusers;
    LinearLayout cheque_lay, dd_lay, rtgs_lay;
    CustomAdapterreceiptgenearte adapterlist;
    String advance = "", recpt_amnt = "";
    EditText edt_remark, edt_bankname, edt_bankbranch, edt_cheno, edt_ddno, edt_ddbank, edt_ddbranch, edt_rtgsno, edt_cusname, edt_receiptamnt, edt_enrol, edt_advanceavail;
    Button btn_submit, btn_transdate, btn_chequedate, btn_dddate;
    ArrayAdapter<String> paytypeadpter, amnttypeadapter;
    DatePickerDialog fromDatePickerDialog;
    //    CustomerAdapterAdvance customeradpater;
    String cid = "", pay_mode = "", str_dddate = "", str_chedate = "", str_transdate = "", str_enrollid = "", str_amnttype = "", cadvance = "", tot = "", cname = "", Groupname = "", Ticketno = "", subid = "", mobile = "";
    String date;
    double peding_value=0,penalty_value=0,bonus_value=0;
    String pending_Strig,penalty_string,bonus_string;
    String Advance = "";
    GlobalValue globalValue;



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
        df = new SimpleDateFormat("MM/dd/yyyy");

        Date date1 = new Date();
        date = df.format(date1);
        dbc = new Databasecustomers(AdvanceAdjustment.this);
        dbrec = new Databaserecepit(AdvanceAdjustment.this);
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
        edt_advanceavail = (EditText) findViewById(R.id.edt_adc_availadvnce);
        spn_paytype = (Spinner) findViewById(R.id.spn_adc_paytype);
        txt_cusname = (TextView) findViewById(R.id.txt_cusname);

        try {
//
//            i.putExtra("pending_value", pending_value);
//            i.putExtra("penalty_value", penalty_value);
//            i.putExtra("bonus_value", bonus_value);
            Intent it = getIntent();
            cusid = it.getStringExtra("cusid");
            cusname = it.getStringExtra("cusname");
            groupname = it.getStringExtra("groupname");
            enrollid = it.getStringExtra("enrollid");
            txt_cusname.setText("Name : " + cusname);

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
            System.out.println("ye");
            reteriveall();
            reteriveadvance();
        } else {
            reteriveloca();
            getlocaladvance(cusid, enrollid);
        }

//====================================================================================================================
        btn_submit.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String ticketno = "";
                String penaltyamt = "";
                String pendingamt = "";
                String paidamt = "";
                String bonusamt = "";
                String payableamount = "";
                String chitvalue = "";
                String cusbranch = "";
                String pendingdays = "";
                String Group = "";
                double pending_double=0;

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


                btn_submit.setVisibility(View.INVISIBLE);
                recpt_amnt = edt_receiptamnt.getText().toString();
                System.out.println("rec " + recpt_amnt);
                String amount2=edt_receiptamnt.getText().toString();
                if (((edt_receiptamnt.getText().toString()).equalsIgnoreCase("")) || ((edt_receiptamnt.getText().toString()).equalsIgnoreCase("0"))) {
                    btn_submit.setVisibility(View.VISIBLE);
                    edt_receiptamnt.setError("Enter valid Amount");
                    edt_receiptamnt.requestFocus();
                    return;

                }
                else if (pendingamt.trim().equalsIgnoreCase("0") || pendingamt.trim().equalsIgnoreCase("")) {
                    Toast.makeText(AdvanceAdjustment.this, "No Pending Available", Toast.LENGTH_LONG).show();
                    btn_submit.setVisibility(View.VISIBLE);
                } else if(Double.parseDouble(amount2)>payamount1333){
                    Toast.makeText(AdvanceAdjustment.this, "Amount Exceeds", Toast.LENGTH_LONG).show();
                    btn_submit.setVisibility(View.VISIBLE);
                }
                else {
                    System.out.println("else rec " + recpt_amnt);
                    if (cd.isConnectedToInternet()) {
                        for (int i = 0; i < enroll_list.size(); i++) {
                            Enrollmodel mod = enroll_list.get(i);
                            enrollid = mod.getEnrollid();
                            bonusamt = mod.getBonus_Amt();
                            paidamt = mod.getPaid_Amt();
                            pendingamt = mod.getPending_Amt();
                            penaltyamt = mod.getPenalty_Amt();
                            Group = mod.getGrpid();
                            ticketno = "";
                            chitvalue = mod.getScheme();
                            cusbranch = mod.getCusbranch();
                            pendingdays = "";
                            payableamount = recpt_amnt;
                            int adv = convertStringtoInt(advance);
                            int recpt = convertStringtoInt(recpt_amnt);
                            System.out.println("else adv " + adv + " recp " + recpt);
                            if (recpt == 0) {
                                edt_receiptamnt.setError("Enter valid Amount");
                                edt_receiptamnt.requestFocus();
                                btn_submit.setVisibility(View.VISIBLE);
                                return;
                            } else if (recpt <= adv) {
                                postentry(enrollid, bonusamt, paidamt, pendingamt, penaltyamt, Group, ticketno, recpt_amnt, "", chitvalue, "daily.rct.adj", cusbranch, pendingdays, "Active", date);
                            }
                        }
                    }
                }
            }

        });
        //=============================================================

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
                            sched.setPayamount("0");
                            sched.setUpcoming_dueamount("0");
                            sched.setUpcommimg_due_date("0");
                            sched.setUpcommimg_installment_no("0");
                            enroll_list.add(sched);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (enroll_list.size() > 0) {


                        try {

//                            dbrecepit.addenroll(enroll_list);
                            adapterlist = new CustomAdapterreceiptgenearte(AdvanceAdjustment.this, enroll_list);
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
                                AdvanceAdjustment.this);
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

    public void reteriveadvance() {

        showDialog();
        System.out.println("advance idd " + enrollid);
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

    private void reteriveloca() {
        showDialog();
        enroll_list.clear();
        dbrec.deletetable();
        enroll_list = dbrec.getreceiptforcustenroll(cusid, enrollid);
        dbrec.addenroll(enroll_list);
        adapterlist = new CustomAdapterreceiptgenearte(AdvanceAdjustment.this, enroll_list);
        adapterlist.notifyDataSetChanged();
        //   ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
        lst_re_enroll.setAdapter(adapterlist);
        hidePDialog();

    }

    public void postentry(final String enrollidd, final String bonusamt, final String paidamt, final String pendingamt, final String penaltyamt, final String Group, final String ticketno, final String payableamount, final String Remark, final String chitvalue, final String paytype, final String Cus_Branch, final String Pending_Days, final String status, final String date) {

        showDialog();
        System.out.println("rec " + recpt_amnt);
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.saverecepit, new Response.Listener<String>() {
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
                    int pend = (convertStringtoInt(pendingamt)) - (convertStringtoInt(recpt_amnt));
                    String pendi = Integer.toString(pend);
                    int pai = (convertStringtoInt(paidamt)) + (convertStringtoInt(recpt_amnt));
                    String paid = Integer.toString(pai);

                    System.out.println("pending amnt postentry" + pendi);
                    System.out.println("paid amnt postentry" + paid);
                    dbrec.updatepaidamnt(cusid, enrollid, pendi, paid, advanceamnt);


                    if (status.equals("1")) {
                        hidePDialog();
                        System.out.println("Collection Activity + receir");
                        String advancetotal = dbc.gettotaladvance(cusid);
                        int advancetot = ((convertStringtoInt(advanceamnt) + (convertStringtoInt(advancetotal))));
                        String advancxeupdate = String.valueOf(advancetot);
                        dbc.updateamnt(cusid, Total_Enrl_Paid, Total_Enrl_Pending);
                        dbrec.updateenroll(paid, Total_Enrl_Pending, enrollid);
                        editor.putString("receiptamnt", receivedamnt);
                        editor.commit();
                        dbrec.addviewreceipt(cusid, cusname, enrollid, chitvalue, penaltyamt, bonusamt, paidamt, Group, recpt_amnt, ticketno, Cus_Branch, Pending_Days, payableamount, "", pendingamt, "", "", "", "", "", "daily.rct.adj", "", "Active", advanceamnt, date, Config.Currenttime());
                        Intent i = new Intent(AdvanceAdjustment.this, CollectionActivity.class);
                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                       startActivity(i);
                                      finish();
                        //  sendsms(recptid);

//                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
//                                AdvanceAdjustment.this, R.style.MyAlertDialogStyleApproved);
//                        alertDialog.setTitle("Success");
//                        alertDialog
//                                .setMessage("Adjustment has been done Successfully");
//                        alertDialog.setCancelable(false);
//                        alertDialog.setPositiveButton("Ok",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog,
//                                                        int which) {
//                                        dialog.cancel();
//                                        finish();
//                                    }
//                                });
////                alertDialog.show();
//
//                        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(
//                                AdvanceAdjustment.this, R.style.MyAlertDialogStyleApproved);
//                        alertDialog.setTitle("Success");
//                        alertDialog
//                                .setMessage("Receipt has been generated Successfully. Do you want to send invoice through WhatsApp ?");
//                        alertDialog.setCancelable(false);
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
//                                            AdvanceAdjustment.HttpAsyncTask hat = new AdvanceAdjustment.HttpAsyncTask();
//                                            hat.execute(whatsappurl);
//
//                                        } catch (Exception e) {
//                                            e.printStackTrace();
//                                        }
//
//                                        Intent i = new Intent(AdvanceAdjustment.this, CollectionActivity.class);
//                                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                        startActivity(i);
//                                        finish();
//                                    }
//                                });
//                        alertDialog.setPositiveButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.dismiss();
//                                Intent i = new Intent(AdvanceAdjustment.this, CollectionActivity.class);
//                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                                startActivity(i);
//                                finish();
//                            }
//                        });
//                        alertDialog.show();


                    } else {
                        btn_submit.setVisibility(View.VISIBLE);

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
                System.out.println("Cust_Id|" + cusid + "|enrollid|" + enrollidd + "|paidamt|" + paidamt + "|pendingamt|" + pendingamt + "|Groupid|" + Group + "|ticketno|" + ticketno + "|payamount|" + payableamount + "|Emp_Id|" + pref.getString("userid", "0") + "|Created_By|" + pref.getString("username", "DEMO") + "|Customer_Name|" + cusname + "|Chit_Value|" + chitvalue + "|Payment_Type|" + paytype + "|Cus_Branch|" + Cus_Branch + "|Pending_Days|" + Pending_Days + "|Emp_Branch|" + pref.getString("empbranch", "3") + "|status|" + status + "|recdate|" + date + "|rectime|" + Config.Currenttime());
                params.put("Cust_Id", cusid);
                params.put("enrollid", enrollidd);
                params.put("bonusamt",bonusamt);
                params.put("paidamt", paidamt);
                params.put("pendingamt", pendingamt);
                params.put("penaltyamt", penaltyamt);
                params.put("Groupid", "");
                params.put("ticketno", ticketno);
                params.put("payamount", payableamount);
                params.put("Emp_Id", pref.getString("userid", "0"));
                params.put("Created_By", pref.getString("username", "DEMO"));
                params.put("Remark", "");
                params.put("Customer_Name", cusname);
                params.put("Chit_Value", chitvalue);
                params.put("Payment_Type", paytype);
                params.put("Cheque_No", "");
                params.put("Cheque_Date", "");
                params.put("Bank_Name", "");
                params.put("Branch_Name", "");
                params.put("Trn_No", "");
                params.put("Trn_Date", "");
                params.put("Cus_Branch", Cus_Branch);
                params.put("Pending_Days", Pending_Days);
                params.put("Emp_Branch", pref.getString("empbranch", "3"));
                params.put("status", status);
                params.put("recdate", date);
                params.put("rectime", Config.Currenttime());

                System.out.println("advance_adjust"+params);

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(90000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
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
}
