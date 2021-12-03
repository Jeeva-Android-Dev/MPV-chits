package com.mazenetsolutions.mzs119.skst_new;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
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
import com.mazenetsolutions.mzs119.skst_new.Adapter.CustomAdapterDatewise;
import com.mazenetsolutions.mzs119.skst_new.Adapter.CustomAdapterLoanDatewise;
import com.mazenetsolutions.mzs119.skst_new.Model.DateWiseViewModel;
import com.mazenetsolutions.mzs119.skst_new.Model.TempEnrollModel;
import com.mazenetsolutions.mzs119.skst_new.Model.colfeedbackmodel;
import com.mazenetsolutions.mzs119.skst_new.Utils.AppController;
import com.mazenetsolutions.mzs119.skst_new.Utils.Config;
import com.mazenetsolutions.mzs119.skst_new.Utils.ConnectionDetector;

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
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Datewise2 extends AppCompatActivity {

    DatePickerDialog datePickerDialog;
    DateFormat df, df2;
    TextView txt_date, txt_tot, tit_cusname, tit_amnt, tit_paytype;
    LinearLayout laydate, total_lay;
    RelativeLayout homelay;
    ListView viewlist;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    private ProgressDialog pDialog;
    private static final String TAG = DateWiseView.class.getSimpleName();
    String url = Config.getallviewbydate, str_frodate, daily_date;
    ArrayList<DateWiseViewModel> listmain = new ArrayList<>();
    ArrayList<DateWiseViewModel> listmain_main = new ArrayList<>();
    ArrayList<TempEnrollModel> loan_list = new ArrayList<>();
    ArrayList<TempEnrollModel> loan_list_main = new ArrayList<>();
    ArrayList<colfeedbackmodel> colllist = new ArrayList<>();
    ArrayList<colfeedbackmodel> colllist_main = new ArrayList<>();
    CustomAdapterDatewise adapterDatewise;
    Adap_collfeedback adap_collfeedback;
    Button btn_collectreceipt, btn_loanreceipt, col_activity;
    String isLoans = "false";
    String duetotal, advancetotal;
    CustomAdapterLoanDatewise loanadaptrerdatewise;
    int tot = 0, tot1 = 0;
    Locale curLocale = new Locale("en", "IN");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datewise2);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();

        cd = new ConnectionDetector(this);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);

        df2 = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        df = new SimpleDateFormat("dd-MM-yyyy");
        txt_date = (TextView) findViewById(R.id.datetext);
        tit_paytype = (TextView) findViewById(R.id.tit_paytype);
        tit_cusname = (TextView) findViewById(R.id.tit_cusname);
        tit_amnt = (TextView) findViewById(R.id.tit_amnt);
        laydate = (LinearLayout) findViewById(R.id.datelay);
        homelay = (RelativeLayout) findViewById(R.id.lay_view_home);
        viewlist = (ListView) findViewById(R.id.list_view_datewise);
        txt_tot = (TextView) findViewById(R.id.txt_datew_total);
        total_lay = (LinearLayout) findViewById(R.id.hl_tot);

        btn_collectreceipt = (Button) findViewById(R.id.collection_receipts);
        btn_loanreceipt = (Button) findViewById(R.id.loan_receipts);
        col_activity = (Button) findViewById(R.id.col_activity);
        showcal();

        btn_collectreceipt.setTextColor(getResources().getColor(R.color.white));
        btn_loanreceipt.setTextColor(getResources().getColor(R.color.black));
        btn_collectreceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewlist.setAdapter(adapterDatewise);
                viewlist.setVisibility(View.VISIBLE);
                total_lay.setVisibility(View.VISIBLE);
                isLoans = "coll";
                tit_cusname.setText("Customer Name");
                tit_amnt.setText("Amount");
                tit_paytype.setText("Payment Type");
                btn_collectreceipt.setTextColor(getResources().getColor(R.color.white));
                btn_loanreceipt.setTextColor(getResources().getColor(R.color.black));
                col_activity.setTextColor(getResources().getColor(R.color.black));
                txt_tot.setText(duetotal);
            }
        });
        btn_loanreceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewlist.setAdapter(loanadaptrerdatewise);
                txt_tot.setVisibility(View.VISIBLE);
                viewlist.setVisibility(View.VISIBLE);
                total_lay.setVisibility(View.VISIBLE);
                tit_cusname.setText("Customer Name");
                tit_amnt.setText("Amount");
                tit_paytype.setText("Payment Type");
                btn_collectreceipt.setTextColor(getResources().getColor(R.color.black));
                btn_loanreceipt.setTextColor(getResources().getColor(R.color.white));
                col_activity.setTextColor(getResources().getColor(R.color.black));
                isLoans = "advc";
                txt_tot.setText(advancetotal);
            }
        });
        col_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewlist.setAdapter(adap_collfeedback);
                txt_tot.setVisibility(View.GONE);
                viewlist.setVisibility(View.VISIBLE);
                total_lay.setVisibility(View.GONE);
                tit_cusname.setText("Customer Name");
                tit_amnt.setText("Feedback");
                tit_paytype.setText("Followup Date");
                isLoans = "feed";
                btn_collectreceipt.setTextColor(getResources().getColor(R.color.black));
                btn_loanreceipt.setTextColor(getResources().getColor(R.color.black));
                col_activity.setTextColor(getResources().getColor(R.color.white));
            }
        });
        btn_collectreceipt.setSelected(true);

        viewlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (isLoans.equalsIgnoreCase("coll")) {
                    final String receiptno = listmain_main.get(i).getReceipt_No();
                    final String cusid = listmain_main.get(i).getCust_Id();
                    final String recid = listmain_main.get(i).getRecptid();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            Datewise2.this, R.style.MyAlertDialogStyleApproved);
                    alertDialog.setTitle("Resend SMS");
                    alertDialog
                            .setMessage("Choose the mode of receipt to be sent");
                    alertDialog.setNegativeButton("WhatsApp",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                    String whatsappurl = null;
                                    try {
                                        whatsappurl = Config.mobile_receipt_print + "rno=" + receiptno + "&Cust_Id=" + cusid;
//                                        sendwhatsapp(receiptno,cusid);
                                        System.out.println("what " + whatsappurl);
                                        HttpAsyncTask hat = new HttpAsyncTask();
                                        hat.execute(whatsappurl);

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                    Intent i = new Intent(Datewise2.this, CollectionActivity.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    finish();
                                }
                            });
                    alertDialog.setPositiveButton("SMS", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            sendsms(recid);
                        }
                    });

                    alertDialog.show();
                } else if (isLoans.equalsIgnoreCase("advc")) {
                    final String recid = loan_list_main.get(i).getTableid();
                    AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                            Datewise2.this, R.style.MyAlertDialogStyleApproved);
                    alertDialog.setTitle("Success");
                    alertDialog
                            .setMessage("Do you want to resend SMS?");
                    alertDialog.setNegativeButton("No",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {
                                    dialog.cancel();
                                }
                            });
                    alertDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            sendadvsms(recid);
                        }
                    });
                    alertDialog.show();
                }

            }
        });


    }

    public void getall() {
        // showDialog();

        tot1 = 0;
        txt_tot.setVisibility(View.GONE);
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());


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
                        Toast.makeText(Datewise2.this, "No receipts Available", Toast.LENGTH_SHORT).show();
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
//                                dvm.setPenaltyamnt(jObj.getString("Penalty_Amt"));
                                dvm.setRecptid(jObj.getString("Id"));
                                dvm.setTotal_Enrl_Paid(jObj.getString("Total_Enrl_Paid"));
                                dvm.setTotal_Enrl_Pending(jObj.getString("Total_Enrl_Pending"));
                                dvm.setAdvance_Amt(jObj.getString("Advance_Amt"));
                                listmain.add(dvm);

                            }
                            if (listmain.size() > 0) {
                                //   hidePDialog();
                                for (int i = 0; i < listmain.size(); i++) {
                                    DateWiseViewModel dm = listmain.get(i);
                                    String paid = dm.getPayment_Type();
                                    if (paid.equalsIgnoreCase("daily.rct.adj")) {

                                    } else {
                                        listmain_main.add(dm);
                                    }
                                }
//                                listmain_main.addAll(listmain);
                                adapterDatewise = new CustomAdapterDatewise(Datewise2.this, listmain_main);
                                adapterDatewise.notifyDataSetChanged();
                                viewlist.setAdapter(adapterDatewise);
                                viewlist.setVisibility(View.VISIBLE);
                                btn_collectreceipt.setTextColor(getResources().getColor(R.color.white));
                                btn_loanreceipt.setTextColor(getResources().getColor(R.color.black));
                                col_activity.setTextColor(getResources().getColor(R.color.black));
                                btn_collectreceipt.setEnabled(true);
                                isLoans = "coll";
                                for (int i = 0; i < listmain_main.size(); i++) {

                                    DateWiseViewModel tem = listmain_main.get(i);
                                    String tote = tem.getTotal_Amount().replaceAll(" ", "");
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
                                duetotal = advance;
                                txt_tot.setText("Rs. " + String.valueOf(advance));
                                txt_tot.setVisibility(View.VISIBLE);
                                Load_loans();

                            } else {
                                //   hidePDialog();
                                Load_loans();

                                total_lay.setVisibility(View.GONE);
                                viewlist.setVisibility(View.GONE);
                                Toast.makeText(Datewise2.this, "No receipts Available", Toast.LENGTH_SHORT).show();

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
                //   hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Emp_Id", pref.getString("userid", ""));
                System.out.println(pref.getString("userid", ""));
                try {
                    System.out.println(str_frodate);
                    params.put("Date", str_frodate);

                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println("cole emp " + pref.getString("userid", "0") + " date " + str_frodate);

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(90000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }

    public void showcal() {
        listmain.clear();
        laydate.setVisibility(View.GONE);
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(Datewise2.this,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                Calendar calendar = Calendar.getInstance();

                newDate.set(year, monthOfYear, dayOfMonth);
                calendar.set(year, monthOfYear, dayOfMonth);
                try {
                    str_frodate = df2.format(newDate.getTime());
                    daily_date = df.format(newDate.getTime());
                    txt_date.setText(daily_date);
                    laydate.setVisibility(View.VISIBLE);
                    homelay.setVisibility(View.VISIBLE);
                    get_colactivity();
                    getall();

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
                showcal();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*   private void hidePDialog() {
           if (pDialog.isShowing()) {
               pDialog.dismiss();

           }
       }

       private void showDialog() {
           if (!pDialog.isShowing())
               pDialog.show();
           pDialog.setContentView(R.layout.my_progress);

       } */
    public void Load_loans() {

        // showDialog();

        System.out.println("loans go");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.getalladvancebydate, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                System.out.println(response.toString());


                try {

                    loan_list.clear();
                    loan_list_main.clear();

                    JSONObject object = new JSONObject(response);
                    String jsonob = object.getString("result");
                    if (jsonob.equals("0")) {
                        //viewlist.setVisibility(View.GONE);
                        Toast.makeText(Datewise2.this, "No Advanced receipts Available", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray ledgerarray = object.getJSONArray("result");
                        try {
                            for (int i = 0; i < ledgerarray.length(); i++) {
                                JSONObject jObj = ledgerarray.getJSONObject(i);


                                TempEnrollModel sched = new TempEnrollModel();
                                sched.setCusname(jObj.getString("First_Name_F"));
                                sched.setTableid(jObj.getString("tableid"));
                                sched.setChequeBank(jObj.getString("Cheque_Bank"));
                                sched.setChequeBranch(jObj.getString("Cheque_Branch"));
                                sched.setChequeDate(jObj.getString("Cheque_Date"));
                                sched.setChequeNo(jObj.getString("Cheque_No"));
                                sched.setCusid(jObj.getString("Cust_Id"));
                                sched.setCusid(jObj.getString("Id"));
                                sched.setPaytype(jObj.getString("Pay_Mode"));
                                sched.setAmount(jObj.getString("Receipt_Amt"));
//                                sched.set(jObj.getString("Receipt_No"));
//                                sched.setTotamnt(jObj.getString("Total_Amt"));
                                sched.setRecdate(jObj.getString("Receipt_Date"));
                                loan_list.add(sched);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();

                        }
                    }

                    if (loan_list.size() > 0) {
                        try {
                            // hidePDialog();
                            System.out.println("loans go 1");

                            loan_list_main.clear();
                            loan_list_main.addAll(loan_list);

                            btn_collectreceipt.setEnabled(true);
                            loanadaptrerdatewise = new CustomAdapterLoanDatewise(Datewise2.this, loan_list_main);
                            loanadaptrerdatewise.notifyDataSetChanged();
                            int tot1 = 0;
                            for (int i = 0; i < loan_list_main.size(); i++) {
                                TempEnrollModel lm = loan_list_main.get(i);
                                String loantot = lm.getAmount().replaceAll(" ", "");
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
                            advancetotal = advance;
//                            txt_tot.setText("Rs. " + advance);
                            System.out.println("loans go 2 " + advance);
                            txt_tot.setVisibility(View.VISIBLE);
                        } catch (Exception e) {

                            e.printStackTrace();
                        }


                    } else {
                        //  hidePDialog();
                        txt_tot.setVisibility(View.VISIBLE);
                        // Toast.makeText(DateWiseView.this, "No Loan receipts Available", Toast.LENGTH_SHORT).show();
                     /*   AlertDialog.Builder alertDialog = new AlertDialog.Builder(
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
                        alertDialog.show(); */
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
                //  hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Emp_Id", pref.getString("userid", "0"));
                System.out.println("advance emp " + pref.getString("userid", "0") + " date " + daily_date);
                params.put("Date", daily_date);

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(90000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
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
                            adap_collfeedback = new Adap_collfeedback(Datewise2.this, colllist_main);
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
                System.out.println("feedback emp " + pref.getString("userid", "0") + " date " + daily_date);
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
    private void sendadvsms(final String recpid) {
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
    private void sendsms(final String recpid) {
        showDialog();
        StringRequest localreq = new StringRequest(Request.Method.POST,
                Config.send_receipt_sms, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                hidePDialog();
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
    public void sendwhatsapp(final String recno, final String cusid) {

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
                params.put("rno", recno);
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
