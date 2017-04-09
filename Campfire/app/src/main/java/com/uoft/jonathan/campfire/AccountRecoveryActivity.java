package com.uoft.jonathan.campfire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.List;

import backend.algorithms.Student;
import backend.database.DbAdapter;

public class AccountRecoveryActivity extends AppCompatActivity {

    private RadioButton credentialsRButton;
    private RadioButton permissionRButton;
    private TextView fNamePrompt;
    private TextView lastNamePrompt;
    private TextView emailPrompt;
    private TextView permissionExplanation;
    private EditText fNameEditText;
    private EditText lastNameEditText;
    private EditText emailEditText;
    private Button checkCredentialsButton;
    private Button requestPermissionButton;

    private Handler handler;
    private List<Student> allStudents;
    private Student currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_recovery);
        setTitle("Account Recovery");
        credentialsRButton = (RadioButton) findViewById(R.id.credentialsRButton);
        permissionRButton = (RadioButton) findViewById(R.id.permissionRButton);
        fNamePrompt = (TextView) findViewById(R.id.fNamePrompt);
        lastNamePrompt = (TextView) findViewById(R.id.lastNamePrompt);
        emailPrompt = (TextView) findViewById(R.id.emailPrompt);
        permissionExplanation = (TextView) findViewById(R.id.permissionExplanation);
        fNameEditText = (EditText) findViewById(R.id.firstNameEditText);
        lastNameEditText = (EditText) findViewById(R.id.lastNameEditText);
        emailEditText = (EditText) findViewById(R.id.emailEditText);
        checkCredentialsButton = (Button) findViewById(R.id.checkCredentialsButton);
        requestPermissionButton = (Button) findViewById(R.id.requestPermissionButton);

        //It is vital to create a thread when performing a DbAdapter call to avoid any delays with the
        //app
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                allStudents = DbAdapter.getAllStudents();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        }).start();
    }

    public void onRadioButtonClicked(View view) {
        //Is there a radio button that's been checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Radiobutton functionality (clicking one would uncheck the other)
        switch(view.getId()) {
            case R.id.credentialsRButton:
                permissionRButton.setChecked(false);
                fNamePrompt.setTextColor(Color.WHITE);
                lastNamePrompt.setTextColor(Color.WHITE);
                emailPrompt.setTextColor(Color.WHITE);
                fNameEditText.setEnabled(true);
                lastNameEditText.setEnabled(true);
                emailEditText.setEnabled(true);
                checkCredentialsButton.setEnabled(true);
                permissionExplanation.setTextColor(Color.argb(255, 0, 70, 121));
                requestPermissionButton.setEnabled(false);
                if (checked)
                    break;
            case R.id.permissionRButton:
                credentialsRButton.setChecked(false);
                fNamePrompt.setTextColor(Color.argb(255, 0, 70, 121));
                lastNamePrompt.setTextColor(Color.argb(255, 0, 70, 121));
                emailPrompt.setTextColor(Color.argb(255, 0, 70, 121));
                fNameEditText.setEnabled(false);
                lastNameEditText.setEnabled(false);
                emailEditText.setEnabled(false);
                checkCredentialsButton.setEnabled(false);
                permissionExplanation.setTextColor(Color.WHITE);
                requestPermissionButton.setEnabled(true);
                if (checked)
                    break;
        }
    }

    public void checkCredentials(View view){
        String fName = fNameEditText.getText().toString();
        fName = fName.trim();
        String lName = lastNameEditText.getText().toString();
        lName = lName.trim();
        String email = emailEditText.getText().toString();
        email = email.trim();
        final String emailCopy = email;
        final String fNameCopy = fName;
        final String lNameCopy = lName;

        final ProgressDialog progressDialog = new ProgressDialog(AccountRecoveryActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setTitle("Checking Credentials");
        progressDialog.show();
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentStudent = DbAdapter.getStudent(emailCopy);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Student foundStudent = null;
                        for (Student s: allStudents){
                            if (s.getEmail().equals(emailCopy)){
                                foundStudent = s;
                            }
                        }
                        if ((foundStudent != null) && (fNameCopy.equals(foundStudent.getFname()))
                                && (lNameCopy.equals(foundStudent.getLname()))){
                            allowPasswordChange();
                        }else{
                            new MaterialDialog.Builder(AccountRecoveryActivity.this)
                                    .title("User not found")
                                    .content("Unable to find account associated with these credentials.")
                                    .positiveText("Try Again")
                                    .show();
                        }
                    }
                });
            }
        }).start();

    }

    public void allowPasswordChange(){
        new MaterialDialog.Builder(this)
                .title("User Found!")
                .content("Enter your new password:")
                .inputRange(8, 50)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input("", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        String userInput = input.toString();
                        changePassword(userInput);
                    }
                }).show();
    }

    public void changePassword(String newPassword){
        final ProgressDialog progressDialog = new ProgressDialog(AccountRecoveryActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setTitle("Changing Password");
        progressDialog.show();
        handler = new Handler();
        final String newPassCopy = newPassword;
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentStudent.setPass(newPassCopy);
                DbAdapter.updateStudent(currentStudent);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(AccountRecoveryActivity.this, "Password Changed", Toast.LENGTH_LONG).show();
                        Intent signInIntent = new Intent(AccountRecoveryActivity.this, LoginActivity.class);
                        startActivity(signInIntent);
                    }
                });
            }
        }).start();
    }


    public void enterEmail(View view){
        new MaterialDialog.Builder(this)
                .title("Enter Email")
                .content("Enter the email associated with your account:")
                .inputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS)
                .input("", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        String userInput = input.toString();
                        checkEmail(userInput);
                    }
                }).show();
    }

    public void checkEmail(String email){
        final ProgressDialog progressDialog = new ProgressDialog(AccountRecoveryActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setTitle("Checking Email");
        progressDialog.show();
        final String emailCopy = email;
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentStudent = DbAdapter.getStudent(emailCopy);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Student foundStudent = null;
                        for (Student s: allStudents){
                            if (s.getEmail().equals(emailCopy)){
                                foundStudent = s;
                            }
                        }
                        if ((foundStudent != null)){
                            sendRequest(foundStudent);
                        }else{
                            new MaterialDialog.Builder(AccountRecoveryActivity.this)
                                    .title("User not found")
                                    .content("Unable to find account associated with this email")
                                    .positiveText("Try Again")
                                    .show();
                        }
                    }
                });
            }
        }).start();
    }

    public void sendRequest(Student s){
        final ProgressDialog progressDialog = new ProgressDialog(AccountRecoveryActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setTitle("Sending Request");
        progressDialog.show();
        final String password = s.getPass() + "*";
        final Student sCopy = s;
        sCopy.setPass(password);
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DbAdapter.updateStudent(sCopy);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(AccountRecoveryActivity.this, "Request Sent", Toast.LENGTH_LONG).show();
                        Intent signInIntent = new Intent(AccountRecoveryActivity.this, LoginActivity.class);
                        startActivity(signInIntent);
                    }
                });
            }
        }).start();
    }
}
