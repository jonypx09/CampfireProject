package com.example.jonat.campfire;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import backend.database.Chat;
import backend.database.Message;

import static backend.database.DbAdapter.addMessage;
import static backend.database.DbAdapter.getChat;

public class MessengerActivity extends AppCompatActivity {

    private String[] newStudentID;
    private String uEmail;
    private Chat curChat;
    private ListView listView;
    private ImageView btnSend;
    private EditText editText;
    boolean isMine = true;
    private List<Message> chatMessages;
    private ArrayAdapter<Message> adapter;
    private Activity temp;
    private String title;
    private Button refresh_btn;
    private Button messengers_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_messenger);

        Intent intent = getIntent();
        newStudentID = intent.getExtras().getStringArray("identity");
        title = intent.getExtras().getString("messengers");
        curChat = getChat(Integer.parseInt(intent.getExtras().getString("chat_id")));
        uEmail = newStudentID[2];

        setTitle(title);

        chatMessages = curChat.getMessages();

        listView = (ListView) findViewById(R.id.list_msg);
        btnSend = (ImageView) findViewById(R.id.btn_chat_send);
        refresh_btn = (Button) findViewById(R.id.refresh_btn);
        messengers_btn = (Button) findViewById(R.id.messengers_btn);
        editText = (EditText) findViewById(R.id.msg_type);

        temp = this;
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
                    addMessage(curChat.getChatID(), uEmail, editText.getText().toString());
                    curChat.addMessage(m);
                    adapter = new ChatAdapter(temp, R.layout.in_message_bg, curChat.getMessages(), uEmail);
                    listView.setAdapter(adapter);
                    editText.setText("");
                }
            }
        });

        // refresh the messages, pulling any new messages
        refresh_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                adapter = new ChatAdapter(temp, R.layout.in_message_bg, curChat.getMessages(), uEmail);
                listView.setAdapter(adapter);
            }
        });

        messengers_btn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                //TODO: go to MessengerFragment to display all users in the chat, and be able to add them to your Campfire
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
