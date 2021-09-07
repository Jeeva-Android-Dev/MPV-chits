package com.mazenet.mzs119.mpvmemberapp;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.mpvmemberapp.Adapter.CustomAdapterAccountcopy;
import com.mazenet.mzs119.mpvmemberapp.Model.CopyModel;
import com.mazenet.mzs119.mpvmemberapp.Utils.AppController;
import com.mazenet.mzs119.mpvmemberapp.Utils.Constants;
import com.mazenet.mzs119.mpvmemberapp.Utils.ListViewHeight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MYAccountCopy extends AppCompatActivity {
    ListView list_copy;
    ArrayList<CopyModel> copy_list = new ArrayList<>();
    CustomAdapterAccountcopy adapterlist;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
    SimpleDateFormat df_daily = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
    SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myaccount_copy);
        pref = getApplicationContext().getSharedPreferences(Constants.preference, MODE_PRIVATE);
        editor = pref.edit();
        getSupportActionBar().setTitle("Account Copy");
        list_copy = (ListView) findViewById(R.id.list_accountcopy);
        retrievecopy();
    }

    private void retrievecopy() {
        copy_list.clear();
        StringRequest stringreq = new StringRequest(Request.Method.POST,
                Constants.retrieveaccountcopy, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MyChits Activity", response.toString());


                try {


                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("result");

                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);

                            CopyModel sched = new CopyModel();
                            sched.setGrpname(jObj.getString("Group_Name"));
                            sched.setAmount(jObj.getString("Total_Amount"));
                            sched.setChit_ticket_type(jObj.getString("Chit_Ticket_Type"));
                            sched.setDate(jObj.getString("Date"));
                            sched.setTime(jObj.getString("time"));
                            sched.setGrpticketno(jObj.getString("Group_Ticket_Name"));
                            sched.setReceipt_no(jObj.getString("Receipt_No"));
                            sched.setPaytype(jObj.getString("Payment_Type"));
                            sched.setDue_advance(jObj.getString("due_advance"));
                            String date = jObj.getString("Date") + " " + jObj.getString("time");
                            Date datetime = new Date();
                            if (jObj.getString("due_advance").equalsIgnoreCase("due")) {
                                try {
                                    datetime = df.parse(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            } else {
                                try {
                                    datetime = df_daily.parse(date);
                                } catch (ParseException e) {
                                    e.printStackTrace();
                                }
                            }
                            sched.setDateitme(datetime);
                            copy_list.add(sched);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (copy_list.size() > 0) {
                        Collections.sort(copy_list, new Comparator<CopyModel>() {
                            public int compare(CopyModel o1, CopyModel o2) {
                                if (o1.getDateitme() == null || o2.getDateitme() == null)
                                    return 0;
                                return o1.getDateitme().compareTo(o2.getDateitme());
                            }
                        });
                        Collections.reverse(copy_list);
                        try {
                            ArrayList<CopyModel> finalarray = new ArrayList<>();
                            for (int i = 0; i < copy_list.size(); i++) {
                                CopyModel cm = copy_list.get(i);
                                String paytype = cm.getPaytype();
                                if (paytype.equalsIgnoreCase("Advance Adjustment") || paytype.equalsIgnoreCase("daily.rct.adj")) {
                                } else if (paytype.equalsIgnoreCase("Bid Payment Adjustment") || paytype.equalsIgnoreCase("b.p.adj")) {
                                } else {
                                    System.out.println("payty " + paytype);
                                    finalarray.add(cm);
                                }
                            }

                            adapterlist = new CustomAdapterAccountcopy(MYAccountCopy.this, finalarray);
                            adapterlist.notifyDataSetChanged();
                            ListViewHeight.setListViewHeightBasedOnChildren(list_copy);
                            list_copy.setAdapter(adapterlist);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Toast.makeText(MYAccountCopy.this, "No Receipts Available", Toast.LENGTH_SHORT).show();
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
                params.put("custid", pref.getString("custtblid", ""));

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
