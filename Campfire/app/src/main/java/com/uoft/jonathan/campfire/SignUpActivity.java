package com.uoft.jonathan.campfire;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import backend.algorithms.Student;
import backend.database.DbAdapter;

public class SignUpActivity extends AppCompatActivity {

    ProgressBar load;
    Button createAccountButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Change transition effect
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_sign_up);
        setTitle("Sign Up");

        load = (ProgressBar) findViewById(R.id.signupProgress);
        createAccountButton = (Button) findViewById(R.id.createAccountButton);
    }

    public void obtainFields(View view){
        EditText fNameField = (EditText) findViewById(R.id.firstNameEditText);
        EditText sNameField = (EditText) findViewById(R.id.newPasswordTextField);
        EditText emailField = (EditText) findViewById(R.id.confirmPassTextField);
        EditText passwordField = (EditText) findViewById(R.id.passwordField);
        EditText courseField = (EditText) findViewById(R.id.courseField);

        String firstName = fNameField.getText().toString();
        String surname = sNameField.getText().toString();
        String email = emailField.getText().toString();
        String password = passwordField.getText().toString();
        String course = courseField.getText().toString();

        boolean validFirstName;
        boolean validSurname;
        final boolean validEmail;
        final boolean validPassword;
        boolean validCourse;

        //Check each of the fields here
        firstName = firstName.trim();
        surname = surname.trim();
        email = email.trim();
        course = course.trim();

        validEmail = verifyEmail(email);
        validPassword = verifyPassword(password);

        //Check if this student exists in the database; if so, prevent user from continuing
        final Student alreadyExists = DbAdapter.getStudent(email);

        load.setVisibility(View.VISIBLE);
        createAccountButton.setText("Checking Fields...");
        createAccountButton.setEnabled(false);

        final Student alreadyExistsCopy = alreadyExists;
        final String firstNameCopy = firstName;
        final String surnameCopy = surname;
        final String courseCopy = course;
        final String emailCopy = email;
        final String passwordCopy = password;
        final boolean validEmailCopy = validEmail;
        final boolean validPasswordCopy = validPassword;

        new CountDownTimer(3000, 1000){

            public void onFinish(){
//                authenticate(emailCopy, passwordCopy);
                checkFields(alreadyExistsCopy, firstNameCopy, surnameCopy,
                        emailCopy, passwordCopy, courseCopy, validEmailCopy, validPasswordCopy);
            }

            public void onTick(long millisUntilFinished){

            }
        }.start();
    }

    public void checkFields(Student alreadyExists, String firstName, String surname, String email,
                            String password, String course, boolean validEmail, boolean validPassword){

        load.setVisibility(View.INVISIBLE);

        if (alreadyExists != null || firstName.equals("") || surname.equals("") || email.equals("") || password.equals("")
                || course.equals("") || (!(validEmail)) || (!(validPassword)) || course.length() != 8){

            createAccountButton.setText("Create Account");
            createAccountButton.setEnabled(true);
            //This notifies the user that there needs to be an email in the field
            AlertDialog missingInfoDialog = new AlertDialog.Builder(SignUpActivity.this).create();
            if (alreadyExists != null){
                missingInfoDialog.setTitle("User Already Exists");
                missingInfoDialog.setMessage("There is already a user associated with this email address. Please enter a different one.");
            }else if (course.length() != 8) {
                missingInfoDialog.setTitle("Invalid Course Code");
                missingInfoDialog.setMessage("Please enter a valid course code (i.e. CSC207H1)");
            }else{
                missingInfoDialog.setTitle("Missing Fields");

                if (!(validEmail)){
                    missingInfoDialog.setMessage("Invalid Email Address");
                }else if (!(validPassword)){
                    missingInfoDialog.setMessage("Password must be at least 8 characters");
                }else{
                    missingInfoDialog.setMessage("You are missing one or more fields");
                }
            }
            missingInfoDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Try Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            missingInfoDialog.show();
        }else{
            createAccountButton.setText("Success!");
            //Send information to next activity
            String[] identity = {firstName, surname, email, password, course};
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

    public void closeKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }
}
