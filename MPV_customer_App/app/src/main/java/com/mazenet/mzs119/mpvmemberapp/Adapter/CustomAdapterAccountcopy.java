package com.mazenet.mzs119.mpvmemberapp.Adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mazenet.mzs119.mpvmemberapp.Model.CopyModel;
import com.mazenet.mzs119.mpvmemberapp.R;

import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


public class CustomAdapterAccountcopy extends BaseAdapter {

    private Activity activity;
    private ArrayList<CopyModel> data;
    private static LayoutInflater inflater = null;

    CopyModel tempValues = null;
    int i = 0;

    Context mContext;
    int sno = 0;
    SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
    SimpleDateFormat df_daily = new SimpleDateFormat("dd-MM-yyyy");
    SimpleDateFormat timedf = new SimpleDateFormat("HH:mm:ss");
    SimpleDateFormat timedfava = new SimpleDateFormat("hh:mm a");

    public CustomAdapterAccountcopy(Activity a, ArrayList<CopyModel> d) {

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

    public CopyModel getItem(int position) {
        return data.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        public TextView txt_amount, txt_grpname, txt_date, txt_paytype, txt_receiptno, txt_dueadvance;
        // txtno,
        LinearLayout receiptname;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {
            sno = 0;
            vi = inflater.inflate(R.layout.accountdetails, null);

            holder = new ViewHolder();
            holder.txt_grpname = (TextView) vi.findViewById(R.id.grpname);
            holder.txt_date = (TextView) vi.findViewById(R.id.date);
            holder.txt_paytype = (TextView) vi.findViewById(R.id.paytype);
            holder.txt_receiptno = (TextView) vi.findViewById(R.id.recptno);
            holder.txt_amount = (TextView) vi.findViewById(R.id.amount);
            holder.txt_dueadvance = (TextView) vi.findViewById(R.id.txt_dueadvance);
            holder.receiptname = (LinearLayout) vi.findViewById(R.id.receiptname);
            //holder.txt_advance = (TextView) vi.findViewById(R.id.ad);
            vi.setTag(holder);
        } else

            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {
            tempValues = null;
            tempValues = (CopyModel) data.get(position);

            String grpname, paid, pending, scheme, bonus, penaulty, amount, advance, completed_aution;
            String paid_d, pending_d, scheme_d, bonus_d, penaulty_d, amount_d, advance_d;

            grpname = tempValues.getGrpname();

            // NumberFormat formatter = NumberFormat.getCurrencyInstance(Locale.getDefault());
            Locale curLocale = new Locale("en", "IN");

            paid_d = tempValues.getAmount();
            try {
                Double d = Double.parseDouble(paid_d);
                String moneyString1 = NumberFormat.getNumberInstance(curLocale).format(d);
                paid_d = moneyString1;
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            String tim = "";
            try {
                Date d = timedf.parse(tempValues.getTime());
                tim = timedfava.format(d);
            } catch (ParseException e) {
                e.printStackTrace();
            }


            holder.txt_amount.setText(paid_d);
            holder.txt_receiptno.setText(tempValues.getReceipt_no());
            holder.receiptname.setVisibility(View.GONE);
            String paytype = tempValues.getPaytype();
            if (paytype.equalsIgnoreCase("daily.rct.adj")) {
                paytype = "Advance Adjustment";
            } else if (paytype.equalsIgnoreCase("b.p adj")) {
                paytype = "C.P Adjustment";
            }
            holder.txt_paytype.setText(paytype);


            if (tempValues.getDue_advance().equalsIgnoreCase("due")) {
                holder.txt_dueadvance.setText("Due Receipt");
                String dat = "";
                try {
                    Date d = df.parse(tempValues.getDate());
                    dat = df_daily.format(d);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                holder.txt_grpname.setText(grpname+"/"+tempValues.getGrpticketno());
                holder.txt_date.setText(dat + " / " + tim);
                holder.txt_dueadvance.setTextColor(mContext.getResources().getColor(R.color.colorPrimary));
            } else {
                holder.txt_grpname.setText("");
                holder.txt_date.setText(tempValues.getDate() + " / " + tim);
                holder.txt_dueadvance.setText("Advance Receipt");
                holder.txt_dueadvance.setTextColor(mContext.getResources().getColor(R.color.colorContrast));
            }
        }
        return vi;
    }


}
