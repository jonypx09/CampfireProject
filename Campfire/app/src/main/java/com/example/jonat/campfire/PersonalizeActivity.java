package com.example.jonat.campfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import backend.algorithms.*;
import backend.database.*;

public class PersonalizeActivity extends AppCompatActivity {

    private String[] newStudentID;
    private ProgrammingLanguagesCriteria newStudentLang;
    private CSCCoursesCriteria newStudentCourses;
    private ElectivesCriteria newStudentElectives;
    private HobbiesCriteria newStudentHobbies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_personalize);
        setTitle("Personalize");

        Intent intent = getIntent();
        newStudentID = intent.getExtras().getStringArray("identity");
    }

    public void toMainScreen(View view){
        Intent mainIntent = new Intent(this, MainActivity.class);
        startActivity(mainIntent);
    }
}
