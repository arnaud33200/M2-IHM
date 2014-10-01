package com.example.arnaud.mylist;

import android.content.Context;
import android.text.AndroidCharacter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

/**
 * Created by arnaud on 30/09/2014.
 */
public class MyAdapter extends ArrayAdapter<String> {

    private LayoutInflater li;

    public MyAdapter(Context context) {
        super(context, android.R.layout.activity_list_item);
        li = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = li.inflate(R.layout.element,parent,false);
        TextView tv = (TextView) v.findViewById(R.id.textView);
        tv.setText(getItem(position));
        return v;
        //return super.getView(position, convertView, parent);
    }
}
