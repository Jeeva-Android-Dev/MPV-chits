package com.mazenet.mzs119.skstowner;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.skstowner.Adapter.Adap_collfeedback;
import com.mazenet.mzs119.skstowner.Adapter.CustomAdapterDatewise;
import com.mazenet.mzs119.skstowner.Model.CustomAdapterLoanDatewise;
import com.mazenet.mzs119.skstowner.Model.DateWiseViewModel;
import com.mazenet.mzs119.skstowner.Model.LoanModelDatewise;
import com.mazenet.mzs119.skstowner.Model.colfeedbackmodel;
import com.mazenet.mzs119.skstowner.Utils.AppController;
import com.mazenet.mzs119.skstowner.Utils.Config;
import com.mazenet.mzs119.skstowner.Utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateWiseView extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    DateFormat df, df_daily;
    TextView txt_date, tit_cusname, tit_amnt, tit_paytype,tit_feedtime;
    LinearLayout laydate;
    ConstraintLayout homelay;
    ListView viewlist;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    String empid = "", daily_date = "",str_frodate="";
    String duetotal,advancetotal;
    private ProgressDialog pDialog;
    private static final String TAG = MainActivity.class.getSimpleName();
    String url = Config.getallviewbydate;
    ArrayList<DateWiseViewModel> listmain = new ArrayList<>();
    ArrayList<DateWiseViewModel> listmain_main = new ArrayList<>();
    CustomAdapterDatewise adapterDatewise;
    Adap_collfeedback adap_collfeedback;
    TextView txt_tot;
    int tot1 = 0;
    ArrayList<LoanModelDatewise> loan_list = new ArrayList<>();
    ArrayList<LoanModelDatewise> loan_list_main = new ArrayList<>();
    CustomAdapterLoanDatewise loanadaptrerdatewise;
    Button btn_collectreceipt, btn_loanreceipt,col_activity;
    Locale curLocale = new Locale("en", "IN");
    Boolean isLoans = false;
    ArrayList<colfeedbackmodel> colllist = new ArrayList<>();
    ArrayList<colfeedbackmodel> colllist_main = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_wise_view);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();

        cd = new ConnectionDetector(this);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);

        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);
        btn_collectreceipt = (Button) findViewById(R.id.collection_receipts);
        btn_loanreceipt = (Button) findViewById(R.id.loan_receipts);

        df = new SimpleDateFormat("MM/dd/yyyy");
        df_daily = new SimpleDateFormat("dd-MM-yyyy");
        txt_date = (TextView) findViewById(R.id.datetext);
        laydate = (LinearLayout) findViewById(R.id.datelay);
        homelay = (ConstraintLayout) findViewById(R.id.lay_view_home);
        viewlist = (ListView) findViewById(R.id.list_view_datewise);
        txt_tot = (TextView) findViewById(R.id.txt_datew_total);
        tit_paytype = (TextView) findViewById(R.id.tit_paytype);
        tit_cusname = (TextView) findViewById(R.id.tit_cusname);
        tit_amnt = (TextView) findViewById(R.id.tit_amnt);
        tit_feedtime = (TextView) findViewById(R.id.tit_feedtime);
        col_activity = (Button) findViewById(R.id.col_activity);
        Intent i = getIntent();
        empid = i.getStringExtra("empid");
        showcal();
        btn_collectreceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewlist.setAdapter(adapterDatewise);
                viewlist.setVisibility(View.VISIBLE);
                txt_tot.setVisibility(View.VISIBLE);
                isLoans = false;
                tit_cusname.setText("Customer Name");
                tit_amnt.setText("Receipt Date");
                tit_paytype.setText("Amount");
                tit_feedtime.setText("Payment Type");
                txt_tot.setText(duetotal);
                btn_collectreceipt.setTextColor(getResources().getColor(R.color.white));
                btn_loanreceipt.setTextColor(getResources().getColor(R.color.black));
                col_activity.setTextColor(getResources().getColor(R.color.black));
            }
        });
        btn_loanreceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewlist.setAdapter(loanadaptrerdatewise);
                txt_tot.setVisibility(View.VISIBLE);
                viewlist.setVisibility(View.VISIBLE);
                tit_feedtime.setVisibility(View.VISIBLE);
                tit_cusname.setText("Customer Name");
                tit_amnt.setText("Receipt Date");
                tit_paytype.setText("Amount");
                tit_feedtime.setText("Payment Type");
                btn_collectreceipt.setTextColor(getResources().getColor(R.color.black));
                btn_loanreceipt.setTextColor(getResources().getColor(R.color.white));
                col_activity.setTextColor(getResources().getColor(R.color.black));
                isLoans = true;
                txt_tot.setText(advancetotal);
            }
        });
        col_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewlist.setAdapter(adap_collfeedback);
                txt_tot.setVisibility(View.GONE);
                viewlist.setVisibility(View.VISIBLE);
                tit_feedtime.setVisibility(View.VISIBLE);
                tit_cusname.setText("Customer Name");
                tit_amnt.setText("Feedback");
                tit_paytype.setText("Followup Date");
                tit_feedtime.setText("Feedback Time");
                btn_collectreceipt.setTextColor(getResources().getColor(R.color.black));
                btn_loanreceipt.setTextColor(getResources().getColor(R.color.black));
                col_activity.setTextColor(getResources().getColor(R.color.white));
            }
        });
       /* viewlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DateWiseViewModel dvm = listmain.get(i);
                Intent it = new Intent(DateWiseView.this, PrintActivityDatewise.class);
                it.putExtra("Cusname", dvm.getFirst_Name_F());
                it.putExtra("Amount", dvm.getTotal_Amount());
                it.putExtra("Groupname", dvm.getGroup_Id());
                it.putExtra("ticketno", dvm.getGroup_Ticket_Id());
                it.putExtra("paymode", dvm.getPayment_Type());
                it.putExtra("pendingamnt", dvm.getPending_Amt());
                it.putExtra("advance", dvm.getAdvance_Amt());
                it.putExtra("Receiptno", dvm.getReceipt_No());
                startActivity(it);
            }
        }); */


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
                            sched.setFeedbacktime(jObj.getString("Feedback_time"));
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
                            adap_collfeedback = new Adap_collfeedback(DateWiseView.this, colllist_main);
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
                params.put("empid",empid);
                System.out.println("feedback emp " +empid + " date " + daily_date);
                params.put("date", daily_date);

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    public void getall(String emid) {
        final String id = emid;
        showDialog();
        tot1 = 0;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                hidePDialog();
                System.out.println("datewisev " + response);
                listmain.clear();
                listmain_main.clear();
                loan_list.clear();
                loan_list_main.clear();

                try {
                    JSONObject jobj = new JSONObject(response);
                    String jsonob = jobj.getString("result");
                    System.out.println("hjbfsgjgh " + jsonob);
                    if (jsonob.equals("0")) {
                        Load_loans();
                        viewlist.setVisibility(View.GONE);
                        Toast.makeText(DateWiseView.this, "No receipts Available", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray object = jobj.getJSONArray("result");
                        try {

                            for (int i = 0; i < object.length(); i++) {
                                JSONObject jObj = object.getJSONObject(i);

                                DateWiseViewModel dvm = new DateWiseViewModel();
                                dvm.setCust_Id(jObj.getString("Cust_Id"));
                                dvm.setCus_Branch(jObj.getString("Cus_Branch"));
                                dvm.setEnrl_Id(jObj.getString("Enrl_Id"));
                                dvm.setTotal_Amount(jObj.getString("Total_Amount"));
                                dvm.setReceipt_No(jObj.getString("Receipt_No"));
                                dvm.setCreated_By(jObj.getString("Created_By"));
                                dvm.setCheque_No(jObj.getString("Cheque_No"));
                                dvm.setCheque_Date(jObj.getString("Cheque_Date"));
                                dvm.setBank_Name(jObj.getString("Bank_Name"));
                                dvm.setBranch_Name(jObj.getString("Branch_Name"));
                                dvm.setTrn_No(jObj.getString("Trn_No"));
                                dvm.setTrn_Date(jObj.getString("Trn_Date"));
                                dvm.setPayment_Type(jObj.getString("Payment_Type"));
                                dvm.setGroup_Id(jObj.getString("Group_Id"));
                                dvm.setGroup_Ticket_Id(jObj.getString("Group_Ticket_Id"));
                                dvm.setFirst_Name_F(jObj.getString("First_Name_F"));
                                dvm.setPending_Amt(jObj.getString("Pending_Amt"));
                                dvm.setTotal_Enrl_Paid(jObj.getString("Total_Enrl_Paid"));
                                dvm.setTotal_Enrl_Pending(jObj.getString("Total_Enrl_Pending"));
                                dvm.setAdvance_Amt(jObj.getString("Advance_Amt"));
                                dvm.setReceipt_time(jObj.getString("Receipt_time"));
                                dvm.setDate(jObj.getString("Date"));
                                listmain.add(dvm);

                            }
                            if (listmain.size() > 0) {
                                for (int i = 0; i < listmain.size(); i++) {
                                    DateWiseViewModel dm = listmain.get(i);
                                    String paid = dm.getPayment_Type().replaceAll(" ","");
                                    if (paid.equalsIgnoreCase("daily.rct.adj")) {

                                    } else {
                                        listmain_main.add(dm);
                                    }
                                }
                                adapterDatewise = new CustomAdapterDatewise(DateWiseView.this, listmain_main);
                                adapterDatewise.notifyDataSetChanged();
                                viewlist.setAdapter(adapterDatewise);
                                viewlist.setVisibility(View.VISIBLE);
                                btn_collectreceipt.setEnabled(true);
                                for (int i = 0; i < listmain_main.size(); i++) {

                                    DateWiseViewModel tem = listmain_main.get(i);
                                    String tote = tem.getTotal_Amount().replaceAll(" ","");

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
                                duetotal=advance;
                                txt_tot.setText("Rs. " + String.valueOf(duetotal));
                                Load_loans();
                            } else {
                                Load_loans();
                                listmain.clear();
                                listmain_main.clear();
                                viewlist.setVisibility(View.GONE);
                                Toast.makeText(DateWiseView.this, "No receipts Available", Toast.LENGTH_SHORT).show();
                            }


                        } catch (Exception e) {

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Emp_Id", id);
                System.out.println(id);
                params.put("Date", str_frodate);
                System.out.println(str_frodate);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }

    public void showcal() {
        listmain.clear();
        laydate.setVisibility(View.GONE);
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(DateWiseView.this,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                Calendar calendar = Calendar.getInstance();

                newDate.set(year, monthOfYear, dayOfMonth);
                calendar.set(year, monthOfYear, dayOfMonth);
                try {
                     str_frodate = df.format(newDate.getTime());
                    daily_date = df_daily.format(newDate.getTime());
                    txt_date.setText(df_daily.format(newDate.getTime()));
                    laydate.setVisibility(View.VISIBLE);
                    homelay.setVisibility(View.VISIBLE);
                    getall(empid);
                    get_colactivity();
                    txt_tot.setText("");
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }, newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Date");
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000 * 24);
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewmenu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_calender:
                listmain.clear();
                laydate.setVisibility(View.GONE);
                showcal();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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

    public void Load_loans() {

        showDialog();
        System.out.println("loans go");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.loanreceiptdatevise, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                System.out.println(response.toString());


                try {

                    loan_list.clear();
                    loan_list_main.clear();

                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("loans");

                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);


                            LoanModelDatewise sched = new LoanModelDatewise();
                            sched.setCust_name(jObj.getString("cust_name"));
                            sched.setAmount(jObj.getString("amount"));
                            sched.setDate(jObj.getString("Date"));
                            sched.setTime(jObj.getString("Receipt_time"));
                            sched.setPaymode(jObj.getString("Payment_Mode"));
//                            sched.setReference_Grp(jObj.getString("Reference_Grp"));
                            loan_list.add(sched);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();

                    }


                    if (loan_list.size() > 0) {
                        try {
                            System.out.println("loans go 1");
                            loan_list_main.clear();
                            loan_list_main.addAll(loan_list);
                            btn_collectreceipt.setEnabled(true);
                            loanadaptrerdatewise = new CustomAdapterLoanDatewise(DateWiseView.this, loan_list_main);
                            loanadaptrerdatewise.notifyDataSetChanged();
                            int tot1=0;
                            for (int i = 0; i < loan_list_main.size(); i++) {
                                LoanModelDatewise lm = loan_list_main.get(i);
                                String loantot = lm.getAmount();
                                System.out.println("loan to " + loantot);
                                int to = Integer.parseInt(loantot);
                                tot1 += to;
                            }
                            String advance = String.valueOf(tot1);
                            try {
                                if (advance.contains("-")) {
                                    advance = "0";
                                }
                                Double d = Double.parseDouble(advance);
                                String moneyString3 = NumberFormat.getNumberInstance(curLocale).format(d);
                                advance = moneyString3;
                            } catch (NumberFormatException e) {
                                e.printStackTrace();
                            }
                            advancetotal=advance;
//                            txt_tot.setText("Rs. " + advancetotal);
                            System.out.println("loans go 2 " + advancetotal);
                            txt_tot.setVisibility(View.VISIBLE);
                        } catch (Exception e) {

                            e.printStackTrace();
                        }


                    } else {
                        txt_tot.setVisibility(View.VISIBLE);
                        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(
                                DateWiseView.this);
                        alertDialog.setTitle("Information");
                        alertDialog
                                .setMessage("No Loan receipts Available");

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
//                        alertDialog.show();
                    }

                    hidePDialog();

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
                params.put("empid", empid);
                params.put("date", daily_date);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
}
