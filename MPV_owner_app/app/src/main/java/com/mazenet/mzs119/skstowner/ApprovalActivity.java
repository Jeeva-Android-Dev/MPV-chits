package com.mazenet.mzs119.skstowner;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.skstowner.Adapter.CustomAdapterApproval;
import com.mazenet.mzs119.skstowner.Model.Approvalmodel;
import com.mazenet.mzs119.skstowner.Utils.AppController;
import com.mazenet.mzs119.skstowner.Utils.Config;
import com.mazenet.mzs119.skstowner.Utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ApprovalActivity extends AppCompatActivity {
    String url = Config.getapproval;
    String url1 = Config.setapproval;
    String url2 = Config.deleteapprove;
    ArrayList<Approvalmodel> alist = new ArrayList<>();
    private ProgressDialog pDialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    ListView list;
    String empid = "";
    CustomAdapterApproval adapterlist;
    String id = "", agentname = "";
    SwipeRefreshLayout refres;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_approval);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();
        cd = new ConnectionDetector(this);

        refres = (SwipeRefreshLayout) findViewById(R.id.refresh_approval);

        list = (ListView) findViewById(R.id.listView);
        try {
            Intent i = getIntent();
            empid = i.getStringExtra("empid");
            retrivedetails(empid);
        } catch (Exception e) {
            e.printStackTrace();
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Approvalmodel amod = alist.get(i);
                id = amod.getTableId();
                agentname = amod.getAgentName();
                String cashin = amod.getAmount();
                showSortPopup1(ApprovalActivity.this, agentname, cashin);
                System.out.println(id + " fierhfrh");

            }
        });
        refres.setEnabled(true);
        refres.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrivedetails(empid);
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
            case R.id.menu_logout:
                editor.putString("username", "");
                editor.putString("userid", "");
                editor.putString("email", "");
                editor.putString("password", "");
                editor.commit();

                Intent irt = new Intent(ApprovalActivity.this, MainActivity.class);
                startActivity(irt);

                finish();
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void showSortPopup1(final Activity context, String agentnme, String cashinhand) {

        final Dialog dialog = new Dialog(ApprovalActivity.this);
        dialog.setContentView(R.layout.custodialogapproval);
        // dialog.setTitle("Agent name : " + agentnme);
        dialog.setCancelable(true);

        // set the custom dialog components - text, image and button
        final TextView text = (TextView) dialog.findViewById(R.id.edt_code);

        Button dialogok = (Button) dialog.findViewById(R.id.btn_ok);
        Button dialogcancel = (Button) dialog.findViewById(R.id.btn_cancel);
        final TextView txt_resend = (TextView) dialog.findViewById(R.id.txt_custo_name);
        final TextView txt_name = (TextView) dialog.findViewById(R.id.txt_titlee);
        final TextView txt_name_1 = (TextView) dialog.findViewById(R.id.txt_titlee2);
        txt_name.setText("Agent Name : ");
        txt_name_1.setText(agentnme);
        txt_resend.setText("Do you wish to approve this settlement amount?");
        // if button is clicked, close the custom dialog
        text.setText("Rs. " + cashinhand);

        dialogcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                setApproval(id);
                // CashInHandActivity.this.finish();
            }
        });

        dialogok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                deleteapprove(id);
            }
        });

        dialog.show();
    }

    public void setApproval(final String id) {
        showDialog();

        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url1, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                hidePDialog();


                try {
                    JSONObject jObj = new JSONObject(response);
                    String Success = jObj.getString("status");
                    if (Success.equalsIgnoreCase("1")) {
                        list.setVisibility(View.INVISIBLE);
                        retrivedetails(empid);

                        finish();
                        Toast.makeText(ApprovalActivity.this, "Approved", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ApprovalActivity.this, "Not Approved", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    hidePDialog();
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
                params.put("id", id);
                params.put("userid", pref.getString("userid", ""));
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }

    public void deleteapprove(final String id) {
        showDialog();

        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                hidePDialog();


                try {
                    JSONObject jObj = new JSONObject(response);
                    String Success = jObj.getString("status");
                    if (Success.equalsIgnoreCase("1")) {
                        list.setVisibility(View.INVISIBLE);
                        retrivedetails(empid);
                        Toast.makeText(ApprovalActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(ApprovalActivity.this, "Not Deleted", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    hidePDialog();
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
                params.put("id", id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }

    public void retrivedetails(String empid) {
        final String id = empid;
        showDialog();
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                hidePDialog();
                alist.clear();
                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray arra = jObj.getJSONArray("result");
                    try {
                        for (int i = 0; i < arra.length(); i++) {
                            JSONObject jobj = arra.getJSONObject(i);

                            Approvalmodel avmod = new Approvalmodel();
                            avmod.setTableId(jobj.getString("Table_id"));
                            avmod.setAgentName(jobj.getString("agentname"));
                            avmod.setCreatorName(jobj.getString("createdby"));
                            avmod.setAmount(jobj.getString("amount"));
                            System.out.println(jobj.getString("Table_id") + "  oneee");
                            alist.add(avmod);

                        }
                    } catch (JSONException e) {
                        refres.setRefreshing(false);

                        e.printStackTrace();
                    }

                    if (alist.size() > 0) {
                        refres.setRefreshing(false);
                        adapterlist = new CustomAdapterApproval(ApprovalActivity.this, alist);
                        adapterlist.notifyDataSetChanged();
                        list.setAdapter(adapterlist);
                        list.setVisibility(View.VISIBLE);
                    } else {
                        refres.setRefreshing(false);
                        list.setVisibility(View.GONE);
                        Toast.makeText(ApprovalActivity.this, "No Unapproved Settlements", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    refres.setRefreshing(false);
                    list.setVisibility(View.GONE);
                    Toast.makeText(ApprovalActivity.this, "No Unapproved Settlements", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                    hidePDialog();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                refres.setRefreshing(false);
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("emp_id", id);

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }

    private void hidePDialog() {

        if ((pDialog != null) && pDialog.isShowing())
         {
            pDialog.dismiss();
             pDialog=null;

        }
    }

    private void showDialog() {

        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);
        if (!pDialog.isShowing())
            pDialog.show();
        pDialog.setContentView(R.layout.my_progress);

    }
    @Override
    public void onDestroy() {
        hidePDialog();
        super.onDestroy();
    }

}



