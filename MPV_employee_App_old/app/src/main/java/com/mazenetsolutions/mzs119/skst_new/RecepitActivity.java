package com.mazenetsolutions.mzs119.skst_new;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenetsolutions.mzs119.skst_new.Adapter.CustomAdapterenrollment;
import com.mazenetsolutions.mzs119.skst_new.Database.Databaserecepit;
import com.mazenetsolutions.mzs119.skst_new.Model.Enrollmodel;
import com.mazenetsolutions.mzs119.skst_new.Utils.AppController;
import com.mazenetsolutions.mzs119.skst_new.Utils.Config;
import com.mazenetsolutions.mzs119.skst_new.Utils.ConnectionDetector;
import com.mazenetsolutions.mzs119.skst_new.Utils.NonScrollListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecepitActivity extends AppCompatActivity {

    public ArrayList<Enrollmodel> enroll_list = new ArrayList<Enrollmodel>();
    public ArrayList<Enrollmodel> enroll_list_local = new ArrayList<Enrollmodel>();
    public ArrayList<Enrollmodel> enroll_listmain = new ArrayList<Enrollmodel>();
    GlobalValue globalValue;
    public ArrayList<Enrollmodel> enroll_listpost = new ArrayList<Enrollmodel>();
    SharedPreferences pref;
    String pending_value,penalty_value,bonus_value;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    String url = Config.reteriveenroll;
    String url3 = Config.retrievealluser;
    TextView txt_advance,txt_reward;
    NonScrollListView lst_re_enroll;
    String grpp_id = "0",cusid = "0", cusname = "", mobile = "";
    CustomAdapterenrollment adapterlist;
    Databaserecepit dbrecepit;
    int advanceavailable = 0;
    private ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recepit);
        globalValue=new GlobalValue(getApplicationContext());

        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();

        txt_advance = findViewById(R.id.txt_advance);
        lst_re_enroll = findViewById(R.id.lst_re_enroll);
        txt_reward = findViewById(R.id.txt_reward);

        cd = new ConnectionDetector(this);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);


        dbrecepit = new Databaserecepit(this);

        try {
            Intent it = getIntent();
            cusid = it.getStringExtra("cusid");
            cusname = it.getStringExtra("cusname");
            mobile = it.getStringExtra("mobile");
            System.out.println("cusid " + cusid);
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (cd.isConnectedToInternet()) {
            reteriveall();
            reteriveadvance();
        } else {
            reterivefromdb();
        }

        //======================================================================================================
        lst_re_enroll.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Enrollmodel sched = enroll_list.get(i);
                final String groupname = sched.getGroup_Name();
                final String Enrolid = sched.getEnrollid();
                final String grup_id = sched.getGrpid();
                String pending = sched.getPending_Amt();
                pending_value = sched.getPending_Amt();
                penalty_value = sched.getPenalty_Amt();
                bonus_value = sched.getBonus_Amt();

                globalValue.putString("next_due_no_adv",sched.getUpcommimg_installment_no());
                globalValue.putString("next_due_amount_adv",sched.getUpcoming_dueamount());
                globalValue.putString("next_due_date_adv",sched.getUpcommimg_due_date());

                globalValue.putString("grpname",sched.getGroup_Name());
                globalValue.putString("paid",sched.getPaid_Amt());
                globalValue.putString("pending",sched.getPending_Amt());
                globalValue.putString("scheme",sched.getScheme());
                globalValue.putString("bonus",sched.getBonus_Amt());
                globalValue.putString("penaulty",sched.getPenalty_Amt());
                globalValue.putString("amount",sched.getPayamount());
                globalValue.putString("advance",sched.getAdvanceamnt());
                globalValue.putString("next_due_no",sched.getUpcommimg_installment_no());
                globalValue.putString("next_due_amount",sched.getUpcoming_dueamount());
                globalValue.putString("next_due_date",sched.getUpcommimg_due_date());

                pending = pending.replaceAll(",", "");
                int pends = 0;
                try {
                    pends = Integer.parseInt(pending);
                } catch (NumberFormatException e) {
                    pends = 0;
                    e.printStackTrace();
                }
                if (pends > 0) {
                    if (advanceavailable > 0) {
                        final Dialog dialog = new Dialog(RecepitActivity.this);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

                        dialog.setContentView(R.layout.adv_remark);
                        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                        final Button btn_recepit = (Button) dialog.findViewById(R.id.btn_recepit);
                        final Button btn_advanceadjust = (Button) dialog.findViewById(R.id.btn_advanceadjust);

                        btn_recepit.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
//                                Intent iy = new Intent(RecepitActivity.this, IndividualReceipt.class);
                                Intent iy = new Intent(RecepitActivity.this, DuewiseIndividualReceipt.class);
                                iy.putExtra("cusid", cusid);
                                iy.putExtra("cusname", cusname);
                                iy.putExtra("groupname", groupname);
                                iy.putExtra("enrollid", Enrolid);
                                iy.putExtra("pending_value", pending_value);
                                iy.putExtra("grp_id", grup_id);
                                iy.putExtra("penalty_value", penalty_value);
                                iy.putExtra("bonus_value", bonus_value);



                                startActivity(iy);
                                finish();

                            }
                        });
                        btn_advanceadjust.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();

                                Intent iy = new Intent(RecepitActivity.this, DuewiseAdvaceReceipt.class);
                                iy.putExtra("cusid", cusid);
                                iy.putExtra("cusname", cusname);
                                iy.putExtra("groupname", groupname);
                                iy.putExtra("enrollid", Enrolid);
                                iy.putExtra("pending_value", pending_value);
                                iy.putExtra("grp_id", grup_id);
                                iy.putExtra("penalty_value", penalty_value);
                                iy.putExtra("bonus_value", bonus_value);

//                                Intent i = new Intent(RecepitActivity.this, AdvanceAdjustment.class);
//                                i.putExtra("cusid", cusid);
//                                i.putExtra("enrollid", Enrolid);
//                                System.out.println("ccuid " + cusid);
//                                i.putExtra("cusname", cusname);
//                                i.putExtra("mobile", mobile);
//                                i.putExtra("pending_value", pending_value);
//                                i.putExtra("penalty_value", penalty_value);
//                                i.putExtra("bonus_value", bonus_value);

                                startActivity(iy);
                                finish();
                            }
                        });

                        dialog.show();
                    } else {
//                        Intent iy = new Intent(RecepitActivity.this, IndividualReceipt.class);
                        Intent iy = new Intent(RecepitActivity.this, DuewiseIndividualReceipt.class);
                        iy.putExtra("cusid", cusid);
                        iy.putExtra("cusname", cusname);
                        iy.putExtra("groupname", groupname);
                        iy.putExtra("enrollid", Enrolid);
                        iy.putExtra("grp_id", grup_id);

                        iy.putExtra("pending_value", pending_value);
                        iy.putExtra("penalty_value", penalty_value);
                        iy.putExtra("bonus_value", bonus_value);

                        startActivity(iy);
                        finish();
                    }


                } else {
                    Intent iy = new Intent(RecepitActivity.this, AdvanceIndividualReceipt.class);
                    iy.putExtra("cusid", cusid);
                    iy.putExtra("cusname", cusname);
                    iy.putExtra("groupname", groupname);
                    iy.putExtra("enrollid", Enrolid);
                    globalValue.putString("next_due_no_adv",sched.getUpcommimg_installment_no());
                    globalValue.putString("next_due_amount_adv",sched.getUpcoming_dueamount());
                    globalValue.putString("next_due_date_adv",sched.getUpcommimg_due_date());



                    startActivity(iy);
                    finish();
                }


            }
        });

        //========================================================================================================


    }


    // ===========================================================================
    private void reterivefromdb() {
        showDialog();
        enroll_list.clear();
        enroll_list = dbrecepit.getreceiptforcust(cusid);
        adapterlist = new CustomAdapterenrollment(RecepitActivity.this, enroll_list);
        adapterlist.notifyDataSetChanged();
        //ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
        lst_re_enroll.setAdapter(adapterlist);
        hidePDialog();
    }


    public void reteriveall() {

        showDialog();
        enroll_list.clear();
        dbrecepit.deletetable();
        StringRequest stringreq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection enroll", response.toString());
                hidePDialog();


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
                            sched.setAdvanceAvail(jObj.getString("advance_available"));
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
                            dbrecepit.deletetable();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        try {
                            dbrecepit.addenroll(enroll_list);
                            adapterlist = new CustomAdapterenrollment(RecepitActivity.this, enroll_list);
                            adapterlist.notifyDataSetChanged();
                            //ListViewHeight.setListViewHeightBasedOnChildren(lst_re_enroll);
                            lst_re_enroll.setAdapter(adapterlist);
                            //customer_list.clear();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                RecepitActivity.this);
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
                params.put("Cust_Id", cusid);


                return params;
            }

        };
        stringreq.setRetryPolicy(new DefaultRetryPolicy(20000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(stringreq);
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

    public void reteriveadvance() {

        System.out.println("advance idd");
//        enroll_list.clear();
        //dbrecepit.deletetable();
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.totalavailableadvance, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection advance", response.toString());
                System.out.println("advance " + response);
                try {


                    JSONObject object = new JSONObject(response);
                    String advance = object.getString("advance");
                    advanceavailable = convertStringtoInt(advance);
                    String reward_point = object.getString("reward_point");

                    if(reward_point.equalsIgnoreCase("null") ||reward_point.equalsIgnoreCase("")){
                        txt_reward.setText("0");
                    }else {
                        txt_reward.setText(reward_point);
                    }
                    txt_advance.setText(advance);
                    txt_advance.setText(advance);
                    globalValue.putString("cus_advance",advance);

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
                params.put("cusid", cusid);
                System.out.println("advancecus id " + cusid);

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(10000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    public static int convertStringtoInt(String string) {
        int formattedInt = 0;
        string = makeStringZeroifNull(string);
        string = string.replaceAll(",", "");
        string = string.replaceAll(" ", "");
        try {
            formattedInt = Integer.parseInt(string);

        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return formattedInt;
    }

    public static String makeStringZeroifNull(String string) {
        try {
            if (string.isEmpty() || string.equalsIgnoreCase("") || string.equalsIgnoreCase("null")) {
                string = "0";
                return string;
            }
        } catch (Exception e) {
            string = "0";

        }
        return string;

    }
}
