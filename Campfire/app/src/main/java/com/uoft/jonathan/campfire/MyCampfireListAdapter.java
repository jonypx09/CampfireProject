package com.uoft.jonathan.campfire;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Quinn on 2/26/2017.
 */

public class MyCampfireListAdapter extends ArrayAdapter<String> {
    private String[] names;
    private String[] desc;
    private Integer[] imageid;
    private Activity context;
    private String[][] names2;
    private String[] emails;

    public MyCampfireListAdapter(Activity context, String[] names, String[] desc, Integer[] imageid) {
        super(context, R.layout.my_campfire_list_item, names);
        this.context = context;
        this.names = names;
        this.desc = desc;
        this.imageid = imageid;
    }

    public MyCampfireListAdapter(Activity context, String[] names, String[] emails, String[] desc, Integer[] imageid) {
        super(context, R.layout.my_campfire_list_item, names);
        this.context = context;
        this.names = names;
        this.desc = desc;
        this.imageid = imageid;
        this.emails = emails;
    }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.my_campfire_list_item, null);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textViewDesc);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imageView);
        textViewName.setText(names[position]);
        textViewDesc.setText(desc[position]);
        if (imageid[position] != null) {
            image.setImageResource(imageid[position]);
        }

        return listViewItem;
    }

}