package com.mazenet.mzs119.skstowner;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.skstowner.Utils.AppController;
import com.mazenet.mzs119.skstowner.Utils.Config;
import com.mazenet.mzs119.skstowner.Utils.ConnectionDetector;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private static final String url = Config.login;
    private static final String TAG = MainActivity.class.getSimpleName();
    private ProgressDialog pDialog;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    String fontPath = Config.FONTPATHMAIN;
    Typeface tf;
    TextView login_title, login_title1;
    Button mEmailSignInButton;
    private AutoCompleteTextView mEmailView;
    private EditText mPasswordView;
    String android_id = "",otp="";
    String email = "";
    String name = "", UserId = "", Branch = "", B_city = "", B_address = "", B_District = "", B_pin = "", B_phone = "", owner = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();
        tf = Typeface.createFromAsset(this.getAssets(), fontPath);
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);

        cd = new ConnectionDetector(this);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);


        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);

        login_title = (TextView) findViewById(R.id.login_title);
        login_title1 = (TextView) findViewById(R.id.login_title1);


        login_title.setTypeface(tf);
        login_title1.setTypeface(tf);

        mEmailView.setText(pref.getString("email", ""));
        mPasswordView.setText(pref.getString("password", ""));

        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == 12345 || id == EditorInfo.IME_NULL) {
                    if (cd.isConnectedToInternet()) {
                        attemptLogin();
                    } else {
                        Toast.makeText(MainActivity.this, "Check the internet connection", Toast.LENGTH_LONG).show();

                    }
                    return true;
                }
                return false;
            }
        });
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);


        mEmailSignInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cd.isConnectedToInternet()) {
                    attemptLogin();
                } else {
                    Toast.makeText(MainActivity.this, "Check the internet connection", Toast.LENGTH_LONG).show();

                }
            }
        });


    }


    private void attemptLogin() {


        mEmailView.setError(null);
        mPasswordView.setError(null);

         email = mEmailView.getText().toString();
        final String password = mPasswordView.getText().toString();

        boolean cancel = false;
        View focusView = null;


        if (TextUtils.isEmpty(password)) {
            mPasswordView.setError("Invalid Pasword");
            focusView = mPasswordView;
            cancel = true;
        }

        if (TextUtils.isEmpty(email)) {
            mEmailView.setError("Enter Valid Email Id");
            focusView = mEmailView;
            cancel = true;
        }

        if (cancel) {

            focusView.requestFocus();
        } else {
            showDialog();

            StringRequest movieReq = new StringRequest(Request.Method.POST,
                    url, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response.toString());
                    hidePDialog();


                    try {
                        JSONObject jObj = new JSONObject(response);
                        String Success = jObj.getString("status");
                        String Details = jObj.getString("details");

                        try {

                            if (Success.equals("0")) {
                                hidePDialog();
                                Toast.makeText(MainActivity.this, Details, Toast.LENGTH_LONG).show();
                            } else if (Success.equals("1")) {
                                name = jObj.getString("username");
                                UserId = jObj.getString("UserId");
                                Branch = jObj.getString("Branch");
                                owner = jObj.getString("isowner");
                                otp=jObj.getString("otp");
                                System.out.println("otp "+otp);
                                hidePDialog();
                                Toast.makeText(MainActivity.this, Details, Toast.LENGTH_LONG).show();

                                if (owner.equalsIgnoreCase("yes")) {
                                    Toast.makeText(MainActivity.this, Details, Toast.LENGTH_LONG).show();
                                    showotpdilog();
                                } else {
                                    Toast.makeText(MainActivity.this, "Not an Owner", Toast.LENGTH_SHORT).show();
                                }


                            } else if (Success.equals("2")) {
                                name = jObj.getString("username");
                                UserId = jObj.getString("UserId");
                                Branch = jObj.getString("Branch");
                                owner = jObj.getString("isowner");
                                hidePDialog();
                                if (owner.equalsIgnoreCase("yes")) {
                                    editor.putString("username", name);
                                    editor.putString("userid", UserId);
                                    editor.putString("empbranch", Branch);
                                    editor.commit();
                                    Toast.makeText(MainActivity.this, Details, Toast.LENGTH_LONG).show();
                                    Intent it = new Intent(MainActivity.this, MenuActivity.class);
                                    startActivity(it);
                                    finish();
                                } else {
                                    Toast.makeText(MainActivity.this, "Not an Owner", Toast.LENGTH_SHORT).show();
                                }

                            }

                            //


                        } catch (Exception e) {

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
                    hidePDialog();

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    params.put("password", password);
                    params.put("regid", android_id);
                    editor.putString("email", email);
                    editor.putString("password", password);
                    editor.commit();

                    return params;
                }

            };

            // Adding request to request queue
            AppController.getInstance().addToRequestQueue(movieReq);


        }
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void showotpdilog() {
        final Dialog dialog = new Dialog(MainActivity.this);
        dialog.setContentView(R.layout.custodialog1);
        dialog.setTitle("Verification");
        dialog.setCancelable(false);

        // set the custom dialog components - text, image and button
        final EditText text = (EditText) dialog.findViewById(R.id.edt_code);

        Button dialogok = (Button) dialog.findViewById(R.id.btn_ok);
        Button dialogcancel = (Button) dialog.findViewById(R.id.btn_cancel);
        final TextView txt_resend = (TextView) dialog
                .findViewById(R.id.txt_resend);
        // if button is clicked, close the custom dialog
        dialogcancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                MainActivity.this.finish();
            }
        });
        txt_resend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialogok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = text.getText().toString();
                if (!(code == null) || (code.length() == 0)) {

                   // otp="123";

                    if (code.equals(otp)) {
                        approveuser();
                        // registerUser(name, email, password, mobile,android_id, comp,partneme);
                        dialog.dismiss();

                    } else {
                        //String toemail = pref.getString("useremail", null);
                        Toast.makeText(
                                getApplicationContext(),
                                "Enter the valid verification code sent to you mail id "
                                , Toast.LENGTH_LONG)
                                .show();
                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Enter the verification code",
                            Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }

    public void approveuser() {
        showDialog();

        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.deviceapprove, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                hidePDialog();
                String status = "", details = "";
                try {
                    JSONObject jObj = new JSONObject(response);
                    status = jObj.getString("status");
                    details = jObj.getString("details");

                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (status.equals("1")) {
                    hidePDialog();
                    editor.putString("username", name);
                    editor.putString("userid", UserId);
                    editor.putString("empbranch", Branch);
                    editor.commit();
                    Intent it = new Intent(MainActivity.this, MenuActivity.class);
                    startActivity(it);
                    finish();
                    Toast.makeText(MainActivity.this, details, Toast.LENGTH_SHORT).show();

                } else {
                    Toast.makeText(MainActivity.this, details, Toast.LENGTH_SHORT).show();
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
                //params.put("name", name);
                params.put("email", email);
                params.put("regid", android_id);
                System.out.println("email " + email);
                System.out.println("rgid " + android_id);

                return params;

            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }
}
