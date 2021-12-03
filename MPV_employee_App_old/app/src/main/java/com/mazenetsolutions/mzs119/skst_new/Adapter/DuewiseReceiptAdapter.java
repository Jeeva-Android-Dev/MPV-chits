package com.mazenetsolutions.mzs119.skst_new.Adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mazenetsolutions.mzs119.skst_new.DuewiseIndividualReceipt;
import com.mazenetsolutions.mzs119.skst_new.Model.Duewise_list_model;
import com.mazenetsolutions.mzs119.skst_new.R;

import java.util.ArrayList;

public class DuewiseReceiptAdapter extends BaseAdapter {
    private Context context;
    public static ArrayList<Duewise_list_model> editModelArrayList;
    public DuewiseReceiptAdapter(Context context, ArrayList<Duewise_list_model> editModelArrayList) {
        this.context = context;
        this.editModelArrayList = editModelArrayList;
    }
    @Override
    public int getViewTypeCount() {
        return getCount();
    }
    @Override
    public int getItemViewType(int position) {
        return position;
    }
    @Override
    public int getCount() {
        return editModelArrayList.size();
    }
    @Override
    public Object getItem(int position) {
        return editModelArrayList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.due_wise_installment_list, null, true);
            holder.txt_auctionno = (TextView) convertView.findViewById(R.id.txt_auctionno);
            holder.txt_pending = (TextView) convertView.findViewById(R.id.txt_pending);
            holder.txt_bonus = (TextView) convertView.findViewById(R.id.txt_bonus);
            holder.txt_total = (EditText) convertView.findViewById(R.id.txt_total);

            convertView.setTag(holder);
        }else {
            // the getTag returns the viewHolder object set as a tag to the view
            holder = (ViewHolder)convertView.getTag();
        }


        String auc_no, penalty, pending,bonus,total;

        auc_no = editModelArrayList.get(position).getAuction_n0();
        penalty = editModelArrayList.get(position).getPenalty();
        pending = editModelArrayList.get(position).getPending();
        bonus = editModelArrayList.get(position).getBonus();
        total = editModelArrayList.get(position).getPaying();

        Log.d("Group Customer Adapter", auc_no);

        holder.txt_auctionno.setText(auc_no);
        holder.txt_pending.setText(pending);

        holder.txt_bonus.setText(bonus);
       // holder.editText.setText(editModelArrayList.get(position).getEditTextValue());
        holder.txt_total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (holder.txt_total.getText().toString() != null && !holder.txt_total.getText().toString().equalsIgnoreCase("")
                        && !holder.txt_total.getText().toString().isEmpty()) {

                    double actual = Double.parseDouble(DuewiseIndividualReceipt.total_penalty_amt);
                    String ent_val = DuewiseIndividualReceipt.total_penalty.getText().toString();
                    double entered = 0;
                    if (ent_val.length() > 0) {
                        entered = Double.parseDouble(ent_val);
                    } else {
                        entered = 0;
                    }

                    if (actual <= entered) {


                        double overall_pending = 0;


                        if (editModelArrayList.get(position).getPenalty() == null || editModelArrayList.get(position).getPenalty().equalsIgnoreCase("0") || editModelArrayList.get(position).getPenalty().length() == 0) {
                            overall_pending = 0;
                        } else {
                            overall_pending = Double.parseDouble(editModelArrayList.get(position).getPenalty());
                        }

                        double total_payable = overall_pending;
                        double entered_amout = Double.parseDouble(holder.txt_total.getText().toString());
                        if (entered_amout > total_payable) {

                            Toast toast = Toast.makeText(context, "Maxmum Amount is " + String.format("%.0f", total_payable), Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP | Gravity.RIGHT, 100, 250);
                            toast.show();
                            holder.txt_total.setText(String.format("%.0f", total_payable));
                        }

                        editModelArrayList.get(position).setPaying(holder.txt_total.getText().toString());
                    }
                else {
                        if (!holder.txt_total.getText().toString().equalsIgnoreCase("0")) {

                            Toast.makeText(context, "Full Penalty has to be paid", Toast.LENGTH_SHORT).show();
                            holder.txt_total.setText("0");
                        }
                    }

                }
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable s) {
                DuewiseIndividualReceipt.Total_Amount();
            }
        });
        return convertView;





    }
    private class ViewHolder {
        public TextView txt_auctionno, txt_pending,txt_bonus;
        EditText txt_total;
    }
}