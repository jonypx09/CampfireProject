package com.example.jonat.campfire;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import backend.algorithms.Student;
import backend.database.DatabaseAdapter;

public class LoginActivity extends AppCompatActivity {

    DatabaseAdapter db;

    ProgressBar load;
    Button loginButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_login);
        setTitle("Log In");

        //Connect to the database
        db = new DatabaseAdapter(this);

        load = (ProgressBar) findViewById(R.id.loginProgress);
        loginButton = (Button) findViewById(R.id.loginButton);
    }

    public void checkFields(View view){

        EditText emailField = (EditText) findViewById(R.id.emailField);
        EditText passwordField = (EditText) findViewById(R.id.passwordField);
        String email = emailField.getText().toString();
        email = email.trim();
        String password = passwordField.getText().toString();

        if (email.equals("") || password.equals("")){

            //This notifies the user that there needs to be an email in the field
            AlertDialog missingEmailDialog = new AlertDialog.Builder(LoginActivity.this).create();
            missingEmailDialog.setTitle("Missing Fields");
            if (!email.equals("")){
                missingEmailDialog.setMessage("Please enter your password");
            }else{
                missingEmailDialog.setMessage("Please enter your email");
            }
            missingEmailDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Try Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            missingEmailDialog.show();


        }else{

            load.setVisibility(View.VISIBLE);
            loginButton.setText("Authenticating...");

            final String emailCopy = email;
            final String passwordCopy = password;

            new CountDownTimer(2000, 1000){
                public void onFinish(){
                    authenticate(emailCopy, passwordCopy);
                }

                public void onTick(long millisUntilFinished){

                }
            }.start();

        }
    }

    //Checks whether the user exists in the database; if so, check that the password is correct
    public void authenticate(String email, String password){

        //Perform validation here
        Student foundStudent;
        foundStudent = db.getStudent(email);

        if ((foundStudent != null) && (password.equals(foundStudent.getPass()))){
            load.setVisibility(View.INVISIBLE);
            loginButton.setText("Success!");
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.putExtra("userEmail", email);
            startActivity(mainIntent);


        }else{
            load.setVisibility(View.INVISIBLE);
            loginButton.setText("Log In");
            //This notifies the user that there needs to be an email in the field
            AlertDialog missingEmailDialog = new AlertDialog.Builder(LoginActivity.this).create();
            missingEmailDialog.setTitle("Authentication Failed");
            if (!password.equals(foundStudent.getPass())){
                missingEmailDialog.setMessage("Incorrect Password");
            }else{
                missingEmailDialog.setMessage("This account does not exist. Please Try Again.");
            }
            missingEmailDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Try Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            missingEmailDialog.show();
        }

    }
}
