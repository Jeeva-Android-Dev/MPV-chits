package com.mazenet.mzs119.mpvmemberapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.mpvmemberapp.Adapter.CustomAdapterenrollment;
import com.mazenet.mzs119.mpvmemberapp.Model.Enrollmodel;
import com.mazenet.mzs119.mpvmemberapp.Utils.AppController;
import com.mazenet.mzs119.mpvmemberapp.Utils.Constants;
import com.mazenet.mzs119.mpvmemberapp.Utils.ListViewHeight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyChits extends AppCompatActivity {
    ListView lst_re_enroll;
    TextView txt_advance,txt_reward;
    public ArrayList<Enrollmodel> enroll_list = new ArrayList<Enrollmodel>();
    CustomAdapterenrollment adapterlist;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_chits);
        pref = getApplicationContext().getSharedPreferences(Constants.preference, MODE_PRIVATE);
        editor = pref.edit();
        getSupportActionBar().setTitle("My Chits");
        lst_re_enroll = (ListView) findViewById(R.id.list_mychit);
        txt_advance = (TextView) findViewById(R.id.txt_advance);
        txt_reward = (TextView) findViewById(R.id.txt_reward);
        reteriveall();
        reteriveadvance();
        lst_re_enroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Enrollmodel sched = enroll_list.get(position);
                String Enrolid = sched.getEnrollid();
                System.out.println("enrlid " + Enrolid);
                Intent iy = new Intent(MyChits.this, MyTransactions.class);
                iy.putExtra("enrollid", Enrolid);
                iy.putExtra("enrollname", sched.getGroup_Name());
                startActivity(iy);
            }
        });
    }

    public void reteriveadvance() {

        System.out.println("advance idd");
//        enroll_list.clear();
        //dbrecepit.deletetable();
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Constants.availableadvance, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("advance " + response);
                try {


                    JSONObject object = new JSONObject(response);
                    String advance = object.getString("advance");

                    String reward_point = object.getString("reward_point");

                    if(reward_point.equalsIgnoreCase("null") ||reward_point.equalsIgnoreCase("")){
                        txt_reward.setText("0");
                    }else {
                        txt_reward.setText(reward_point);
                    }
                    txt_advance.setText(advance);

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
                params.put("cusid", pref.getString("custtblid", ""));
                System.out.println("advancecus id " + pref.getString("custtblid", ""));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    public void reteriveall() {


        enroll_list.clear();
        StringRequest stringreq = new StringRequest(Request.Method.POST,
                Constants.reteriveenroll, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MyChits Activity", response.toString());
                System.out.println("mychits_response " + response);

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
                            sched.setPaymentType(jObj.getString("Payment_Type"));
//                            sched.setCompleted_auction(jObj.getString("Completed_auction"));
                            sched.setPaid_details(jObj.getString("Paid_Details"));
                            sched.setPayamount("0");

                            sched.setUpcoming_dueamount(jObj.getString("next_due_amount"));
                            sched.setUpcommimg_due_date(jObj.getString("next_due_date"));
                            sched.setUpcommimg_installment_no(jObj.getString("next_installment_no"));
                            enroll_list.add(sched);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (enroll_list.size() > 0) {

                        try {

                            adapterlist = new CustomAdapterenrollment(MyChits.this, enroll_list);
                            adapterlist.notifyDataSetChanged();
                            ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
                            lst_re_enroll.setAdapter(adapterlist);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                MyChits.this, R.style.MyAlertDialogStyleApproved);
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

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Cust_Id", pref.getString("custtblid", ""));

                return params;
            }

        };
        stringreq.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringreq);
    }
}
