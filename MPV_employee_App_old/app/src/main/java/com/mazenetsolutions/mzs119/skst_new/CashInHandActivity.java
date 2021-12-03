package com.mazenetsolutions.mzs119.skst_new;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenetsolutions.mzs119.skst_new.Utils.AppController;
import com.mazenetsolutions.mzs119.skst_new.Utils.Config;
import com.mazenetsolutions.mzs119.skst_new.Utils.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CashInHandActivity extends AppCompatActivity {
    String url = Config.getcashinhand;
    String settleurl = Config.settleurl;
    EditText edt_cxash,edt_all_receipts;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    ProgressDialog pDialog;
    ScrollView srl_settle;
    Boolean show = false;
    String tot = "";
    String tot_amnnt = "",tot_rec_amnt="";
    Button btn_settle, btn_view, btn_cash_settle;
    EditText edt_2000, edt_1000, edt_500, edt_200, edt_100, edt_50, edt_20, edt_10, edt_5, edt_2, edt_1, edt_total, edt_misc;

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
        btn_settle = (Button) findViewById(R.id.btn_settlement);
        btn_view = (Button) findViewById(R.id.btn_ch_view);
        srl_settle = (ScrollView) findViewById(R.id.srl_settle);
        edt_cxash = (EditText) findViewById(R.id.edt_cashinhand);
        edt_1 = (EditText) findViewById(R.id.edt_ch_1);
        edt_2 = (EditText) findViewById(R.id.edt_ch_2);
        edt_5 = (EditText) findViewById(R.id.edt_ch_5);
        edt_10 = (EditText) findViewById(R.id.edt_ch_10);
        edt_20 = (EditText) findViewById(R.id.edt_ch_20);
        edt_50 = (EditText) findViewById(R.id.edt_ch_50);
        edt_100 = (EditText) findViewById(R.id.edt_ch_100);
        edt_200 = (EditText) findViewById(R.id.edt_ch_200);
        edt_500 = (EditText) findViewById(R.id.edt_ch_500);
        edt_1000 = (EditText) findViewById(R.id.edt_ch_1000);
        edt_2000 = (EditText) findViewById(R.id.edt_ch_2000);
        edt_total = (EditText) findViewById(R.id.edt_ch_total);
        edt_misc = (EditText) findViewById(R.id.edt_ch_misc);
        edt_all_receipts = (EditText) findViewById(R.id.edt_all_receipts);
        btn_cash_settle = (Button) findViewById(R.id.btn_sett);
        if (cd.isConnectedToInternet()) {
            getcashinhand();
        } else {
            Toast.makeText(CashInHandActivity.this, "Connect to Internet", Toast.LENGTH_SHORT).show();
        }
        btn_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cd.isConnectedToInternet()) {
                    Intent i = new Intent(CashInHandActivity.this, Datewise2.class);
                    startActivity(i);
                } else {
                    Toast.makeText(CashInHandActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        edt_1000.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_misc.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_10.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_20.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_50.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_100.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }


            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_200.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_500.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });
        edt_2000.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                textwatch();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                edt_total.setText(tot);
            }
        });

        btn_settle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cd.isConnectedToInternet()) {
                    srl_settle.setVisibility(View.VISIBLE);
                } else {
                    Toast.makeText(CashInHandActivity.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                }

            }
        });
        btn_cash_settle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredTotal = "";
                double settle_total = 0, cashinhand = 0;
                try {
                    enteredTotal = edt_total.getText().toString();
                    settle_total = Integer.parseInt(enteredTotal);
                } catch (Exception e) {
                    enteredTotal = "0";
                    settle_total=0;
                    e.printStackTrace();
                }

                cashinhand = Double.parseDouble(tot_amnnt);;

                if (edt_total.getText().toString().isEmpty()||settle_total<=0) {
                    Toast.makeText(CashInHandActivity.this, "Enter Settlement Amount", Toast.LENGTH_SHORT).show();
                } else if (settle_total>cashinhand) {
                    Toast.makeText(CashInHandActivity.this, "Entered Amount cannot be greater than Cashinhand", Toast.LENGTH_SHORT).show();

                }
                else
                {
                    settleamount();
                }


            }
        });


    }

    public void textwatch() {
        int edt2000 = 0, edt1 = 0, edt2 = 0, edt5 = 0, edt10 = 0, edt20 = 0, edt50 = 0, edt100 = 0, edt200 = 0, edt500 = 0, edtmisc = 0, edt1000 = 0;
        try {
            String str1 = edt_1.getText().toString();
            if (str1.equalsIgnoreCase("")) {
                str1 = "0";
                edt1 = Integer.parseInt(str1);
            } else {
                edt1 = Integer.parseInt(str1);
            }
            String str2 = edt_2.getText().toString();
            if (str2.equalsIgnoreCase("")) {
                str2 = "0";
                edt2 = Integer.parseInt(str2);
            } else {
                edt2 = Integer.parseInt(str2);
            }
            String str5 = edt_5.getText().toString();
            if (str5.equalsIgnoreCase("")) {
                str5 = "0";
                edt5 = Integer.parseInt(str5);
            } else {
                edt5 = Integer.parseInt(str5);
            }
            String str10 = edt_10.getText().toString();
            if (str10.equalsIgnoreCase("")) {
                str10 = "0";
                edt10 = Integer.parseInt(str10);
            } else {
                edt10 = Integer.parseInt(str10);
            }
            String str20 = edt_20.getText().toString();
            if (str20.equalsIgnoreCase("")) {
                str20 = "0";
                edt20 = Integer.parseInt(str20);
            } else {
                edt20 = Integer.parseInt(str20);
            }
            String str50 = edt_50.getText().toString();
            if (str50.equalsIgnoreCase("")) {
                str50 = "0";
                edt50 = Integer.parseInt(str50);
            } else {
                edt50 = Integer.parseInt(str50);
            }
            String str100 = edt_100.getText().toString();
            if (str100.equalsIgnoreCase("")) {
                str100 = "0";
                edt100 = Integer.parseInt(str100);
            } else {
                edt100 = Integer.parseInt(str100);
            }
            String str200 = edt_200.getText().toString();
            if (str200.equalsIgnoreCase("")) {
                str200 = "0";
                edt200 = Integer.parseInt(str200);
            } else {
                edt200 = Integer.parseInt(str200);
            }
            String str500 = edt_500.getText().toString();
            if (str500.equalsIgnoreCase("")) {
                str500 = "0";
                edt500 = Integer.parseInt(str500);
            } else {
                edt500 = Integer.parseInt(str500);
            }
            String str1000 = edt_1000.getText().toString();
            if (str1000.equalsIgnoreCase("")) {
                str1000 = "0";
                edt1000 = Integer.parseInt(str1000);
            } else {
                edt1000 = Integer.parseInt(str1000);
            }
            String str2000 = edt_2000.getText().toString();
            System.out.println("str 2000  " + str2000);
            if (str2000.equalsIgnoreCase("")) {
                str2000 = "0";
                edt2000 = Integer.parseInt(str2000);
            } else {
                edt2000 = Integer.parseInt(str2000);
            }
            String strmisc = edt_misc.getText().toString();
            if (strmisc.equalsIgnoreCase("")) {
                strmisc = "0";
                edtmisc = Integer.parseInt(strmisc);
            } else {
                edtmisc = Integer.parseInt(strmisc);
            }

            tot = String.valueOf((edt1) + (edt2 * 2) + (edt5 * 5) + (edt10 * 10) + (edt20 * 20) + (edt50 * 50) + (edt100 * 100) + (edt200 * 200) + (edt500 * 500) + (edt2000 * 2000) + (edt1000 * 1000) + (edtmisc));
            System.out.println(tot + "  hhoesogjoie");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void settleamount() {
        showDialog();
        final Locale curLocale = new Locale("en", "IN");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                settleurl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                hidePDialog();


                try {


                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");

                    if (status.equalsIgnoreCase("1")) {
                        edt_1.setText("");
                        edt_2.setText("");
                        edt_5.setText("");
                        edt_10.setText("");
                        edt_20.setText("");
                        edt_50.setText("");
                        edt_100.setText("");
                        edt_200.setText("");
                        edt_500.setText("");
                        edt_1000.setText("");
                        edt_2000.setText("");
                        edt_misc.setText("");
                        edt_total.setText("");
                        srl_settle.setVisibility(View.GONE);
                        finish();
                        startActivity(getIntent());
                        Toast.makeText(CashInHandActivity.this, "Amount Settled", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CashInHandActivity.this, "No response from Server. Try after few minutes", Toast.LENGTH_SHORT).show();
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
                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("amt_1", edt_1.getText().toString());
                params.put("amt_2", edt_2.getText().toString());
                params.put("amt_5", edt_5.getText().toString());
                params.put("amt_10", edt_10.getText().toString());
                params.put("amt_20", edt_20.getText().toString());
                params.put("amt_50", edt_50.getText().toString());
                params.put("amt_100", edt_100.getText().toString());
                params.put("amt_200", edt_200.getText().toString());
                params.put("amt_500", edt_500.getText().toString());
                params.put("amt_1000", edt_1000.getText().toString());
                params.put("amt_2000", edt_2000.getText().toString());
                System.out.println("200000  " + edt_2000.getText().toString());
                params.put("amt_misc", edt_misc.getText().toString());
                params.put("Emp_Id", pref.getString("userid", "0"));
                System.out.println("empid " + pref.getString("userid", "0"));
                params.put("branch_Id", pref.getString("empbranch", "0"));
                params.put("Total_Amount", tot_amnnt);
                params.put("Received_Amount", edt_total.getText().toString());
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }

    public void getcashinhand() {
        showDialog();
        final Locale curLocale = new Locale("en", "IN");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("resv " + response);
                hidePDialog();


                try {


                    JSONObject object = new JSONObject(response);
                    tot_amnnt = object.getString("tot_amnt");
                    tot_rec_amnt = object.getString("tot_rec_amnt");
                    String totl = tot_amnnt;
                    if (!totl.isEmpty()) {

                        Double d = Double.parseDouble(totl);
                        String moneyString2 = NumberFormat.getNumberInstance(curLocale).format(d);
                        totl = moneyString2;
                        edt_cxash.setText(totl);

                        if(!tot_rec_amnt.isEmpty())
                        {
                            Double d1 = Double.parseDouble(tot_rec_amnt);
                            String moneyString = NumberFormat.getNumberInstance(curLocale).format(d1);
                            String to = moneyString;
                            edt_all_receipts.setText(to);
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
                params.put("emp_id", pref.getString("userid", "0"));
                System.out.println(pref.getString("userid", "0"));
                System.out.println(pref.getString("empbranch", "0"));
                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(90000,
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
