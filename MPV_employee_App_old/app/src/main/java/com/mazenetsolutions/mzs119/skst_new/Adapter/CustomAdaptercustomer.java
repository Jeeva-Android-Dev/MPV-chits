package com.mazenetsolutions.mzs119.skst_new.Adapter;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mazenetsolutions.mzs119.skst_new.Model.Custmodel;
import com.mazenetsolutions.mzs119.skst_new.R;
import com.mazenetsolutions.mzs119.skst_new.Utils.Config;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class CustomAdaptercustomer extends BaseAdapter {

    private Activity activity;
    private ArrayList<Custmodel> data;
    private static LayoutInflater inflater = null;
    String fontPath = Config.FONTPATHMAIN;
    Typeface tf;
    Custmodel tempValues = null;
    int i = 0;
    SimpleDateFormat df;
    String todaydate = "", yesterday = "";
    Context mContext;
    ObjectAnimator colorAnim;

    public CustomAdaptercustomer(Activity a, ArrayList<Custmodel> d) {

        activity = a;
        data = d;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mContext = activity.getApplicationContext();
        tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);
        df = new SimpleDateFormat("dd");
        Date c = Calendar.getInstance().getTime();
        todaydate = df.format(c);
        System.out.println("Current time => " + todaydate);

        Calendar now = Calendar.getInstance();
        now.add(Calendar.DATE, 1);
        Date resultdate = new Date(now.getTimeInMillis());
        yesterday = df.format(resultdate);
        System.out.println("then date : " + yesterday);

    }

    public int getCount() {

        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public Custmodel getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        public TextView txtname, txtpaid, txtpending, txtadvance, txt_vip, txt_vip1, txt_vip2, txt_vip3, txt_collectdatetoday, txt_collectdatetom;
        // txtno,

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.billdetails1, null);

            holder = new ViewHolder();
            holder.txtname = (TextView) vi.findViewById(R.id.txt_name);
            holder.txtadvance = (TextView) vi.findViewById(R.id.txt_Advance);
            holder.txtpaid = (TextView) vi.findViewById(R.id.txt_paid);
            holder.txtpending = (TextView) vi.findViewById(R.id.txt_pending);
            holder.txt_vip = (TextView) vi.findViewById(R.id.txt_vip);
            holder.txt_vip1 = (TextView) vi.findViewById(R.id.txt_vip1);
            holder.txt_vip2 = (TextView) vi.findViewById(R.id.txt_vip2);
            holder.txt_vip3 = (TextView) vi.findViewById(R.id.txt_vip3);
            holder.txt_collectdatetoday = (TextView) vi.findViewById(R.id.txt_collectdatetoday);
            holder.txt_collectdatetom = (TextView) vi.findViewById(R.id.txt_collectdatetom);
//            blink(holder.txt_vip1);

            holder.txtpaid.setTypeface(tf);
            holder.txtname.setTypeface(tf);
            holder.txtadvance.setTypeface(tf);
            holder.txtpending.setTypeface(tf);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (Custmodel) data.get(position);

            String name, paid, pending, advance;

            name = tempValues.getNAME();
            paid = tempValues.getEnrlpaid();
            // advance = tempValues.getAdvanceamt();
            pending = tempValues.getTotalenrlpending();


            //NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());

            Locale curLocale = new Locale("en", "IN");
//            Log.d("Collection Adapter", name);

            try {
                Double d = Double.parseDouble(paid);
                String moneyString1 = NumberFormat.getNumberInstance(curLocale).format(d);
                paid = moneyString1;
                paid = "Rs. " + paid;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            try {
                if (pending.contains("-")) {
                    pending = "0";
                }
                Double d = Double.parseDouble(pending);
                String moneyString = NumberFormat.getNumberInstance(curLocale).format(d);
                pending = moneyString;
                pending = "Rs. " + pending;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
         /*   try {
                if (advance.contains("-")) {
                    advance = "0.00";
                }
                String moneyString2 = formatter.format(Double.valueOf(advance));
                advance = moneyString2;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }*/
//            ((TextView)findViewById(R.id.text)).setText(Html.fromHtml("X<sup>2</sup>"));
            blinktext(holder.txt_collectdatetoday);
            if (tempValues.getLastdate().equalsIgnoreCase(todaydate)) {
                holder.txt_collectdatetoday.setText("Can be Collected Today");
                holder.txt_collectdatetoday.setVisibility(View.VISIBLE);
                holder.txt_collectdatetom.setVisibility(View.GONE);
            } else {
//                holder.txt_collectdatetoday.setVisibility(View.GONE);
//                holder.txt_collectdatetom.setVisibility(View.GONE);
//                Date userDob = null;
//                try {
//                    userDob = df.parse(todaydate);
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//
//                long diff = 0;
//                try {
//                    diff = df.parse(tempValues.getLastdate()).getTime() - userDob.getTime();
//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }
//                int numOfDays = (int) (diff / (1000 * 60 * 60 * 24));
//                int hours = (int) (diff / (1000 * 60 * 60));
//                int minutes = (int) (diff / (1000 * 60));
//                int seconds = (int) (diff / (1000));
//                System.out.println("hours diff " + String.valueOf(hours));
//                if(hours<=24)
//                { holder.txt_collectdatetom.setText("Can be Collected Tommorrow");
//                holder.txt_collectdatetom.setVisibility(View.VISIBLE);
//                holder.txt_collectdatetoday.setVisibility(View.GONE);
//                }
//                else
//                {
//                    holder.txt_collectdatetoday.setVisibility(View.GONE);
//                holder.txt_collectdatetom.setVisibility(View.GONE);
//                }
                if (tempValues.getLastdate().equalsIgnoreCase(yesterday)) {
                    holder.txt_collectdatetom.setText("Can be Collected Tommorrow");
                    holder.txt_collectdatetom.setVisibility(View.VISIBLE);
                    holder.txt_collectdatetoday.setVisibility(View.GONE);
                } else {
                    holder.txt_collectdatetoday.setVisibility(View.GONE);
                    holder.txt_collectdatetom.setVisibility(View.GONE);
                }
            }

//            if (tempValues.getTomorowdate().equalsIgnoreCase(yesterday)) {
//                holder.txt_collectdatetom.setText("Can be Collected Tommorrow");
//                holder.txt_collectdatetom.setVisibility(View.VISIBLE);
//                holder.txt_collectdatetoday.setVisibility(View.GONE);
//            } else {
//                holder.txt_collectdatetoday.setVisibility(View.GONE);
//                holder.txt_collectdatetom.setVisibility(View.GONE);
//            }
//            if (tempValues.getLevel().equalsIgnoreCase("Awaiting Payment")) {
//
////                SpannableString styledString = new SpannableString("VIP");
////                styledString.setSpan(new RelativeSizeSpan(0.8f), 0, 3, 0);
////                styledString.setSpan(new SuperscriptSpan(), 0, 3, 0);
////                holder.txtname.setText(name);
////                holder.txt_vip.setText(styledString);
//                holder.txt_vip1.setVisibility(View.VISIBLE);
//                holder.txt_vip2.setVisibility(View.GONE);
//                holder.txt_vip3.setVisibility(View.GONE);
//                holder.txt_vip1.setText("LEVEL 1");
//                holder.txt_vip3.setText("");
//                holder.txt_vip2.setText("");
//
//            } else if (tempValues.getLevel().equalsIgnoreCase("Customer, Guarantor Letter") || tempValues.getLevel().equalsIgnoreCase("Customer Letter")) {
//                holder.txt_vip1.setVisibility(View.GONE);
//                holder.txt_vip2.setVisibility(View.VISIBLE);
//                holder.txt_vip3.setVisibility(View.GONE);
//                holder.txt_vip3.setText("");
//                holder.txt_vip1.setText("");
//                holder.txt_vip2.setText("LEVEL 2");
//
//            } else if (tempValues.getLevel().equalsIgnoreCase("Arbitration / Court") || tempValues.getLevel().equalsIgnoreCase("Legal Noticer") || tempValues.getLevel().trim().equalsIgnoreCase("")) {
//
//                holder.txt_vip1.setVisibility(View.GONE);
//                holder.txt_vip2.setVisibility(View.GONE);
//                holder.txt_vip3.setVisibility(View.VISIBLE);
//                holder.txt_vip3.setText("LEVEL 3");
//                holder.txt_vip2.setText("");
//                holder.txt_vip1.setText("");
//
//            } else {
//                holder.txt_vip1.setVisibility(View.GONE);
//                holder.txt_vip2.setVisibility(View.GONE);
//                holder.txt_vip3.setVisibility(View.GONE);
//                holder.txt_vip3.setText("");
//                holder.txt_vip2.setText("");
//                holder.txt_vip1.setText("");
//            }
            holder.txtname.setText(name);
            holder.txtpaid.setText(paid);
            //holder.txtadvance.setText(advance);
            holder.txtpending.setText(pending);
        }
        return vi;
    }

    private void blink(final TextView txt) {
        final Handler handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                int timeToBlink = 100;    //in milissegunds
                try {
                    Thread.sleep(timeToBlink);
                } catch (Exception e) {
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {

                        if (txt.getVisibility() == View.VISIBLE) {
                            txt.setVisibility(View.INVISIBLE);
                        } else {
                            txt.setVisibility(View.VISIBLE);
                        }
                        blink(txt);
                    }
                });
            }
        }).start();
    }

    private void blinktext(TextView txt) {
        colorAnim = ObjectAnimator.ofInt(txt, "textColor", Color.GREEN, Color.TRANSPARENT); //you can change colors
        colorAnim.setDuration(400); //duration of flash
        colorAnim.setEvaluator(new ArgbEvaluator());
        colorAnim.setRepeatCount(ValueAnimator.INFINITE);
        colorAnim.setRepeatMode(ValueAnimator.REVERSE);
        colorAnim.start();
    }

}
