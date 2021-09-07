package com.mazenet.mzs119.skstowner.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.mazenet.mzs119.skstowner.Model.Approvalmodel;
import com.mazenet.mzs119.skstowner.R;
import com.mazenet.mzs119.skstowner.Utils.Config;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by MZS119 on 3/28/2018.
 */

public class CustomAdapterApproval extends BaseAdapter {

    private static LayoutInflater inflater = null;
    String fontPath = Config.FONTPATHMAIN;
    Typeface tf;
    Approvalmodel tempValues = null;
    int i = 0;
    Context mContext;
    private Activity activity;
    private ArrayList<Approvalmodel> data;
  //  Intent positionintent;
    public static final String BROADCAST_position = "position";

    public CustomAdapterApproval(Activity a, ArrayList<Approvalmodel> d) {

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

    public Approvalmodel getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View convertView, final ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;
       // positionintent = new Intent(BROADCAST_position);
        if (convertView == null) {

            vi = inflater.inflate(R.layout.billdetails1, null);

            holder = new ViewHolder();
            holder.txtname = (TextView) vi.findViewById(R.id.txt_name);
            holder.txtpaid = (TextView) vi.findViewById(R.id.txt_paid);
            holder.txtpending = (TextView) vi.findViewById(R.id.txt_pending);
          //  holder.btn_appro = (Button) vi.findViewById(R.id.btn_approvee);
           /* holder.btn_appro.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    View parentRow = (View) view.getParent();
                    ListView listView = (ListView) parentRow.getParent();
                    final int position = listView.getPositionForView(parentRow);
                    positionintent.putExtra("position",position);
                    activity.sendBroadcast(positionintent);
                }
            }); */

            holder.txtpaid.setTypeface(tf);
            holder.txtname.setTypeface(tf);
            holder.txtpending.setTypeface(tf);
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (Approvalmodel) data.get(position);

            String name, paid, pending;

            name = tempValues.getAgentName();
            paid = tempValues.getCreatorName();
            pending = tempValues.getAmount();
            Locale curLocale = new Locale("en", "IN");
            Log.d("Collection Adapter", name);
            // NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
            try {
                if (pending.contains("-")) {
                    pending = "0";
                }
                Double d = Double.parseDouble(pending);
                String moneyString2 = NumberFormat.getNumberInstance(curLocale).format(d);
                pending = moneyString2;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            pending = "Rs. " + pending;
            holder.txtname.setText(name);
            holder.txtpaid.setText(paid);
            holder.txtpending.setText(pending);
        }
        return vi;
    }

    public static class ViewHolder {

        public TextView txtname, txtpaid, txtpending;
        public Button btn_appro;
        // txtno,

    }


}

