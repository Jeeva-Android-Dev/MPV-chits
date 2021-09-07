package com.mazenet.mzs119.mpvmemberapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.mpvmemberapp.Adapter.CustomAdapterMyTrans;
import com.mazenet.mzs119.mpvmemberapp.Model.DateWiseViewModel;
import com.mazenet.mzs119.mpvmemberapp.Utils.AppController;
import com.mazenet.mzs119.mpvmemberapp.Utils.Constants;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MyTransactions extends AppCompatActivity {
    DateFormat df;
    TextView txt_tot;
    ListView viewlist;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private static final String TAG = MainActivity.class.getSimpleName();
    String url = Constants.reterivereceipts, cusid = "", enrollid = "";
    int tot = 0, tot1 = 0;
    ArrayList<DateWiseViewModel> listmain = new ArrayList<>();
    CustomAdapterMyTrans adapterDatewise;
    Locale curLocale = new Locale("en", "IN");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_transactions);

        pref = getApplicationContext().getSharedPreferences(Constants.preference, MODE_PRIVATE);
        editor = pref.edit();
        viewlist = (ListView) findViewById(R.id.list_prevtransacs);
        txt_tot = (TextView) findViewById(R.id.txt_mytrans_total);
        try {
            Intent it = getIntent();
            enrollid = it.getStringExtra("enrollid");
            getSupportActionBar().setTitle(it.getStringExtra("enrollname"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        getall();
    }

    public void getall() {
        tot1 = 0;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                System.out.println(response);
                listmain.clear();
                try {
                    JSONObject jobj = new JSONObject(response);
                    String jsonob = jobj.getString("result");
                    System.out.println("hjbfsgjgh " + jsonob);
                    if (jsonob.equals("0")) {
                        Toast.makeText(MyTransactions.this, "No receipts Available", Toast.LENGTH_SHORT).show();
                    } else {
                        JSONArray object = jobj.getJSONArray("result");
                        try {

                            for (int i = 0; i < object.length(); i++) {
                                JSONObject jObj = object.getJSONObject(i);

                                DateWiseViewModel dvm = new DateWiseViewModel();
                                dvm.setCust_Id(jObj.getString("Cust_Id"));
                                dvm.setCus_Branch(jObj.getString("Cus_Branch"));
                                dvm.setEnrl_Id(jObj.getString("Enrl_Id"));
                                dvm.setTotal_Amount(jObj.getString("Total_Amount"));
                                dvm.setReceipt_No(jObj.getString("Receipt_No"));
                                dvm.setCreated_By(jObj.getString("Created_By"));
                                dvm.setCheque_No(jObj.getString("Cheque_No"));
                                dvm.setCheque_Date(jObj.getString("Cheque_Date"));
                                dvm.setBank_Name(jObj.getString("Bank_Name"));
                                dvm.setBranch_Name(jObj.getString("Branch_Name"));
                                dvm.setTrn_No(jObj.getString("Trn_No"));
                                dvm.setTrn_Date(jObj.getString("Trn_Date"));
                                dvm.setPayment_Type(jObj.getString("Payment_Type"));
                                dvm.setGroup_Id(jObj.getString("Group_Id"));
                                dvm.setGroup_Ticket_Id(jObj.getString("Group_Ticket_Id"));
                                dvm.setFirst_Name_F(jObj.getString("First_Name_F"));
                                dvm.setDate(jObj.getString("Date"));
                                dvm.setTime(jObj.getString("time"));
                                dvm.setDue_advance(jObj.getString("due_advance"));
                                dvm.setDateitme(jObj.getString("Date") + " " + jObj.getString("time"));
                                listmain.add(dvm);
                            }
                            if (listmain.size() > 0) {
//                                Collections.sort(listmain, new Comparator<DateWiseViewModel>() {
//                                    public int compare(DateWiseViewModel o1, DateWiseViewModel o2) {
//                                        if (o1.getDateitme() == null || o2.getDateitme() == null)
//                                            return 0;
//                                        return o1.getDateitme().compareTo(o2.getDateitme());
//                                    }
//                                });
//                                Collections.reverse(listmain);
                                ArrayList<DateWiseViewModel> finalarray = new ArrayList<>();
                                for (int i = 0; i < listmain.size(); i++) {
                                    DateWiseViewModel cm = listmain.get(i);
                                    String paytype = cm.getPayment_Type();
                                    finalarray.add(cm);
//                                    if (paytype.equalsIgnoreCase("Advance Adjustment")||paytype.equalsIgnoreCase("daily.rct.adj")) {
//                                    } else if (paytype.equalsIgnoreCase("C.P Adjustment")||paytype.equalsIgnoreCase("b.p.adj")) {
//                                        finalarray.add(cm);
//                                    } else {
//                                        System.out.println("payty " + paytype);
//                                        finalarray.add(cm);
//                                    }
                                }
                                adapterDatewise = new CustomAdapterMyTrans(MyTransactions.this, listmain);
                                adapterDatewise.notifyDataSetChanged();
                                viewlist.setAdapter(adapterDatewise);
                                viewlist.setVisibility(View.VISIBLE);
                                for (int i = 0; i < listmain.size(); i++) {
                                    DateWiseViewModel tem = listmain.get(i);
                                    String tote = tem.getTotal_Amount().replaceAll(" ","");
                                    try {
                                        int tot = Integer.parseInt(tote);
                                        tot1 += tot;
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                System.out.println("toto " + tot1);
                                String advance = String.valueOf(tot1);
                                try {
                                    Double d = Double.parseDouble(advance);
                                    String moneyString2 = NumberFormat.getNumberInstance(curLocale).format(d);
                                    advance = moneyString2;
                                    System.out.println("ad " + advance);
                                } catch (NumberFormatException e) {
                                    e.printStackTrace();
                                }
                                txt_tot.setText("Rs. " + advance);

                            } else {

                                viewlist.setVisibility(View.GONE);
                                Toast.makeText(MyTransactions.this, "No receipts Available", Toast.LENGTH_SHORT).show();

                            }


                        } catch (Exception e) {

                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("custid", pref.getString("custtblid", ""));
                params.put("enrlid", enrollid);
                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(90000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }
}
