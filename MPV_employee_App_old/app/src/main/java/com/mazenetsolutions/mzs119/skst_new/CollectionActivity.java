package com.mazenetsolutions.mzs119.skst_new;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.mazenetsolutions.mzs119.skst_new.Adapter.CustomAdaptercustomer;
import com.mazenetsolutions.mzs119.skst_new.Adapter.Listadapterfeedback;
import com.mazenetsolutions.mzs119.skst_new.Database.Databasecustomers;
import com.mazenetsolutions.mzs119.skst_new.Database.Databasefeedback;
import com.mazenetsolutions.mzs119.skst_new.Database.Databaserecepit;
import com.mazenetsolutions.mzs119.skst_new.Model.Custmodel;
import com.mazenetsolutions.mzs119.skst_new.Model.Enrollmodel;
import com.mazenetsolutions.mzs119.skst_new.Model.FeedbackModel;
import com.mazenetsolutions.mzs119.skst_new.Utils.AppController;
import com.mazenetsolutions.mzs119.skst_new.Utils.Config;
import com.mazenetsolutions.mzs119.skst_new.Utils.ConnectionDetector;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CollectionActivity extends AppCompatActivity {


    public ArrayList<Custmodel> customer_list = new ArrayList<Custmodel>();
    public ArrayList<Custmodel> customer_listmain = new ArrayList<Custmodel>();
    public ArrayList<FeedbackModel> feedlist = new ArrayList<FeedbackModel>();
    public ArrayList<FeedbackModel> feedlistmain = new ArrayList<FeedbackModel>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    String url = Config.reteriveusers;
    String url1 = Config.sendfeedback;
    String urlfeed = Config.reterivefeedback;
    Databasecustomers dbcust;
    Databasefeedback dbfeed;
    TextView head_cusname, head_pending, head_penddays;
    ListView list;
    CustomAdaptercustomer adapterlist;
    EditText edt_search;
    boolean check = true, follow = false;
    Listadapterfeedback adapter1;
    private ProgressDialog pDialog;
    String url3 = Config.retrievealluser;
    Databaserecepit dbrecepit;
    public ArrayList<Enrollmodel> enroll_list_local = new ArrayList<Enrollmodel>();
    private IntentIntegrator qrScan;
    String DOB1 = "";
    Boolean sortname = false, sortdays = false, sortpending = false;
    SimpleDateFormat df_daily = new SimpleDateFormat("dd-MM-yyyy");
    // ArrayList<String> ledgername = new ArrayList<String>();
    //  private ArrayList<String> name11 = new ArrayList<String>();
    SimpleDateFormat df;
    String todaydate = "", tomorowdate = "";

    //Getting the scan results
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (result != null) {
            //if qrcode has nothing in it
            if (result.getContents() == null) {
                Toast.makeText(this, "Result Not Found", Toast.LENGTH_LONG).show();
            } else {
                //if qr contains data
                try {
                    //converting the data to json
                    JSONObject obj = new JSONObject(result.getContents());

                } catch (JSONException e) {
                    e.printStackTrace();
                    //if control comes here
                    //that means the encoded format not matches
                    //in this case you can display whatever data is available on the qrcode
                    //to a toast
                    //edt_search.setVisibility(View.VISIBLE);
                    edt_search.setText(result.getContents());
                    //Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();

                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_collection);

        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();
//        df = new SimpleDateFormat("dd-MM-yyyy");
        df = new SimpleDateFormat("dd");
        cd = new ConnectionDetector(this);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);
        head_cusname = (TextView) findViewById(R.id.head_cusname);
        head_pending = (TextView) findViewById(R.id.head_pending);
        head_penddays = (TextView) findViewById(R.id.head_penddays);
        Date c = Calendar.getInstance().getTime();
        todaydate = df.format(c);
        System.out.println("Current time => " + todaydate);

        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, 1);
        Date resultdate = new Date(now.getTimeInMillis());
        tomorowdate = df.format(resultdate);
        System.out.println("then date : " + tomorowdate);

        dbcust = new Databasecustomers(this);
        dbfeed = new Databasefeedback(this);

        //intializing scan object
        qrScan = new IntentIntegrator(this);

        list = (ListView) findViewById(R.id.list_trading1);
        edt_search = (EditText) findViewById(R.id.edt_search);

        dbrecepit = new Databaserecepit(this);
        head_cusname.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sortname) {
                    Collections.sort(
                            customer_listmain,
                            new Comparator<Custmodel>() {
                                public int compare(Custmodel lhs, Custmodel rhs) {
                                    return lhs.getNAME().compareTo(rhs.getNAME());
                                }
                            }
                    );
                    head_cusname.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_ascend, 0);
                    head_cusname.setCompoundDrawablePadding(1);
                    head_pending.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    head_penddays.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    sortname = true;
                    try {
                        adapterlist.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    Collections.sort(
                            customer_listmain,
                            new Comparator<Custmodel>() {
                                public int compare(Custmodel lhs, Custmodel rhs) {
                                    return (rhs.getNAME().compareTo(lhs.getNAME()));
                                }
                            }
                    );
                    head_cusname.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_descend, 0);
                    head_cusname.setCompoundDrawablePadding(1);
                    head_pending.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    head_penddays.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    sortname = false;
                    try {
                        adapterlist.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        head_pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sortpending) {

                    Collections.sort(
                            customer_listmain,
                            new Comparator<Custmodel>() {
                                public int compare(Custmodel lhs, Custmodel rhs) {
                                    return Integer.parseInt(lhs.getTotalenrlpending()) - (Integer.parseInt(rhs.getTotalenrlpending()));
                                }
                            }
                    );
                    head_pending.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_ascend, 0);
                    head_pending.setCompoundDrawablePadding(1);
                    head_cusname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    head_penddays.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    sortpending = true;
                    try {
                        adapterlist.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    Collections.sort(
                            customer_listmain,
                            new Comparator<Custmodel>() {
                                public int compare(Custmodel lhs, Custmodel rhs) {
                                    return Integer.parseInt(rhs.getTotalenrlpending()) - (Integer.parseInt(lhs.getTotalenrlpending()));
                                }
                            }
                    );
                    head_pending.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_descend, 0);
                    head_pending.setCompoundDrawablePadding(1);
                    head_cusname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    head_penddays.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    sortpending = false;
                    try {
                        adapterlist.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        head_penddays.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sortdays) {

                    Collections.sort(
                            customer_listmain,
                            new Comparator<Custmodel>() {
                                public int compare(Custmodel lhs, Custmodel rhs) {
                                    return Integer.parseInt(lhs.getEnrlpaid()) - (Integer.parseInt(rhs.getEnrlpaid()));
                                }
                            }
                    );
                    head_penddays.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_ascend, 0);
                    head_penddays.setCompoundDrawablePadding(1);
                    head_cusname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    head_pending.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    sortdays = true;
                    try {
                        adapterlist.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {

                    Collections.sort(
                            customer_listmain,
                            new Comparator<Custmodel>() {
                                public int compare(Custmodel lhs, Custmodel rhs) {
                                    return Integer.parseInt(rhs.getEnrlpaid()) - (Integer.parseInt(lhs.getEnrlpaid()));
                                }
                            }
                    );
                    head_penddays.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_descend, 0);
                    head_penddays.setCompoundDrawablePadding(1);
                    head_cusname.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    head_pending.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
                    sortdays = false;
                    try {
                        adapterlist.notifyDataSetChanged();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Custmodel cmd = customer_listmain.get(position);
                Intent i = new Intent(CollectionActivity.this, Customer_Info.class);
                i.putExtra("name", cmd.getNAME());
                i.putExtra("mobile", cmd.getMOBILE());
                i.putExtra("custid", cmd.getCusid());
                startActivity(i);
                return true;

            }
        });
        //===================================================================
        // reteriveall();
        if (cd.isConnectedToInternet()) {
            try {

                Calendar newCalendar = Calendar.getInstance();

                int ddd = newCalendar.get(Calendar.DAY_OF_MONTH);
                int wwww = newCalendar.get(Calendar.WEEK_OF_YEAR);
                System.out.println("day " + String.valueOf(ddd));
                if (ddd == pref.getInt("dailycheckdaymain", 0) && wwww == pref.getInt("dailycheckmonthmain", 0) && dbcust.getContactsCount() != 0) {
                    reterivelocal();
                    System.out.println("loc " + String.valueOf(ddd));
                } else {


                    reteriveall();
                    editor.putInt("dailycheckdaymain",
                            newCalendar.get(Calendar.DAY_OF_MONTH));
                    editor.putInt("dailycheckmonthmain",
                            newCalendar.get(Calendar.WEEK_OF_YEAR));


                    editor.commit();
                    System.out.println("day " + String.valueOf(ddd));
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

            try {

                Calendar newCalendar = Calendar.getInstance();

                int ddd1 = newCalendar.get(Calendar.DAY_OF_MONTH);
                int wwww1 = newCalendar.get(Calendar.WEEK_OF_YEAR);

                if (ddd1 == pref.getInt("dailycheckdayfeed", 0) && wwww1 == pref.getInt("dailycheckmonthfeed", 0) && dbfeed.getContactsCount() != 0) {
                    reterivelocalfeed();

                } else {


                    reterivefeedback();
                    editor.putInt("dailycheckdayfeed",
                            newCalendar.get(Calendar.DAY_OF_MONTH));
                    editor.putInt("dailycheckmonthfeed",
                            newCalendar.get(Calendar.WEEK_OF_YEAR));

                    editor.commit();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (dbcust.getContactsCount() != 0) {
                reterivelocal();

            } else {
                Toast.makeText(CollectionActivity.this, "First Choose Go Offline to work offline", Toast.LENGTH_SHORT).show();
            }
            if (dbfeed.getContactsCount() != 0) {
                reterivelocalfeed();

            } else {
                Toast.makeText(CollectionActivity.this, "First Choose Go Offline to work offline", Toast.LENGTH_SHORT).show();
            }
        }


        edt_search.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {

                String text = edt_search.getText().toString();
                if (text.equals("") || text.equals(null)) {

                    customer_listmain.clear();

                    customer_listmain.addAll(customer_list);


                    if (customer_listmain.size() > 0) {
                        list.setVisibility(View.VISIBLE);
                        adapterlist = new CustomAdaptercustomer(CollectionActivity.this, customer_listmain);
                        adapterlist.notifyDataSetChanged();
                        list.setAdapter(adapterlist);

                    } else {

                        list.setVisibility(View.GONE);
                    }
                } else {
                    setnewadapter();
                    list.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                // lv.setVisibility(View.GONE);

                if (customer_listmain.size() > 0) {
                    list.setVisibility(View.VISIBLE);

                } else {

                    list.setVisibility(View.GONE);

                }

            }
        });


        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Custmodel cmd = customer_listmain.get(i);
                listclick(cmd.getCusid(), cmd.getNAME(), cmd.getMOBILE(), cmd.getTotalenrlpending());

            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.menu_search:
                if (check) {
                    edt_search.setVisibility(View.VISIBLE);
                    check = false;
                } else {
                    edt_search.setVisibility(View.GONE);
                    check = true;
                }
                return true;

            case R.id.menu_refresh:

                if (cd.isConnectedToInternet()) {
                    reteriveall();
                } else {
                    Toast.makeText(CollectionActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
                }


//                    reterivelocalreceipts();
                return true;

            case R.id.menu_qrcode:
                qrScan.initiateScan();


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void reterivelocalreceipts() {

        showDialog();
        enroll_list_local.clear();
        StringRequest localreq = new StringRequest(Request.Method.POST,
                url3, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());


                try {

                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("customer");

                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);

                            Enrollmodel sched = new Enrollmodel();
                            sched.setEnrollid(jObj.getString("enrollid"));
                            sched.setCusid(jObj.getString("Cust_Id"));
                            sched.setScheme(jObj.getString("chitvalue"));
                            sched.setPending_Amt(jObj.getString("pendingamnt"));
                            sched.setPaid_Amt(jObj.getString("paidamnt"));
                            sched.setPenalty_Amt("0");
                            sched.setBonus_Amt("0");
                            sched.setGroup_Name(jObj.getString("grpname"));
                            sched.setPendingdys(jObj.getString("pendingdays"));
                            sched.setCusbranch(pref.getString("empbranch", ""));
                            sched.setGroup_Ticket_Name(jObj.getString("grpticketno"));
                            sched.setPayamount("0");
                            sched.setGrpid(jObj.getString("grpid"));
                            sched.setAdvanceamnt(jObj.getString("advanceamnt"));
                            System.out.println("advanceamnt  " + jObj.getString("advanceamnt"));
                            enroll_list_local.add(sched);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (enroll_list_local.size() > 0) {

                        try {
                            dbrecepit.deletetableaLLREC();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            hidePDialog();
                            Toast.makeText(CollectionActivity.this, "database loaded", Toast.LENGTH_SHORT).show();
                            dbrecepit.addallreceipt(enroll_list_local);

                            // adapterlist = new CustomAdapterenrollment(RecepitActivity.this, enroll_list_local);
                            //adapterlist.notifyDataSetChanged();
                            //ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
                            // lst_re_enroll.setAdapter(adapterlist);
                            //customer_list.clear();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                CollectionActivity.this);
                        alertDialog.setTitle("Information");
                        alertDialog
                                .setMessage("No Data from Server  locall. contact Admin");

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


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", pref.getString("userid", ""));

                return params;
            }

        };
        localreq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(localreq);
    }

    // ========================================================================
    public void reteriveall() {

        showDialog();

        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("res_url" + response);
                hidePDialog();


                try {

                    customer_list.clear();


                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("customer");

                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);


                            Custmodel sched = new Custmodel();
                            sched.setCusid(jObj.getString("Id"));
                            sched.setCustomer_id(jObj.getString("Customer_Id"));
                            sched.setNAME(jObj.getString("First_Name_F"));
                            sched.setMOBILE(jObj.getString("Mobile_F"));
                            sched.setAdvanceamt(jObj.getString("Advanced_Amt"));
                            sched.setPendingamt(jObj.getString("Pending_Amt"));
                            sched.setTotalenrlpending(jObj.getString("Total_Enrl_Pending"));
                            sched.setEnrlpaid(jObj.getString("Total_Enrl_Paid"));
                            sched.setLevel(jObj.getString("Level"));
                            sched.setBonusamt(jObj.getString("Bonus_Amt"));
                            sched.setPenaltyamt(jObj.getString("Penalty_Amt"));
                            sched.setPendingdays(jObj.getString("Pending_Days"));
                            sched.setLastdate(jObj.getString("lastdate"));
                            try {
                                sched.setTomorowdate(jObj.getString("tomorrow"));
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
//                            System.out.println("last " + jObj.getString("lastdate"));
//                            System.out.println("tom " + jObj.getString("tomorrow"));
                            customer_list.add(sched);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (customer_list.size() > 0) {


                        try {
                            dbcust.deletetable();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {

                            dbcust.addcustomer(customer_list);

                            customer_listmain.clear();

                            ArrayList<Custmodel> level1 = new ArrayList<>();
                            ArrayList<Custmodel> level2 = new ArrayList<>();
                            ArrayList<Custmodel> level3 = new ArrayList<>();
                            ArrayList<Custmodel> todaylist = new ArrayList<>();
                            ArrayList<Custmodel> yesterdaylist = new ArrayList<>();

                            for (int i = 0; i < customer_list.size(); i++) {
                                Custmodel c = customer_list.get(i);
                                String level = c.getLevel();
                                String tod = c.getLastdate();
                                String yes = c.getTomorowdate();
                                if (tod.equalsIgnoreCase(todaydate)) {
                                    yesterdaylist.add(c);
                                } else {
                                    if (tod.equalsIgnoreCase(tomorowdate)) {
                                        todaylist.add(c);
                                    } else {
                                        level3.add(c);
                                    }
                                }
//                                if (level.equalsIgnoreCase("Awaiting Payment")) {
//
//                                    level1.add(c);
//                                } else if (level.equalsIgnoreCase("Customer, Guarantor Letter") || level.equalsIgnoreCase("Customer Letter")) {
//
//                                    level2.add(c);
//                                } else {
//
//                                    level3.add(c);
//                                }

                            }
//                            customer_listmain.addAll(level1);
//                            customer_listmain.addAll(level2);

                            customer_listmain.addAll(yesterdaylist);
                            customer_listmain.addAll(todaylist);
                            customer_listmain.addAll(level3);
//                            customer_listmain.addAll(customer_list);
                            level1.clear();
                            level2.clear();
                            level3.clear();
                            yesterdaylist.clear();
                            todaylist.clear();
                            adapterlist = new CustomAdaptercustomer(CollectionActivity.this, customer_listmain);
                            adapterlist.notifyDataSetChanged();
                            list.setAdapter(adapterlist);

                            // customer_list.clear();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                CollectionActivity.this);
                        alertDialog.setTitle("Information");
                        alertDialog
                                .setMessage("No Data from Server. contact apdi Admin");

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
                params.put("userid", pref.getString("userid", "0"));
                System.out.println("use" +
                        "rid " + pref.getString("userid", "0"));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(35000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }


    // ===========================================================================


    public void reterivelocal() {

        customer_list.clear();
        customer_listmain.clear();
        customer_list = dbcust.getAllContacts();
        ArrayList<Custmodel> level1 = new ArrayList<>();
        ArrayList<Custmodel> level2 = new ArrayList<>();
        ArrayList<Custmodel> level3 = new ArrayList<>();
        ArrayList<Custmodel> todaylist = new ArrayList<>();
        ArrayList<Custmodel> yesterdaylist = new ArrayList<>();
        for (int i = 0; i < customer_list.size(); i++) {
            Custmodel c = customer_list.get(i);
            String level = c.getLevel();
            String tod = c.getLastdate();
            String yes = c.getTomorowdate();
            if (tod.equalsIgnoreCase(todaydate)) {
                yesterdaylist.add(c);
            } else {
                if (tod.equalsIgnoreCase(tomorowdate)) {
                    todaylist.add(c);
                } else {
                    level3.add(c);
                }

            }
//            if (level.equalsIgnoreCase("Awaiting Payment")) {
//
//                level1.add(c);
//            } else if (level.equalsIgnoreCase("Customer, Guarantor Letter") || level.equalsIgnoreCase("Customer Letter")) {
//
//                level2.add(c);
//            } else {
//
//                level3.add(c);
//            }

        }
//        customer_listmain.addAll(level1);
//        customer_listmain.addAll(level2);
//        customer_listmain.addAll(level3);
//        level1.clear();
//        level2.clear();
//        level3.clear();
//        customer_listmain.addAll(customer_list);

        customer_listmain.addAll(yesterdaylist);
        customer_listmain.addAll(todaylist);
        customer_listmain.addAll(level3);
        level1.clear();
        level2.clear();
        level3.clear();
        yesterdaylist.clear();
        todaylist.clear();
        adapterlist = new CustomAdaptercustomer(CollectionActivity.this, customer_listmain);
        adapterlist.notifyDataSetChanged();
        list.setAdapter(adapterlist);


    }


    //============================================================================

    public void reterivelocalfeed() {

        feedlist.clear();
        feedlistmain.clear();
        feedlist = dbfeed.getAllfeed();
        feedlistmain.addAll(feedlist);


    }

    // ========================================================================
    public void reterivefeedback() {

        showDialog();

        StringRequest movieReq = new StringRequest(Request.Method.POST,
                urlfeed, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                hidePDialog();


                try {

                    feedlist.clear();


                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("customer");

                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);


                            FeedbackModel sched = new FeedbackModel();
                            sched.setName(jObj.getString("Feedback"));
                            sched.setFollowup(jObj.getString("Follow_up"));


                            feedlist.add(sched);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (feedlist.size() > 0) {


                        try {
                            dbfeed.deletetable();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {

                            dbfeed.addfeedback(feedlist);

                            feedlistmain.clear();
                            feedlistmain.addAll(feedlist);

                            // customer_list.clear();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                CollectionActivity.this);
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
                // params.put("compname", pref.getString("company", ""));


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }


    // ===========================================================================


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

    // ===========================================================================


    private void setnewadapter() {
        customer_listmain.clear();

        String text = edt_search.getText().toString();
        text = text.toLowerCase(Locale.getDefault());
        if (text.equals(null)) {

            list.setVisibility(View.GONE);
            return;

        } else {


            for (int i = 0; i < customer_list.size(); i++) {
                Custmodel schedte = customer_list.get(i);
                String name = schedte.getNAME();
                String cusid = schedte.getCustomer_id();
                String mobile = schedte.getMOBILE();

                name = name.toLowerCase(Locale.getDefault());
                mobile = mobile.toLowerCase(Locale.getDefault());
                cusid = cusid.toLowerCase(Locale.getDefault());

                if (name.contains(text) || mobile.contains(text) || cusid.contains(text)) {
                    customer_listmain.add(customer_list.get(i));

                }

            }


        }

        if (customer_listmain.size() > 0) {
            list.setVisibility(View.VISIBLE);
            adapterlist = new CustomAdaptercustomer(CollectionActivity.this, customer_listmain);
            adapterlist.notifyDataSetChanged();
            list.setAdapter(adapterlist);

        } else {

            list.setVisibility(View.GONE);
        }

    }

    //=========================================================================================================

    public void listclick(final String cusid, final String cusname, final String mobile, final String pending) {

        final Dialog dialog = new Dialog(CollectionActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(R.layout.diaremark);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        final SimpleDateFormat dftt = new SimpleDateFormat("MM/dd/yyyy");


        final Button btn_recepit = (Button) dialog.findViewById(R.id.btn_recepit);
        final Button btn_Collectionactivity = (Button) dialog.findViewById(R.id.btn_Collectionactivity);
        final Button next_followup = (Button) dialog.findViewById(R.id.next_followup);
        final Button btn_advance_recepit = (Button) dialog.findViewById(R.id.btn_advancerecepit);
        final Button btn_subsinfo = (Button) dialog.findViewById(R.id.btn_subsinfo);

        Button submit = (Button) dialog.findViewById(R.id.submit);

        final EditText edt_remark = (EditText) dialog.findViewById(R.id.edt_remark);


        final LinearLayout lay_sign_in = (LinearLayout) dialog.findViewById(R.id.lay_sign_in);
        final ListView lv = (ListView) dialog.findViewById(R.id.list);

        try {
            int pendings = Integer.parseInt(pending);
            if (pendings <= 0) {
                btn_advance_recepit.setVisibility(View.VISIBLE);
                btn_recepit.setVisibility(View.GONE);
            } else {
                btn_advance_recepit.setVisibility(View.GONE);
                btn_recepit.setVisibility(View.VISIBLE);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        adapter1 = new Listadapterfeedback(CollectionActivity.this, feedlistmain);
        lv.setAdapter(adapter1);

        edt_remark.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,
                                      int arg3) {
                // When user changed the Text
                // Salesentry.this.adapter.getFilter().filter(cs);
                String text = edt_remark.getText().toString();
                if (text.equals("") || text.equals(null) || text.isEmpty()) {
                    lv.setVisibility(View.GONE);
                } else {
                    {
                        feedlistmain.clear();

                        text = text.toLowerCase(Locale.getDefault());
                        if (text.equals(null)) {

                            // adapter.refresh(ledgername);
                            lv.setVisibility(View.GONE);
                            return;

                        } else {
                            for (int i = 0; i < feedlist.size(); i++) {
                                FeedbackModel feedm = feedlist.get(i);
                                String name = feedm.getName();
                                name = name.toLowerCase(Locale.getDefault());
                                if (name.startsWith(text)) {
                                    feedlistmain.add(feedlist.get(i));

                                }

                            }
                        }

                        if (feedlistmain.size() > 0) {
                            lv.setVisibility(View.VISIBLE);
                            adapter1 = new Listadapterfeedback(CollectionActivity.this, feedlistmain);
                            lv.setAdapter(adapter1);

                        } else {
                            lv.setVisibility(View.GONE);
                        }
                    }

                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,
                                          int arg2, int arg3) {
                // TODO Auto-generated method stub

            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                // lv.setVisibility(View.GONE);

                if (feedlistmain.size() > 0) {
                    lv.setVisibility(View.VISIBLE);

                } else {

                    lv.setVisibility(View.GONE);
                }

            }
        });

        next_followup.setVisibility(View.VISIBLE);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // TODO Auto-generated method stub
                if (feedlistmain.size() > 0) {

                    FeedbackModel feedm = feedlistmain.get(position);
                    edt_remark.setText(feedm.getName());
                    String isfoll = feedm.getFollowup();

                    if (isfoll.equalsIgnoreCase("Yes")) {
                        follow = true;
                        next_followup.setVisibility(View.VISIBLE);
                    } else {
                        follow = false;
                        next_followup.setVisibility(View.GONE);

                    }
                    lv.setVisibility(View.GONE);
                } else {
                    edt_remark.setText("");
                    lv.setVisibility(View.GONE);
                    next_followup.setVisibility(View.GONE);

                }
            }
        });
        btn_recepit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent irt = new Intent(CollectionActivity.this, RecepitActivity.class);
                irt.putExtra("cusid", cusid);
                irt.putExtra("cusname", cusname);
                irt.putExtra("mobile", mobile);
                startActivity(irt);

            }
        });
        btn_advance_recepit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                Intent i = new Intent(CollectionActivity.this, Advance_receipt.class);
                i.putExtra("cusid", cusid);
                System.out.println("ccuid " + cusid);
                i.putExtra("cusname", cusname);
                i.putExtra("mobile", mobile);
                startActivity(i);

            }
        });
        btn_Collectionactivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lay_sign_in.setVisibility(View.VISIBLE);
                btn_recepit.setVisibility(View.GONE);
                btn_Collectionactivity.setVisibility(View.GONE);
                btn_advance_recepit.setVisibility(View.GONE);
                btn_subsinfo.setVisibility(View.GONE);


            }
        });
        btn_subsinfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent i = new Intent(CollectionActivity.this, Customer_Info.class);
                i.putExtra("name", cusname);
                i.putExtra("mobile", mobile);
                i.putExtra("custid", cusid);
                startActivity(i);

            }
        });

        next_followup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar newCalendar = Calendar.getInstance();

                DatePickerDialog fromDatePickerDialog = new DatePickerDialog(CollectionActivity.this, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();

                        newDate.set(year, monthOfYear, dayOfMonth);
                        calendar.set(year, monthOfYear, dayOfMonth);

                        DOB1 = dftt.format(newDate.getTime());

                        next_followup.setText(df_daily.format(newDate.getTime()));


                    }

                }, newCalendar.get(Calendar.YEAR),
                        newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                fromDatePickerDialog.setTitle("Folllow up date");

                fromDatePickerDialog.show();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String feedbackcus = edt_remark.getText().toString();
                String nextdate = next_followup.getText().toString();
//                if (follow) {
//
//                } else {
//                    if (feedbackcus.isEmpty()) {
//                        Toast.makeText(getApplicationContext(), "Enter Collection Activity",
//                                Toast.LENGTH_SHORT).show();
//
//                    } else {
//                        dialog.dismiss();
//                        sendfeedback(cusid, feedbackcus, DOB1);
//                    }
//                }
                if (feedbackcus.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Enter Collection Activity",
                            Toast.LENGTH_SHORT).show();

                } else if (DOB1.isEmpty() || DOB1.equalsIgnoreCase("Next Follow up Date")) {
                    Toast.makeText(getApplicationContext(), "Select Next Follow Up Date",
                            Toast.LENGTH_SHORT).show();

                } else {
                    dialog.dismiss();
                    sendfeedback(cusid, feedbackcus, DOB1);
                }

            }
        });

        dialog.show();

    }


    public void sendfeedback(final String cusid, final String feedbackcus, final String nextdate) {


        showDialog();

        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                hidePDialog();


                try {
                    JSONObject jObj = new JSONObject(response);
                    String Success = jObj.getString("status");
                    String Details = jObj.getString("details");


                    if (Success.equals("0")) {
                        hidePDialog();
                        Toast.makeText(CollectionActivity.this, Details, Toast.LENGTH_LONG).show();
                        //  listclick(cusid);
                    } else {


                        Toast.makeText(CollectionActivity.this, Details, Toast.LENGTH_LONG).show();


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
                // params.put("compname", pref.getString("company", ""));
                params.put("custid", cusid);
                params.put("empid", pref.getString("userid", "0"));
                params.put("feedback", feedbackcus);
                params.put("branchid", pref.getString("empbranch", ""));
                params.put("date", nextdate);
//                if (follow) {
//
//                } else {
//                    params.put("date", "");
//                }
                params.put("empname", pref.getString("username", "DEMO"));
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    public void refstate() {
        if (adapterlist == null) {

        } else {
            arrangearray();
            adapterlist.notifyDataSetChanged();
        }


    }

    @Override
    protected void onResume() {
        if (adapterlist == null) {

        }else
        {
            reterivelocal();
            System.out.println("notnull");
        }
        super.onResume();

    }

    public void arrangearray() {
        ArrayList<Custmodel> level1 = new ArrayList<>();
        ArrayList<Custmodel> level2 = new ArrayList<>();
        ArrayList<Custmodel> level3 = new ArrayList<>();
        ArrayList<Custmodel> todaylist = new ArrayList<>();
        ArrayList<Custmodel> yesterdaylist = new ArrayList<>();
        for (int i = 0; i < customer_list.size(); i++) {
            Custmodel c = customer_list.get(i);
            String level = c.getLevel();
            String tod = c.getLastdate();
            String yes = c.getTomorowdate();
            if (tod.equalsIgnoreCase(todaydate)) {
                yesterdaylist.add(c);
            } else {
                if (tod.equalsIgnoreCase(tomorowdate)) {
                    todaylist.add(c);
                } else {
                    level3.add(c);
                }

            }
//            if (level.equalsIgnoreCase("Awaiting Payment")) {
//
//                level1.add(c);
//            } else if (level.equalsIgnoreCase("Customer, Guarantor Letter") || level.equalsIgnoreCase("Customer Letter")) {
//
//                level2.add(c);
//            } else {
//
//                level3.add(c);
//            }

        }
//        customer_listmain.addAll(level1);
//        customer_listmain.addAll(level2);
//        customer_listmain.addAll(level3);
//        level1.clear();
//        level2.clear();
//        level3.clear();
//        customer_listmain.addAll(customer_list);

        customer_listmain.addAll(yesterdaylist);
        customer_listmain.addAll(todaylist);
        customer_listmain.addAll(level3);
        level1.clear();
        level2.clear();
        level3.clear();
        yesterdaylist.clear();
        todaylist.clear();

    }
}



