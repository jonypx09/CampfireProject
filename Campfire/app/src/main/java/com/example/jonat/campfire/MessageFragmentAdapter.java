package com.example.jonat.campfire;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MessageFragmentAdapter extends ArrayAdapter<String> {
        private String[] desc;
        private Integer[] imageid;
        private Activity context;
        private String[][] names;

        public MessageFragmentAdapter(Activity context, String[][] names, String[] desc, Integer[] imageid) {
            super(context, R.layout.my_message_adapter);
            this.context = context;
            this.names = names;
            this.desc = desc;
            this.imageid = imageid;
        }

    @NonNull
    public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = context.getLayoutInflater();
            View listViewItem = inflater.inflate(R.layout.my_message_adapter, null);
            TextView textViewName = (TextView) listViewItem.findViewById(R.id.textViewName);
            TextView textViewDesc = (TextView) listViewItem.findViewById(R.id.textViewDesc);
            ImageView image = (ImageView) listViewItem.findViewById(R.id.imageView);

            String temp[] = new String[position];
            int i = 0;
            for (String n: names[position]) {
                if (names[position].length == 1 || names[position][i].equals(n)) {
                    temp[position] += n;
                } else {
                    temp[position] += " " + n + ",";
                }
                i++;
            }
            textViewName.setText(temp[position]);
            textViewDesc.setText(desc[position]);
            image.setImageResource(imageid[position]);

            return listViewItem;
        }

}

