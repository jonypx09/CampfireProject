package com.example.jonat.campfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import backend.algorithms.*;
import backend.database.*;

public class PersonalizeActivity extends AppCompatActivity {

    DatabaseAdapter db;

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

        //Connect to the database
        db = new DatabaseAdapter(this);

        Intent intent = getIntent();
        newStudentID = intent.getExtras().getStringArray("identity");
    }

    public void toMainScreen(View view){

        Course newCourse = new Course(newStudentID[3], "Some Course Name", "Some Instructor");
        Student newStudent = new Student(newStudentID[0], newStudentID[1], newStudentID[2], newStudentID[3], null, null);
        db.addStudent(newStudent);
        db.addCourse(newCourse);

        Intent mainIntent = new Intent(this, ScheduleActivity.class);
        mainIntent.putExtra("userEmail", newStudentID[2]);
        startActivity(mainIntent);
    }
}
