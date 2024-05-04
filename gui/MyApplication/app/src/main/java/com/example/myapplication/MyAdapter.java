package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


public class MyAdapter extends BaseAdapter {

    // override other abstract methods here
    private LayoutInflater inflater;

    private ArrayList<String> items;

    public MyAdapter(LayoutInflater inflater, ArrayList<String> items){
        this.inflater = inflater;
        this.items = items;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup container) {

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item, container, false);
        }

        ((TextView) convertView.findViewById(R.id.list_text))
                .setText(items.get(position));
        return convertView;
    }
}