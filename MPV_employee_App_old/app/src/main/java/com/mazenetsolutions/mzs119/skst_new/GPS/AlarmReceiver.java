package com.mazenetsolutions.mzs119.skst_new.GPS;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mazenetsolutions.mzs119.skst_new.Database.AttenDB;
import com.mazenetsolutions.mzs119.skst_new.Model.GPSModel;
import com.mazenetsolutions.mzs119.skst_new.Utils.AppController;
import com.mazenetsolutions.mzs119.skst_new.Utils.Config;
import com.mazenetsolutions.mzs119.skst_new.Utils.ConnectionDetector;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by admin on 15/10/2016.
 */
public class AlarmReceiver extends BroadcastReceiver {
    AttenDB db;
    ConnectionDetector cd;
    ArrayList<GPSModel> lst = new ArrayList<GPSModel>();
//    String sendlocalityoffline = Constants.sendlocalityoffline;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context tttt;

    @Override
    public void onReceive(Context arg0, Intent arg1) {
        // For our recurring task, we'll just display a message
//         Toast.makeText(arg0, "I'm running", Toast.LENGTH_SHORT).show();
        GPSTracker gps = new GPSTracker(arg0);
        tttt = arg0;
        pref = arg0.getSharedPreferences(Config.preff, arg0.MODE_PRIVATE);
        editor = pref.edit();

        if (gps.canGetLocation()) {
            db = new AttenDB(arg0);
            cd = new ConnectionDetector(arg0);

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();

            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            String formattedDate = df.format(c.getTime());
            SimpleDateFormat df1 = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate1 = df1.format(c.getTime());

            GPSModel schec = new GPSModel();

            schec.setName(pref.getString("empid", "0"));
            schec.setLon(String.valueOf(longitude));
            schec.setLat(String.valueOf(latitude));
            schec.setTime(formattedDate);
            schec.setDatee(formattedDate1);

            ArrayList<GPSModel> test = new ArrayList<GPSModel>();
            test.clear();
            test.add(schec);

            db.addattedence(test);

            if (cd.isConnectedToInternet()) {
                lst.clear();
                lst = db.getnotposted();

                for (int i = 0; i < lst.size(); i++) {
                    GPSModel sched = lst.get(i);

                    try {
                        db.upcontact(sched.getId());
//                        sendemail(sched.getLat(), sched.getLon(), sched.getName(), sched.getTime(), sched.getId(), sched.getDatee());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
            else
            {
                lst.clear();
                lst = db.getnotposted();

                for (int i = 0; i < lst.size(); i++) {
                    GPSModel sched = lst.get(i);

                    try {
                        db.upcontact(sched.getId());
//                        sendemail(sched.getLat(), sched.getLon(), sched.getName(), sched.getTime(), sched.getId(), sched.getDatee());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }

    }


    public void sendemail(final String latitude, final String longitude, final String name, final String time, final String Id, final String datee) {


        StringRequest movieReq = new StringRequest(Request.Method.POST,
                Config.sendlocalityoffline, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                try {
                    // Toast.makeText(tttt, "updated", Toast.LENGTH_SHORT).show();

                    db.upcontact(Id);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {


            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();

                params.put("latitude", latitude);
                params.put("longitude", longitude);
                params.put("name", name);
                params.put("time", time);
                params.put("datee", datee);


                return params;
            }

        };

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }

}
