package com.a02.team.project.csc301.campfire;

import android.app.ActionBar;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Change transition effect
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_sign_up);

        Intent intent = getIntent();
        this.setTitle("Sign Up");

//        ActionBar actionBar = getActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
    }


    /**
     * Properly send this user to the login screen
     * @param view View object to react on button click
     */
    public void toHomeScreen(View view){
        Intent homeIntent = new Intent(this, HomeActivity.class);
        startActivity(homeIntent);
    }

    public boolean onOptionsItemSelected(MenuItem item){
        Intent myIntent = new Intent(getApplicationContext(), PromoActivity.class);
        startActivityForResult(myIntent, 0);
        return true;

    }
}
