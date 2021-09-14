package com.mazenet.mzs119.mpvmemberapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
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

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.mazenet.mzs119.mpvmemberapp.Utils.AppController;
import com.mazenet.mzs119.mpvmemberapp.Utils.Config;
import com.mazenet.mzs119.mpvmemberapp.Utils.ConnectionDetector;
import com.mazenet.mzs119.mpvmemberapp.Utils.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    Button btn_login, btn_signup;
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    ConstraintLayout fingerprint_layout, passwordlay;
    EditText edt_userid, edt_password;
    boolean cancel = false;
    View focusView = null;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    TextView txt_forgetpass;
    String device_id="xss";
    String Status = "", Customer_name = "", customer_id = "", custtblid = "", Branch_Name = "", Branch_city = "", customer_address = "", Branch_Address = "", Branch_district = "", Branch_pincode = "", Branch_Phone = "", Branch_State = "", Customer_email = "", Details = "", userid = "", pass = "";
    private static final String TAG = MainActivity.class.getSimpleName();
     String cusid="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edt_userid = (EditText) findViewById(R.id.edt_userid);
        edt_password = (EditText) findViewById(R.id.edt_password);
        btn_login = (Button) findViewById(R.id.btn_ma_login);
        txt_forgetpass = (TextView) findViewById(R.id.txt_forgetpassword);
        btn_signup = (Button) findViewById(R.id.btn_ma_signup);
        fingerprint_layout = (ConstraintLayout) findViewById(R.id.fingerprint_layout);
        passwordlay = (ConstraintLayout) findViewById(R.id.passwordlay);
        pref = getApplicationContext().getSharedPreferences(Constants.preference, MODE_PRIVATE);
        editor = pref.edit();
        forceUpdate();

//        FirebaseInstanceId.getInstance().getInstanceId()
//                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
//                    @Override
//                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
//                        if (!task.isSuccessful()) {
//                            Log.w("FIREBASE", "getInstanceId failed", task.getException());
//                            return;
//                        }
//
//                        // Get new Instance ID token
//                        String token = task.getResult().getToken();
//                        device_id = task.getResult().getToken();
//
//                        // Log and toast
//                        String msg ="FIREBASE"+token;
//                        Log.d("FIREBASE_token", msg);
//                        //   Toast.makeText(login.this, msg, Toast.LENGTH_SHORT).show();
//                    }
//                });
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
         device_id=refreshedToken;

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btn_login.getText().equals(getResources().getString(R.string.signin))) {
                    login();
                } else {


                    String id = edt_userid.getText().toString();
                    if (TextUtils.isEmpty(id)) {
                        edt_userid.setError("Enter User-ID or Mobile No.");
                        return;
                    } else {
                        btn_login.setEnabled(false);

                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                btn_login.setEnabled(true);

                            }
                        }, 1000);

                        editor.putString("customer_id", id);
                        editor.commit();
                       getotp();
                    }
                }

            }
        });
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordlay.setVisibility(View.INVISIBLE);
                btn_login.setVisibility(View.INVISIBLE);
                txt_forgetpass.setVisibility(View.INVISIBLE);
                String userid = edt_userid.getText().toString();
                if (TextUtils.isEmpty(userid)) {
                    edt_userid.setError("Enter User-ID or Mobile No.");
                } else {
                    signup(userid);
                }
            }
        });
        edt_password.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                btn_signup.setVisibility(View.GONE);

            }

            @Override
            public void afterTextChanged(Editable s) {
                String tex = edt_password.getText().toString();
                if (!TextUtils.isEmpty(tex)) {
                    btn_signup.setVisibility(View.GONE);
                } else {
                    btn_signup.setVisibility(View.VISIBLE);
                }
            }
        });
        txt_forgetpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                passwordlay.setVisibility(View.INVISIBLE);
                btn_login.setText("Submit");
                txt_forgetpass.setVisibility(View.INVISIBLE);
                fingerprint_layout.setVisibility(View.INVISIBLE);
            }
        });



        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);

                   // displayFirebaseRegId();

                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                  //  txtMessage.setText(message);
                }
            }
        };


    }

    public void login() {
        cancel = false;
        edt_password.setError(null);
        edt_userid.setError(null);
        userid = edt_userid.getText().toString();
        pass = edt_password.getText().toString();

        if (TextUtils.isEmpty(userid)) {
            edt_userid.setError("Enter User Id");
            focusView = edt_userid;
            cancel = true;
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            edt_password.setError("Enter Password");
            focusView = edt_password;
            cancel = true;
            return;
        }
        if (cancel) {
            focusView.requestFocus();
        } else {
            System.out.println("lob ");
            loginvoly();
        }
    }

    private void signup(final String userid) {

        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Constants.signup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                System.out.println(response);



                    try {
                        JSONObject jObj = new JSONObject(response);
                        Status = jObj.getString("Status");
                        Details = jObj.getString("Details");
                        if (Status.equals("0")) {
                            cusid = jObj.getString("cusid");
                            System.out.println("cusid signup "+cusid);
                            editor.putString("customer_id", userid);
                            editor.putString("cusid", cusid);
                            editor.putString("loginStatus", Status);
                            editor.commit();
                            System.out.println("cusid signup pref "+pref.getString("cusid", ""));
                            Toast.makeText(MainActivity.this, Details, Toast.LENGTH_SHORT).show();
                            getotp();
//                            Intent i = new Intent(MainActivity.this, ChangePassword.class);
//                            i.putExtra("withcus", "cusid");
//                            startActivity(i);

                        } else if (Status.equals("1")) {
                            btn_login.setVisibility(View.VISIBLE);
                            passwordlay.setVisibility(View.VISIBLE);
                            txt_forgetpass.setVisibility(View.VISIBLE);
                            Toast.makeText(MainActivity.this, Details, Toast.LENGTH_SHORT).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(),"Customer Not Exists",Toast.LENGTH_SHORT).show();

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
                params.put("Custid", userid);
                return params;
            }



        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    public void loginvoly() {
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Constants.login, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                System.out.println(response);

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jarray = jsonObject.getJSONArray("result");
                    try {
                        for (int i = 0; i < jarray.length(); i++) {
                            JSONObject jObj = jarray.getJSONObject(i);
                            Status = jObj.getString("Status");
                            Details = jObj.getString("Details");
                            customer_id = jObj.getString("Customer_Id");
                            System.out.println("deta " + Details);
                            custtblid = jObj.getString("Customer_tableid");
                            Customer_name = jObj.getString("Customer_name");
                            Customer_email = jObj.getString("Customer_email");
                            Branch_Name = jObj.getString("Branch_Name");
                            customer_address = jObj.getString("Customer_address");
                            Branch_Address = jObj.getString("Branch_Address");
                            // Branch_district = jObj.getString("Branch_district");
                            // Branch_pincode = jObj.getString("Branch_pincode");
                            //  Branch_Phone = jObj.getString("Branch_Phone");
                            // Branch_State = jObj.getString("Branch_State");

                        }

                    } catch (Exception e) {

                    }
                    if (Status.equals("0")) {
                        editor.putString("customer_id", customer_id);
                        editor.putString("loginStatus", Status);
                        editor.commit();
                        System.out.println("deta 0 " + Details);
                       getotp();
                        Toast.makeText(MainActivity.this, Details, Toast.LENGTH_SHORT).show();

                    } else if (Status.equals("1")) {


                        System.out.println("deta 1" + Details);
                        System.out.println("deta " + Details);
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
                        Intent i = new Intent(MainActivity.this, MenuActivity.class);
                        startActivity(i);
                        finish();
                        Toast.makeText(MainActivity.this, Details, Toast.LENGTH_SHORT).show();

                    } else if (Status.equals("2")) {
                        System.out.println("deta 2" + Details);
                        editor.putString("loginStatus", Status);
                        editor.commit();
                        Toast.makeText(MainActivity.this, Details, Toast.LENGTH_SHORT).show();

                    } else if (Status.equals("3")) {

                        editor.putString("loginStatus", Status);
                        editor.commit();
                        Toast.makeText(MainActivity.this, Details, Toast.LENGTH_SHORT).show();

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
                params.put("Custid", userid);
                params.put("Password", pass);
                params.put("fcm_id", device_id);
                System.out.println("userid " + userid);
                System.out.println("fcm_id " + device_id);
                System.out.println("param " + params);
                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
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
                        String OTP = jObj.getString("OTP");
                        System.out.println("otp " + OTP);
                        editor.putString("OTP", OTP);
                        editor.putString("cusid", jObj.getString("id"));
                        editor.commit();
                        Toast.makeText(MainActivity.this, Details, Toast.LENGTH_SHORT).show();
                        Intent i = new Intent(MainActivity.this, ChangePassword.class);
                        i.putExtra("withcus", "no");
                        startActivity(i);


                    } else if (Status.equals("1")) {
                        Toast.makeText(MainActivity.this, Details, Toast.LENGTH_SHORT).show();
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
                    if(!((Activity)context).isFinishing()){
                        showForceUpdateDialog();
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
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // register GCM registration complete receiver
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(Config.REGISTRATION_COMPLETE));
//
//        // register new push message receiver
//        // by doing this, the activity will be notified each time a new message arrives
//        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
//                new IntentFilter(Config.PUSH_NOTIFICATION));
//
//        // clear the notification area when the app is opened
//        NotificationUtils.clearNotifications(getApplicationContext());
//    }

//    @Override
//    protected void onPause() {
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
//        super.onPause();
//    }
}
