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
import android.view.Window;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

import backend.algorithms.Student;
import backend.database.DatabaseAdapter;
import backend.database.DbAdapter;

public class ClassmatesProfileActivity extends AppCompatActivity {

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
        getWindow().requestFeature(Window.FEATURE_CONTENT_TRANSITIONS);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classmates_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        uEmail = intent.getExtras().getString("userEmail");
        myStudent = DbAdapter.getStudent(uEmail);
        setTitle(myStudent.getFname() + " " + myStudent.getLname());

        View view = findViewById(R.id.userInfo);
        TextView userEmail = (TextView) view.findViewById(R.id.emailTextview);
        userEmail.setText("Student's Email:   " + uEmail);

        TextView greeting = (TextView) view.findViewById(R.id.aboutStudentTextview);
        greeting.append(" " + myStudent.getFname() + ":");

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
        saturdayCheckbox = (CheckBox) findViewById(R.id.saturdayCheckbox);
        saturdayCheckbox.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                saturdayCheckbox.setChecked(true);
                openSaturdaySchedule();
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

        updateCSCCourses();
        updateElectiveCourses();
        updateHobbies();

        for (String day : schedule.keySet()){
            if (day.equals("Sunday") && schedule.get(day).size() != 0){
                sundayCheckbox.setChecked(true);
                sundayCheckbox.setEnabled(true);
                sundayCheckbox.setText("Sunday (Tap to open details)");
            }else if (day.equals("Monday") && schedule.get(day).size() != 0){
                mondayCheckbox.setChecked(true);
                mondayCheckbox.setEnabled(true);
                mondayCheckbox.setText("Monday (Tap to open details)");
            }else if (day.equals("Tuesday") && schedule.get(day).size() != 0){
                tuesdayCheckbox.setChecked(true);
                tuesdayCheckbox.setEnabled(true);
                tuesdayCheckbox.setText("Tuesday (Tap to open details)");
            }else if (day.equals("Wednesday") && schedule.get(day).size() != 0){
                wednesdayCheckbox.setChecked(true);
                wednesdayCheckbox.setEnabled(true);
                wednesdayCheckbox.setText("Wednesday (Tap to open details)");
            }else if (day.equals("Thursday") && schedule.get(day).size() != 0){
                thursdayCheckbox.setChecked(true);
                thursdayCheckbox.setEnabled(true);
                thursdayCheckbox.setText("Thursday (Tap to open details)");
            }else if (day.equals("Friday") && schedule.get(day).size() != 0){
                fridayCheckbox.setChecked(true);
                fridayCheckbox.setEnabled(true);
                fridayCheckbox.setText("Friday (Tap to open details)");
            }else if (day.equals("Saturday") && schedule.get(day).size() != 0){
                saturdayCheckbox.setChecked(true);
                saturdayCheckbox.setEnabled(true);
                saturdayCheckbox.setText("Saturday (Tap to open details)");
            }
        }

    }

    public void openSundaySchedule(){
        if (sundayCheckbox.isChecked()){
            scheduleDialog("Sunday");
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

    public void updateCSCCourses(){
        String headerCS = ("Previous CSC Courses:\n");
        String csCourses = "";
        for (String course: previousCSCourses){
            csCourses += "- " + course;
            if (!previousCSCourses.get(previousCSCourses.size() - 1).equals(course)){
                csCourses += "\n";
            }
        }
        previousCSCourse.setText(headerCS + csCourses);
    }

    public void updateElectiveCourses(){
        String headerE = ("Previous Elective Courses:\n");
        String electiveCourses = "";
        for (String course: previousElectiveCourses){
            electiveCourses += "- " + course;
            if (!previousElectiveCourses.get(previousElectiveCourses.size() - 1).equals(course)){
                electiveCourses += "\n";
            }
        }
        previousElective.setText(headerE + electiveCourses);
    }

    public void updateHobbies(){
        String headerO = ("Your Hobbies:\n");
        String hobbies = "";
        for (String hobby: hobbiesList){
            hobbies += "- " + hobby;
            if (!hobbiesList.get(hobbiesList.size() - 1).equals(hobby)){
                hobbies += "\n";
            }
        }
        hobbyTextview.setText(headerO + hobbies);
    }
}

