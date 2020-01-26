package com.vinu.projectx;

import android.app.Activity;
import android.content.Context;
import android.database.DataSetObserver;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

class CustomListViewAdapter extends ArrayAdapter<Product> {
    Context context;

    public CustomListViewAdapter(@NonNull Context context, int resource, List<Product> item) {
        super(context, resource, item);
        this.context = context;
    }

    private class ViewHolder {
        ImageView imageView;
        TextView textName;
        TextView textPrice;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder holder = null;
        Product rowItem = getItem(position);

        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.activity_layout, null);
            holder = new ViewHolder();

            holder.textName = (TextView) convertView.findViewById(R.id.textViewTitle);
            holder.textPrice = (TextView) convertView.findViewById(R.id.textViewPrice);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageView);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();

            holder.textName.setText(rowItem.getName());
            holder.textPrice.setText("INR:" + rowItem.getPrice()+"/-");

            holder.imageView.setImageBitmap(rowItem.getImage());

            //return convertView;
        }


        return convertView;
    }
}


