package com.ndp.flazzbca.util;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ndp.flazzbca.R;

import java.util.List;

public class BackListAdapter extends BaseAdapter {

    private List<String> list = null;
    private Context context;
    private int pos = -1;

    public BackListAdapter(List<String> list, Context context) {
        this.list = list;
        this.context = context;
    }

    public void setPos(int pos) {
        this.pos = pos;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder = null;
        if (convertView == null) {
//            convertView = LayoutInflater.from(context).inflate(R.layout.fragment_text, parent, false);
            holder = new Holder();
//            holder.textView = (TextView) convertView.findViewById(R.id.fragment_textview);
            convertView.setTag(holder);
        } else {
            holder = (Holder) convertView.getTag();
        }

        if (pos == position) {
            holder.textView.setTextColor(Color.BLUE);
        } else {
            holder.textView.setTextColor(Color.BLACK);
        }
        holder.textView.setTextSize(18);
        holder.textView.setPadding(0, 15, 0, 15);
        holder.textView.setText(list.get(position));
        return convertView;
    }

    class Holder {
        TextView textView;
    }

}
