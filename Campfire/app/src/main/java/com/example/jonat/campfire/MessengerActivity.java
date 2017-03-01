package com.example.jonat.campfire;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.util.ArrayList;

public class MessengerActivity extends AppCompatActivity {

    private EditText message;
    private ImageView addMessage;
    private ListView lv;
    ArrayList<String> list = new ArrayList<String>();
    ArrayAdapter<String> adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        addMessage = (ImageView)findViewById(R.id.sendMessageButton);
        addMessage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String input = message.getText().toString();
                if(input.length() > 0)
                {
                    // add string to the adapter, not the listview
                    adapter.add(input);
                    message.setText("");
                }
            }

        });

        message = (EditText)findViewById(R.id.messageEditText);
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1, list);

        lv=(ListView)findViewById(R.id.msgListView);
        lv.setAdapter(adapter);
    }


}
