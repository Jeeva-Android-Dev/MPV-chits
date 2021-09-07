package com.mazenet.mzs119.skstowner.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mazenet.mzs119.skstowner.Model.colfeedbackmodel;
import com.mazenet.mzs119.skstowner.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class Adap_collfeedback extends BaseAdapter {

    private Activity activity;
    private ArrayList<colfeedbackmodel> data;
    private static LayoutInflater inflater = null;
    Typeface tf;
    colfeedbackmodel tempValues = null;
    int i = 0;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat df_daily = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat timedfava = new SimpleDateFormat("hh:mm a");
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

        public TextView txtname, txt_paytype, txt_feedtime, txtcolected;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.billdetailsview, null);

            holder = new ViewHolder();
            holder.txtname = (TextView) vi.findViewById(R.id.txt_custname);
            holder.txtcolected = (TextView) vi.findViewById(R.id.txt_date);
            holder.txt_paytype = (TextView) vi.findViewById(R.id.txt_colectedamnt);
            holder.txt_feedtime = (TextView) vi.findViewById(R.id.txt_feedtime);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (colfeedbackmodel) data.get(position);
            String dat = "";
            try {
                Date d = df.parse(tempValues.getCreatedon());
                dat = df_daily.format(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String tim = "";
            try {
                Date d = timedf.parse(tempValues.getFeedbacktime());
                tim = timedfava.format(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            holder.txt_feedtime.setVisibility(View.VISIBLE);
            holder.txt_feedtime.setText(tim);
            holder.txtname.setText(tempValues.getName());
            holder.txt_paytype.setText(dat);
            holder.txtcolected.setText(tempValues.getFeedback());
        }
        return vi;
    }


}
