package com.uoft.jonathan.campfire;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import backend.algorithms.Comparable;
import backend.algorithms.Student;
import backend.database.DbAdapter;

public class LoginActivity extends AppCompatActivity {

    ProgressBar load;
    Button loginButton;
    ImageView correctLogin;
    ImageView incorrectLogin;
    ImageView incorrectEmailIcon;
    ImageView incorrectPasswordIcon;

    private Handler handler;
    private List<Student> allStudents;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    private String[] possDescriptions = {"I like hardware!",
                                         "Team up with me if you wanna have loads of fun :)",
                                         "Work hard.. Then work harder ;)",
                                         "I like android development!!!",
                                         "Work late and sleep late is my motto",
                                         "Shooting for a lazy 60, just want a chill partner",
                                         "Message me if you have experience with Web",
                                         "I use Vim!",
                                         "If you use vim don't talk to me >:(",
                                         "If you don't use Linux need not apply",
                                         "I do it all! Don't hesitate to reach out!",
                                         "I got a 90 in 373",
                                         "Working with Angular changed my life",
                                         "Got tons of ideas, just need a friend :(",
                                         "Code from sun up till sun down, I'm like a reverse nightowl, Which I guess is a dayowl, but owls aren't really up during the day, so I guess more of a daybird",
                                         "I don't like bios"};

    private String[] possLanguages = {"Python", "Java", "C", "HTML", "Javascript", "SQL"};
    private String[] possHobbies = {"Arts/Content Creation", "Music", "Sports", "Video Games",
                                    "Cooking", "TV/Netflix", "Swimming", "Watching Sports",
                                    "Photography", "Parkour", "Riddles", "Puppeteering",
                                    "Yodeling", "Dancing", "Freestyle Rapping", "Table Tennis",
                                    "Exploring", "Hiking", "Programming", "Writing"};
    private String[] days = {"Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"};

    private HashMap<String, ArrayList<String>> schedMap = new HashMap<String, ArrayList<String>>();

    private ArrayList<ArrayList<Comparable>> criterias = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_login);
        setTitle("Log In");

        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                } else {
                    // User is signed out
                }
            }
        };

        load = (ProgressBar) findViewById(R.id.loginProgress);
        loginButton = (Button) findViewById(R.id.loginButton);
        correctLogin = (ImageView) findViewById(R.id.successfulLogin);
        incorrectLogin = (ImageView) findViewById(R.id.unsuccessfulLogin);
        incorrectEmailIcon = (ImageView) findViewById(R.id.incorrectEmailIcon);
        incorrectPasswordIcon = (ImageView) findViewById(R.id.incorrectPasswordIcon);
    }

    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void checkFields(View view){

        EditText emailField = (EditText) findViewById(R.id.confirmPassTextField);
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
            loginButton.setEnabled(false);
            incorrectEmailIcon.setVisibility(View.INVISIBLE);
            incorrectPasswordIcon.setVisibility(View.INVISIBLE);

            final String emailCopy = email;
            final String passwordCopy = password;

            new CountDownTimer(1000, 500){
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

        if (email.equals("admin@campfire.com") && (password.equals("bonfire301"))){
            Toast.makeText(LoginActivity.this, "Superuser Enabled", Toast.LENGTH_LONG).show();
            Intent adminIntent = new Intent(this, AdminActivity.class);
            startActivity(adminIntent);
        }else{
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                load.setVisibility(View.INVISIBLE);
                                loginButton.setText("Success!");
                                loginButton.setEnabled(false);
                                correctLogin.setVisibility(View.VISIBLE);
                                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                                mainIntent.putExtra("loggedIn", "1");
                                startActivity(mainIntent);
                            }else{

                            }
                        }
                    });

//
//            if ((foundStudent != null) && (password.equals(foundStudent.getPass()))){
//                load.setVisibility(View.INVISIBLE);
//                loginButton.setText("Success!");
//                loginButton.setEnabled(false);
//                correctLogin.setVisibility(View.VISIBLE);
//                Intent mainIntent = new Intent(this, MainActivity.class);
//
//                /**
//                 * For devs: Details of the newStudentID package are as follows:
//                 * newStudentID[0]: First name
//                 * newStudentID[1]: Last name
//                 * newStudentID[2]: Email Address
//                 * newStudentID[3]: Password
//                 * newStudentID[4]: Course
//                 * newStudentID[5]: Previous Course Taken
//                 * newStudentID[6]: Elective Course Taken
//                 * newStudentID[7]: Favourite Pastime Activity
//                 */
//                String[] newStudentID = {foundStudent.getFname(), foundStudent.getLname(),
//                        foundStudent.getEmail(), foundStudent.getPass()};
//                mainIntent.putExtra("identity", newStudentID);
//                startActivity(mainIntent);
//
//
//            }else{
//                load.setVisibility(View.INVISIBLE);
//                loginButton.setText("Log In");
//
//                incorrectLogin.setVisibility(View.VISIBLE);
//                loginButton.setEnabled(true);
//                //This notifies the user that there needs to be an email in the field
//                AlertDialog missingEmailDialog = new AlertDialog.Builder(LoginActivity.this).create();
//                missingEmailDialog.setTitle("Authentication Failed");
//                if (foundStudent != null){
//                    if (!password.equals(foundStudent.getPass())){
//                        missingEmailDialog.setMessage("Incorrect Password");
//                        incorrectPasswordIcon.setVisibility(View.VISIBLE);
//                    }else{
//                        missingEmailDialog.setMessage("This account does not exist. Please Try Again.");
//                        incorrectEmailIcon.setVisibility(View.VISIBLE);
//                    }
//                }else{
//                    missingEmailDialog.setMessage("This account does not exist. Please Try Again.");
//                    incorrectEmailIcon.setVisibility(View.VISIBLE);
//                }
//                missingEmailDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Try Again",
//                        new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int which) {
//                                correctLogin.setVisibility(View.INVISIBLE);
//                                incorrectLogin.setVisibility(View.INVISIBLE);
//                                dialog.dismiss();
//                            }
//                        });
//                missingEmailDialog.show();
//            }
        }

    }

    public void closeKeyboard(View view){
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
    }

    public void toRecovery(View view){
        Intent recoverIntent = new Intent(this, AccountRecoveryActivity.class);
        startActivity(recoverIntent);
    }
}
