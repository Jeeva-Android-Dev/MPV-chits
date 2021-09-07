package com.mazenet.mzs119.skstowner.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mazenet.mzs119.skstowner.Model.EmpwiseOuts;
import com.mazenet.mzs119.skstowner.R;
import com.mazenet.mzs119.skstowner.Utils.Config;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by MZS119 on 3/28/2018.
 */

public class CustomAdapterEmpWiseouts extends BaseAdapter {

    private static LayoutInflater inflater = null;
    String fontPath = Config.FONTPATHMAIN;
    Typeface tf;
    EmpwiseOuts tempValues = null;
    int i = 0;
    Context mContext;
    private Activity activity;
    private ArrayList<EmpwiseOuts> data;

    public CustomAdapterEmpWiseouts(Activity a, ArrayList<EmpwiseOuts> d) {

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

    public EmpwiseOuts getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {
        Locale curLocale = new Locale("en", "IN");
        View vi = convertView;
        final ViewHolder holder;
        if (convertView == null) {

            vi = inflater.inflate(R.layout.empwiseouts, null);

            holder = new ViewHolder();
            holder.txtname = (TextView) vi.findViewById(R.id.txt_name);
            holder.txtpending = (TextView) vi.findViewById(R.id.txt_pending);
            holder.txt_ticket = (TextView) vi.findViewById(R.id.txt_ticketno);
            holder.txt_grpname = (TextView) vi.findViewById(R.id.txt_grpname);
            holder.txtpending.setTypeface(tf);
            holder.txtname.setTypeface(tf);
            holder.txt_ticket.setTypeface(tf);
            holder.txt_grpname.setTypeface(tf);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (EmpwiseOuts) data.get(position);

            String name, paid, pending;
            String cash = "";
            name = tempValues.getCustname();
            paid = tempValues.getPaid();
            pending = tempValues.getPending();
            try {
                if (pending.contains("-")) {
                    pending = "0";
                }
                Double d = Double.parseDouble(pending);
                String moneyString2 = NumberFormat.getNumberInstance(curLocale).format(d);
                cash = moneyString2;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            holder.txtname.setText(name);

            holder.txtpending.setText(" Rs. " + cash);
            holder.txt_grpname.setText(tempValues.getGrpname());
            System.out.println("grpname " + tempValues.getGrpname());
            holder.txt_ticket.setText(tempValues.getTicketno());
            holder.txtname.setText(tempValues.getCustname());

        }
        return vi;
    }

    public static class ViewHolder {

        public TextView txtname, txtpending, txt_grpname, txt_ticket;
        // txtno,

    }


}

