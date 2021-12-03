package com.mazenetsolutions.mzs119.skst_new;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenetsolutions.mzs119.skst_new.GPS.GPSTracker;
import com.mazenetsolutions.mzs119.skst_new.Utils.AppController;
import com.mazenetsolutions.mzs119.skst_new.Utils.Config;
import com.mazenetsolutions.mzs119.skst_new.Utils.ConnectionDetector;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class Customer_Info extends AppCompatActivity {
    TextView name, address, mobile, intoname, collect_area, grpname, grpticket;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    String str_name = "", str_address = "", str_mobile = "", str_custid = "", str_collect_area = "", str_introname = "", str_grpname = "", str_grpticket = "", str_locationupdated = "";
    ConnectionDetector cd;
    Double str_uplat = 0.0, str_uplong = 0.0;
    LinearLayout lay_intro, lay_area;
    CardView lay_grpname, laygrpticket;
    Button btn_updatelocation;
    Boolean flash = false;
    ObjectAnimator colorAnim;
    CircleImageView profile_image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer__info);
        cd = new ConnectionDetector(this);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();
        name = (TextView) findViewById(R.id.txt_prf_name);
        address = (TextView) findViewById(R.id.txt_prf_addr);
        mobile = (TextView) findViewById(R.id.txt_prf_mobile);
        intoname = (TextView) findViewById(R.id.txt_Intro_name);
        collect_area = (TextView) findViewById(R.id.txt_collectarea);
        grpname = (TextView) findViewById(R.id.txt_prf_non_prizedchit);
        grpticket = (TextView) findViewById(R.id.txt_prf_sub_paidamt);
        lay_area = (LinearLayout) findViewById(R.id.lay_coolectarea);
        lay_intro = (LinearLayout) findViewById(R.id.lay_introname);
        lay_grpname = (CardView) findViewById(R.id.card_non_pricedchit);
        laygrpticket = (CardView) findViewById(R.id.card_sub_paidamt);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        btn_updatelocation = (Button) findViewById(R.id.btn_updatelocation);
        lay_intro.setVisibility(View.GONE);
        lay_area.setVisibility(View.GONE);

        try {
            Intent i = getIntent();
            str_custid = i.getStringExtra("custid");
            str_mobile = i.getStringExtra("mobile");
            str_name = i.getStringExtra("name");
//            str_grpname = i.getStringExtra("grpname");
//            str_grpticket = i.getStringExtra("grpticket");
            System.out.println("custid " + str_custid + "grpn " + str_grpname + " grpti " + str_grpticket);

        } catch (Exception e) {

        }
        get_location();
        btn_updatelocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ContextCompat.checkSelfPermission(Customer_Info.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(Customer_Info.this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                    GPSTracker gps = new GPSTracker(Customer_Info.this);
//                String uri = String.format(Locale.ENGLISH, "geo:%f,%f", gps.getLatitude(), gps.getLongitude());
//                String uri = "http://maps.google.com/maps?q=loc:" + gps.getLatitude() + "," + gps.getLongitude() + " (" + "Customer" + ")";
                    GPSTracker gps = new GPSTracker(Customer_Info.this);
                    System.out.println("lat " + gps.getLatitude());
                    System.out.println("lat " + gps.getLongitude());
                    Double str_uplat = gps.getLatitude();
                    Double str_uplong = gps.getLongitude();
                    builddilogstanding("Message", "Are You Currently Near Customer's House", Customer_Info.this, str_uplat, str_uplong);

                } else {
                    ActivityCompat.requestPermissions(Customer_Info.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);
                }
            }
        });
        address.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("google.navigation:q=an+" + str_address + "+coimbatore"));
//                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + str_address ));
                startActivity(intent);
            }
        });
//        if (!str_grpname.equalsIgnoreCase("")) {
//            System.out.println("custid " + str_custid+"grpn "+str_grpname+" grpti "+str_grpticket);
//            lay_grpname.setVisibility(View.VISIBLE);
//            grpname.setText(str_grpname);
//        }
//        if (!str_grpticket.equalsIgnoreCase("")) {
//            System.out.println("custid " + str_custid+"grpn "+str_grpname+" grpti "+str_grpticket);
//            laygrpticket.setVisibility(View.VISIBLE);
//            grpticket.setText(str_grpticket);
//        }
        if (cd.isConnectedToInternet()) {
            name.setText(str_name);
            mobile.setText("Mobile No. " + str_mobile);
            getinfo();

        } else {
            Toast.makeText(Customer_Info.this, "No Internet Connection ! ", Toast.LENGTH_SHORT).show();
            name.setText(str_name);
            mobile.setText("Mobile No. " + str_mobile);
        }
    }

    private void getinfo() {
        StringRequest req = new StringRequest(Request.Method.POST, Config.getcustinfo, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("Response " + response);
                try {
                    JSONObject object = new JSONObject(response);
                    str_address = object.getString("address");
                    str_collect_area = object.getString("collect_area");
                    str_introname = object.getString("Introducer_name");
                    String path = object.getString("Cust_Phot");
                    try {
                        String aUrl;
                        if (!path.isEmpty()) {
                            if(!path.startsWith("https")){
                                 aUrl = path.replace("http", "https");

                            }else {
                                 aUrl = path.replace("http", "http");

                            }

                            Picasso.with(Customer_Info.this).load(aUrl).resize(150, 150).onlyScaleDown().placeholder(R.drawable.profilepic).error(R.drawable.profilepic).centerInside().into(profile_image);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (!str_introname.trim().equalsIgnoreCase("")) {
                        intoname.setText(str_introname);
                        lay_intro.setVisibility(View.VISIBLE);
                    } else {
                        intoname.setVisibility(View.GONE);
                        lay_intro.setVisibility(View.GONE);
                    }
                    if (!str_collect_area.trim().equalsIgnoreCase("")) {
                        collect_area.setText(str_collect_area);
                        lay_area.setVisibility(View.VISIBLE);
                    } else {
                        collect_area.setVisibility(View.GONE);
                        lay_area.setVisibility(View.GONE);
                    }
                    if (!str_address.trim().equalsIgnoreCase("")) {
                        address.setText(str_address);
                    } else {
                        address.setVisibility(View.GONE);
                    }
                } catch (Exception e) {

                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("custid", str_custid);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(req);

    }

    public void builddilogdecline(String title, String content, Context context) {
        System.out.println(title);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context, R.style.MyAlertDialogStyleDeclined);
        alertDialog.setTitle(title);
        alertDialog
                .setMessage(content);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();

                    }
                });

        alertDialog.show();
    }

    public void builddilogstanding(String title, final String content, Context context, final Double lat, final Double longi) {
        System.out.println(title);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context, R.style.MyAlertDialogStyleApproved);
        alertDialog.setTitle(title);
        alertDialog
                .setMessage(content);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();
                        update_location(lat, longi);
                    }
                });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    public void builddilogsuccess(String title, final String content, Context context) {
        System.out.println(title);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                context, R.style.MyAlertDialogStyleApproved);
        alertDialog.setTitle(title);
        alertDialog
                .setMessage(content);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.gpsmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.menu_showlocation:
                if (str_locationupdated.equalsIgnoreCase("Yes")) {
                    String uri = "http://maps.google.com/maps?q=loc:" + str_uplat.toString() + "," + str_uplong.toString() + " (" + str_name + ")";
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                } else {
                    builddilogdecline("Error", "Customer Location not Updated", Customer_Info.this);
                }

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void blink(final Button txt) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 200;    //in milissegunds
                try {
                    Thread.sleep(timeToBlink);
                } catch (Exception e) {
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (txt.getVisibility() == View.VISIBLE) {
                            txt.setVisibility(View.INVISIBLE);
                        } else {
                            txt.setVisibility(View.VISIBLE);
                        }
                        blink(txt);
                    }
                });
            }
        }).start();
    }

    public void update_location(final Double lat, final Double longi) {
//        showDialog();
//        dbrecepit.deletetableadvance();
        StringRequest localreq = new StringRequest(Request.Method.POST,
                Config.update_location, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("location " + response.toString());
                String status = "";
                try {

                    JSONObject jObj = new JSONObject(response);
                    try {
                        status = jObj.getString("status");
//                        str_uplat = Double.parseDouble(jObj.getString("latitude"));
//                        str_uplong = Double.parseDouble(jObj.getString("longitude"));
//                        str_locationupdated = jObj.getString("locationupdated");


                    } catch (JSONException e) {
//                        hidePDialog();
                        e.printStackTrace();
                    }
                    if (status.equalsIgnoreCase("1")) {
                        builddilogsuccess("Success", "Location Successfully Updated", Customer_Info.this);
                        get_location();
                    } else {
                        builddilogdecline("Error", "Location Not Updated", Customer_Info.this);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cusid", str_custid);
                params.put("latitude", String.valueOf(lat));
                params.put("longitude", String.valueOf(longi));
                params.put("locationupdated", "yes");

                return params;
            }
        };

        localreq.setRetryPolicy(new DefaultRetryPolicy(1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(localreq);
    }

    public void get_location() {
//        showDialog();
//        dbrecepit.deletetableadvance();
        StringRequest localreq = new StringRequest(Request.Method.POST,
                Config.get_location, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("location " + response.toString());
                String status = "";
                try {

                    JSONObject jObj = new JSONObject(response);
                    try {
                        status = jObj.getString("status");
                        str_uplat = Double.parseDouble(jObj.getString("latitude"));
                        str_uplong = Double.parseDouble(jObj.getString("longitude"));
//                      str_locationupdated = jObj.getString("locationupdated");


                    } catch (JSONException e) {
//                        hidePDialog();
                        e.printStackTrace();
                    }
                    if (status.equalsIgnoreCase("1")) {
                        str_locationupdated = "Yes";
                        if (flash) {
                            colorAnim.end();
                            colorAnim.cancel();
                            btn_updatelocation.setTextColor(getResources().getColor(R.color.colorPrimary));
                            flash = false;
                        } else {
                        }
                    } else {
                        str_locationupdated = "No";
                        if (!flash) {
                            colorAnim = ObjectAnimator.ofInt(btn_updatelocation, "textColor", Color.BLACK, Color.TRANSPARENT); //you can change colors
                            colorAnim.setDuration(400); //duration of flash
                            colorAnim.setEvaluator(new ArgbEvaluator());
                            colorAnim.setRepeatCount(ValueAnimator.INFINITE);
                            colorAnim.setRepeatMode(ValueAnimator.REVERSE);
                            colorAnim.start();
                            flash = true;
                        }

//                        blink(btn_updatelocation);
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();
//                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cusid", str_custid);

                return params;
            }
        };

        localreq.setRetryPolicy(new DefaultRetryPolicy(1000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(localreq);
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
