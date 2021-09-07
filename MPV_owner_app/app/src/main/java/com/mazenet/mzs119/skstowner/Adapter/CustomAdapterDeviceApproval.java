package com.mazenet.mzs119.skstowner.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.mazenet.mzs119.skstowner.DeviceApproval;
import com.mazenet.mzs119.skstowner.Model.Approvalmodel;
import com.mazenet.mzs119.skstowner.Model.DateWiseViewModel;
import com.mazenet.mzs119.skstowner.Model.DeviceApprovalModel;
import com.mazenet.mzs119.skstowner.R;
import com.mazenet.mzs119.skstowner.Utils.Config;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class CustomAdapterDeviceApproval extends BaseAdapter {

    private Activity activity;
    private ArrayList<DeviceApprovalModel> data;
    private static LayoutInflater inflater = null;
    String fontPath = Config.FONTPATHMAIN;
    Typeface tf;
    DeviceApprovalModel tempValues = null;
    int i = 0;

    Context mContext;

    public CustomAdapterDeviceApproval(Activity a, ArrayList<DeviceApprovalModel> d) {

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

    public DeviceApprovalModel getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        public TextView txtname, txtpaid, txtpending, txtcolected;
        // txtno,

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.apprve_dev, null);

            holder = new ViewHolder();
            holder.txtname = (TextView) vi.findViewById(R.id.txt_name);
            holder.txtcolected = (TextView) vi.findViewById(R.id.txt_paid);
            holder.txtname.setTypeface(tf);
            holder.txtcolected.setTypeface(tf);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (DeviceApprovalModel) data.get(position);

            String name, paid, pending, advance;

            name = tempValues.getId();
            // paid = tempValues.getEnrlpaid();
            advance = tempValues.getName();


            holder.txtname.setText(name);
            holder.txtcolected.setText(advance);
        }
        return vi;
    }


}
