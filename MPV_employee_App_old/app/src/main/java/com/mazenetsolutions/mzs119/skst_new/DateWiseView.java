package com.mazenetsolutions.mzs119.skst_new;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenetsolutions.mzs119.skst_new.Adapter.CustomAdapterDatewise;
import com.mazenetsolutions.mzs119.skst_new.Model.DateWiseViewModel;
import com.mazenetsolutions.mzs119.skst_new.Utils.AppController;
import com.mazenetsolutions.mzs119.skst_new.Utils.Config;
import com.mazenetsolutions.mzs119.skst_new.Utils.ConnectionDetector;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class DateWiseView extends AppCompatActivity {
    DatePickerDialog datePickerDialog;
    DateFormat df, df2;
    TextView txt_date;
    LinearLayout laydate;
    RelativeLayout homelay;
    ListView viewlist;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    ConnectionDetector cd;
    private ProgressDialog pDialog;
    private static final String TAG = MainActivity.class.getSimpleName();
    String url = Config.getallviewbydate;
    ArrayList<DateWiseViewModel> listmain = new ArrayList<>();
    CustomAdapterDatewise adapterDatewise;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date_wise_view);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();

        cd = new ConnectionDetector(this);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);


        df = new SimpleDateFormat("dd-MM-yyyy");
        df2 = new SimpleDateFormat("MM/dd/yyyy", Locale.ENGLISH);
        txt_date = (TextView) findViewById(R.id.datetext);
        laydate = (LinearLayout) findViewById(R.id.datelay);
        homelay = (RelativeLayout) findViewById(R.id.lay_view_home);
        viewlist = (ListView) findViewById(R.id.list_view_datewise);
        showcal();

        viewlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                DateWiseViewModel dvm = listmain.get(i);
//               Intent it = new Intent(DateWiseView.this, PrintActivityDatewise.class);
//                it.putExtra("Cusname", dvm.getFirst_Name_F());
//                it.putExtra("Amount", dvm.getTotal_Amount());
//                it.putExtra("Groupname", dvm.getGroup_Id());
//                it.putExtra("ticketno", dvm.getGroup_Ticket_Id());
//                it.putExtra("paymode", dvm.getPayment_Type());
//                it.putExtra("pendingamnt", dvm.getPending_Amt());
//                it.putExtra("advance", dvm.getAdvance_Amt());
//                it.putExtra("Receiptno", dvm.getReceipt_No());
//                startActivity(it);
            }
        });


    }

    public void getall() {
        showDialog();
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                url, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, response.toString());
                System.out.println("rf "+response.toString());
                hidePDialog();

                listmain.clear();
                try {
                    JSONObject jobj = new JSONObject(response);
                    String jsonob = jobj.getString("result");

                    if (jsonob.equals("0")) {
                        viewlist.setVisibility(View.GONE);
                        Toast.makeText(DateWiseView.this, "No receipts Available", Toast.LENGTH_SHORT).show();
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
                                dvm.setPending_Amt(jObj.getString("Pending_Amt"));
                                dvm.setTotal_Enrl_Paid(jObj.getString("Total_Enrl_Paid"));
                                dvm.setTotal_Enrl_Pending(jObj.getString("Total_Enrl_Pending"));
                                dvm.setAdvance_Amt(jObj.getString("Advance_Amt"));
                                listmain.add(dvm);

                            }
                            if (listmain.size() > 0) {
                                adapterDatewise = new CustomAdapterDatewise(DateWiseView.this, listmain);
                                adapterDatewise.notifyDataSetChanged();
                                viewlist.setAdapter(adapterDatewise);
                                viewlist.setVisibility(View.VISIBLE);
                            } else {
                                listmain.clear();
                                viewlist.setVisibility(View.GONE);
                                Toast.makeText(DateWiseView.this, "No receipts Available", Toast.LENGTH_SHORT).show();

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
                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Emp_Id", pref.getString("userid", ""));
                System.out.println(pref.getString("userid", ""));

                try {
                    System.out.println(String.valueOf(df2.format(df.parse(txt_date.getText().toString()))));
                    params.put("Date", String.valueOf(df2.format(df.parse(txt_date.getText().toString()))));

                } catch (Exception e) {
                    e.printStackTrace();
                }
                System.out.println(txt_date.getText().toString());

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(90000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }

    public void showcal() {
        listmain.clear();
        laydate.setVisibility(View.GONE);
        Calendar newCalendar = Calendar.getInstance();
        datePickerDialog = new DatePickerDialog(DateWiseView.this,
                AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                Calendar calendar = Calendar.getInstance();

                newDate.set(year, monthOfYear, dayOfMonth);
                calendar.set(year, monthOfYear, dayOfMonth);
                try {
                    String str_frodate = df.format(newDate.getTime());
                    txt_date.setText(str_frodate);
                    laydate.setVisibility(View.VISIBLE);
                    homelay.setVisibility(View.VISIBLE);
                    getall();

                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }, newCalendar.get(Calendar.YEAR),
                newCalendar.get(Calendar.MONTH),
                newCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.setTitle("Date");
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000 * 24);
        datePickerDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.viewmenu, menu);
        return super.onCreateOptionsMenu(menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_calender:
                listmain.clear();
                laydate.setVisibility(View.GONE);
                Calendar newCalendar = Calendar.getInstance();
                datePickerDialog = new DatePickerDialog(DateWiseView.this,
                        AlertDialog.THEME_DEVICE_DEFAULT_LIGHT, new DatePickerDialog.OnDateSetListener() {

                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {
                        Calendar newDate = Calendar.getInstance();
                        Calendar calendar = Calendar.getInstance();

                        newDate.set(year, monthOfYear, dayOfMonth);
                        calendar.set(year, monthOfYear, dayOfMonth);
                        try {
                            String str_frodate = df.format(newDate.getTime());
                            txt_date.setText(str_frodate);
                            laydate.setVisibility(View.VISIBLE);
                            homelay.setVisibility(View.VISIBLE);
                            getall();

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }, newCalendar.get(Calendar.YEAR),
                        newCalendar.get(Calendar.MONTH),
                        newCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.setTitle("Date");
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000 * 24);
                datePickerDialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
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
