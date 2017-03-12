package com.example.jonat.campfire;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import backend.algorithms.Student;
import backend.database.DatabaseAdapter;

public class MyProfileActivity extends AppCompatActivity {

    DatabaseAdapter db;
    private Student myStudent;
    private String uEmail;
    private ArrayList<String> programmingLanguages;
    private ArrayList<String> previousCSCourses;
    private ArrayList<String> previousElectiveCourses;
    private ArrayList<String> hobbiesList;
    private HashMap<String, ArrayList<String>> schedule;

    CheckBox pythonCheckbox;
    CheckBox javaCheckbox;
    CheckBox cCheckbox;
    CheckBox htmlCheckbox;
    CheckBox javascriptCheckbox;
    CheckBox sqlCheckbox;

    CheckBox sundayCheckbox;
    CheckBox mondayCheckbox;
    CheckBox tuesdayCheckbox;
    CheckBox wednesdayCheckbox;
    CheckBox thursdayCheckbox;
    CheckBox fridayCheckbox;
    CheckBox saturdayCheckbox;

    TextView previousCSCourse;
    TextView previousElective;
    TextView hobbyTextview;

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

        pythonCheckbox = (CheckBox) findViewById(R.id.pythonCheckboxP);
        javaCheckbox = (CheckBox) findViewById(R.id.javaCheckboxP);
        cCheckbox = (CheckBox) findViewById(R.id.cCheckboxP);
        htmlCheckbox = (CheckBox) findViewById(R.id.htmlCheckboxP);
        javascriptCheckbox = (CheckBox) findViewById(R.id.javascriptCheckboxP);
        sqlCheckbox = (CheckBox) findViewById(R.id.sqlCheckboxP);

        sundayCheckbox = (CheckBox) findViewById(R.id.sundayCheckbox);
        mondayCheckbox = (CheckBox) findViewById(R.id.mondayCheckbox);
        tuesdayCheckbox = (CheckBox) findViewById(R.id.tuesdayCheckbox);
        wednesdayCheckbox = (CheckBox) findViewById(R.id.wednesdayCheckbox);
        thursdayCheckbox = (CheckBox) findViewById((R.id.thursdayCheckbox));
        fridayCheckbox = (CheckBox) findViewById((R.id.fridayCheckbox));

        previousCSCourse = (TextView) findViewById(R.id.previousCourseTextview);
        previousElective = (TextView) findViewById(R.id.electiveCourseTextview);
        hobbyTextview = (TextView) findViewById(R.id.hobbiesTextview);

        programmingLanguages = myStudent.getProgramming();
        previousCSCourses = myStudent.getCSCCourses();
        previousElectiveCourses = myStudent.getElectives();
        hobbiesList = myStudent.getHobbies();
        schedule = myStudent.getCalendar();

        for (String language : programmingLanguages) {
            if (language == null){
                break;
            }else if (language.equals("Python")){
                pythonCheckbox.setChecked(true);
            }else if (language.equals("Java")){
                javaCheckbox.setChecked(true);
            }else if (language.equals("C")){
                cCheckbox.setChecked(true);
            }else if (language.equals("HTML")){
                htmlCheckbox.setChecked(true);
            }else if (language.equals("Javascript")){
                javascriptCheckbox.setChecked(true);
            }else {
                sqlCheckbox.setChecked(true);
            }
        }

        for (String course: previousCSCourses){
            previousCSCourse.setText("Previous CSC Course:   " + course);
        }

        for (String course: previousElectiveCourses){
            previousElective.setText("Previous Elective Course:   " + course);
        }

        for (String hobby: hobbiesList){
            hobbyTextview.setText("Your Hobbies:   " + hobby);
        }

        for (String day : schedule.keySet()){
            if (day.equals("Sunday") && schedule.get(day).size() != 0){
                sundayCheckbox.setChecked(true);
            }else if (day.equals("Monday") && schedule.get(day).size() != 0){
                mondayCheckbox.setChecked(true);
            }else if (day.equals("Tuesday") && schedule.get(day).size() != 0){
                tuesdayCheckbox.setChecked(true);
            }else if (day.equals("Wednesday") && schedule.get(day).size() != 0){
                wednesdayCheckbox.setChecked(true);
            }else if (day.equals("Thursday") && schedule.get(day).size() != 0){
                thursdayCheckbox.setChecked(true);
            }else if (day.equals("Friday") && schedule.get(day).size() != 0){
                fridayCheckbox.setChecked(true);
            }else if (day.equals("Saturday") && schedule.get(day).size() != 0){
                saturdayCheckbox.setChecked(true);
            }
        }

    }
}
