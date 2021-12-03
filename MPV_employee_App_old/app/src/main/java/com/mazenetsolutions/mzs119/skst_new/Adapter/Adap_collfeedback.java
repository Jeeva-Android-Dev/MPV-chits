package com.mazenetsolutions.mzs119.skst_new.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mazenetsolutions.mzs119.skst_new.Model.colfeedbackmodel;
import com.mazenetsolutions.mzs119.skst_new.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class Adap_collfeedback extends BaseAdapter {

    private Activity activity;
    private ArrayList<colfeedbackmodel> data;
    private static LayoutInflater inflater = null;
    Typeface tf;
    colfeedbackmodel tempValues = null;
    int i = 0;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat df_daily = new SimpleDateFormat("dd-MM-yyyy");
    Context mContext;

    public Adap_collfeedback(Activity a, ArrayList<colfeedbackmodel> d) {

        activity = a;
        data = d;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mContext = activity.getApplicationContext();

    }

    public int getCount() {

        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public colfeedbackmodel getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        public TextView txtname, txt_paytype, txtpending, txtcolected;
        // txtno,

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.billdetailsview, null);

            holder = new ViewHolder();
            holder.txtname = (TextView) vi.findViewById(R.id.txt_custname);
            holder.txtcolected = (TextView) vi.findViewById(R.id.txt_colectedamnt);
            holder.txt_paytype = (TextView) vi.findViewById(R.id.txt_paytype);
            //  holder.txtpending = (TextView) vi.findViewById(R.id.txt_pending);


            holder.txt_paytype.setTypeface(tf);
            //   holder.txtpending.setTypeface(tf);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (colfeedbackmodel) data.get(position);

            String name, paid, pending, advance;

            name = tempValues.getName();
            paid = tempValues.getCreatedon();
            advance = tempValues.getFeedback();
            //  pending = tempValues.getTotalenrlpending();


            // NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
            Locale curLocale = new Locale("en", "IN");
            Log.d("Collection Adapter", name);

/*
            try {
                String moneyString1 = formatter.format(Double.valueOf(paid));
                paid = moneyString1;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            try {
                if (pending.contains("-")) {
                    pending = "0.00";
                }
                String moneyString = formatter.format(Double.valueOf(pending));
                pending = moneyString;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            } */
            String dat = "";
            try {
                Date d = df.parse(tempValues.getCreatedon());
                dat = df_daily.format(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.txtname.setText(name);
            holder.txt_paytype.setText(dat);
            holder.txtcolected.setText(advance);
            //holder.txtpending.setText(pending);
        }
        return vi;
    }


}
