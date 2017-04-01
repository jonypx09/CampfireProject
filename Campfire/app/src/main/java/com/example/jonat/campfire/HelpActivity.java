package com.example.jonat.campfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

public class HelpActivity extends AppCompatActivity {

    private String[] newStudentID;
    private String uEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_help);
        setTitle("Help");

        Intent intent = getIntent();
        newStudentID = intent.getExtras().getStringArray("identity");
        uEmail = newStudentID[2];
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
