package com.example.jonat.campfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import backend.database.SQLiteController;

public class PromoActivity extends AppCompatActivity {

    private SQLiteController sqlcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_promo);

        //Connect to the database
        sqlcon = new SQLiteController();

    }

    public void toLoginScreen(View view){
        Intent signInIntent = new Intent(this, LoginActivity.class);
        startActivity(signInIntent);
    }

    public void toSignUpScreen(View view){
        Intent signInIntent = new Intent();

        startActivity(signInIntent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }
}
