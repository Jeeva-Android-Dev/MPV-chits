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

import com.mazenetsolutions.mzs119.skst_new.Model.TempEnrollModel;
import com.mazenetsolutions.mzs119.skst_new.R;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;


public class CustomAdapterLoanDatewise extends BaseAdapter {

    private Activity activity;
    private ArrayList<TempEnrollModel> data;
    private static LayoutInflater inflater = null;
    Typeface tf;
    TempEnrollModel tempValues = null;
    int i = 0;
    SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat timedfava = new SimpleDateFormat("hh:mm a");
    Context mContext;

    public CustomAdapterLoanDatewise(Activity a, ArrayList<TempEnrollModel> d) {

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

    public TempEnrollModel getItem(int position) {
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

            vi = inflater.inflate(R.layout.billdetailsviewloans, null);

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
            tempValues = (TempEnrollModel) data.get(position);

            String name, paid, pending, advance;

            name = tempValues.getCusname();
             paid = tempValues.getPaytype();
            advance = tempValues.getAmount();
            //  pending = tempValues.getTotalenrlpending();
            if(paid.equalsIgnoreCase("daily.rct.adj"))
            {
                paid="Advance Adjustment";
            }else if(paid.equalsIgnoreCase("b.p adj")||(paid.equalsIgnoreCase("b.p.adjustment")))
            {
                paid="C.P Adjustment";
            }

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
              holder.txt_paytype.setText(paid);
            holder.txtcolected.setText(advance);
            //holder.txtpending.setText(pending);
        }
        return vi;
    }


}
