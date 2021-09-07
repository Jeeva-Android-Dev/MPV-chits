package com.mazenet.mzs119.mpvmemberapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.mazenet.mzs119.mpvmemberapp.Model.DateWiseViewModel;
import com.mazenet.mzs119.mpvmemberapp.R;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class CustomAdapterMyTrans extends BaseAdapter {

    private Activity activity;
    private ArrayList<DateWiseViewModel> data;
    private static LayoutInflater inflater = null;
    Typeface tf;
    DateWiseViewModel tempValues = null;
    int i = 0;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat timedfava = new SimpleDateFormat("hh:mm a");
    SimpleDateFormat df_daily = new SimpleDateFormat("dd-MM-yyyy");
    Context mContext;

    public CustomAdapterMyTrans(Activity a, ArrayList<DateWiseViewModel> d) {

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

    public DateWiseViewModel getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        public TextView txt_date, txt_amount, txt_paytype, txt_dueadvance, recptno;
        // txtno,

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.billdetailsmytrans, null);

            holder = new ViewHolder();

            holder.txt_date = (TextView) vi.findViewById(R.id.txt_rc_date);
            holder.txt_amount = (TextView) vi.findViewById(R.id.txt_rc_amount);
            holder.txt_paytype = (TextView) vi.findViewById(R.id.txt_rc_paytype);
            holder.txt_dueadvance = (TextView) vi.findViewById(R.id.txt_dueadvance);
            holder.recptno = (TextView) vi.findViewById(R.id.recptno);

            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (DateWiseViewModel) data.get(position);
            String name, paid, pending, advance;
            advance = tempValues.getTotal_Amount();
            Locale curLocale = new Locale("en", "IN");

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


            String tim = "";
            try {
                Date d = timedf.parse(tempValues.getTime());
                tim = timedfava.format(d);
            } catch (ParseException e) {
                tim = tempValues.getTime();
                e.printStackTrace();
            }

            holder.txt_amount.setText("Rs."+advance);
            String paytype = tempValues.getPayment_Type();
            if (paytype.equalsIgnoreCase("daily.rct.adj")) {
                paytype = "Advance Adjustment";
            } else if (paytype.equalsIgnoreCase("b.p adj")) {
                paytype = "C.P Adjustment";
            }
            holder.txt_paytype.setText(paytype);
            holder.recptno.setText(tempValues.getReceipt_No());
            if (tempValues.getDue_advance().equalsIgnoreCase("due")) {
                holder.txt_dueadvance.setText("Due Receipt");
                String dat = "";
                try {
                    Date d = df.parse(tempValues.getDate());
                    dat = df_daily.format(d);
                } catch (ParseException e) {
                    dat = tempValues.getDate();
                    e.printStackTrace();
                }
                holder.txt_date.setText(dat + " / " + tim);
                holder.txt_dueadvance.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            } else {
                holder.txt_date.setText(tempValues.getDate() + " / " + tim);
                holder.txt_dueadvance.setText("Advance Receipt");
                holder.txt_dueadvance.setTextColor(mContext.getResources().getColor(R.color.colorContrast));
            }
        }
        return vi;
    }


}
