package com.example.jonat.campfire;

import android.graphics.Color;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

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
    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
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
}
