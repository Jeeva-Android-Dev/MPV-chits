package com.mazenet.mzs119.mpvmemberapp;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.mpvmemberapp.Adapter.CustomAdapterNewChits;
import com.mazenet.mzs119.mpvmemberapp.Model.NewChitModel;
import com.mazenet.mzs119.mpvmemberapp.Utils.AppController;
import com.mazenet.mzs119.mpvmemberapp.Utils.Constants;
import com.mazenet.mzs119.mpvmemberapp.Utils.ListViewHeight;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class NewCommencedChits extends AppCompatActivity {
    ListView lst_re_enroll;
    public ArrayList<NewChitModel> enroll_list = new ArrayList<NewChitModel>();
    CustomAdapterNewChits adapterlist;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static String Custtblid;
    private static Context context;

    public static Context getAppContext() {
        return NewCommencedChits.context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_commenced_chits);
        NewCommencedChits.context = getApplicationContext();
        getSupportActionBar().setTitle("New Commenced Chits");
        pref = getApplicationContext().getSharedPreferences(Constants.preference, MODE_PRIVATE);
        editor = pref.edit();
        lst_re_enroll = (ListView) findViewById(R.id.list_newchits);
        reteriveall();
        Custtblid = pref.getString("custtblid", "");
        lst_re_enroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                NewChitModel sched = enroll_list.get(position);
                String grpname = sched.getChitvalue();
                System.out.println("grp " + grpname);
                //sendmsg(grpname);
            }
        });
    }

    public void reteriveall() {


        enroll_list.clear();
        StringRequest stringreq = new StringRequest(Request.Method.POST,
                Constants.reterivenewchits, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MyChits Activity", response);
                try {


                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("result");

                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);

                            NewChitModel sched = new NewChitModel();
                            sched.setChitvalue(jObj.getString("Chit_Value"));
                            sched.setId(jObj.getString("Id"));
                            sched.setMonth(jObj.getString("Month"));
                            sched.setMonthly_Amt(jObj.getString("Monthly_Amt"));
                            sched.setScheme_Format(jObj.getString("Scheme_Format"));
                            enroll_list.add(sched);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (enroll_list.size() > 0) {

                        try {

                            adapterlist = new CustomAdapterNewChits(NewCommencedChits.this, enroll_list);
                            adapterlist.notifyDataSetChanged();
                            ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
                            lst_re_enroll.setAdapter(adapterlist);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                NewCommencedChits.this,R.style.MyAlertDialogStyleApproved);
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
                params.put("Cust_Id", pref.getString("custtblid", ""));

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringreq);
    }

    public static void sendmsg(final String grpname) {
        StringRequest stringreq = new StringRequest(Request.Method.POST,
                Constants.sendinterest, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("MyChits Activity", response);
                System.out.println(response);
                try {


                    JSONObject object = new JSONObject(response);
                    String status = object.getString("Status");
                    String details = object.getString("Details");

                    if (status.equalsIgnoreCase("1")) {
                        toast(details);
                    } else {
                        toast(details);
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
                toast(error.getMessage());

            }
        })

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Cust_Id", Custtblid);
                System.out.println(" cus "+Custtblid);
                System.out.println(" grpna "+grpname);
                params.put("Grpname", grpname);

                return params;
            }

        };
        stringreq.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringreq);
    }

    public static void toast(String Message) {
        Toast.makeText(NewCommencedChits.context, Message, Toast.LENGTH_SHORT).show();
    }
}


