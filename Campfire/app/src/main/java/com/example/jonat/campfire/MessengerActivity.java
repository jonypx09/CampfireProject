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

import backend.database.Chat;
import backend.database.DatabaseAdapter;
import backend.database.Message;

import static backend.database.DbAdapter.getChat;

public class MessengerActivity extends AppCompatActivity {

    DatabaseAdapter db;
    private String[] newStudentID;
    private String uEmail;
    private Chat curChat;
    private ListView listView;
    private ImageView btnSend;
    private EditText editText;
    boolean isMine = true;
    private List<Message> chatMessages;
    private ArrayAdapter<Message> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        //Connect to the database
        db = new DatabaseAdapter(this);

        Intent intent = getIntent();
        newStudentID = intent.getExtras().getStringArray("identity");
        uEmail = newStudentID[2];
        curChat = getChat(Integer.parseInt(intent.getExtras().getString("chatid")));

        //TODO: title should be the first name of the person you are messaging
        setTitle(String.valueOf(curChat.getChatID()));

        chatMessages = curChat.getMessages();
        System.out.println(chatMessages);

        listView = (ListView) findViewById(R.id.list_msg);
        btnSend = (ImageView) findViewById(R.id.btn_chat_send);
        editText = (EditText) findViewById(R.id.msg_type);

        //set ListView adapter first
        adapter = new ChatAdapter(this, R.layout.in_message_bg, curChat.getMessages(), uEmail);
        listView.setAdapter(adapter);

        //event for button SEND
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().toString().trim().equals("")) {
                    Toast.makeText(MessengerActivity.this, "Please input some text...", Toast.LENGTH_SHORT).show();
                } else {
                    //add message to list
                    //TODO: need to be able to add messages to online database
                    Message m = new Message(uEmail, editText.getText().toString(), String.valueOf(new java.util.Date()));
                    curChat.addMessage(m);
                    adapter.notifyDataSetChanged();
                    editText.setText("");
                }
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.putExtra("identity", newStudentID);
            startActivity(mainIntent);
            return true;
        }
        return false;
    }
}
