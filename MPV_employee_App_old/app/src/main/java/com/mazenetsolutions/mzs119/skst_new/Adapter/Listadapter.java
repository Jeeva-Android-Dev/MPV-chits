package com.mazenetsolutions.mzs119.skst_new.Adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.mazenetsolutions.mzs119.skst_new.R;
import com.mazenetsolutions.mzs119.skst_new.Utils.Config;

import java.util.ArrayList;


public class Listadapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<String> data;
    private static LayoutInflater inflater = null;
    String fontPath = Config.FONTPATHMAIN;
    Typeface tf;
    int i = 0;

    Context mContext;

    public Listadapter(Activity a, ArrayList<String> d) {

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

    public Object getItem(int position) {
        return position;
    }

    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {

        public TextView txt;

    }

    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        ViewHolder holder;

        if (convertView == null) {

            vi = inflater.inflate(R.layout.tabitem2, null);

            holder = new ViewHolder();
            holder.txt = (TextView) vi.findViewById(R.id.textname);
            holder.txt.setTypeface(tf);

            vi.setTag(holder);
        } else
            holder = (ViewHolder) vi.getTag();

        if (data.size() <= 0) {

        } else {

            String NAME;

            NAME = data.get(position);
            holder.txt.setText(NAME);

        }
        return vi;
    }

    public void refresh(ArrayList<String> mStrings1) {
        // TODO Auto-generated method stub
        data = mStrings1;
        notifyDataSetChanged();
    }

}
