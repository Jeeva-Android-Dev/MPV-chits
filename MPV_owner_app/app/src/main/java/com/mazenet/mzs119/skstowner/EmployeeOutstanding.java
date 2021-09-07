package com.mazenet.mzs119.skstowner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenet.mzs119.skstowner.Adapter.CustomAdapterAgentlist;
import com.mazenet.mzs119.skstowner.Adapter.CustomAdapterAgentouts;
import com.mazenet.mzs119.skstowner.Model.AgentModel;
import com.mazenet.mzs119.skstowner.Model.AgentOutsModel;
import com.mazenet.mzs119.skstowner.Utils.AppController;
import com.mazenet.mzs119.skstowner.Utils.Config;
import com.mazenet.mzs119.skstowner.Utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EmployeeOutstanding extends AppCompatActivity {
    EditText edt_cxash;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    ListView agentlist;
    ArrayList<AgentOutsModel> alist = new ArrayList<>();
    CustomAdapterAgentouts adapterlist;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_outstanding);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();
        cd = new ConnectionDetector(this);
        agentlist = (ListView) findViewById(R.id.list_outsagent);
        getallagent();
        agentlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                AgentOutsModel aom = alist.get(position);
                Intent i = new Intent(EmployeeOutstanding.this, EmployeeWiseOuts.class);
                i.putExtra("empid", aom.getEmpid());
                i.putExtra("empname", aom.getEmpname());
                startActivity(i);
            }
        });

    }

    public void getallagent() {

        final Locale curLocale = new Locale("en", "IN");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.getallreport, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());


                try {


                    JSONObject object = new JSONObject(response);
                    JSONArray arra = object.getJSONArray("result");
                    try {
                        for (int i = 0; i < arra.length(); i++) {
                            JSONObject jobj = arra.getJSONObject(i);

                            AgentOutsModel avmod = new AgentOutsModel();
                            avmod.setEmpid(jobj.getString("Collect_Emp"));
                            avmod.setEmpname(jobj.getString("empname"));
                            avmod.setPaid(jobj.getString("paid"));
                            avmod.setPending(jobj.getString("pending"));
                            alist.add(avmod);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    adapterlist = new CustomAdapterAgentouts(EmployeeOutstanding.this, alist);
                    adapterlist.notifyDataSetChanged();
                    agentlist.setAdapter(adapterlist);
                    agentlist.setVisibility(View.VISIBLE);


                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_SHORT).show();

            }
        })

        {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);

    }
}
