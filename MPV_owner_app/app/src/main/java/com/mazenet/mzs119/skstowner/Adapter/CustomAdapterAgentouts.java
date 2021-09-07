package com.mazenet.mzs119.skstowner.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mazenet.mzs119.skstowner.Model.AgentOutsModel;
import com.mazenet.mzs119.skstowner.R;
import com.mazenet.mzs119.skstowner.Utils.Config;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by MZS119 on 3/28/2018.
 */

public class CustomAdapterAgentouts extends BaseAdapter {

    private static LayoutInflater inflater = null;
    String fontPath = Config.FONTPATHMAIN;
    Typeface tf;
    AgentOutsModel tempValues = null;
    int i = 0;
    Context mContext;
    private Activity activity;
    private ArrayList<AgentOutsModel> data;

    public CustomAdapterAgentouts(Activity a, ArrayList<AgentOutsModel> d) {

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

    public AgentOutsModel getItem(int position) {
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

            vi = inflater.inflate(R.layout.outstanding, null);

            holder = new ViewHolder();
            holder.txtname = (TextView) vi.findViewById(R.id.txt_name);
            holder.txtpaid = (TextView) vi.findViewById(R.id.txt_paid);

            holder.txtpaid.setTypeface(tf);
            holder.txtname.setTypeface(tf);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (AgentOutsModel) data.get(position);

            String name, paid, pending;
            String cash="";
            name = tempValues.getEmpname();
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

            holder.txtpaid.setText(" Rs. "+cash);
           // holder.txtpaid.setText(paid);

        }
        return vi;
    }

    public static class ViewHolder {

        public TextView txtname, txtpaid;
        // txtno,

    }


}

