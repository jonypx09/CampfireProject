package com.a02.team.project.csc301.campfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.concurrent.TimeUnit;

public class PromoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setTitle("Welcome");

        //Change transition effect
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_promo);

    }

    /**
     * Properly send this user to the login screen
     * @param view View object to react on button click
     */
    public void toLoginScreen(View view){
        Intent signInIntent = new Intent(this, SignInActivity.class);
        startActivity(signInIntent);
    }

    /**
     * Properly send this user to the signup screen
     * @param view View object to react on button click
     */
    public void toSignUpScreen(View view){
        Intent signUpIntent = new Intent(this, SignUpActivity.class);
        startActivity(signUpIntent);
    }
}
