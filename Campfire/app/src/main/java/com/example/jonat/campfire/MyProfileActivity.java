package com.example.jonat.campfire;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import backend.algorithms.Student;
import backend.database.DatabaseAdapter;

public class MyProfileActivity extends AppCompatActivity {

    DatabaseAdapter db;
    private Student myStudent;
    private String uEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Edit some specific elements of your profile", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        //Connect to the database
        db = new DatabaseAdapter(this);

        Intent intent = getIntent();
        uEmail = intent.getExtras().getString("userEmail");
        myStudent = db.getStudent(uEmail);
        setTitle(myStudent.getFname() + " " + myStudent.getLname());

        View view = findViewById(R.id.userInfo);
        TextView userEmail = (TextView) view.findViewById(R.id.emailTextview);
        userEmail.setText("You Email:   " + uEmail);


    }
}
