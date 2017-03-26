package com.example.jonat.campfire;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import backend.algorithms.Student;
import backend.database.DatabaseAdapter;
import backend.database.DbAdapter;

public class ChangePasswordActivity extends AppCompatActivity {

    private String[] newStudentID;
    private String uEmail;
    private Student currentStudent;

    ProgressBar passwordLoad;
    Button changePasswordButton;

    EditText oldPasswordField;
    EditText newPasswordField;
    EditText confirmPasswordField;

    ImageView oldPassWrongIcon;
    ImageView newPassWrongIcon;
    ImageView confirmPassWrongIcon;
    ImageView passwordChangedIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        setTitle("Change Password");

        Intent intent = getIntent();
        newStudentID = intent.getExtras().getStringArray("identity");
        uEmail = newStudentID[2];
        currentStudent = DbAdapter.getStudent(uEmail);

        oldPasswordField = (EditText) findViewById(R.id.oldPasswordTextField);
        newPasswordField = (EditText) findViewById(R.id.newPasswordTextField);
        confirmPasswordField = (EditText) findViewById(R.id.confirmPassTextField);

        passwordLoad = (ProgressBar) findViewById(R.id.passwordChangeProgress);
        changePasswordButton = (Button) findViewById(R.id.changePasswordButton);

        oldPassWrongIcon = (ImageView) findViewById(R.id.oldPassWrongIcon);
        newPassWrongIcon = (ImageView) findViewById(R.id.newPassWrongIcon);
        confirmPassWrongIcon = (ImageView) findViewById(R.id.confirmPassWrongIcon);
        passwordChangedIcon = (ImageView) findViewById(R.id.passwordChangedIcon);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.putExtra("identity", newStudentID);
            startActivity(mainIntent);
            return true;
        }
        return false;
    }

    public void obtainPasswordFields(View view){
        EditText oldPasswordField = (EditText) findViewById(R.id.oldPasswordTextField);
        EditText newPasswordField = (EditText) findViewById(R.id.newPasswordTextField);
        EditText confirmPasswordField = (EditText) findViewById(R.id.confirmPassTextField);

        String oldPassword = oldPasswordField.getText().toString();
        String newPassword = newPasswordField.getText().toString();
        String confirmPassword = confirmPasswordField.getText().toString();

        passwordLoad.setVisibility(View.VISIBLE);
        oldPassWrongIcon.setVisibility(View.INVISIBLE);
        newPassWrongIcon.setVisibility(View.INVISIBLE);
        confirmPassWrongIcon.setVisibility(View.INVISIBLE);
        changePasswordButton.setText("Changing Password...");
        changePasswordButton.setEnabled(false);

        final String oldPasswordCopy = oldPassword;
        final String newPasswordCopy = newPassword;
        final String copyPasswordCopy = confirmPassword;

        new CountDownTimer(4000, 1000){

            public void onFinish(){
                checkPasswordFields(oldPasswordCopy, newPasswordCopy, copyPasswordCopy);
            }

            public void onTick(long millisUntilFinished){
                //Do nothing on ticks
            }
        }.start();
    }

    public void checkPasswordFields(String oldPassword, String newPassword, final String confirmPassword){
        passwordLoad.setVisibility(View.INVISIBLE);
        if (oldPassword.equals("") || newPassword.equals("") || confirmPassword.equals("")){
            AlertDialog missingInfoDialog = new AlertDialog.Builder(ChangePasswordActivity.this).create();
            if (oldPassword.equals("")){
                oldPassWrongIcon.setVisibility(View.VISIBLE);
            }
            if (newPassword.equals("")) {
                newPassWrongIcon.setVisibility(View.VISIBLE);
            }
            if (confirmPassword.equals("")){
                confirmPassWrongIcon.setVisibility(View.VISIBLE);
            }
            missingInfoDialog.setTitle("Missing Fields");
            missingInfoDialog.setMessage("Missing one or more fields!");
            missingInfoDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Try Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            changePasswordButton.setText("Change Password");
                            changePasswordButton.setEnabled(true);
                            dialog.dismiss();
                        }
                    });
            missingInfoDialog.show();
        }else{

            String currentPassword = newStudentID[3];

            if (!(currentPassword.equals(oldPassword))){
                oldPassWrongIcon.setVisibility(View.VISIBLE);
                AlertDialog wrongPassInfoDialog = new AlertDialog.Builder(ChangePasswordActivity.this).create();
                wrongPassInfoDialog.setTitle("Incorrect Password");
                wrongPassInfoDialog.setMessage("According to our records, you entered the wrong password. Please Try Again.");
                wrongPassInfoDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Try Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                changePasswordButton.setText("Change Password");
                                changePasswordButton.setEnabled(true);
                                oldPasswordField.setText("");
                                dialog.dismiss();
                            }
                        });
                wrongPassInfoDialog.show();
            }else if (!(newPassword.equals(confirmPassword))){
                newPassWrongIcon.setVisibility(View.VISIBLE);
                confirmPassWrongIcon.setVisibility(View.VISIBLE);
                AlertDialog mismatchPassDialog = new AlertDialog.Builder(ChangePasswordActivity.this).create();
                mismatchPassDialog.setTitle("New Password Mismatch");
                mismatchPassDialog.setMessage("The new passwords do not match. Please Try Again");
                mismatchPassDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Try Again",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                changePasswordButton.setText("Change Password");
                                changePasswordButton.setEnabled(true);
                                newPasswordField.setText("");
                                confirmPasswordField.setText("");
                                dialog.dismiss();
                            }
                        });
                mismatchPassDialog.show();
            }else{
                currentStudent.setPass(newPassword);
                DbAdapter.updateStudent(currentStudent);

                changePasswordButton.setText("Password Changed!");
                passwordChangedIcon.setVisibility(View.VISIBLE);
                new CountDownTimer(1500, 1000){

                    public void onFinish(){
                        returnToMainScreen();
                    }

                    public void onTick(long millisUntilFinished){
                        //Do nothing on ticks
                    }
                }.start();
            }
        }
    }

    public void returnToMainScreen(){
        Intent mainIntent = new Intent(this, MainActivity.class);
        mainIntent.putExtra("identity", newStudentID);
        startActivity(mainIntent);
    }

}
