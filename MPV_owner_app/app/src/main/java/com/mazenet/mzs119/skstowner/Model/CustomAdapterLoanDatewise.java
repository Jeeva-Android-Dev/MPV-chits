package com.mazenet.mzs119.skstowner.Model;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
;

import com.mazenet.mzs119.skstowner.R;
import com.mazenet.mzs119.skstowner.Utils.Config;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class CustomAdapterLoanDatewise extends BaseAdapter {

    private Activity activity;
    private ArrayList<LoanModelDatewise> data;
    private static LayoutInflater inflater = null;
    String fontPath = Config.FONTPATHMAIN;
    Typeface tf;
    LoanModelDatewise tempValues = null;
    int i = 0;
    SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat timedfava = new SimpleDateFormat("hh:mm a");
    Context mContext;

    public CustomAdapterLoanDatewise(Activity a, ArrayList<LoanModelDatewise> d) {

        activity = a;
        data = d;
        inflater = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        mContext = activity.getApplicationContext();
        tf = Typeface.createFromAsset(mContext.getAssets(), fontPath);

    }

    public int getCount() {

        if (data.size() <= 0)
            return 1;
        return data.size();
    }

    public LoanModelDatewise getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        public TextView txtname, txt_date, txt_feedtime, txtcolected;
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
              holder.txt_date = (TextView) vi.findViewById(R.id.txt_date);
              holder.txt_feedtime = (TextView) vi.findViewById(R.id.txt_feedtime);


            //   holder.txtpaid.setTypeface(tf);
            holder.txtname.setTypeface(tf);
            holder.txtcolected.setTypeface(tf);
            //   holder.txtpending.setTypeface(tf);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (LoanModelDatewise) data.get(position);

            String name, paid, pending, advance;

            name = tempValues.getCust_name();
            // paid = tempValues.getEnrlpaid();
            advance = tempValues.getAmount();
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
            try {
                if (advance.contains("-")) {
                    advance = "0";
                }
                Double d = Double.parseDouble(advance);
                String moneyString2 = NumberFormat.getNumberInstance(curLocale).format(d);
                advance = moneyString2;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            advance = "Rs. " + advance;
            holder.txtname.setText(name);
            //  holder.txtpaid.setText(paid);
            holder.txtcolected.setText(advance);
            String tim="";
            try {
                Date d=timedf.parse(tempValues.getTime());
                tim=timedfava.format(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            paid = tempValues.getPaymode();
            if(paid.equalsIgnoreCase("daily.rct.adj"))
            {
                paid="Advance Adjustment";
            }else if(paid.equalsIgnoreCase("b.p.adj") || paid.equalsIgnoreCase("b.p.adjustment"))
            {
                paid="C.P Adjustment";
            }
            holder.txt_feedtime.setVisibility(View.VISIBLE);
            holder.txt_feedtime.setText(paid);
            holder.txt_date.setText(tempValues.getDate() + "\n(" + tim + ")");
            //holder.txtpending.setText(pending);
        }
        return vi;
    }


}
