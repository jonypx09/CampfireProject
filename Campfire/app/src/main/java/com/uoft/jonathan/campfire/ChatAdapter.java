package com.uoft.jonathan.campfire;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Created by andrewgoupil on 2017-03-01.
 */

import backend.database.Message;

public class ChatAdapter extends ArrayAdapter<Message> {

    private Activity activity;
    private List<Message> messages;
    private String uEmail;

    public ChatAdapter(Activity context, int resource, List<Message> messages, String uEmail) {
        super(context, resource, messages);
        this.activity = context;
        this.messages = messages;
        this.uEmail = uEmail;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        LayoutInflater inflater = (LayoutInflater) activity.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);

        int layoutResource = 0; // determined by view type
        Message chatMessage = getItem(position);

        if (chatMessage.equals(messages.get(messages.size() - 1))) {
            chatMessage = messages.get(messages.size() - 1);
        }

        int viewType = getItemViewType(position);

        if (chatMessage.getSender_email().equals(uEmail)) {
            layoutResource = R.layout.in_message_bg;
        } else {
            layoutResource = R.layout.out_message_bg;
        }

        if (convertView != null) {
            holder = (ViewHolder) convertView.getTag();
        } else {
            convertView = inflater.inflate(layoutResource, parent, false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }

        //set message content
        holder.msg.setText(chatMessage.getText());

        return convertView;
    }

    @Override
    public int getViewTypeCount() {
        // return the total number of view types. this value should never change
        // at runtime
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        // return a value between 0 and (getViewTypeCount - 1)
        return position % 2;
    }

    private class ViewHolder {
        private TextView msg;

        public ViewHolder(View v) {
            msg = (TextView) v.findViewById(R.id.txt_msg);
        }
    }
}
