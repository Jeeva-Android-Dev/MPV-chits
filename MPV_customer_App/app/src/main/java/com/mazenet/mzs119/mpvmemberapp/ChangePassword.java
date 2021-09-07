package com.mazenet.mzs119.mpvmemberapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.mpvmemberapp.Utils.AppController;
import com.mazenet.mzs119.mpvmemberapp.Utils.ConnectionDetector;
import com.mazenet.mzs119.mpvmemberapp.Utils.Constants;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChangePassword extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    TextView resendotp, dialogok, txt_resend;
    private static final String TAG = MainActivity.class.getSimpleName();
    String Status = "", Details = "";
    EditText edt_newpass, edt_confirmpass, edt_custid, text;
    Button btn_submit;
    public int counter;
    boolean cancel = false;
    View focusView = null;
    String cus = "";
    Boolean isVerified = false;
    PopupWindow otp_login_popup;
    String Customer_name = "", customer_id = "", custtblid = "", Branch_Name = "", customer_address = "", Branch_Address = "", Customer_email = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custodialog);
        pref = getApplicationContext().getSharedPreferences(Constants.preference, MODE_PRIVATE);
        editor = pref.edit();
        dialogok = findViewById(R.id.btn_ok);

        // set the custom dialog components - text, image and button
        text = (EditText) findViewById(R.id.edt_code);



        txt_resend = (TextView) findViewById(R.id.txt_resend);


        txt_resend.setEnabled(false);

        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

                txt_resend.setText("00:"+new SimpleDateFormat("ss").format(new Date(millisUntilFinished)) + "\n" + "Resend OTP");

                SpannableString WordtoSpan = SpannableString.valueOf(txt_resend.getText());

                WordtoSpan.setSpan(new ForegroundColorSpan(Color.GRAY), 5, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                txt_resend.setText(WordtoSpan);

                counter++;
            }

            @Override
            public void onFinish() {
                txt_resend.setText("Resend OTP");
                txt_resend.setEnabled(true);

            }
        }.start();



        try {
            Intent i = getIntent();
            cus = i.getStringExtra("withcus");
            System.out.println("witc  " + cus);
            if (cus.equalsIgnoreCase("yes")) {
                System.out.println("witc  ");
                edt_custid.setVisibility(View.VISIBLE);
            } else {
                edt_custid.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        txt_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txt_resend.setEnabled(false);

                getotp();

                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        txt_resend.setText("00:"+new SimpleDateFormat("ss").format(new Date(millisUntilFinished)) + "\n" + "Resend OTP");
                        SpannableString WordtoSpan = SpannableString.valueOf(txt_resend.getText());

                        WordtoSpan.setSpan(new ForegroundColorSpan(Color.GRAY), 5, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

                        txt_resend.setText(WordtoSpan);
                        counter++;
                    }

                    @Override
                    public void onFinish() {
                        txt_resend.setText("Resend OTP");
                        txt_resend.setEnabled(true);

                    }
                }.start();
            }
        });

        dialogok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = text.getText().toString();
                if (!code.isEmpty()) {

                    if (code.equals(pref.getString("OTP", ""))) {
                        showotpdilog();



                    } else {
                        Toast.makeText(getApplicationContext(),
                                "Enter the valid verification code",
                                Toast.LENGTH_LONG).show();

                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Enter the verification code",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    private void changepass() {
        edt_confirmpass.setError(null);
        edt_newpass.setError(null);
        final String newpass = edt_newpass.getText().toString();
        final String confirmpass = edt_confirmpass.getText().toString();

        if (TextUtils.isEmpty(newpass)) {
            edt_newpass.setError("Enter User Id");
            focusView = edt_newpass;
            cancel = true;
            return;
        }
        if (TextUtils.isEmpty(confirmpass)) {
            edt_confirmpass.setError("Enter Password");
            focusView = edt_confirmpass;
            cancel = true;
            return;
        }
        if (newpass.equals(confirmpass)) {
            StringRequest movieReq = new StringRequest(Request.Method.POST,
                    Constants.changepasswordfirst, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response.toString());
                    System.out.println(response);

                    try {
                        JSONObject jObj = new JSONObject(response);
                        Status = jObj.getString("Status");
                        Details = jObj.getString("Details");

                        if (Status.equals("0")) {
                            Toast.makeText(ChangePassword.this, Details, Toast.LENGTH_SHORT).show();

                        } else if (Status.equals("1")) {
                            customer_id = jObj.getString("Customer_Id");
                            System.out.println("deta " + Details);
                            custtblid = jObj.getString("Customer_tableid");
                            Customer_name = jObj.getString("Customer_name");
                            Customer_email = jObj.getString("Customer_email");
                            Branch_Name = jObj.getString("Branch_Name");
                            customer_address = jObj.getString("Customer_address");
                            Branch_Address = jObj.getString("Branch_Address");
                            editor.putString("custtblid", custtblid);
                            editor.putString("customer_id", customer_id);
                            editor.putString("Customer_name", Customer_name);
                            editor.putString("Customer_email", Customer_email);
                            editor.putString("Branch_Name", Branch_Name);
                            editor.putString("Customer_address", customer_address);
                            // editor.putString("Branch_district", Branch_district);
                            editor.putString("Branch_Address", Branch_Address);
                            //editor.putString("Branch_pincode", Branch_pincode);
                            //editor.putString("Branch_Phone", Branch_Phone);
                            // editor.putString("Branch_State", Branch_State);
                            editor.putString("loginStatus", Status);
                            editor.commit();
                            otp_login_popup.dismiss();
                            Toast.makeText(ChangePassword.this, Details, Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(ChangePassword.this, MenuActivity.class);
                            startActivity(i);
                            finish();
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
                    System.out.println("cusid " + pref.getString("cusid", "") + " pas " + newpass);
                    params.put("custid", pref.getString("cusid", ""));
                    params.put("newpass", newpass);
                    return params;
                }

            };

            movieReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(movieReq);

        } else {
            Toast.makeText(ChangePassword.this, "Password Does Not Match!", Toast.LENGTH_SHORT).show();
        }
    }

    private void getotp() {
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Constants.getotp, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                System.out.println(response);

                try {
                    JSONObject jObj = new JSONObject(response);
                    Status = jObj.getString("Status");
                    Details = jObj.getString("Details");

                    if (Status.equals("0")) {
                        isVerified = true;
                        String OTP = jObj.getString("OTP");
                        System.out.println("otp " + OTP);
                        editor.putString("OTP", OTP);
                        editor.putString("cusid", jObj.getString("id"));
                        editor.commit();
                        Toast.makeText(ChangePassword.this, Details, Toast.LENGTH_SHORT).show();


                    } else if (Status.equals("1")) {
                        isVerified = false;
                        Toast.makeText(ChangePassword.this, Details, Toast.LENGTH_SHORT).show();
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
                isVerified = false;

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                System.out.println("cusid otp "+pref.getString("customer_id", ""));
                params.put("custid", pref.getString("customer_id", ""));
                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }

    public void showotpdilog() {
        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.activity_change_password, null);

        otp_login_popup = new PopupWindow(this);
        otp_login_popup.setContentView(layout);
        otp_login_popup.setWidth(ConstraintLayout.LayoutParams.MATCH_PARENT);
        otp_login_popup.setHeight(ConstraintLayout.LayoutParams.MATCH_PARENT);
        otp_login_popup.setAnimationStyle(R.style.AnimationPopup);
        otp_login_popup.setFocusable(true);
        otp_login_popup.setBackgroundDrawable(null);
        otp_login_popup.setOutsideTouchable(false);
        otp_login_popup.showAtLocation(layout, Gravity.CENTER, 0, 0);
        edt_newpass = (EditText) layout.findViewById(R.id.edt_newpassword);
        edt_confirmpass = (EditText) layout.findViewById(R.id.edt_confirmpassword);
        btn_submit = (Button) layout.findViewById(R.id.btn_cp_submit);
        edt_custid = (EditText) layout.findViewById(R.id.edt_cp_custid);
        resendotp = (TextView) layout.findViewById(R.id.resendotp);


        resendotp.setVisibility(View.GONE);
        resendotp.setEnabled(false);

        new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {


                resendotp.setText("00:"+new SimpleDateFormat("ss").format(new Date(millisUntilFinished)) + "\n" + "Resend OTP");
                SpannableString WordtoSpan = SpannableString.valueOf(resendotp.getText());

                WordtoSpan.setSpan(new ForegroundColorSpan(Color.GRAY), 5, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                resendotp.setText(WordtoSpan);
                counter++;
            }

            @Override
            public void onFinish() {
                resendotp.setText("Resend OTP");
                resendotp.setEnabled(true);

            }
        }.start();



        resendotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getotp();

                resendotp.setEnabled(false);

                new CountDownTimer(60000, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {



                        resendotp.setText("00:"+new SimpleDateFormat("ss").format(new Date(millisUntilFinished)) + "\n" + "Resend OTP");

                        SpannableString WordtoSpan = SpannableString.valueOf(resendotp.getText());

                        WordtoSpan.setSpan(new ForegroundColorSpan(Color.GRAY), 5, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


                        resendotp.setText(WordtoSpan);

                        counter++;
                    }

                    @Override
                    public void onFinish() {
                        resendotp.setText("Resend OTP");
                        resendotp.setEnabled(true);

                    }
                }.start();
            }
        });
        otp_login_popup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                otp_login_popup.dismiss();
                onBackPressed();
            }
        });
        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changepass();
            }
        });

    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
