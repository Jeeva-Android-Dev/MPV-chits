package com.mazenet.mzs119.mpvmemberapp;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.BaseSliderView.OnSliderClickListener;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.mazenet.mzs119.mpvmemberapp.Sliders.ChildAnimationExample;
import com.mazenet.mzs119.mpvmemberapp.Sliders.SliderLayout;
import com.mazenet.mzs119.mpvmemberapp.Utils.Constants;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;

public class MenuActivity extends AppCompatActivity implements OnSliderClickListener {
    SharedPreferences pref;
    SharedPreferences.Editor editor;
    GridLayout grid;
    TextView txt_welcome;
    RelativeLayout layout_mychits, layout_myacntcopy, layout_myouts, layout_profile, layout_newchits;
    SliderLayout slider;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_logout:
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(
                        MenuActivity.this, R.style.MyAlertDialogStyleApproved);
                alertDialog.setTitle("Information");
                alertDialog.setCancelable(false);
                alertDialog
                        .setMessage("Do you want to Logout?");

                alertDialog.setPositiveButton("yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.cancel();
                                logout();
                            }
                        });
                alertDialog.setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                            }
                        });
                alertDialog.show();

                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        forceUpdate();
        pref = getApplicationContext().getSharedPreferences(Constants.preference, MODE_PRIVATE);
        editor = pref.edit();
        getSupportActionBar().setTitle("Dashboard");
        txt_welcome = (TextView) findViewById(R.id.txt_welcome);
        layout_myacntcopy = (RelativeLayout) findViewById(R.id.layout_myacntcopy);
        layout_mychits = (RelativeLayout) findViewById(R.id.layout_mychits);
        layout_myouts = (RelativeLayout) findViewById(R.id.layout_myouts);
        layout_profile = (RelativeLayout) findViewById(R.id.layout_profile);
        layout_newchits = (RelativeLayout) findViewById(R.id.layout_newchits);
        slider = (SliderLayout) findViewById(R.id.slider);
        txt_welcome.setText("Hi "+pref.getString("Customer_name", "").replace(" ",""));
        layout_myacntcopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, MYAccountCopy.class);
                startActivity(i);
            }
        });
        layout_mychits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, MyChits.class);
                startActivity(i);
            }
        });
        layout_myouts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, MyOutstandings.class);
                startActivity(i);
            }
        });
        layout_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, MyProfile.class);
                startActivity(i);
            }
        });
        layout_newchits.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(MenuActivity.this, NewCommencedChits.class);
                startActivity(i);
            }
        });
        HashMap<String, Integer> file_maps = new HashMap<String, Integer>();
        file_maps.put("1", R.drawable.chitimg1);
        file_maps.put("2", R.drawable.chitimg2);
        file_maps.put("3", R.drawable.chitimg3);


        for (String name : file_maps.keySet()) {
            TextSliderView textSliderView = new TextSliderView(MenuActivity.this);
            // initialize a SliderLayout
            textSliderView
                    //  .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(com.daimajia.slider.library.SliderTypes.BaseSliderView.ScaleType.CenterInside)
                    .setOnSliderClickListener(this);


            textSliderView.bundle(new Bundle());
            textSliderView.getBundle().putString("extra", name);

            slider.addSlider(textSliderView);

        }

        slider.setPresetTransformer(SliderLayout.Transformer.Default);
        slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        slider.setCustomAnimation(new ChildAnimationExample());
        slider.setDuration(3000);

    }

    private void logout() {
        editor.putString("loginStatus", "0");
        editor.putString("custtblid", "");
        editor.putString("customer_id", "");
        editor.putString("Customer_name", "");
        editor.putString("Customer_email", "");
        editor.putString("Customer_address", "");
        editor.putString("Branch_Name", "");
        editor.putString("Branch_Address", "");
        editor.commit();
        Intent irt = new Intent(MenuActivity.this, SplashScreen.class);
        startActivity(irt);
        finish();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

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
       new ForceUpdateAsync(currentVersion,MenuActivity.this).execute();
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
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(new ContextThemeWrapper(context,
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
