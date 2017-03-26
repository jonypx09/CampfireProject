package com.example.jonat.campfire;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.yarolegovich.lovelydialog.LovelyTextInputDialog;

import java.util.ArrayList;
import java.util.HashMap;

import backend.algorithms.Student;
import backend.database.DatabaseAdapter;
import backend.database.DbAdapter;

public class MyProfileActivity extends AppCompatActivity {

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

    FloatingActionButton fab;
    FloatingActionButton addPrevCourseFab;
    FloatingActionButton addElectiveCourseFab;
    FloatingActionButton addHobbiesFab;
    private boolean editMode = false;

    private Integer clockImage = R.drawable.ic_access_time_black_48dp;
    private Integer saveImage = R.drawable.ic_save_white_48dp;
    private Integer editImage = R.drawable.ic_edit_white_48dp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_profile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        addPrevCourseFab = (FloatingActionButton) findViewById(R.id.addPreviousCourseFab);
        addPrevCourseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPreviousCourse();
            }
        });

        addElectiveCourseFab = (FloatingActionButton) findViewById(R.id.addElectivesFab);
        addElectiveCourseFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addElectiveCourse();
            }
        });

        addHobbiesFab = (FloatingActionButton) findViewById(R.id.addHobbiesFab);
        addHobbiesFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                addHobby();
            }
        });

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Edit some specific elements of your profile", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                if (editMode){
                    if (userAuthenticated()){
                        fab.setImageResource(editImage);
                        addPrevCourseFab.setVisibility(View.INVISIBLE);
                        addElectiveCourseFab.setVisibility(View.INVISIBLE);
                        addHobbiesFab.setVisibility(View.INVISIBLE);
                        editMode = false;
                    }
                }else{
                    addPrevCourseFab.setVisibility(View.VISIBLE);
                    addElectiveCourseFab.setVisibility(View.VISIBLE);
                    addHobbiesFab.setVisibility(View.VISIBLE);
                    fab.setImageResource(saveImage);
                    editMode = true;
                }
            }
        });

        Intent intent = getIntent();
        uEmail = intent.getExtras().getString("userEmail");
        myStudent = DbAdapter.getStudent(uEmail);
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

    public boolean userAuthenticated(){
        new LovelyTextInputDialog(this)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setTitle("Authentication Required")
                .setMessage("Please enter your password to confirm changes")
                .setIcon(R.drawable.ic_security_black_48dp)
                .setInputFilter("Incorrect Password", new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.matches(myStudent.getPass());
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        DbAdapter.updateStudent(myStudent);
                        Toast.makeText(MyProfileActivity.this, "Changes Saved", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(MyProfileActivity.this, "Changes Cancelled", Toast.LENGTH_SHORT).show();
                    }
                })
                .setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .show();
        return true;
    }

    public void addPreviousCourse(){
        new LovelyTextInputDialog(this)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setTitle("Add Previous CS Course")
                .setMessage("Enter a course code")
                .setInputFilter("Invalid course code", new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.length() == 8;
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
//                        previousCSCourses.add(text);
                        myStudent.getCSCCourses().add(text);
                        updateCSCCourses();
                        //This doesn't update the student's information in the db!!!
                        Toast.makeText(MyProfileActivity.this, "Course Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Do Nothing Here
                    }
                })
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .show();
    }

    public void addElectiveCourse(){
        new LovelyTextInputDialog(this)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setTitle("Add Previous Elective Course")
                .setMessage("Enter a course code")
                .setInputFilter("Invalid course code", new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.length() == 8;
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        myStudent.getElectives().add(text);
                        updateElectiveCourses();
                        Toast.makeText(MyProfileActivity.this, "Course Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Do Nothing Here
                    }
                })
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .show();
    }

    public void addHobby(){
        new LovelyTextInputDialog(this)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setTitle("Add Hobby")
                .setMessage("Enter the name of an activity")
                .setInputFilter("Invalid Input", new LovelyTextInputDialog.TextFilter() {
                    @Override
                    public boolean check(String text) {
                        return text.length() > 0;
                    }
                })
                .setConfirmButton(android.R.string.ok, new LovelyTextInputDialog.OnTextInputConfirmListener() {
                    @Override
                    public void onTextInputConfirmed(String text) {
                        myStudent.getHobbies().add(text);
                        updateHobbies();
                        Toast.makeText(MyProfileActivity.this, "Hobby Added", Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton("Cancel", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Do Nothing Here
                    }
                })
                .setInputType(InputType.TYPE_CLASS_TEXT)
                .show();
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
