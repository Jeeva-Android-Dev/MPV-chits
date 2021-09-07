package com.mazenet.mzs119.mpvmemberapp;

import android.content.SharedPreferences;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.mpvmemberapp.Utils.AppController;
import com.mazenet.mzs119.mpvmemberapp.Utils.Constants;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyProfile extends AppCompatActivity {
    TextView name, address, noofenroll, prizedchits, nonprized, totpending, totpaid, companypaid, companypending,txt_prf_mobile;
    String str_name = "", str_address = "", str_noofenroll = "", str_prizedchits = "", str_nonprized = "", str_totpending = "", str_totpaid = "", str_companypaid = "", str_companypending = "";
    String str_photo = "";
    CircleImageView profile_image;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Locale curLocale;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        pref = getApplicationContext().getSharedPreferences(Constants.preference, MODE_PRIVATE);
        editor = pref.edit();
        getSupportActionBar().setTitle("My Profile");
        curLocale = new Locale("en", "IN");
        name = (TextView) findViewById(R.id.txt_prf_name);
        address = (TextView) findViewById(R.id.txt_prf_addr);
//        noofenroll = (TextView) findViewById(R.id.txt_prf_totEnr);
        txt_prf_mobile = (TextView) findViewById(R.id.txt_prf_mobile);
//        prizedchits = (TextView) findViewById(R.id.txt_prf_prizedchit);
//        nonprized = (TextView) findViewById(R.id.txt_prf_non_prizedchit);
//        totpending = (TextView) findViewById(R.id.txt_prf_sub_pendingamt);
//        totpaid = (TextView) findViewById(R.id.txt_prf_sub_paidamt);
//        companypaid = (TextView) findViewById(R.id.txt_prf_cmpy_paidamt);
//        companypending = (TextView) findViewById(R.id.txt_cmpy_pendingamt);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        reteriveall();
    }

    public void reteriveall() {

        StringRequest stringreq = new StringRequest(Request.Method.POST,
                Constants.getprofile, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d("MyChits Activity", response.toString());


                try {


                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("result");

                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);
                            str_noofenroll = jObj.getString("Noofenroll");
                            str_totpending = jObj.getString("Total_Enrl_Pending");
                            str_totpaid = jObj.getString("Total_Enrl_Paid");
                            str_nonprized = jObj.getString("Non_prized_chits");
                            str_companypaid = jObj.getString("Company_paid");
                            str_companypending = jObj.getString("Company_pending");
                            str_prizedchits = jObj.getString("Prized_chits");
                            str_photo = jObj.getString("Cust_Phot");
                            address.setText(jObj.getString("address")+"\n"+jObj.getString("Pincode_F"));
                            txt_prf_mobile.setText(jObj.getString("Mobile_F"));
                            System.out.println("photo " + str_photo);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    assignvalues();
                    Picasso.with(getApplicationContext()).load(str_photo).resize(150, 150).onlyScaleDown()
                            .placeholder(getResources().getDrawable(R.drawable.ic_man)
                            ).error(R.drawable.ic_man)
                            .centerInside().into(profile_image);
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

    private void assignvalues() {
        try {
            name.setText(pref.getString("Customer_name", ""));

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
