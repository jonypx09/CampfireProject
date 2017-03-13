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
import android.widget.ImageView;
import android.widget.ProgressBar;

import java.util.ArrayList;

import backend.algorithms.Comparable;
import backend.algorithms.Course;
import backend.algorithms.HobbiesCriteria;
import backend.algorithms.ProgrammingLanguagesCriteria;
import backend.algorithms.Student;
import backend.database.DatabaseAdapter;

public class LoginActivity extends AppCompatActivity {

    DatabaseAdapter db;

    ProgressBar load;
    Button loginButton;
    ImageView correctLogin;
    ImageView incorrectLogin;
    ImageView incorrectEmailIcon;
    ImageView incorrectPasswordIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_login);
        setTitle("Log In");

        //Connect to the database
        db = new DatabaseAdapter(this);
        // Database sample population so we don't need to signup.
        if (db.getCourse("csc301h1") == null) {
            Course course = new Course("csc301h1", "Introduction to Software Engineering", "Joey Freund");
            db.addCourse(course);
        }
        ArrayList<String> c1 = new ArrayList<>();
        c1.add("Java");
        c1.add("HTML");
        c1.add("CSS");
        c1.add("JavaScript");

        ArrayList<String> c2 = new ArrayList<>();
        c2.add("Running");
        c2.add("Video Games");
        c2.add("Reading");
        c2.add("Watching Sports");

        ProgrammingLanguagesCriteria languages = new ProgrammingLanguagesCriteria(c1);
        HobbiesCriteria hobbies = new HobbiesCriteria(c2);

        ArrayList<Comparable> crit = new ArrayList<>();
        crit.add(languages);
        crit.add(hobbies);

        ArrayList<Student> sampleStudents = new ArrayList<>();
        sampleStudents.add(new Student("Adam", "Capparelli", "adam@mail.com", "12345678", crit));
        sampleStudents.add(new Student("Andrew", "Goupil", "andrew@mail.com", "12345678", crit));
        sampleStudents.add(new Student("Fullchee", "Zhang", "fullchee@mail.com", "12345678", crit));
        sampleStudents.add(new Student("Jonathan", "Pelastine", "jonathan@mail.com", "12345678", crit));
        sampleStudents.add(new Student("Quinn", "Daneyko", "quinn@mail.com", "12345678", crit));
        sampleStudents.add(new Student("Rod", "Mazloomi", "rod@mail.com", "12345678", crit));
        sampleStudents.add(new Student("Vlad", "Chapurny", "vlad@mail.com", "12345678", crit));
        for (Student s : sampleStudents) {
            s.setDescription("Sample Description");
            if (db.getStudent(s.getEmail()) == null) {
                db.addStudent(s);
                db.addToTaking("csc301h1", s.getEmail());
            }
        }

        load = (ProgressBar) findViewById(R.id.loginProgress);
        loginButton = (Button) findViewById(R.id.loginButton);
        correctLogin = (ImageView) findViewById(R.id.successfulLogin);
        incorrectLogin = (ImageView) findViewById(R.id.unsuccessfulLogin);
        incorrectEmailIcon = (ImageView) findViewById(R.id.incorrectEmailIcon);
        incorrectPasswordIcon = (ImageView) findViewById(R.id.incorrectPasswordIcon);
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
            correctLogin.setVisibility(View.VISIBLE);
            Intent mainIntent = new Intent(this, MainActivity.class);

            /**
             * For devs: Details of the newStudentID package are as follows:
             * newStudentID[0]: First name
             * newStudentID[1]: Last name
             * newStudentID[2]: Email Address
             * newStudentID[3]: Password
             * newStudentID[4]: Course
             * newStudentID[5]: Previous Course Taken
             * newStudentID[6]: Elective Course Taken
             * newStudentID[7]: Favourite Pastime Activity
             */
            String[] newStudentID = {foundStudent.getFname(), foundStudent.getLname(),
                                        foundStudent.getEmail(), foundStudent.getPass()};
            mainIntent.putExtra("identity", newStudentID);
            startActivity(mainIntent);


        }else{
            load.setVisibility(View.INVISIBLE);
            loginButton.setText("Log In");

            incorrectLogin.setVisibility(View.VISIBLE);
            loginButton.setEnabled(true);
            //This notifies the user that there needs to be an email in the field
            AlertDialog missingEmailDialog = new AlertDialog.Builder(LoginActivity.this).create();
            missingEmailDialog.setTitle("Authentication Failed");
            if (foundStudent != null){
                if (!password.equals(foundStudent.getPass())){
                    missingEmailDialog.setMessage("Incorrect Password");
                    incorrectPasswordIcon.setVisibility(View.VISIBLE);
                }else{
                    missingEmailDialog.setMessage("This account does not exist. Please Try Again.");
                    incorrectEmailIcon.setVisibility(View.VISIBLE);
                }
            }else{
                missingEmailDialog.setMessage("This account does not exist. Please Try Again.");
                incorrectEmailIcon.setVisibility(View.VISIBLE);
            }
            missingEmailDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Try Again",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            correctLogin.setVisibility(View.INVISIBLE);
                            incorrectLogin.setVisibility(View.INVISIBLE);
                            dialog.dismiss();
                        }
                    });
            missingEmailDialog.show();
        }

    }
}
