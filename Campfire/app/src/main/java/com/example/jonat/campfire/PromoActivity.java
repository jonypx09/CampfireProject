package com.example.jonat.campfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import backend.database.DatabaseAdapter;

public class PromoActivity extends AppCompatActivity {

    DatabaseAdapter database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_promo);
        setTitle("");

        //Connect to the database
        database = new DatabaseAdapter(this);
    }

    public void toLoginScreen(View view){
        Intent signInIntent = new Intent(this, LoginActivity.class);
        startActivity(signInIntent);
    }

    public void toSignUpScreen(View view){
        Intent signInIntent = new Intent(this, SignUpActivity.class);
        startActivity(signInIntent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
