package com.mazenet.mzs119.skstowner.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mazenet.mzs119.skstowner.Model.AgentModel;
import com.mazenet.mzs119.skstowner.R;
import com.mazenet.mzs119.skstowner.Utils.Config;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by MZS119 on 3/28/2018.
 */

public class CustomAdapterAgentlist extends BaseAdapter {

    private static LayoutInflater inflater = null;
    String fontPath = Config.FONTPATHMAIN;
    Typeface tf;
    AgentModel tempValues = null;
    int i = 0;
    Context mContext;
    private Activity activity;
    private ArrayList<AgentModel> data;

    public CustomAdapterAgentlist(Activity a, ArrayList<AgentModel> d) {

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

    public AgentModel getItem(int position) {
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

            vi = inflater.inflate(R.layout.agent, null);

            holder = new ViewHolder();
            holder.txtname = (TextView) vi.findViewById(R.id.txt_name);
            holder.txtpaid = (TextView) vi.findViewById(R.id.txt_paid);
            holder.txt_totalcollected = (TextView) vi.findViewById(R.id.txt_totalcollected);

            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (AgentModel) data.get(position);

            String name, paid, pending;
            String cash = "", totalamnt = "";
            name = tempValues.getAgentname();
            paid = tempValues.getAgentId();
            pending = tempValues.getCashinhand();
            String totcolelcted = tempValues.getTotalcollected();
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
            try {
                if (totcolelcted.contains("-")) {
                    totcolelcted = "0";
                }
                Double d = Double.parseDouble(totcolelcted);
                String moneyString2 = NumberFormat.getNumberInstance(curLocale).format(d);
                totalamnt = moneyString2;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            holder.txtname.setText(name + "  /  " + paid);
            holder.txt_totalcollected.setText(totalamnt);
            holder.txtpaid.setText(cash);
            // holder.txtpaid.setText(paid);

        }
        return vi;
    }

    public static class ViewHolder {

        public TextView txtname, txtpaid, txt_totalcollected;
        // txtno,

    }


}

