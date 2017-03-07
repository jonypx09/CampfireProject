package com.example.jonat.campfire;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by Quinn on 2/26/2017.
 */
//TODO: Should be using ArrayLists instead of String[] probably.
public class MyCampfireList extends ArrayAdapter<String> {
    private String[] names;
    private String[] desc;
    private Integer[] imageid;
    private Activity context;

    public MyCampfireList(Activity context, String[] names, String[] desc, Integer[] imageid) {
        super(context, R.layout.my_campfire_list_item, names);
        this.context = context;
        this.names = names;
        this.desc = desc;
        this.imageid = imageid;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.my_campfire_list_item, null, true);
        TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
        TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textViewDesc);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.imageView);

        textViewName.setText(names[position]);
        textViewDesc.setText(desc[position]);
        image.setImageResource(imageid[position]);
        return listViewItem;
    }

}