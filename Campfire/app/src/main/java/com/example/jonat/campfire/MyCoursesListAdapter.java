package com.example.jonat.campfire;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyCoursesListAdapter extends ArrayAdapter<String> {
    private String[] names;
    private Integer[] imageid;
    private Activity context;

    public MyCoursesListAdapter(Activity context, String[] names, Integer[] imageid) {
        super(context, R.layout.my_courses_list_item, names);
        this.context = context;
        this.names = names;
        this.imageid = imageid;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.my_courses_list_item, null);
        TextView textViewCourse = (TextView) listViewItem.findViewById(R.id.textViewCourse);
        ImageView image = (ImageView) listViewItem.findViewById(R.id.courseImageView);

        textViewCourse.setText(names[position]);
        image.setImageResource(imageid[position]);

        return listViewItem;
    }

}