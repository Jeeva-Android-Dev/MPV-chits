package com.mazenetsolutions.mzs119.skst_new;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenetsolutions.mzs119.skst_new.Utils.AppController;
import com.mazenetsolutions.mzs119.skst_new.Utils.Config;
import com.mazenetsolutions.mzs119.skst_new.Utils.ConnectionDetector;



import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String url = Config.login;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    String fontPath = Config.FONTPATHMAIN;

    TextView login_title, login_title1;
    Button mEmailSignInButton, signup;
    private EditText mEmailView;
    private EditText mPasswordView;
    private ProgressDialog pDialog;
    String android_id = "";
    String email = "", otp = "", Photo_E = "";
    String name = "", UserId = "", Branch = "", B_city = "", B_address = "", B_District = "", B_pin = "", B_phone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();

        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        cd = new ConnectionDetector(this);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);


        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);
        signup = (Button) findViewById(R.id.email_signup_button);


        mEmailView.setText(pref.getString("email", ""));
        mPasswordView.setText(pref.getString("password", ""));
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, RgisterActivity.class);
                startActivity(i);
                finish();
            }
        });
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

        forceUpdate();
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


                        if (Success.equals("0")) {
                            hidePDialog();
                            Toast.makeText(MainActivity.this, Details, Toast.LENGTH_LONG).show();
                        } else if (Success.equals("1")) {
                            hidePDialog();

                            showotpdilog();
                            name = jObj.getString("username");
                            UserId = jObj.getString("UserId");
                            Branch = jObj.getString("Branch");
                            B_city = jObj.getString("B_city");
                            B_address = jObj.getString("B_Address");
                            B_District = jObj.getString("B_district");
                            B_pin = jObj.getString("B_pincode");
                            B_phone = jObj.getString("B_brnchphone");
                            otp = jObj.getString("otp");
                            Photo_E = jObj.getString("Photo_E");
                            System.out.println("otp " + otp);
                            // caccess = jObj.getString("Cust_access");
                            Toast.makeText(MainActivity.this, Details, Toast.LENGTH_LONG).show();

                        } else if (Success.equals("2")) {
                            hidePDialog();
                            String name = jObj.getString("username");
                            String UserId = jObj.getString("UserId");
                            String Branch = jObj.getString("Branch");
                            String B_city = jObj.getString("B_city");
                            String B_address = jObj.getString("B_Address");
                            String B_District = jObj.getString("B_district");
                            String B_pin = jObj.getString("B_pincode");
                            String B_phone = jObj.getString("B_brnchphone");
                            // caccess = jObj.getString("Cust_access");
                            editor.putString("username", name);
                            editor.putString("userid", UserId);
                            editor.putString("empbranch", Branch);
                            editor.putString("B_Address", B_address);
                            editor.putString("B_district", B_District);
                            editor.putString("B_pincode", B_pin);
                            editor.putString("B_brnchphone", B_phone);
                            editor.putString("B_city", B_city);
                            editor.putString("Photo_E",  jObj.getString("Photo_E"));
                            //editor.putString("caccess", caccess);
                            editor.commit();

                            Intent i = new Intent(MainActivity.this, MenuActivity.class);
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
                    hidePDialog();

                }
            }) {

                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("email", email);
                    params.put("password", password);
                    params.put("regid", android_id);
                    params.put("url", url);
                    System.out.println("regid " + android_id);
                    System.out.println("password " + password);
                    System.out.println("email " + email);
                    editor.putString("email", email);
                    editor.commit();
                    return params;
                }

            };
            movieReq.setRetryPolicy(new DefaultRetryPolicy(40000,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
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
        dialog.setContentView(R.layout.custodialog);
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
                    editor.putString("B_Address", B_address);
                    editor.putString("B_district", B_District);
                    editor.putString("B_pincode", B_pin);
                    editor.putString("B_brnchphone", B_phone);
                    editor.putString("B_city", B_city);
                    editor.putString("Photo_E", Photo_E);
                    //  editor.putString("caccess", caccess);
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
                System.out.println("regid " + android_id);

                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }

    public void forceUpdate(){
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo =  packageManager.getPackageInfo(getPackageName(),0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String currentVersion = packageInfo.versionName;
        new ForceUpdateAsync(currentVersion,MainActivity.this).execute();
    }


    public class ForceUpdateAsync extends AsyncTask<String, String, JSONObject> {

        private String latestVersion;
        private String currentVersion;
        private Context context;
        public ForceUpdateAsync(String currentVersion, Context context){
            this.currentVersion = currentVersion;
            this.context = context;
        }

        @Override
        protected JSONObject doInBackground(String... params) {

            try {

                latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=" + context.getPackageName()+ "&hl=en")
                        .timeout(30000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select("div.hAyfc:nth-child(4) > span:nth-child(2) > div:nth-child(1) > span:nth-child(1)")
                        .first()
                        .ownText();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if(latestVersion!=null){
                if(!currentVersion.equalsIgnoreCase(latestVersion)){
                    // Toast.makeText(context,"update is available.",Toast.LENGTH_LONG).show();
                    if(!(context instanceof SplashScreen)) {
                        if(!((Activity)context).isFinishing()){
                            showForceUpdateDialog();
                        }
                    }
                }
            }
            super.onPostExecute(jsonObject);
        }

        public void showForceUpdateDialog(){
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(new ContextThemeWrapper(context,
                    R.style.MyTheme));

            alertDialogBuilder.setTitle(context.getString(R.string.youAreNotUpdatedTitle));
            alertDialogBuilder.setMessage(context.getString(R.string.youAreNotUpdatedMessage) + " " + latestVersion + context.getString(R.string.youAreNotUpdatedMessage1));
            alertDialogBuilder.setCancelable(false);
            alertDialogBuilder.setPositiveButton(R.string.update, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + context.getPackageName())));
                    dialog.cancel();
                }
            });
            alertDialogBuilder.show();
        }
    }


}

