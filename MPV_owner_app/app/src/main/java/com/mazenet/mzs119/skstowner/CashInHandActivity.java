package com.mazenet.mzs119.skstowner;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.skstowner.Adapter.CustomAdapterAgentlist;
import com.mazenet.mzs119.skstowner.Model.AgentModel;
import com.mazenet.mzs119.skstowner.Utils.AppController;
import com.mazenet.mzs119.skstowner.Utils.Config;
import com.mazenet.mzs119.skstowner.Utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CashInHandActivity extends AppCompatActivity {
    String url = Config.getcashinhand;
    String url2 = Config.getallagent;
    EditText edt_cxash;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    ScrollView srl_settle;
    Boolean show = false;
    String tot = "";
    PopupWindow changeSortPopUp;
    ListView agentlist;
    ArrayList<AgentModel> alist = new ArrayList<>();
    String tot_amnnt = "";
    CustomAdapterAgentlist adapterlist;
    String agentname = "", agentid = "";
    Button btn_settle, btn_view, btn_cash_settle;
    EditText edt_2000, edt_1000, edt_500, edt_200, edt_100, edt_50, edt_20, edt_10, edt_5, edt_2, edt_1, edt_total, edt_misc;
    TextView txt_totalno, txt_totalamnt;
    SimpleDateFormat dateFormat;
    String date = "";
    String total_amnt = "", totalno = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cash_in_hand);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();
        cd = new ConnectionDetector(this);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);
        agentlist = (ListView) findViewById(R.id.listagent);
        txt_totalamnt = (TextView) findViewById(R.id.txt_totalofapproved);
        txt_totalno = (TextView) findViewById(R.id.txt_noofapproved);

        txt_totalamnt.setText(" -/ ");
        txt_totalno.setText(" -/ ");
        dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        // dateFormat.setTimeZone(TimeZone.getTimeZone("IST"));
        Date date1 = new Date();
        date = dateFormat.format(date1);
        getallagent();
        getapproved(date);

        agentlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AgentModel am = alist.get(i);
                agentname = am.getAgentname();
                agentid = am.getAgentId();
                getcashinhand(agentid);

            }
        });

        //  edt_cxash = (EditText) findViewById(R.id.edt_cashinhand);
        if (cd.isConnectedToInternet()) {

        } else {
            Toast.makeText(CashInHandActivity.this, "Connect to Internet", Toast.LENGTH_SHORT).show();
        }


    }

    public void getcashinhand(String idd) {
        showDialog();
        final String id = idd;
        System.out.println("empid " + id);
        final Locale curLocale = new Locale("en", "IN");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                hidePDialog();


                try {


                    JSONObject object = new JSONObject(response);
                    tot_amnnt = object.getString("tot_amnt");
                    String totl = tot_amnnt;
                    System.out.println("toto " + tot_amnnt);
                    if (!totl.isEmpty()) {

                        Double d = Double.parseDouble(totl);

                        String moneyString2 = NumberFormat.getNumberInstance(curLocale).format(d);
                        System.out.println("cashin " + moneyString2);
                        totl = moneyString2;
                        showSortPopup1(CashInHandActivity.this, agentname, totl);

                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                CashInHandActivity.this);
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
                params.put("emp_id", id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }

    public void getapproved(final String date) {
        showDialog();

        final String id = pref.getString("userid", "");
        final Locale curLocale = new Locale("en", "IN");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.getapprovedamount, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                hidePDialog();


                try {


                    JSONObject object = new JSONObject(response);
                    total_amnt = object.getString("Total_approved");
                    totalno = object.getString("No_approved");
                    if (!total_amnt.isEmpty()) {
                        try {
                            Double d = Double.parseDouble(total_amnt);

                            String moneyString2 = NumberFormat.getNumberInstance(curLocale).format(d);
                            total_amnt = moneyString2;
                            txt_totalamnt.setText(total_amnt);
                            txt_totalno.setText(totalno);
                        } catch (Exception e) {

                        }

                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                CashInHandActivity.this);
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
                params.put("empid", id);
                params.put("date", date);
                System.out.println("emp " + id);
                System.out.println("date " + date);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }

    public void getallagent() {
        showDialog();
        final Locale curLocale = new Locale("en", "IN");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(" Activity", response.toString());
                System.out.println("allagent "+response);
                hidePDialog();


                try {


                    JSONObject object = new JSONObject(response);
                    JSONArray arra = object.getJSONArray("result");
                    try {
                        for (int i = 0; i < arra.length(); i++) {
                            JSONObject jobj = arra.getJSONObject(i);

                            AgentModel avmod = new AgentModel();
                            avmod.setAgentname(jobj.getString("Name"));
                            avmod.setAgentId(jobj.getString("Emp_Id"));
                            avmod.setCashinhand(jobj.getString("Amount"));
                            avmod.setTotalcollected(jobj.getString("tot_amnt"));
                            alist.add(avmod);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapterlist = new CustomAdapterAgentlist(CashInHandActivity.this, alist);
                    adapterlist.notifyDataSetChanged();
                    agentlist.setAdapter(adapterlist);
                    agentlist.setVisibility(View.VISIBLE);


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
                return params;
            }

        };

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

    private void showSortPopup1(final Activity context, String agentnme, String cashinhand) {

        final Dialog dialog = new Dialog(CashInHandActivity.this);
        dialog.setContentView(R.layout.custodialog);
        // dialog.setTitle("Agent name : " + agentnme);
        dialog.setCancelable(true);

        // set the custom dialog components - text, image and button
        final EditText text = (EditText) dialog.findViewById(R.id.edt_code);

        Button dialogok = (Button) dialog.findViewById(R.id.btn_ok);
        Button dialogcancel = (Button) dialog.findViewById(R.id.btn_cancel);
        final TextView txt_resend = (TextView) dialog.findViewById(R.id.txt_custo_name);
        final TextView txt_name = (TextView) dialog.findViewById(R.id.txt_titlee);
        txt_name.setText("Employee Name : " + agentnme);
        txt_resend.setText("Cash In Hand ");
        // if button is clicked, close the custom dialog
        text.setText("Rs. " + cashinhand);
        text.setText("Rs. " + cashinhand);

        dialogcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // CashInHandActivity.this.finish();
            }
        });

        dialogok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), DateWiseView.class);
                i.putExtra("empid", agentid);
                startActivity(i);
                dialog.dismiss();
            }
        });

        dialog.show();
    }


    private void showSortPopup(final Activity context, String agentnme, String cashinhand) {
        // Inflate the popup_layout.xml
        LinearLayout viewGroup = (LinearLayout) context.findViewById(R.id.popup);
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = layoutInflater.inflate(R.layout.cashinhanpopup, viewGroup);

        // Creating the PopupWindow
        changeSortPopUp = new PopupWindow(context);
        changeSortPopUp.setContentView(layout);
        changeSortPopUp.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        changeSortPopUp.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        changeSortPopUp.setFocusable(true);

        // Some offset to align the popup a bit to the left, and a bit down, relative to button's position.
        int OFFSET_X = -20;
        int OFFSET_Y = 95;

        // Clear the default translucent background
        changeSortPopUp.setBackgroundDrawable(new BitmapDrawable());

        // Displaying the popup at the specified location, + offsets.
        changeSortPopUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
        TextView agentname = (TextView) layout.findViewById(R.id.txt_cpop_name);
        agentname.setText(agentnme);
        EditText cashedt = (EditText) layout.findViewById(R.id.edt_cpopo_cashinhand);
        cashedt.setText(cashinhand);
        ImageButton bntclo = (ImageButton) layout.findViewById(R.id.btn_cpop_close);
        bntclo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                changeSortPopUp.dismiss();
            }
        });
        TextView txt_vie = (TextView) layout.findViewById(R.id.txt_cpop_view);
        txt_vie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getApplicationContext(), DateWiseView.class);
                i.putExtra("empid", agentid);
                startActivity(i);
                changeSortPopUp.dismiss();
            }
        });
        // Getting a reference to Close button, and close the popup when clicked.
      /*  Button close = (Button) layout.findViewById(R.id.close);
        close.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                changeSortPopUp.dismiss();
            }
        }); */

    }
}
