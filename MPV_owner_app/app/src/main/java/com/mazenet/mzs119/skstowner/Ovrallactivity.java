package com.mazenet.mzs119.skstowner;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.skstowner.Adapter.CustomAdapterApproval;
import com.mazenet.mzs119.skstowner.Adapter.CustomAdapterOverall;
import com.mazenet.mzs119.skstowner.Model.Approvalmodel;
import com.mazenet.mzs119.skstowner.Model.OverallModel;
import com.mazenet.mzs119.skstowner.Utils.AppController;
import com.mazenet.mzs119.skstowner.Utils.Config;
import com.mazenet.mzs119.skstowner.Utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Ovrallactivity extends AppCompatActivity {
    String url1 = Config.overalllink;
    ArrayList<OverallModel> alist = new ArrayList<>();
    private ProgressDialog pDialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    ListView list;
    CustomAdapterOverall adapterlist;
    SwipeRefreshLayout refresh;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ovrallactivity);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();
        cd = new ConnectionDetector(this);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);
        refresh = (SwipeRefreshLayout) findViewById(R.id.refresh_overall);
        list = (ListView) findViewById(R.id.list_oa_list);
        retrivedetails();
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                OverallModel amod = alist.get(i);
                String id = amod.getEmp_id();
                Intent it = new Intent(Ovrallactivity.this, ApprovalActivity.class);
                it.putExtra("empid", id);
                startActivity(it);
            }
        });
        refresh.setEnabled(true);
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrivedetails();
            }
        });
    }

    public void retrivedetails() {
        showDialog();
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                hidePDialog();
                alist.clear();
                try {
                    JSONObject jObj = new JSONObject(response);

                        JSONArray arra = jObj.getJSONArray("overall");
                        try {
                            for (int i = 0; i < arra.length(); i++) {
                                JSONObject jobj = arra.getJSONObject(i);

                                OverallModel avmod = new OverallModel();
                                avmod.setName(jobj.getString("Name"));
                                avmod.setEmp_id(jobj.getString("EMP_NAME"));
                                avmod.setTot_amnt(jobj.getString("Total_amnt"));
                                alist.add(avmod);

                            }
                        } catch (JSONException e) {
                            refresh.setRefreshing(false);
                            e.printStackTrace();
                        }
                        if (alist.size() > 0) {
                            refresh.setRefreshing(false);
                            adapterlist = new CustomAdapterOverall(Ovrallactivity.this, alist);
                            adapterlist.notifyDataSetChanged();
                            list.setAdapter(adapterlist);
                            list.setVisibility(View.VISIBLE);
                        } else {
                            Toast.makeText(Ovrallactivity.this, "No Unapproved Settlements", Toast.LENGTH_SHORT).show();
                            refresh.setRefreshing(false);
                            list.setVisibility(View.GONE);
                        }

                } catch (JSONException e) {
                    Toast.makeText(Ovrallactivity.this, "No Unapproved Settlements", Toast.LENGTH_SHORT).show();
                    refresh.setRefreshing(false);
                    list.setVisibility(View.GONE);
                    refresh.setRefreshing(false);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                refresh.setRefreshing(false);
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidePDialog();

            }
        });
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


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

}
