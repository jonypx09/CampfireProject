package com.example.jonat.campfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import backend.database.DatabaseAdapter;

public class MessengerActivity extends AppCompatActivity {

    DatabaseAdapter db;
    private String uEmail;
    private ListView listView;
    private ImageView btnSend;
    private EditText editText;
    boolean isMine = true;
    private List<ChatMessage> chatMessages;
    private ArrayAdapter<ChatMessage> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);
        setTitle("Messenger");

        //Connect to the database
        db = new DatabaseAdapter(this);

        Intent intent = getIntent();
        uEmail = intent.getExtras().getString("userEmail");

        chatMessages = new ArrayList<>();

        listView = (ListView) findViewById(R.id.list_msg);
        btnSend = (ImageView) findViewById(R.id.btn_chat_send);
        editText = (EditText) findViewById(R.id.msg_type);

        //set ListView adapter first
        adapter = new ChatAdapter(this, R.layout.in_message_bg, chatMessages);
        listView.setAdapter(adapter);

        //event for button SEND
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().equals("")) {
                    Toast.makeText(MessengerActivity.this, "Please input some text...", Toast.LENGTH_SHORT).show();
                } else {
                    //add message to list
                    ChatMessage chatMessage = new ChatMessage(editText.getText().toString(), isMine);
                    chatMessages.add(chatMessage);
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                    if (isMine) {
                        isMine = false;
                    } else {
                        isMine = true;
                    }
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.putExtra("userEmail", uEmail);
            startActivity(mainIntent);
            return true;
        }
        return false;
    }
}
