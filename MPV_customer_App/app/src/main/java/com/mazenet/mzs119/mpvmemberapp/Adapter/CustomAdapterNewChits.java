package com.mazenet.mzs119.mpvmemberapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mazenet.mzs119.mpvmemberapp.Model.NewChitModel;
import com.mazenet.mzs119.mpvmemberapp.NewCommencedChits;
import com.mazenet.mzs119.mpvmemberapp.R;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;


public class CustomAdapterNewChits extends BaseAdapter {

    private Activity activity;
    private ArrayList<NewChitModel> data;
    private static LayoutInflater inflater = null;

    NewChitModel tempValues = null;
    int i = 0;

    Context mContext;

    public CustomAdapterNewChits(Activity a, ArrayList<NewChitModel> d) {

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

    public NewChitModel getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        public TextView txt_grpname, txt_noofmembers, txt_noofmonths, txt_grpstart, txt_grpend, txt_chitvalue, txt_sno;
        LinearLayout btn_iminterested;
        // txtno,

    }

    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.newchitdetails, null);

            holder = new ViewHolder();
            holder.txt_grpname = (TextView) vi.findViewById(R.id.txt_grpname);
            holder.txt_noofmembers = (TextView) vi.findViewById(R.id.txt_noofmembers);
            holder.txt_noofmonths = (TextView) vi.findViewById(R.id.txt_noofmonths);
            holder.txt_chitvalue = (TextView) vi.findViewById(R.id.txt_scheme);
            holder.btn_iminterested = (LinearLayout) vi.findViewById(R.id.btn_iminterested);
            holder.btn_iminterested.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NewCommencedChits.sendmsg( data.get(position).getChitvalue());
                }
            });
            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (NewChitModel) data.get(position);

            String grpname, paid, pending, scheme, bonus, penaulty, amount, advance, completed_aution;
            String paid_d, pending_d, scheme_d, bonus_d, penaulty_d, amount_d, advance_d;

            grpname = tempValues.getScheme_Format();

            // NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
            Locale curLocale = new Locale("en", "IN");
            holder.btn_iminterested.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // Toast.makeText(mContext, "clicked " + data.get(position).getGrpname(), Toast.LENGTH_SHORT).show();
                    NewCommencedChits.sendmsg( data.get(position).getScheme_Format());
                }
            });
            paid_d = tempValues.getChitvalue();
            try {
                Double d = Double.parseDouble(paid_d);
                String moneyString1 = NumberFormat.getNumberInstance(curLocale).format(d);
                paid_d = moneyString1;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            paid_d = tempValues.getChitvalue();
            try {
                Double d = Double.parseDouble(paid_d);
                String moneyString1 = NumberFormat.getNumberInstance(curLocale).format(d);
                paid_d = moneyString1;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            pending_d = tempValues.getMonthly_Amt();
            try {
                Double d = Double.parseDouble(pending_d);
                String moneyString1 = NumberFormat.getNumberInstance(curLocale).format(d);
                pending_d = moneyString1;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            holder.txt_grpname.setText(grpname);
            holder.txt_chitvalue.setText( paid_d);
            holder.txt_noofmembers.setText(tempValues.getMonth()+" Months");
            holder.txt_noofmonths.setText(pending_d);


        }
        return vi;
    }


}
