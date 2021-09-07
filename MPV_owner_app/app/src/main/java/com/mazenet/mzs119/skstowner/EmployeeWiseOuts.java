package com.mazenet.mzs119.skstowner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.skstowner.Adapter.CustomAdapterEmpWiseouts;
import com.mazenet.mzs119.skstowner.Model.EmpwiseOuts;
import com.mazenet.mzs119.skstowner.Utils.AppController;
import com.mazenet.mzs119.skstowner.Utils.Config;
import com.mazenet.mzs119.skstowner.Utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EmployeeWiseOuts extends AppCompatActivity {
    String empid = "", empname = "";
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    ListView agentlist;
    String list="all";
    CustomAdapterEmpWiseouts adapterlist;
    ArrayList<EmpwiseOuts> alist = new ArrayList<>();
    ArrayList<EmpwiseOuts> dailylist = new ArrayList<>();
    ArrayList<EmpwiseOuts> dailylistmain = new ArrayList<>();
    ArrayList<EmpwiseOuts> weeklylist = new ArrayList<>();
    ArrayList<EmpwiseOuts> weeklylistmain = new ArrayList<>();
    ArrayList<EmpwiseOuts> monthlylist = new ArrayList<>();
    ArrayList<EmpwiseOuts> monthlylistmain = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_wise_outs);
        agentlist = (ListView) findViewById(R.id.list_empwiseouts);
        try {
            Intent i = getIntent();
            empid = i.getStringExtra("empid");
            empname = i.getStringExtra("empname");
        } catch (Exception e) {

        }
        agentlist.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if(list.equalsIgnoreCase("all")) {
                    EmpwiseOuts aom = alist.get(position);
                    Intent i = new Intent(EmployeeWiseOuts.this, EmployeeWiseOuts.class);
                    i.putExtra("name", aom.getCustname());
                    i.putExtra("address", aom.getAddress());
                    i.putExtra("mobile",aom.getMobile());
                    i.putExtra("custid",aom.getCustid());
                    startActivity(i);
                }
                else if(list.equalsIgnoreCase("daily")) {
                    EmpwiseOuts aom = dailylist.get(position);
                    Intent i = new Intent(EmployeeWiseOuts.this, EmployeeWiseOuts.class);
                    i.putExtra("name", aom.getCustname());
                    i.putExtra("address", aom.getAddress());
                    i.putExtra("mobile",aom.getMobile());
                    i.putExtra("custid",aom.getCustid());
                    startActivity(i);
                }
                else if(list.equalsIgnoreCase("weekly")) {
                    EmpwiseOuts aom = weeklylist.get(position);
                    Intent i = new Intent(EmployeeWiseOuts.this, EmployeeWiseOuts.class);
                    i.putExtra("name", aom.getCustname());
                    i.putExtra("address", aom.getAddress());
                    i.putExtra("mobile",aom.getMobile());
                    i.putExtra("custid",aom.getCustid());
                    startActivity(i);
                }
                else if(list.equalsIgnoreCase("monthly")) {
                    EmpwiseOuts aom = monthlylist.get(position);
                    Intent i = new Intent(EmployeeWiseOuts.this, EmployeeWiseOuts.class);
                    i.putExtra("name", aom.getCustname());
                    i.putExtra("address", aom.getAddress());
                    i.putExtra("mobile",aom.getMobile());
                    i.putExtra("custid",aom.getCustid());
                    startActivity(i);
                }

                return true;
            }
        });
        getSupportActionBar().setTitle(empname);
        getempwisereport();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.payment_menu, menu);
        System.out.println("its clickable");

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        System.out.println("clicked");
        switch (item.getItemId()) {

            case R.id.menu_All:
                getempwisereport();
                list="all";
                break;
            case R.id.menu_daily:
                getDailywisereport();
                list="daily";
                break;
            case R.id.menu_weekly:
                getweeklyreport();
                list="weekly";
                break;
            case R.id.menu_monthly:
                getmonthlyreport();
                list="monthly";
                break;

        }

        return true;
    }

    public void getDailywisereport() {
        final Locale curLocale = new Locale("en", "IN");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.getempwisereport, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("empouts " + response);
                dailylist.clear();
                dailylistmain.clear();
                alist.clear();
                weeklylistmain.clear();
                monthlylistmain.clear();
                weeklylist.clear();
                monthlylist.clear();
                try {


                    JSONObject object = new JSONObject(response);
                    JSONArray arra = object.getJSONArray("result");
                    try {
                        for (int i = 0; i < arra.length(); i++) {
                            JSONObject jobj = arra.getJSONObject(i);

                            EmpwiseOuts avmod = new EmpwiseOuts();
                            avmod.setCustname(jobj.getString("First_Name_F"));
                            avmod.setGrpname(jobj.getString("grpname"));
                            avmod.setPaid(jobj.getString("Paid_Amt"));
                            avmod.setPending(jobj.getString("Pending_Amt"));
                            avmod.setTicketno(jobj.getString("Group_Ticket_Name"));
                            avmod.setPaymenttype(jobj.getString("Payment_Type"));
                            avmod.setAddress(jobj.getString("address"));
                            avmod.setMobile(jobj.getString("Mobile_F"));
                            avmod.setCustid(jobj.getString("Id"));
                            dailylist.add(avmod);

                        }
                        for (int i = 0; i < dailylist.size(); i++) {
                            EmpwiseOuts avmod = dailylist.get(i);
                            String paymentType = avmod.getPaymenttype();
                            System.out.println("name" + paymentType);
                            if (paymentType.equalsIgnoreCase("daily")) {
                                dailylistmain.add(dailylist.get(i));
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("count" + dailylist.size());
                    if (dailylistmain.size() > 0) {
                        System.out.println("dailylistmain " + dailylistmain);
                        adapterlist = new CustomAdapterEmpWiseouts(EmployeeWiseOuts.this, dailylistmain);
                        adapterlist.notifyDataSetChanged();
                        agentlist.setAdapter(adapterlist);
                        agentlist.setVisibility(View.VISIBLE);
                    } else {
                        agentlist.setVisibility(View.GONE);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        })

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empid", empid);
                System.out.println("emp " + empid);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }

    public void getmonthlyreport() {
        final Locale curLocale = new Locale("en", "IN");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.getempwisereport, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                monthlylist.clear();
                monthlylistmain.clear();
                alist.clear();
                dailylistmain.clear();
                weeklylistmain.clear();
                weeklylist.clear();
                dailylist.clear();
                try {


                    JSONObject object = new JSONObject(response);
                    JSONArray arra = object.getJSONArray("result");
                    try {
                        for (int i = 0; i < arra.length(); i++) {
                            JSONObject jobj = arra.getJSONObject(i);

                            EmpwiseOuts avmod = new EmpwiseOuts();
                            avmod.setCustname(jobj.getString("First_Name_F"));
                            avmod.setGrpname(jobj.getString("grpname"));
                            avmod.setPaid(jobj.getString("Paid_Amt"));
                            avmod.setPending(jobj.getString("Pending_Amt"));
                            avmod.setTicketno(jobj.getString("Group_Ticket_Name"));
                            avmod.setPaymenttype(jobj.getString("Payment_Type"));
                            avmod.setAddress(jobj.getString("address"));
                            avmod.setMobile(jobj.getString("Mobile_F"));
                            avmod.setCustid(jobj.getString("Id"));
                            monthlylist.add(avmod);

                        }

                        for (int i = 0; i < monthlylist.size(); i++) {
                            EmpwiseOuts avmod = monthlylist.get(i);
                            String paymentType = avmod.getPaymenttype();
                            System.out.println("name" + paymentType);
                            if (paymentType.equalsIgnoreCase("monthly")) {
                                monthlylistmain.add(monthlylist.get(i));
                            }

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("count" + monthlylist.size());
                    if (monthlylistmain.size() > 0) {
                        System.out.println("monht  " + monthlylistmain);
                        adapterlist = new CustomAdapterEmpWiseouts(EmployeeWiseOuts.this, monthlylistmain);
                        adapterlist.notifyDataSetChanged();
                        agentlist.setAdapter(adapterlist);
                        agentlist.setVisibility(View.VISIBLE);
                    } else {
                        agentlist.setVisibility(View.GONE);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        })

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empid", empid);
                System.out.println("emp " + empid);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    public void getweeklyreport() {
        final Locale curLocale = new Locale("en", "IN");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.getempwisereport, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());

                weeklylist.clear();
                weeklylistmain.clear();
                alist.clear();
                dailylistmain.clear();
                monthlylistmain.clear();
                dailylist.clear();
                monthlylist.clear();
                try {


                    JSONObject object = new JSONObject(response);
                    JSONArray arra = object.getJSONArray("result");
                    try {
                        for (int i = 0; i < arra.length(); i++) {
                            JSONObject jobj = arra.getJSONObject(i);

                            EmpwiseOuts avmod = new EmpwiseOuts();
                            avmod.setCustname(jobj.getString("First_Name_F"));
                            avmod.setGrpname(jobj.getString("grpname"));
                            avmod.setPaid(jobj.getString("Paid_Amt"));
                            avmod.setPending(jobj.getString("Pending_Amt"));
                            avmod.setTicketno(jobj.getString("Group_Ticket_Name"));
                            avmod.setPaymenttype(jobj.getString("Payment_Type"));
                            avmod.setAddress(jobj.getString("address"));
                            avmod.setMobile(jobj.getString("Mobile_F"));
                            avmod.setCustid(jobj.getString("Id"));
                            weeklylist.add(avmod);

                        }

                        for (int i = 0; i < weeklylist.size(); i++) {
                            EmpwiseOuts avmod = weeklylist.get(i);
                            String paymentType = avmod.getPaymenttype();
                            System.out.println("name" + paymentType);
                            if (paymentType.equalsIgnoreCase("weekly")) {
                                weeklylistmain.add(weeklylist.get(i));
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    System.out.println("count" + weeklylistmain.size());
                    if (weeklylistmain.size() > 0) {
                        System.out.println("wee  " + weeklylistmain);
                        adapterlist = new CustomAdapterEmpWiseouts(EmployeeWiseOuts.this, weeklylistmain);
                        adapterlist.notifyDataSetChanged();
                        agentlist.setAdapter(adapterlist);
                        agentlist.setVisibility(View.VISIBLE);
                    } else {
                        agentlist.setVisibility(View.GONE);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        })

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empid", empid);
                System.out.println("emp " + empid);
                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    public void getempwisereport() {

        final Locale curLocale = new Locale("en", "IN");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.getempwisereport, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                weeklylist.clear();
                weeklylistmain.clear();
                alist.clear();
                dailylistmain.clear();
                monthlylistmain.clear();
                dailylist.clear();
                monthlylist.clear();


                try {


                    JSONObject object = new JSONObject(response);
                    JSONArray arra = object.getJSONArray("result");
                    try {
                        for (int i = 0; i < arra.length(); i++) {
                            JSONObject jobj = arra.getJSONObject(i);

                            EmpwiseOuts avmod = new EmpwiseOuts();
                            avmod.setCustname(jobj.getString("First_Name_F"));
                            avmod.setGrpname(jobj.getString("grpname"));
                            avmod.setPaid(jobj.getString("Paid_Amt"));
                            avmod.setPending(jobj.getString("Pending_Amt"));
                            avmod.setTicketno(jobj.getString("Group_Ticket_Name"));
                            avmod.setAddress(jobj.getString("address"));
                            avmod.setMobile(jobj.getString("Mobile_F"));
                            avmod.setCustid(jobj.getString("Id"));
                            alist.add(avmod);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapterlist = new CustomAdapterEmpWiseouts(EmployeeWiseOuts.this, alist);
                    adapterlist.notifyDataSetChanged();
                    agentlist.setAdapter(adapterlist);
                    agentlist.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        })

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("empid", empid);
                System.out.println("emp " + empid);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }
}
