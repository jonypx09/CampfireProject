package com.example.jonat.campfire;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
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

    private Integer clockImage = R.drawable.ic_access_time_black_48dp;

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
        userEmail.setText("Your Email:   " + uEmail);

        pythonCheckbox = (CheckBox) findViewById(R.id.pythonCheckboxP);
        javaCheckbox = (CheckBox) findViewById(R.id.javaCheckboxP);
        cCheckbox = (CheckBox) findViewById(R.id.cCheckboxP);
        htmlCheckbox = (CheckBox) findViewById(R.id.htmlCheckboxP);
        javascriptCheckbox = (CheckBox) findViewById(R.id.javascriptCheckboxP);
        sqlCheckbox = (CheckBox) findViewById(R.id.sqlCheckboxP);

        sundayCheckbox = (CheckBox) findViewById(R.id.sundayCheckbox);
        sundayCheckbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sundayCheckbox.setChecked(true);
                openSundaySchedule();
            }
        });
        mondayCheckbox = (CheckBox) findViewById(R.id.mondayCheckbox);
        mondayCheckbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mondayCheckbox.setChecked(true);
                openMondaySchedule();
            }
        });
        tuesdayCheckbox = (CheckBox) findViewById(R.id.tuesdayCheckbox);
        tuesdayCheckbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                tuesdayCheckbox.setChecked(true);
                openTuesdaySchedule();
            }
        });
        wednesdayCheckbox = (CheckBox) findViewById(R.id.wednesdayCheckbox);
        wednesdayCheckbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                wednesdayCheckbox.setChecked(true);
                openWednesdaySchedule();
            }
        });
        thursdayCheckbox = (CheckBox) findViewById((R.id.thursdayCheckbox));
        thursdayCheckbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                thursdayCheckbox.setChecked(true);
                openThursdaySchedule();
            }
        });
        fridayCheckbox = (CheckBox) findViewById((R.id.fridayCheckbox));
        fridayCheckbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                fridayCheckbox.setChecked(true);
                openFridaySchedule();
            }
        });

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
                sundayCheckbox.setText("Sunday (Tap to open details)");
            }else if (day.equals("Monday") && schedule.get(day).size() != 0){
                mondayCheckbox.setChecked(true);
                mondayCheckbox.setText("Monday (Tap to open details)");
            }else if (day.equals("Tuesday") && schedule.get(day).size() != 0){
                tuesdayCheckbox.setChecked(true);
                tuesdayCheckbox.setText("Tuesday (Tap to open details)");
            }else if (day.equals("Wednesday") && schedule.get(day).size() != 0){
                wednesdayCheckbox.setChecked(true);
                wednesdayCheckbox.setText("Wednesday (Tap to open details)");
            }else if (day.equals("Thursday") && schedule.get(day).size() != 0){
                thursdayCheckbox.setChecked(true);
                thursdayCheckbox.setText("Thursday (Tap to open details)");
            }else if (day.equals("Friday") && schedule.get(day).size() != 0){
                fridayCheckbox.setChecked(true);
                fridayCheckbox.setText("Friday (Tap to open details)");
            }else if (day.equals("Saturday") && schedule.get(day).size() != 0){
                saturdayCheckbox.setChecked(true);
                saturdayCheckbox.setText("Saturday (Tap to open details)");
            }
        }

    }

    public void openSundaySchedule(){
        if (sundayCheckbox.isChecked()){
            ArrayList<String> times = this.schedule.get("Sunday");
            AlertDialog scheduleDialog = new AlertDialog.Builder(this).create();
            scheduleDialog.setTitle("Details for: Sunday");
            scheduleDialog.setMessage("Something1 \n");
            scheduleDialog.setMessage("Something2 \n");
            scheduleDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Close",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            scheduleDialog.show();
        }
    }

    public void openMondaySchedule(){
        if (mondayCheckbox.isChecked()){
            scheduleDialog("Monday");
        }
    }

    public void openTuesdaySchedule(){
        if (tuesdayCheckbox.isChecked()){
            scheduleDialog("Tuesday");
        }
    }

    public void openWednesdaySchedule(){
        if (wednesdayCheckbox.isChecked()){
            scheduleDialog("Wednesday");
        }
    }

    public void openThursdaySchedule(){
        if (thursdayCheckbox.isChecked()){
            scheduleDialog("Thursday");
        }
    }

    public void openFridaySchedule(){
        if (fridayCheckbox.isChecked()){
            scheduleDialog("Friday");
        }
    }

    public void openSaturdaySchedule(){
        if (saturdayCheckbox.isChecked()){
            scheduleDialog("Saturday");
        }
    }

    public void scheduleDialog(String day){
        ArrayList<String> times = this.schedule.get(day);
        AlertDialog scheduleDialog = new AlertDialog.Builder(this).create();
        scheduleDialog.setTitle("Details for: " + day);

        String details = "Available Times:\n\n";
        for (int i = 0; i < times.size(); i++) {
            details += times.get(i);
            if (i != times.size() - 1){
                details += "\n";
            }
        }
        scheduleDialog.setMessage(details);
        scheduleDialog.setIcon(clockImage);
        scheduleDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Close",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        scheduleDialog.show();
    }
}
