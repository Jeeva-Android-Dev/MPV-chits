package com.mazenet.mzs119.mpvmemberapp;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;


import androidx.core.app.NotificationCompat;

import com.mazenet.mzs119.mpvmemberapp.Utils.Constants;

import static android.content.Context.NOTIFICATION_SERVICE;
 
/**
 * Created by Belal on 12/8/2017.
 */
 
public class MyNotificationManager {
 
    private Context mCtx;
    private static MyNotificationManager mInstance;
    GlobalValue globalValue;
 
    private MyNotificationManager(Context context) {
        mCtx = context;
    }
 
    public static synchronized MyNotificationManager getInstance(Context context) {

        if (mInstance == null) {
            mInstance = new MyNotificationManager(context);
        }
        return mInstance;
    }
 
    public void displayNotification(String title, String body) {
        globalValue=new GlobalValue(mCtx);
 
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(mCtx, Constants.CHANNEL_ID)
                        .setSmallIcon(R.drawable.mpvlogo)
                        .setContentTitle(title)
                        .setContentText(body);
 
 
        /*
        *  Clicking on the notification will take us to this intent
        *  Right now we are using the MainActivity as this is the only activity we have in our application
        *  But for your project you can customize it as you want
        * */
 
        Intent resultIntent;

        if(globalValue.getString("user_role").equalsIgnoreCase("User")){
            resultIntent = new Intent(mCtx, MyChits.class);

        }else {
            resultIntent = new Intent(mCtx, MyChits.class);

        }

        PendingIntent pendingIntent = PendingIntent.getActivity(mCtx, 0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
 
        /*
        *  Setting the pending intent to notification builder
        * */
 
        mBuilder.setContentIntent(pendingIntent);
 
        NotificationManager mNotifyMgr =
                (NotificationManager) mCtx.getSystemService(NOTIFICATION_SERVICE);
 
        /*
        * The first parameter is the notification id
        * better don't give a literal here (right now we are giving a int literal)
        * because using this id we can modify it later
        * */
        if (mNotifyMgr != null) {
            mNotifyMgr.notify(1, mBuilder.build());
        }
    }
 
}