package com.mazenet.mzs119.skstowner;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;

import com.mazenet.mzs119.skstowner.Utils.Config;

public class MenuActivity extends AppCompatActivity {
    Button btn_approve, btn_cashinhand, btn_logout, btn_deviceapprove, btn_empouts;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        pref = getApplicationContext().getSharedPreferences(Config.preff, MODE_PRIVATE);
        editor = pref.edit();
        btn_approve = (Button) findViewById(R.id.btn_approval);
        btn_cashinhand = (Button) findViewById(R.id.btn_cashinhand);
        btn_logout = (Button) findViewById(R.id.btn_logout);
        btn_deviceapprove = (Button) findViewById(R.id.btn_deviceapprove);
        btn_empouts = (Button) findViewById(R.id.btn_empouts);
        btn_deviceapprove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MenuActivity.this, DeviceApproval.class);
                startActivity(i);
            }
        });
        btn_approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(MenuActivity.this, Ovrallactivity.class);
                startActivity(it);
            }
        });
        btn_empouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(MenuActivity.this, EmployeeOutstanding.class);
                startActivity(it);
            }
        });
        btn_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("username", "");
                editor.putString("userid", "");
                editor.putString("email", "");
                editor.putString("password", "");
                editor.commit();

                Intent irt = new Intent(MenuActivity.this, MainActivity.class);
                startActivity(irt);
                finish();
            }
        });
        btn_cashinhand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent irt = new Intent(MenuActivity.this, CashInHandActivity.class);
                startActivity(irt);
            }
        });

    }
}
