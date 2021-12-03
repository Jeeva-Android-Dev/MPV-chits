package com.mazenetsolutions.mzs119.skst_new;

import android.content.SharedPreferences;
import android.os.Bundle;

import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenetsolutions.mzs119.skst_new.Adapter.Adap_collfeedback;
import com.mazenetsolutions.mzs119.skst_new.Adapter.CustomAdapterViewCol;
import com.mazenetsolutions.mzs119.skst_new.Database.Databaserecepit;
import com.mazenetsolutions.mzs119.skst_new.Model.TempEnrollModel;
import com.mazenetsolutions.mzs119.skst_new.Model.colfeedbackmodel;
import com.mazenetsolutions.mzs119.skst_new.Utils.AppController;
import com.mazenetsolutions.mzs119.skst_new.Utils.Config;
import com.mazenetsolutions.mzs119.skst_new.Utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class ViewCollection extends AppCompatActivity {
    ArrayList<TempEnrollModel> viewreceipt = new ArrayList<>();
    ArrayList<TempEnrollModel> advancereceipt = new ArrayList<>();
    Databaserecepit dbrecepit;
    ConnectionDetector cd;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ListView view_list;
    CustomAdapterViewCol cav;
    TextView txt_datew_total, tit_cusname, tit_amnt, tit_paytype;
    SimpleDateFormat dateFormat, dateFormat2;
    String date = "", date2 = "";
    Button collection_receipts, loan_receipts, col_activity;
    int tot1 = 0;
    Locale curLocale = new Locale("en", "IN");
    ArrayList<colfeedbackmodel> colllist = new ArrayList<>();
    ArrayList<colfeedbackmodel> colllist_main = new ArrayList<>();
    Adap_collfeedback adap_collfeedback;

    private Date yesterday() {
        final Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return cal.getTime();
    }

    private String getYesterdayDateString() {
        dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        return dateFormat.format(yesterday());
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_collection);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();
        tit_paytype = (TextView) findViewById(R.id.tit_paytype);
        tit_cusname = (TextView) findViewById(R.id.tit_cusname);
        tit_amnt = (TextView) findViewById(R.id.tit_amnt);
        col_activity = (Button) findViewById(R.id.col_activity);
        cd = new ConnectionDetector(this);
        view_list = (ListView) findViewById(R.id.view_list);
        txt_datew_total = (TextView) findViewById(R.id.txt_datew_total);
        dbrecepit = new Databaserecepit(ViewCollection.this);
        loan_receipts = (Button) findViewById(R.id.loan_receipts);
        collection_receipts = (Button) findViewById(R.id.collection_receipts);
        dateFormat = new SimpleDateFormat("MM/dd/yyyy");
        dateFormat2 = new SimpleDateFormat("dd-MM-yyyy");
        Date date1 = new Date();
        date = dateFormat.format(date1);
        date2 = dateFormat2.format(date1);
        viewreceipt.clear();
        advancereceipt.clear();
        viewreceipt = dbrecepit.getAllViewenroll(date);
        advancereceipt = dbrecepit.getAdvanceViewenroll(date2);
        if (viewreceipt.size() > 0) {

            for (int i = 0; i < viewreceipt.size(); i++) {
                System.out.println("receipt post entry");
                TempEnrollModel tem = viewreceipt.get(i);
            }
            cav = new CustomAdapterViewCol(ViewCollection.this, viewreceipt);
            cav.notifyDataSetChanged();
            view_list.setAdapter(cav);
            view_list.setVisibility(View.VISIBLE);

        } else {
            view_list.setVisibility(View.GONE);
        }
        collection_receipts.setTextColor(getResources().getColor(R.color.white));
        loan_receipts.setTextColor(getResources().getColor(R.color.black));
        col_activity.setTextColor(getResources().getColor(R.color.black));
        maketotal(viewreceipt);
        if (cd.isConnectedToInternet()) {
            get_colactivity();
        } else {
            Toast.makeText(ViewCollection.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
        }

        collection_receipts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewreceipt.clear();
                tit_cusname.setText("Customer Name");
                tit_amnt.setText("Amount");
                tit_paytype.setText("Payment Type");
                viewreceipt = dbrecepit.getAllViewenroll(date);
                collection_receipts.setTextColor(getResources().getColor(R.color.white));
                loan_receipts.setTextColor(getResources().getColor(R.color.black));
                if (viewreceipt.size() > 0) {

                    for (int i = 0; i < viewreceipt.size(); i++) {
                        System.out.println("receipt post entry");
                        TempEnrollModel tem = viewreceipt.get(i);
                    }
                    cav = new CustomAdapterViewCol(ViewCollection.this, viewreceipt);
                    cav.notifyDataSetChanged();
                    view_list.setAdapter(cav);
                    maketotal(viewreceipt);
                    view_list.setVisibility(View.VISIBLE);

                } else {
                    view_list.setVisibility(View.GONE);
                }
            }
        });
        loan_receipts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                advancereceipt.clear();
                tit_cusname.setText("Customer Name");
                tit_amnt.setText("Amount");
                tit_paytype.setText("Payment Type");
                loan_receipts.setTextColor(getResources().getColor(R.color.white));
                collection_receipts.setTextColor(getResources().getColor(R.color.black));
                col_activity.setTextColor(getResources().getColor(R.color.black));
                advancereceipt = dbrecepit.getAdvanceViewenroll(date2);
                System.out.println("receipt ADVANCES");
                if (advancereceipt.size() > 0) {
                    for (int i = 0; i < advancereceipt.size(); i++) {
                        System.out.println("receipt post entry");
                        TempEnrollModel tem = advancereceipt.get(i);
                    }
                    cav = new CustomAdapterViewCol(ViewCollection.this, advancereceipt);
                    cav.notifyDataSetChanged();
                    view_list.setAdapter(cav);
                    view_list.setVisibility(View.VISIBLE);
                    maketotal(advancereceipt);
                } else {
                    view_list.setVisibility(View.GONE);
                }
            }
        });
        col_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view_list.setAdapter(adap_collfeedback);
                view_list.setVisibility(View.VISIBLE);
                tit_cusname.setText("Customer Name");
                tit_amnt.setText("Feedback");
                tit_paytype.setText("Followup Date");
                loan_receipts.setTextColor(getResources().getColor(R.color.black));
                collection_receipts.setTextColor(getResources().getColor(R.color.black));
                col_activity.setTextColor(getResources().getColor(R.color.white));

            }
        });

        view_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (viewreceipt.size() > 0) {
                    String cusid = "";
                    TempEnrollModel tem = viewreceipt.get(i);
                    String enrollid = tem.getEnrollid();
                    String bonusamnt = tem.getBonus_Amt();
                    String paidamnt = tem.getPaid_Amt();
                    String pendingamnt = tem.getPending_Amt();
                    String penaltyamnt = tem.getPenalty_Amt();
                    String Group = tem.getGroup_Name();
                    String ticketno = tem.getGroup_Ticket_Name();
                    String payableamnt = tem.getInsamt();
                    String payamont = tem.getPayamount();
                    String Remark = tem.getRemark();
                    String chitvalue = tem.getScheme();
                    String paytype = tem.getPaytype();
                    String cus_branch = tem.getCusbranch();
                    String pendingdays = tem.getPendingdys();
                    String status = tem.getStatus();
                    cusid = tem.getCusid();
                    String cusname = tem.getCusname();
                    String cheqno = tem.getChequeNo();
                    String cheqdate = tem.getChequeDate();
                    String cheqbank = tem.getChequeBank();
                    String cheqbranch = tem.getChequeBranch();
                    String transno = tem.getTransNo();
                    String rtgsdate = tem.getTransDate();
                    String advance = tem.getAdvance();
//                    Intent it = new Intent(ViewCollection.this, PrintActivity.class);
//                    it.putExtra("Cusname", cusname);
//                    it.putExtra("Amount", payamont);
//                    it.putExtra("Groupname", Group);
//                    it.putExtra("ticketno", ticketno);
//                    it.putExtra("paymode", paytype);
//                    it.putExtra("bonusamnt", bonusamnt);
//                    it.putExtra("pendingamnt", pendingamnt);
//                    it.putExtra("penaltyamnt", penaltyamnt);
//                    it.putExtra("instlmntamnt", payableamnt);
//                    it.putExtra("advance", advance);
//                    it.putExtra("Receiptno", "");
//                    startActivity(it);

                } else {


                }
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }

    public void maketotal(ArrayList<TempEnrollModel> listmain_main) {
        tot1 = 0;
        for (int i = 0; i < listmain_main.size(); i++) {

            TempEnrollModel tem = listmain_main.get(i);
            String tote = tem.getPayamount().trim();
            System.out.println("tote " + tote);
            try {
                int tot = Integer.parseInt(tote);
                tot1 += tot;

            } catch (Exception e) {

            }
        }
        System.out.println("toto " + tot1);
        String advance = String.valueOf(tot1);
        try {
            if (advance.contains("-")) {
                advance = "0";
            }
            Double d = Double.parseDouble(advance);
            String moneyString2 = NumberFormat.getNumberInstance(curLocale).format(d);
            advance = moneyString2;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        txt_datew_total.setText("Rs. " + String.valueOf(advance));
        txt_datew_total.setVisibility(View.VISIBLE);
    }

    public void get_colactivity() {

        colllist.clear();
        colllist_main.clear();
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.get_collfeedback, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                System.out.println("feedback res " + response.toString());


                try {


                    JSONObject object = new JSONObject(response);

                    JSONArray ledgerarray = object.getJSONArray("result");
                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);


                            colfeedbackmodel sched = new colfeedbackmodel();
                            sched.setName(jObj.getString("First_Name_F"));
                            sched.setFeedback(jObj.getString("Feedback"));
                            sched.setCreatedon(jObj.getString("Created_On"));
                            colllist.add(sched);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }


                    if (colllist.size() > 0) {
                        try {
                            System.out.println("l emp ");
                            colllist_main.clear();
                            colllist_main.addAll(colllist);
                            col_activity.setEnabled(true);
                            adap_collfeedback = new Adap_collfeedback(ViewCollection.this, colllist_main);
                            adap_collfeedback.notifyDataSetChanged();

                        } catch (Exception e) {

                            e.printStackTrace();
                        }

                    }
//
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

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empid", pref.getString("userid", "0"));
                System.out.println("feedback emp " + pref.getString("userid", "0") + " date " + date);
                params.put("date", date2);

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
}