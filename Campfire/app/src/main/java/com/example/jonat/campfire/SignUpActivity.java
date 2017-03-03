package com.example.jonat.campfire;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import backend.database.*;
import backend.algorithms.*;

public class SignUpActivity extends AppCompatActivity {

    DatabaseAdapter db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Change transition effect
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        //Connect to the database
        db = new DatabaseAdapter(this);
    }

    public void checkFields(View view){
        EditText fNameField = (EditText) findViewById(R.id.firstNameTextField);
        EditText sNameField = (EditText) findViewById(R.id.surnameTextField);
        EditText emailField = (EditText) findViewById(R.id.emailField);
        EditText passwordField = (EditText) findViewById(R.id.passwordField);
        EditText courseField = (EditText) findViewById(R.id.courseField);

        String firstName = fNameField.getText().toString();
        String surname = sNameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String course = courseField.getText().toString();

        boolean validFirstName;
        boolean validSurname;
        boolean validEmail;
        boolean validPassword;
        boolean validCourse;

        //Check each of the fields here
        firstName = firstName.trim();
        surname = surname.trim();
        email = email.trim();
        course = course.trim();

        validEmail = verifyEmail(email);
        validPassword = verifyPassword(password);

        if (firstName.equals("") || surname.equals("") || email.equals("") || password.equals("")
                || course.equals("") || (!(validEmail)) || (!(validPassword))){

            //This notifies the user that there needs to be an email in the field
            AlertDialog missingInfoDialog = new AlertDialog.Builder(SignUpActivity.this).create();
            missingInfoDialog.setTitle("Missing Fields");

            if (!(validEmail)){
                missingInfoDialog.setMessage("Invalid Email Address");
            }else if (!(validPassword)){
                missingInfoDialog.setMessage("Password must be at least 8 characters");
            }else{
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

            //Check if this student exists in the database

            //Send information to next activity
            String[] identity = {firstName, surname, email, course};

            //TEMPORARY STATEMENT
            Student newStudent = new Student(firstName, surname, email, course, null, null);
            //TEMPORARY STATEMENT

            Intent personalIntent = new Intent(this, PersonalizeActivity.class);
            personalIntent.putExtra("identity", identity);
            startActivity(personalIntent);
        }
    }

    public boolean verifyEmail(String email){
        if (email.contains("@")){
            return true;
        }else{
            return false;
        }
    }

    public boolean verifyPassword(String password){
        if (password.length() >= 8){
            return true;
        }else{
            return false;
        }
    }
}
