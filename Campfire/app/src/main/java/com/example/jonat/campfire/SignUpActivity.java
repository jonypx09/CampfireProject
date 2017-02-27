package com.example.jonat.campfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class SignUpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Change transition effect
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_sign_up);

        setTitle("Sign Up");
    }

    public void checkFields(){
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
    }

    public void toPersonalizeScreen(View view){
        Intent personalIntent = new Intent(this, PersonalizeActivity.class);
        startActivity(personalIntent);
    }

    public boolean verifyEmail(){
        return true;
    }
}
