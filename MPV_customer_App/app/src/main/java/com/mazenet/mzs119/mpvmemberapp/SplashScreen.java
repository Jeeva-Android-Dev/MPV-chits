package com.mazenet.mzs119.mpvmemberapp;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.mazenet.mzs119.mpvmemberapp.Utils.ConnectionDetector;
import com.mazenet.mzs119.mpvmemberapp.Utils.Constants;

public class SplashScreen extends AppCompatActivity {
    private static int SPLASH_TIME_OUT = 1000;
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    private String android_id;
    int MY_PERMISSION_ACCESS_COURSE_LOCATION = 1;
    ConnectionDetector cd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        cd = new ConnectionDetector(this);
        pref = getApplicationContext().getSharedPreferences(Constants.preference,
                MODE_PRIVATE);
        editor = pref.edit();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(SplashScreen.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                    ) {


                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        if (cd.isConnectedToInternet()) {
                            if (pref.getString("loginStatus", "").equalsIgnoreCase("0")) {
                                Intent it = new Intent(SplashScreen.this, MainActivity.class);
                                //it.putExtra("withcus","no");
                                startActivity(it);
                                finish();
                            } else if (pref.getString("loginStatus", "").equalsIgnoreCase("1")) {
                                Intent it = new Intent(SplashScreen.this, MenuActivity.class);
                                startActivity(it);
                                finish();

                            } else if (pref.getString("loginStatus", "").equalsIgnoreCase("2")) {
                                Intent it = new Intent(SplashScreen.this, MainActivity.class);
                                startActivity(it);
                                finish();
                            }
                            else{
                                Intent it = new Intent(SplashScreen.this, MainActivity.class);
                                startActivity(it);
                                finish();
                            }


                        } else {
                            Toast.makeText(SplashScreen.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                        }
                    }

                }, SPLASH_TIME_OUT);


            } else {
                ActivityCompat.requestPermissions(SplashScreen.this,
                        new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.WRITE_CONTACTS, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.CALL_PHONE, Manifest.permission.CAMERA},
                        MY_PERMISSION_ACCESS_COURSE_LOCATION);

            }


        } else {


            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {

                    if (cd.isConnectedToInternet()) {
                        if (pref.getString("loginStatus", "").equalsIgnoreCase("0")) {
                            Intent it = new Intent(SplashScreen.this, ChangePassword.class);
                            startActivity(it);
                            finish();
                        } else if (pref.getString("loginStatus", "").equalsIgnoreCase("1")) {
                            Intent it = new Intent(SplashScreen.this, MenuActivity.class);
                            startActivity(it);
                            finish();

                        } else if (pref.getString("loginStatus", "").equalsIgnoreCase("2")) {
                            Intent it = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(it);
                            finish();
                        }
                        else{
                            Intent it = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(it);
                            finish();
                        }


                    } else {
                        Toast.makeText(SplashScreen.this, "No Internet Connection!", Toast.LENGTH_SHORT).show();
                    }

                }
            }, SPLASH_TIME_OUT);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {

        Intent it = new Intent(SplashScreen.this, SplashScreen.class);
        startActivity(it);
        finish();

    }
}
