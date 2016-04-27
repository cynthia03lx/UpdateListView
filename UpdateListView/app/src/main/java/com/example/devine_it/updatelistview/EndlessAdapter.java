package com.example.devine_it.updatelistview;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class EndlessAdapter extends ArrayAdapter<Place> {


    private final ArrayList<Place> itemsArrayList;
    private Context ctx;


    public EndlessAdapter(Context ctx, ArrayList<Place> itemsArrayList) {
        super(ctx, R.layout.row_layout, itemsArrayList);
        this.ctx = ctx;
        this.itemsArrayList = itemsArrayList;

    }
    public void clearData() {
        // clear the data
        itemsArrayList.clear();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // 1. Create inflater
        LayoutInflater inflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Get rowView from inflater
        View rowView = inflater.inflate(R.layout.row_layout, parent, false);
        // We should use class holder pattern
        TextView tv = (TextView) rowView.findViewById(R.id.txt1);
        TextView tv1 = (TextView) rowView.findViewById(R.id.txt2);
        TextView tv2 = (TextView)rowView.findViewById(R.id.txt3);

        tv.setText(itemsArrayList.get(position).getName());

       // System.out.println("-------------"+itemList1.get(position));
      tv1.setText(itemsArrayList.get(position).getVicinity());


        DecimalFormat df = new DecimalFormat("#.0000");

       tv2.setText("Distance  "+Double.toString(Double.parseDouble(df.format(itemsArrayList.get(position).getDistance())))+"km");

        return rowView;

    }




}