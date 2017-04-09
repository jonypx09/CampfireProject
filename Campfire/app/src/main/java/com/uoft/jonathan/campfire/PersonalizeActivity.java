package com.uoft.jonathan.campfire;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

public class PersonalizeActivity extends AppCompatActivity {

    private String[] newStudentID;
    private String[] programmingLanguages = {null, null, null, null, null, null};

    Spinner pastimeSpinner;
    CheckBox pythonCheckbox;
    CheckBox javaCheckbox;
    CheckBox cCheckbox;
    CheckBox htmlCheckbox;
    CheckBox javascriptCheckbox;
    CheckBox sqlCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_personalize);
        setTitle("Personalize");

        Intent intent = getIntent();
        newStudentID = intent.getExtras().getStringArray("identity");

        pastimeSpinner = (Spinner) findViewById(R.id.pastimeSpinner);
        pythonCheckbox = (CheckBox) findViewById(R.id.pythonCheckbox);
        javaCheckbox = (CheckBox) findViewById(R.id.javaCheckbox);
        cCheckbox = (CheckBox) findViewById(R.id.cCheckbox);
        htmlCheckbox = (CheckBox) findViewById(R.id.htmlCheckbox);
        javascriptCheckbox = (CheckBox) findViewById(R.id.javascriptCheckbox);
        sqlCheckbox = (CheckBox) findViewById(R.id.sqlCheckbox);
    }

    public void processUserPreferences(View view){

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
            if (pythonCheckbox.isChecked()){
                programmingLanguages[0] = "Python";
            }
            if (javaCheckbox.isChecked()){
                programmingLanguages[1] = "Java";
            }
            if (cCheckbox.isChecked()){
                programmingLanguages[2] = "C";
            }
            if (htmlCheckbox.isChecked()){
                programmingLanguages[3] = "HTML";
            }
            if (javascriptCheckbox.isChecked()){
                programmingLanguages[4] = "Javascript";
            }
            if (sqlCheckbox.isChecked()){
                programmingLanguages[5] = "SQL";
            }

            String pastime = pastimeSpinner.getSelectedItem().toString();

            //Prompt User for Input if "Other" is selected
            if (pastime.equals("Other (Enter Your Own)")){
                LayoutInflater pastimeInflator = LayoutInflater.from(this);
                View inputView = pastimeInflator.inflate(R.layout.pastime_input_dialog_box, null);
                AlertDialog.Builder pastimeBuilderInput = new AlertDialog.Builder(this);
                pastimeBuilderInput.setView(inputView);

                final String previousCourseCopy = previousCourse;
                final String electiveCourseCopy = electiveCourse;
                final EditText userInputPastimeDialog = (EditText) inputView.findViewById(R.id.userInputPastime);
                pastimeBuilderInput
                        .setCancelable(false)
                        .setPositiveButton("Enter", new DialogInterface.OnClickListener(){
                            public void onClick(DialogInterface dialogBox, int id){
                                String custom = userInputPastimeDialog.getText().toString();
                                custom = custom.trim();
                                final String customPastime = custom;
                                if (!customPastime.equals("")) {
                                    toScheduleScreen(previousCourseCopy, electiveCourseCopy, customPastime);
                                }
                            }
                        });
                AlertDialog alertDialogPastime = pastimeBuilderInput.create();
                alertDialogPastime.show();
            }else{
                toScheduleScreen(previousCourse, electiveCourse, pastime);
            }
        }
    }

    public void toScheduleScreen(String previousCourse, String electiveCourse, String pastime){
        String[] fullStudentID = {newStudentID[0], newStudentID[1], newStudentID[2], newStudentID[3], newStudentID[4],
                previousCourse, electiveCourse, pastime};
        Intent scheduleIntent = new Intent(this, ScheduleActivity.class);
        scheduleIntent.putExtra("identity", fullStudentID);
        scheduleIntent.putExtra("programmingLanguages", programmingLanguages);
        startActivity(scheduleIntent);
    }

    public void closeKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
