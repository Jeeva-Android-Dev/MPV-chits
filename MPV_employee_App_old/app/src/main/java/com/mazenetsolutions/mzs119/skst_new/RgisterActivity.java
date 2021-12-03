package com.mazenetsolutions.mzs119.skst_new;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.provider.Settings;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenetsolutions.mzs119.skst_new.Utils.AppController;
import com.mazenetsolutions.mzs119.skst_new.Utils.Config;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RgisterActivity extends AppCompatActivity {
    EditText edt_name, edt_mob, edt_email, edt_pass;
    Button btn_register;
    String android_id, pstatus, status, details;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private ProgressDialog pDialog;
    private static final String TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgister);
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);

        edt_name = (EditText) findViewById(R.id.edt_rg_name);
        edt_mob = (EditText) findViewById(R.id.edt_rg_mobile);
        edt_email = (EditText) findViewById(R.id.edt_rg_mail);
        edt_pass = (EditText) findViewById(R.id.edt_rg_password);
        btn_register = (Button) findViewById(R.id.btn_rg_Register);

        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edt_name.getText().toString();
                String mob = edt_mob.getText().toString();
                String mail = edt_email.getText().toString();
                String pass = edt_pass.getText().toString();

                registeruser(name, mail, mob, pass);
            }
        });
    }

    private void registeruser(final String name, final String mail, final String mob, final String pass) {
        boolean cancel = false;
        View focusView = null;
        if (TextUtils.isEmpty(name)) {
            edt_name.setError("Invalid Pasword");
            focusView = edt_name;
            cancel = true;
        }

        if (TextUtils.isEmpty(mail)) {
            edt_email.setError("Enter Valid Email Id");
            focusView = edt_email;
            cancel = true;
        }
        if (TextUtils.isEmpty(mob)) {
            edt_mob.setError("Enter Valid Email Id");
            focusView = edt_mob;
            cancel = true;
        }

        if (TextUtils.isEmpty(pass)) {
            edt_pass.setError("Enter Valid Email Id");
            focusView = edt_pass;
            cancel = true;
        }


        if (cancel) {

            focusView.requestFocus();
        } else {
            showDialog();

            StringRequest movieReq = new StringRequest(Request.Method.POST,
                    Config.registeractivity, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    Log.d(TAG, response.toString());
                    hidePDialog();


                    try {
                        JSONObject jObj = new JSONObject(response);
                        JSONArray jaray = jObj.getJSONArray("result");

                        for (int i = 0; i < jaray.length(); i++) {
                            JSONObject obj = jaray.getJSONObject(i);
                            pstatus = String.valueOf(obj.getJSONObject("pstatus"));
                            status = String.valueOf(obj.getJSONObject("status"));
                            details = String.valueOf(obj.getJSONObject("details"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (pstatus.equals("1")) {
                        if (status.equals("1")) {
                            Toast.makeText(RgisterActivity.this, "Registration is Successful", Toast.LENGTH_SHORT).show();

                        }
                        else{
                            Toast.makeText(RgisterActivity.this, "Please Try Again after sometimes", Toast.LENGTH_SHORT).show();
                        }
                        Toast.makeText(RgisterActivity.this, "Your mobile no Does not match", Toast.LENGTH_SHORT).show();
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
                    params.put("name", name);
                    params.put("email", mail);
                    params.put("mobile", mob);
                    params.put("regid", android_id);
                    params.put("password", pass);

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
}