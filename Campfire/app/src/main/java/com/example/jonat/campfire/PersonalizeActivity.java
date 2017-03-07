package com.example.jonat.campfire;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import backend.algorithms.CSCCoursesCriteria;
import backend.algorithms.ElectivesCriteria;
import backend.algorithms.HobbiesCriteria;
import backend.algorithms.ProgrammingLanguagesCriteria;
import backend.database.DatabaseAdapter;

public class PersonalizeActivity extends AppCompatActivity {

    DatabaseAdapter db;

    private String[] newStudentID;

    private ProgrammingLanguagesCriteria newStudentLang;
    private CSCCoursesCriteria newStudentCourses;
    private ElectivesCriteria newStudentElectives;
    private HobbiesCriteria newStudentHobbies;

    Spinner pastimeSpinner;

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

        pastimeSpinner = (Spinner) findViewById(R.id.pastimeSpinner);
    }

    public void toScheduleScreen(View view){

//        Course newCourse = new Course(newStudentID[3], "Some Course Name", "Some Instructor");
//        Student newStudent = new Student(newStudentID[0], newStudentID[1], newStudentID[2], newStudentID[3], null);
//        db.addStudent(newStudent);
//        db.addCourse(newCourse);

        EditText PreviousCourseField = (EditText) findViewById(R.id.previousField);
        EditText ElectiveCourseField = (EditText) findViewById(R.id.electivesField);

        String previousCourse = PreviousCourseField.getText().toString();
        String electiveCourse = ElectiveCourseField.getText().toString();

        previousCourse = previousCourse.trim();
        electiveCourse = electiveCourse.trim();

        if (previousCourse.equals("") || electiveCourse.equals("") || previousCourse.length() != 8
                || electiveCourse.length() != 8 || pastimeSpinner.getSelectedItem().toString().equals("Choose an activity")){
            AlertDialog missingInfoDialog = new AlertDialog.Builder(PersonalizeActivity.this).create();
            if (previousCourse.length() != 8){
                missingInfoDialog.setTitle("Invalid Previous Course Code");
                missingInfoDialog.setMessage("Please enter a valid course (i.e. CSC148H1)");
            }else if (electiveCourse.length() != 8) {
                missingInfoDialog.setTitle("Invalid Elective Course Code");
                missingInfoDialog.setMessage("Please enter a valid course code (i.e. CSC207H1)");
            }else if (pastimeSpinner.getSelectedItem().toString().equals("Choose an activity")){
                missingInfoDialog.setTitle("Choose a pastime");
                missingInfoDialog.setMessage("You must choose a pastime activity");
            }else{
                missingInfoDialog.setTitle("Missing Fields");
                missingInfoDialog.setMessage("You are missing one or more fields");
            }
            missingInfoDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Try Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            missingInfoDialog.show();
        }else{
            String pastime = pastimeSpinner.getSelectedItem().toString();
            String[] fullStudentID = {newStudentID[0], newStudentID[1], newStudentID[2], newStudentID[3], newStudentID[4],
                    previousCourse, electiveCourse, pastime};
            Intent scheduleIntent = new Intent(this, ScheduleActivity.class);
            scheduleIntent.putExtra("identity", newStudentID);
            startActivity(scheduleIntent);
        }
    }
}
