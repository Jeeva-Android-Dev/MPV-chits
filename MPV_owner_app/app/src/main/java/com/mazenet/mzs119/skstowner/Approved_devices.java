package com.mazenet.mzs119.skstowner;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.skstowner.Adapter.CustomAdapterApproval;
import com.mazenet.mzs119.skstowner.Adapter.CustomAdapterDeviceApproval;
import com.mazenet.mzs119.skstowner.Model.Approvalmodel;
import com.mazenet.mzs119.skstowner.Model.DeviceApprovalModel;
import com.mazenet.mzs119.skstowner.Utils.AppController;
import com.mazenet.mzs119.skstowner.Utils.Config;
import com.mazenet.mzs119.skstowner.Utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Approved_devices extends Fragment {
    private ProgressDialog pDialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    ListView list;
    ArrayList<DeviceApprovalModel> alist = new ArrayList<>();
    ArrayList<DeviceApprovalModel> alistmain = new ArrayList<>();
    private static final String TAG = MainActivity.class.getSimpleName();
    CustomAdapterDeviceApproval adapterlist;
    SwipeRefreshLayout swiperef;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_approved_devices, container, false);
        cd = new ConnectionDetector(getContext());
        pDialog = new ProgressDialog(getContext(), R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);
        list = (ListView) rootView.findViewById(R.id.approve_device_list);
        swiperef=(SwipeRefreshLayout)rootView.findViewById(R.id.swiperefresh_approved);
        retrivedetails();
        swiperef.setEnabled(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DeviceApprovalModel dam = alistmain.get(i);
                String email = dam.getEmail();
                String regiid = dam.getRegid();
                deletedevice(email, regiid);
            }
        });
        swiperef.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retrivedetails();
            }
        });
        return rootView;
    }

    private void deletedevice(final  String email,final String regiid) {
       // showDialog();
        alist.clear();
        alistmain.clear();
        list.setVisibility(View.INVISIBLE);
        swiperef.setRefreshing(true);
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.device_delte, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                //hidePDialog();
                alist.clear();
                try {
                    JSONObject jObj = new JSONObject(response);
                    String Success = jObj.getString("status");
                    String Details = jObj.getString("details");
                    try {
                        if (Success.equals("1")) {//hidePDialog();
                            swiperef.setRefreshing(false);
                            Toast.makeText(getContext(), Details, Toast.LENGTH_LONG).show();
                            retrivedetails();
                        } else {
                          // hidePDialog();
                            swiperef.setRefreshing(false);
                            Toast.makeText(getContext(), Details, Toast.LENGTH_LONG).show();
                        }

                    } catch (Exception e) {
                        swiperef.setRefreshing(false);
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    swiperef.setRefreshing(false);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
               // hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("email", email);
                params.put("regid", regiid);
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

    public void retrivedetails() {
       //showDialog();
        swiperef.setRefreshing(true);
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.get_approved_devices, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
               // hidePDialog();
                alist.clear();
                alistmain.clear();

                try {
                    JSONObject jObj = new JSONObject(response);
                    JSONArray arra = jObj.getJSONArray("result");
                    try {
                        for (int i = 0; i < arra.length(); i++) {
                            JSONObject jobj = arra.getJSONObject(i);
                            DeviceApprovalModel avmod = new DeviceApprovalModel();
                            avmod.setId(jobj.getString("Id"));
                            avmod.setName(jobj.getString("Name"));
                            avmod.setEmail(jobj.getString("Email"));
                            avmod.setRegid(jobj.getString("register_Id"));
                            alist.add(avmod);

                        }
                    } catch (JSONException e) {
                        swiperef.setRefreshing(false);
                        e.printStackTrace();
                    }
                    if (alist.size() > 0) {
                        alistmain.addAll(alist);
                        adapterlist = new CustomAdapterDeviceApproval(getActivity(), alistmain);
                        adapterlist.notifyDataSetChanged();
                        list.setAdapter(adapterlist);
                        list.setVisibility(View.VISIBLE);
                        swiperef.setRefreshing(false);
                        swiperef.setEnabled(false);
                        swiperef.setEnabled(true);
                    } else {
                        swiperef.setRefreshing(false);
                        swiperef.setEnabled(false);
                        swiperef.setEnabled(true);
                        list.setVisibility(View.GONE);
                    }
                } catch (JSONException e) {
                    swiperef.setRefreshing(false);
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
               // hidePDialog();
                swiperef.setRefreshing(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

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
