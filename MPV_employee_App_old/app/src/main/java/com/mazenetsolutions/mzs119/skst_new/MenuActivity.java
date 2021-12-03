package com.mazenetsolutions.mzs119.skst_new;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.mazenetsolutions.mzs119.skst_new.Database.Databasecustomers;
import com.mazenetsolutions.mzs119.skst_new.Database.Databasefeedback;
import com.mazenetsolutions.mzs119.skst_new.Database.Databaserecepit;
import com.mazenetsolutions.mzs119.skst_new.Model.Custmodel;
import com.mazenetsolutions.mzs119.skst_new.Model.Enrollmodel;
import com.mazenetsolutions.mzs119.skst_new.Model.FeedbackModel;
import com.mazenetsolutions.mzs119.skst_new.Model.TempEnrollModel;
import com.mazenetsolutions.mzs119.skst_new.Model.locadvancemodel;
import com.mazenetsolutions.mzs119.skst_new.Utils.AppController;
import com.mazenetsolutions.mzs119.skst_new.Utils.Config;
import com.mazenetsolutions.mzs119.skst_new.Utils.ConnectionDetector;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MenuActivity extends AppCompatActivity {

    //    Button btn_addcustomer, btn_addenrollment, btn_collection, btn_logout, btn_outstanding, btn_viewcol, btn_cashinhand;
    ConstraintLayout btn_collection, btn_cashinhand, btn_sync, btn_viewcol,btn_add_subscriber;
    CircleImageView btn_logout,profile_image;
    ArrayList<TempEnrollModel> tempreceipt = new ArrayList<>();
    ArrayList<TempEnrollModel> advancetempreceipt = new ArrayList<>();
    ArrayList<locadvancemodel> advances = new ArrayList<>();
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Databaserecepit dbrecepit;
    ConnectionDetector cd;
    ArrayList<Enrollmodel> groupreceipt = new ArrayList<>();
    ArrayList<Enrollmodel> groupreceiptmain = new ArrayList<>();
    String url = Config.reteriveindienroll;
    String url2 = Config.saverecepit;
    String Enroll_update_cust = Config.Enroll_update_cust;
    private ProgressDialog pDialog;
    int MY_PERMISSION_ACCESS_COURSE_LOCATION = 1;
    private PendingIntent pendingIntent;
    private AlarmManager manager;
    Databasecustomers dbcust;
    Databasefeedback dbfeed;
    public ArrayList<Enrollmodel> enroll_list_local = new ArrayList<Enrollmodel>();
    public ArrayList<Custmodel> customer_list = new ArrayList<Custmodel>();
    public ArrayList<Custmodel> customer_listmain = new ArrayList<Custmodel>();
    public ArrayList<FeedbackModel> feedlist = new ArrayList<FeedbackModel>();
    public ArrayList<FeedbackModel> feedlistmain = new ArrayList<FeedbackModel>();
    TextView txt_empname, txt_branchname, txt_notifibanner;
    String todaydate = "", tomorowdate = "", android_id = "";
    SimpleDateFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homelayout);
        df = new SimpleDateFormat("dd");
        txt_empname = (TextView) findViewById(R.id.txt_empname);
        txt_branchname = (TextView) findViewById(R.id.txt_branchname);
        txt_notifibanner = (TextView) findViewById(R.id.txt_notifibanner);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();
        android_id = Settings.Secure.getString(this.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        cd = new ConnectionDetector(this);
//        btn_addenrollment.setVisibility(View.GONE);
        txt_empname.setText(pref.getString("username", "Employee").replace(" ", ""));
        txt_branchname.setText(pref.getString("B_district", "Branch"));
//        Intent alarmIntent = new Intent(MenuActivity.this, AlarmReceiver.class);
        // btn_addcustomer = (Button) findViewById(R.id.btn_addcustomer);
        //  btn_addenrollment = (Button) findViewById(R.id.btn_addenrollment);
//        btn_collection = (Button) findViewById(R.id.btn_collection);
//        //   btn_outstanding = (Button) findViewById(R.id.btn_outstanding);
//        btn_viewcol = (Button) findViewById(R.id.btn_viewCol);
//        btn_logout = (Button) findViewById(R.id.btn_logout);
//        btn_cashinhand = (Button) findViewById(R.id.btn_cashinhand);
        if (cd.isConnectedToInternet()) {
            approvedevice(pref.getString("username", ""), android_id);
        } else {

        }

        btn_collection = (ConstraintLayout) findViewById(R.id.btn_collection);
        btn_sync = (ConstraintLayout) findViewById(R.id.btn_sync);
        btn_logout = (CircleImageView) findViewById(R.id.btn_logout);
        profile_image = (CircleImageView) findViewById(R.id.profile_image);
        btn_cashinhand = (ConstraintLayout) findViewById(R.id.btn_cashinhand);
        btn_add_subscriber = (ConstraintLayout) findViewById(R.id.btn_add_subscriber);
        btn_viewcol = (ConstraintLayout) findViewById(R.id.btn_viewCol);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();
        cd = new ConnectionDetector(this);
        pDialog = new ProgressDialog(this, R.style.MyTheme);
        pDialog.setCancelable(false);
        pDialog.setIndeterminate(true);
        pDialog.setProgressStyle(android.R.style.Widget_ProgressBar_Small);
        pDialog.getWindow().setGravity(Gravity.CENTER);
        dbrecepit = new Databaserecepit(MenuActivity.this);
        dbcust = new Databasecustomers(this);
        dbfeed = new Databasefeedback(this);
        Date c = Calendar.getInstance().getTime();
        todaydate = df.format(c);
        System.out.println("Current time => " + todaydate);

        btn_add_subscriber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(getApplicationContext(),Add_subscriber.class);
                startActivity(i);
            }
        });


        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, 1);
        Date resultdate = new Date(now.getTimeInMillis());
        tomorowdate = df.format(resultdate);
        System.out.println("then date : " + tomorowdate);
        if (cd.isConnectedToInternet()) {
         //   forceUpdate();
            get_advances();

            try {
                System.out.println(pref.getString("Photo_E",""));
                String path=pref.getString("Photo_E","");
                String aUrl;

                if(!path.startsWith("https")){
                    aUrl = path.replace("http", "https");

                }else {
                    aUrl = path.replace("http", "http");

                }

             //    aUrl = pref.getString("Photo_E","").replace("http", "https");
                Picasso.with(getApplicationContext()).load(aUrl).resize(150, 150).onlyScaleDown()
                        .placeholder(getResources().getDrawable(R.drawable.ic_profile)
                        ).centerInside().into(profile_image);
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CONTACTS) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSION_ACCESS_COURSE_LOCATION);
            }


        }
//        Intent alarmIntent = new Intent(MenuActivity.this, AlarmReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//           pendingIntent.cancel();

//        GPSTracker gps = new GPSTracker(this);
//
//        if (gps.canGetLocation()) {
//
//            manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
////              manager.cancel(pendingIntent);
//
////            int interval = 600000;
//            int interval = 1200;
//
//            manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
//
//
//        } else {
//            gps.showSettingsAlert();
//        }
    /*    btn_addcustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, AddCustomer.class);
                startActivity(i);
            }
        });
        btn_addenrollment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, AddEnrollment.class);
                startActivity(i);
            }
        }); */
        btn_viewcol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, ViewCollection.class);
                startActivity(it);
            }
        });
        btn_collection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, CollectionActivity.class);
                startActivity(it);
            }
        });
        btn_cashinhand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, CashInHandActivity.class);
                startActivity(it);
            }
        });
        btn_sync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cd.isConnectedToInternet()) {
                    uploadoffline();
                } else {
                    Toast.makeText(MenuActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
                }

            }
        });

        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        if (cd.isConnectedToInternet()) {
            uploadoffline();
        } else {
            Toast.makeText(MenuActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
    }

    private void loggedout() {
        editor.putString("username", "");
        editor.putString("userid", "");
        editor.putString("email", "");
        editor.putString("password", "");
        editor.putString("empbranch", "");
        editor.putString("B_Address", "");
        editor.putString("B_district", "");
        editor.putString("B_pincode", "");
        editor.putString("B_brnchphone", "");
        editor.putString("B_city", "");
        editor.putInt("dailycheckdaymain", 0);
        editor.putInt("dailycheckmonthmain", 0);
        editor.putInt("dailycheckdayfeed", 0);
        editor.putInt("dailycheckmonthfeed", 0);
        editor.commit();
        Toast.makeText(this, "You have been logged out. Contact Owner", Toast.LENGTH_LONG).show();
        Intent irt = new Intent(MenuActivity.this, MainActivity.class);
        startActivity(irt);
        finish();
    }

    private void logout() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MenuActivity.this, R.style.MyAlertDialogStyleDeclined);
        alertDialog.setTitle("Exit");
        alertDialog
                .setMessage("Do you wish to Logout ?");

        alertDialog.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {
                        editor.putString("username", "");
                        editor.putString("userid", "");
                        editor.putString("email", "");
                        editor.putString("password", "");
                        editor.putString("empbranch", "");
                        editor.putString("B_Address", "");
                        editor.putString("B_district", "");
                        editor.putString("B_pincode", "");
                        editor.putString("B_brnchphone", "");
                        editor.putString("B_city", "");
                        editor.putInt("dailycheckdaymain", 0);
                        editor.putInt("dailycheckmonthmain", 0);
                        editor.putInt("dailycheckdayfeed", 0);
                        editor.putInt("dailycheckmonthfeed", 0);
                        editor.commit();
                        dbrecepit.deletetableaLLREC();
                        dbcust.deletetable();
                        dbrecepit.deletetableadvview();
                        dbrecepit.deletetableview();
                        Intent irt = new Intent(MenuActivity.this, MainActivity.class);
                        startActivity(irt);
                        finish();
                    }
                });
        alertDialog.setNegativeButton("no",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        dialog.cancel();
                    }
                });
        alertDialog.show();
    }

    public void uploadoffline() {

        if (cd.isConnectedToInternet()) {
            get_version();
        } else {
            Toast.makeText(MenuActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
        tempreceipt = dbrecepit.getAllTempenroll();
        if (tempreceipt.size() > 0) {
            String cusid = "";

            for (int i = 0; i < tempreceipt.size(); i++) {
                System.out.println("receipt post entry");
                TempEnrollModel tem = tempreceipt.get(i);
                String enrollid = tem.getEnrollid();
                String bonusamnt = tem.getBonus_Amt();
                String paidamnt = tem.getPaid_Amt();
                String pendingamnt = tem.getPending_Amt();
                String penaltyamnt = tem.getPenalty_Amt();
                String Group = tem.getGroup_Name();
                String ticketno = tem.getGroup_Ticket_Name();
                String payableamnt = tem.getInsamt();
                String Remark = tem.getRemark();
                String chitvalue = tem.getScheme();
                String paytype = tem.getPaytype();
                String cus_branch = tem.getCusbranch();
                String pendingdays = tem.getPendingdys();
                String status = tem.getStatus();
                cusid = tem.getCusid();
                String cusname = tem.getCusname();
                String cheqno = tem.getChequeNo();
                String cheqdate = tem.getChequeDate();
                String cheqbank = tem.getChequeBank();
                String cheqbranch = tem.getChequeBranch();
                String transno = tem.getTransNo();
                String rtgsdate = tem.getTransDate();
                String recdate = tem.getRecdate();
                String rectime = tem.getRectime();
                postentry(enrollid, bonusamnt, paidamnt, pendingamnt, penaltyamnt, Group, ticketno, payableamnt, Remark, chitvalue, paytype, cus_branch, pendingdays, status, cusid, cusname, cheqno, cheqdate, cheqbank, cheqbranch, transno, rtgsdate, recdate,rectime);
            }
            //updateenroll(cusid);
            tempreceipt.clear();
            dbrecepit.deletetabletemp();


        } else {
            Toast.makeText(MenuActivity.this, "No Offline Receipt Entries to Upload", Toast.LENGTH_SHORT).show();
        }


        advancetempreceipt = dbrecepit.getAllAdvanceTempenroll();
        if (advancetempreceipt.size() > 0) {

            for (int i = 0; i < advancetempreceipt.size(); i++) {
                System.out.println("receipt post entry");
                TempEnrollModel tem = advancetempreceipt.get(i);
                String enrollid = tem.getEnrollid();
                String Remark = tem.getRemark();
                String paytype = tem.getPaytype();
                String cus_branch = tem.getCusbranch();
                String status = tem.getStatus();
                String cusid = tem.getCusid();
                String cusname = tem.getCusname();
                String cheqno = tem.getChequeNo();
                String cheqdate = tem.getChequeDate();
                String cheqbank = tem.getChequeBank();
                String cheqbranch = tem.getChequeBranch();
                String transno = tem.getTransNo();
                String rtgsdate = tem.getTransDate();
                String recptamnt = tem.getAmount();
                String recdate = tem.getRecdate();
                String rectime = tem.getRectime();
                dailycollection(recptamnt, cheqno, cheqdate, cheqbranch, cheqbank, transno, rtgsdate, cusid, enrollid, Remark, paytype, recdate,rectime);
            }
            advancetempreceipt.clear();
            dbrecepit.deletetableadvtemp();
        } else {
            Toast.makeText(MenuActivity.this, "No Offline Advance Receipt Entries to Upload", Toast.LENGTH_SHORT).show();
        }
    }

    public void dailycollection(final String receeiptamnt, final String chequno, final String chedate, final String chebranch, final String chebank, final String transno, final String transdate, final String cusid, final String enrollid, final String remark, final String paymode, final String recdate,final String time) {
        showDialog();
//        System.out.println(" daily col  " + str_amnttype);
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.dailyreceipt, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("customer Activity", response.toString());
                System.out.println(" daily col  " + response.toString());

                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.getString("status");
                    String receipt = object.getString("receipt");
                    String details = object.getString("details");

                    if (status.equalsIgnoreCase("1")) {
                        System.out.println("receipt  " + receipt);
                        hidePDialog();
//                        Toast.makeText(MenuActivity.this, details, Toast.LENGTH_SHORT).show();
//
                    } else {
//                        btn_submit.setVisibility(View.VISIBLE);
//                        builddilogerror(details);
//                        Toast.makeText(AdvanceIndividualReceipt.this, details, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(),error.getMessage(), Toast.LENGTH_SHORT).show();
                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Branchid", pref.getString("empbranch", ""));
                params.put("cust_id", cusid);
                params.put("enrlid", enrollid);
                params.put("userid", pref.getString("userid", ""));
                params.put("pay_mode", paymode);
                params.put("recpt_amnt", receeiptamnt);
                params.put("cheque_no", chequno);
                params.put("cheque_date", chedate);
                params.put("cheque_brnch", chebranch);
                params.put("cheque_bank", chebank);
                params.put("trn_no", transno);
                params.put("trn_date", transdate);
                params.put("createdby", pref.getString("username", ""));
                params.put("updatedby", pref.getString("username", ""));
                params.put("remark", remark);
                params.put("recdate", recdate);
                params.put("rectime", time);
                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.mainmenu, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.menu_gofline:
                if (cd.isConnectedToInternet()) {
                    get_advances();
                    reterivelocalreceipts();
                    getcustomers();
                    reterivefeedback();
                } else {
                    Toast.makeText(MenuActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
                }

                return true;
//            case R.id.menu_syncnow:
//                if (cd.isConnectedToInternet()) {
//                    uploadoffline();
//                } else {
//                    Toast.makeText(MenuActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
//                }
//
//                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void postentry(final String enrollidd, final String bonusamt, final String paidamt, final String pendingamt, final String penaltyamt, final String Group, final String ticketno, final String payableamount, final String Remark, final String chitvalue, final String paytype, final String Cus_Branch, final String Pending_Days, final String status, final String cusid, final String cusname, final String str_cheno, final String str_chedate, final String str_chebank, final String str_chebranch, final String str_tranno, final String str_rtgsdate, final String recdate,final String time) {

        showDialog();
        StringRequest menureq = new StringRequest(Request.Method.POST,
                url2, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                hidePDialog();


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
                params.put("enrollid", enrollidd);
                params.put("bonusamt", bonusamt);
                params.put("paidamt", paidamt);
                params.put("pendingamt", pendingamt);
                params.put("penaltyamt", "0");
                params.put("Groupid", Group);
                params.put("ticketno", ticketno);
                params.put("payamount", payableamount);
                params.put("Emp_Id", pref.getString("userid", "0"));
                params.put("Created_By", pref.getString("username", "DEMO"));
                params.put("Remark", Remark);
                params.put("Customer_Name", cusname);
                params.put("Chit_Value", chitvalue);
                params.put("Payment_Type", paytype);
                params.put("Cheque_No", str_cheno);
                params.put("Cheque_Date", str_chedate);
                params.put("Bank_Name", str_chebank);
                params.put("Branch_Name", str_chebranch);
                params.put("Trn_No", str_tranno);
                params.put("Trn_Date", str_rtgsdate);
                params.put("Cus_Branch", Cus_Branch);
                params.put("Pending_Days", Pending_Days);
                params.put("Emp_Branch", pref.getString("empbranch", "3"));
                params.put("status", status);
                params.put("recdate", recdate);
                params.put("rectime", time);



                return params;
            }

        };
        menureq.setRetryPolicy(new DefaultRetryPolicy(40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(menureq);
    }

    //=============================================================================================================================
    public void updateenroll(String cuid) {
        System.out.println("receipt post ");
        showDialog();
        final String cusid = cuid;
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Enroll_update_cust, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                hidePDialog();


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("custid", cusid);

                return params;
            }

        };

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

    // check version on play store and force update
    public int forceUpdate() {
        PackageManager packageManager = this.getPackageManager();
        PackageInfo packageInfo = null;
        try {
            packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return packageInfo.versionCode;
//        System.out.println(currentVersion);
//        new ForceUpdateSync(currentVersion, this).execute();
    }

    public void showForceUpdateDialog(final String latestVersion) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                MenuActivity.this);
        alertDialog.setTitle(getApplicationContext().getString(R.string.youAreNotUpdatedTitle));
        alertDialog
                .setMessage(getApplicationContext().getString(R.string.youAreNotUpdatedMessage) + " " + latestVersion + " " + getApplicationContext().getString(R.string.youAreNotUpdatedMessage1) + "\nPlease Sync all unsaved Receipts before Updating");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,
                                        int which) {

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + getPackageName())));
                        dialog.cancel();

                    }
                });
        alertDialog.setNegativeButton("I need to Sync", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (cd.isConnectedToInternet()) {
                    uploadoffline();
                } else {
                    Toast.makeText(MenuActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
                }

            }
        });
        alertDialog.show();
    }

    public void get_version() {
        StringRequest localreq = new StringRequest(Request.Method.POST,
                Config.get_version, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());


                try {

                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("versions");

                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);
                            String version = jObj.getString("version");
                            String detail = jObj.getString("detail");
                            String code = jObj.getString("code");
                            if (Integer.parseInt(code) <= forceUpdate()) {
                                System.out.println("details " + String.valueOf(forceUpdate()));
                            } else {
                                showForceUpdateDialog(version);
                                System.out.println("details " + detail);
                            }

                        }
                    } catch (JSONException e) {
                        hidePDialog();
                        e.printStackTrace();
                    }

                } catch (JSONException e) {
                    hidePDialog();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hidePDialog();
                VolleyLog.d("Activity", "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });

//        {
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("userid", pref.getString("userid", ""));
//
//                return params;
//            }


        localreq.setRetryPolicy(new DefaultRetryPolicy(5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(localreq);
    }

    public void get_advances() {
        showDialog();
//        dbrecepit.deletetableadvance();
        StringRequest localreq = new StringRequest(Request.Method.POST,
                Config.get_advances, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());


                try {

                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("advance");


                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);
                            locadvancemodel sched = new locadvancemodel();
                            sched.setCusid(jObj.getString("Cust_Id"));
                            sched.setEnrlid(jObj.getString("Enl_Id"));
                            sched.setAmnt(jObj.getString("Rect_Amt"));

                            advances.add(sched);

                        }
                    } catch (JSONException e) {
                        hidePDialog();
                        e.printStackTrace();
                    }

//                            locadvancemodel ml=advances.get(i);
                    if (advances.size() > 0) {

                        try {
                            dbrecepit.deletetableadvance();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dbrecepit.addalladvance(advances);
                        hidePDialog();
                    }


                } catch (Exception e) {
                    hidePDialog();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                hidePDialog();

            }
        });

//        {
//
//            @Override
//            protected Map<String, String> getParams() {
//                Map<String, String> params = new HashMap<String, String>();
////                params.put("userid", pref.getString("userid", ""));
//
//                return params;
//            }


        localreq.setRetryPolicy(new DefaultRetryPolicy(30000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(localreq);
    }

    private void reterivelocalreceipts() {

        showDialog();
        System.out.println("enrollments");
        enroll_list_local.clear();
        StringRequest localreq = new StringRequest(Request.Method.POST,
                Config.retrievealluser, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("enrollment resp " + response);

                try {

                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("customer");

                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);

                            Enrollmodel sched = new Enrollmodel();
                            sched.setEnrollid(jObj.getString("enrollid"));
                            sched.setCusid(jObj.getString("Cust_Id"));
                            sched.setScheme(jObj.getString("chitvalue"));
                            sched.setPending_Amt(jObj.getString("pendingamnt"));
                            sched.setPaid_Amt(jObj.getString("paidamnt"));
                            sched.setPenalty_Amt("0");
                            sched.setBonus_Amt("0");
                            sched.setGroup_Name(jObj.getString("grpname"));
                            sched.setPendingdys(jObj.getString("pendingdays"));
                            sched.setCusbranch(pref.getString("empbranch", ""));
                            sched.setGroup_Ticket_Name(jObj.getString("grpticketno"));
                            sched.setPayamount("0");
                            sched.setGrpid(jObj.getString("grpid"));
                            sched.setAdvanceamnt(jObj.getString("advanceamnt"));
                            enroll_list_local.add(sched);
                        }
                    } catch (JSONException e) {
                        hidePDialog();
                        e.printStackTrace();
                    }

                    if (enroll_list_local.size() > 0) {

                        try {
                            dbrecepit.deletetableaLLREC();
                        } catch (Exception e) {
                            e.printStackTrace();
                            hidePDialog();
                        }

                        try {
                            dbrecepit.addallreceipt(enroll_list_local);
                            hidePDialog();
                            AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                    MenuActivity.this, R.style.MyAlertDialogStyleApproved);
                            alertDialog.setTitle("Success");
                            alertDialog.setCancelable(false);
                            alertDialog
                                    .setMessage("Offline Database Downloaded");

                            alertDialog.setPositiveButton("ok",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog,
                                                            int which) {
                                            dialog.cancel();


                                        }
                                    });
                            alertDialog.show();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        hidePDialog();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                MenuActivity.this);
                        alertDialog.setTitle("Information");
                        alertDialog
                                .setMessage("No Data from Server  locall. contact Admin");

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
                    hidePDialog();
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", pref.getString("userid", ""));

                return params;
            }

        };
        localreq.setRetryPolicy(new DefaultRetryPolicy(40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(localreq);
    }

    public void getcustomers() {

        showDialog();

        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.reteriveusers, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                System.out.println("resp " + response.toString());
                hidePDialog();


                try {

                    customer_list.clear();


                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("customer");

                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);


                            Custmodel sched = new Custmodel();
                            sched.setCusid(jObj.getString("Id"));
                            sched.setCustomer_id(jObj.getString("Customer_Id"));
                            sched.setNAME(jObj.getString("First_Name_F"));
                            sched.setMOBILE(jObj.getString("Mobile_F"));
                            sched.setAdvanceamt(jObj.getString("Advanced_Amt"));
                            sched.setPendingamt(jObj.getString("Pending_Amt"));
                            sched.setTotalenrlpending(jObj.getString("Total_Enrl_Pending"));
                            sched.setEnrlpaid(jObj.getString("Total_Enrl_Paid"));
                            sched.setLevel(jObj.getString("Level"));
                            sched.setBonusamt(jObj.getString("Bonus_Amt"));
                            sched.setPenaltyamt(jObj.getString("Penalty_Amt"));
                            sched.setPendingamt(jObj.getString("Pending_Days"));
                            sched.setLastdate(jObj.getString("lastdate"));
                            sched.setTomorowdate(jObj.getString("tomorrow"));
                            customer_list.add(sched);

                        }
                    } catch (JSONException e) {
                        hidePDialog();
                        e.printStackTrace();
                    }


                    if (customer_list.size() > 0) {


                        try {
                            dbcust.deletetable();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {


                            customer_listmain.clear();
                            ArrayList<Custmodel> level1 = new ArrayList<>();
                            ArrayList<Custmodel> level2 = new ArrayList<>();
                            ArrayList<Custmodel> level3 = new ArrayList<>();
                            ArrayList<Custmodel> todaylist = new ArrayList<>();
                            ArrayList<Custmodel> yesterdaylist = new ArrayList<>();

                            for (int i = 0; i < customer_list.size(); i++) {
                                Custmodel c = customer_list.get(i);
                                String level = c.getLevel();
                                String tod = c.getLastdate();
                                String yes = c.getTomorowdate();
                                if (tod.equalsIgnoreCase(todaydate)) {
                                    yesterdaylist.add(c);
                                } else {
                                    if (tod.equalsIgnoreCase(tomorowdate)) {
                                        todaylist.add(c);
                                    } else {
                                        level3.add(c);
                                    }
                                }
//                                if (level.equalsIgnoreCase("Awaiting Payment")) {
//
//                                    level1.add(c);
//                                } else if (level.equalsIgnoreCase("Customer, Guarantor Letter") || level.equalsIgnoreCase("Customer Letter")) {
//
//                                    level2.add(c);
//                                } else {
//
//                                    level3.add(c);
//                                }

                            }
                            customer_listmain.addAll(yesterdaylist);
                            customer_listmain.addAll(todaylist);
                            customer_listmain.addAll(level3);
                            level1.clear();
                            level2.clear();
                            level3.clear();
                            yesterdaylist.clear();
                            todaylist.clear();
                            dbcust.addcustomer(customer_listmain);
                            customer_list.clear();
                            try {

                                Calendar newCalendar = Calendar.getInstance();

                                int ddd = newCalendar.get(Calendar.DAY_OF_MONTH);
                                int wwww = newCalendar.get(Calendar.WEEK_OF_YEAR);

                                if (ddd == pref.getInt("dailycheckdaymain", 0) && wwww == pref.getInt("dailycheckmonthmain", 0) && dbcust.getContactsCount() != 0) {
//                                    reterivelocal();

                                } else {

                                    editor.putInt("dailycheckdaymain",
                                            newCalendar.get(Calendar.DAY_OF_MONTH));
                                    editor.putInt("dailycheckmonthmain",
                                            newCalendar.get(Calendar.WEEK_OF_YEAR));

                                    System.out.println("aplied");
                                    editor.commit();
//                                    reterivefeedback();
                                    System.out.println("aplied 2 " + pref.getInt("dailycheckdaymain", 0));
                                }

                            } catch (Exception e) {
                                hidePDialog();
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            hidePDialog();
                            e.printStackTrace();
                        }


                    } else {
                        hidePDialog();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                MenuActivity.this, R.style.MyDialogTheme);
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
                    hidePDialog();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("userid", pref.getString("userid", "0"));
                System.out.println("userid " + pref.getString("userid", "0"));

                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(40000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    public void reterivefeedback() {

        showDialog();

        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.reterivefeedback, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("Collection Activity", response.toString());
                hidePDialog();


                try {

                    feedlist.clear();


                    JSONObject object = new JSONObject(response);
                    JSONArray ledgerarray = object.getJSONArray("customer");

                    try {
                        for (int i = 0; i < ledgerarray.length(); i++) {
                            JSONObject jObj = ledgerarray.getJSONObject(i);


                            FeedbackModel sched = new FeedbackModel();
                            sched.setName(jObj.getString("Feedback"));
                            sched.setFollowup(jObj.getString("Follow_up"));


                            feedlist.add(sched);

                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    if (feedlist.size() > 0) {


                        try {
                            dbfeed.deletetable();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        try {

                            dbfeed.addfeedback(feedlist);
                            try {

                                Calendar newCalendar = Calendar.getInstance();

                                int ddd1 = newCalendar.get(Calendar.DAY_OF_MONTH);
                                int wwww1 = newCalendar.get(Calendar.WEEK_OF_YEAR);

                                if (ddd1 == pref.getInt("dailycheckdayfeed", 0) && wwww1 == pref.getInt("dailycheckmonthfeed", 0) && dbfeed.getContactsCount() != 0) {
//                                    reterivelocalfeed();

                                } else {

                                    editor.putInt("dailycheckdayfeed",
                                            newCalendar.get(Calendar.DAY_OF_MONTH));
                                    editor.putInt("dailycheckmonthfeed",
                                            newCalendar.get(Calendar.WEEK_OF_YEAR));
                                    System.out.println("aplied");
                                    editor.commit();
//                                    reterivefeedback();
                                    System.out.println("aplied 1 " + pref.getInt("dailycheckdayfeed", 0));
                                }

                            } catch (Exception e) {
                                hidePDialog();
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            hidePDialog();
                            e.printStackTrace();
                        }

                    } else {
                        hidePDialog();
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                                MenuActivity.this);
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
                    hidePDialog();
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Activity", "Error: " + error.getMessage());
//                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                // params.put("compname", pref.getString("company", ""));


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

    @Override
    protected void onResume() {
        if (cd.isConnectedToInternet()) {
            get_version();
        } else {
            Toast.makeText(MenuActivity.this, "No Internet Connection !", Toast.LENGTH_SHORT).show();
        }
        approvedevice(pref.getString("username", ""), android_id);
        super.onResume();
    }

    private void approvedevice(final String email, final String regiid) {
        System.out.println("approving");
        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.get_registration, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println("repso " + response);
                //hidePDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String Success = jObj.getString("status");
                    String Details = jObj.getString("details");
                    try {
                        if (Success.equals("1")) {
                            // posttoserver();
                            Toast.makeText(MenuActivity.this, Details, Toast.LENGTH_LONG).show();
                        } else {
                            loggedout();
                            Toast.makeText(MenuActivity.this, Details, Toast.LENGTH_LONG).show();
                        }

                        // retrivedetails();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // VolleyLog.d(TAG, "Error: " + error.getMessage());
                Toast.makeText(MenuActivity.this,
                        error.getMessage(), Toast.LENGTH_SHORT).show();
                // hidePDialog();

            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("name", email);
                params.put("regid", regiid);
                System.out.println("emil" + email);
                System.out.println("reg " + regiid);
                return params;
            }

        };
        movieReq.setRetryPolicy(new DefaultRetryPolicy(
                50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);


    }
}