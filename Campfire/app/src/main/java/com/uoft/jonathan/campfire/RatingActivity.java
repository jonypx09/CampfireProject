package com.uoft.jonathan.campfire;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import backend.algorithms.CSCCoursesCriteria;
import backend.algorithms.Comparable;
import backend.algorithms.Course;
import backend.algorithms.ElectivesCriteria;
import backend.algorithms.HobbiesCriteria;
import backend.algorithms.ProgrammingLanguagesCriteria;
import backend.algorithms.ScheduleCriteria;
import backend.algorithms.Student;
import backend.database.DbAdapter;

public class RatingActivity extends AppCompatActivity {

    private String[] newStudentID;
    private String[] programmingLanguages;
    String uEmail;
    HashMap<String, ArrayList<String>> schedule = new HashMap<>();

    private ArrayList<Comparable> newStudentCriteria = new ArrayList<Comparable>();
    private ProgrammingLanguagesCriteria newStudentLanguages;
    private CSCCoursesCriteria newStudentPreviousCourses;
    private ElectivesCriteria newStudentElectives;
    private HobbiesCriteria newStudentHobbies;


    private RatingBar ratingBarLanguages;
    private RatingBar ratingBarCsCourse;
    private RatingBar ratingBarElective;
    private RatingBar ratingBarHobby;
    private RatingBar ratingBarSchedule;
    private Button btnSubmit;
    private int critLang, critCs, critElec, critHob, critSched;

    private Handler handler;


    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private FirebaseUser currentUser;
    private String currentUserID;
    private Student newStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_rating);
        setTitle("Select Matching Criteria");

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mAuth.signOut();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser newUser = firebaseAuth.getCurrentUser();
                if (newUser != null) {
                    // User is signed in
                    String name = newUser.getDisplayName();
                    String email = newUser.getEmail();
                    String uid = newUser.getUid();
                    DatabaseReference myRef = database.getReference("Users/" + uid);
                    myRef.setValue(newStudent);
                } else {
                    // User is signed out
                }
                // ...
            }
        };

        Intent intent = getIntent();
        newStudentID = intent.getExtras().getStringArray("identity");
        programmingLanguages = intent.getExtras().getStringArray("programmingLanguages");
        schedule = (HashMap<String,ArrayList<String>>) intent.getSerializableExtra("schedule");
        uEmail = newStudentID[2];

        // default criteria rating
        critLang = 1;
        critCs = 1;
        critElec = 1;
        critHob = 1;
        critSched = 1;

        addListenerOnRatingBar();
        addListenerOnButton();
    }

    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop(){
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }

    public void addListenerOnRatingBar() {

        ratingBarLanguages = (RatingBar) findViewById(R.id.ratingBarLang);
        ratingBarCsCourse = (RatingBar) findViewById(R.id.ratingBarCs);
        ratingBarElective = (RatingBar) findViewById(R.id.ratingBarElec);
        ratingBarHobby = (RatingBar) findViewById(R.id.ratingBarHob);
        ratingBarSchedule = (RatingBar) findViewById(R.id.ratingBarSched);

        ratingBarLanguages.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser)
            {
                critLang = Math.round(rating);
                System.out.println(critLang);
            }

        });

        ratingBarCsCourse.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser)
            {
                critCs = Math.round(rating);
                System.out.println(critCs);
            }

        });

        ratingBarElective.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser)
            {
                critElec = Math.round(rating);
                System.out.println(critElec);
            }

        });

        ratingBarHobby.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser)
            {
                critHob = Math.round(rating);
                System.out.println(critHob);
            }

        });

        ratingBarSchedule.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser)
            {
                critSched = Math.round(rating);
                System.out.println(critSched);
            }

        });
    }



    public void addListenerOnButton() {
        btnSubmit = (Button) findViewById(R.id.done);
        btnSubmit.setBackgroundColor(Color.GREEN);

        //if click on me, then display the current rating value.
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //TODO: Change
                /**
                 * 1. Create the comparables associated with the student's preferences (Refer to
                 *    the comment above to determine which index of newStudentID to use.
                 * 2. Create a new Student Object as well as Course Object
                 * 2. Import into Database
                 * 3. Perform matching in the MainActivity, not here
                 */
                Course newCourse = new Course(newStudentID[4], "Some Course Name", "Some Instructor");
                ArrayList<String> programmingLang = new ArrayList(Arrays.asList(programmingLanguages));
                newStudentLanguages = new ProgrammingLanguagesCriteria(programmingLang, critLang);

                ArrayList<String> previousCourses = new ArrayList<String>();
                previousCourses.add(0, newStudentID[5]);
                newStudentPreviousCourses = new CSCCoursesCriteria(previousCourses, critCs);

                ArrayList<String> electiveCourses = new ArrayList<String>();
                electiveCourses.add(0, newStudentID[6]);
                newStudentElectives = new ElectivesCriteria(electiveCourses, critElec);

                ArrayList<String> pastime = new ArrayList<String>();
                pastime.add(0, newStudentID[7]);
                newStudentHobbies = new HobbiesCriteria(pastime, critHob);

                newStudentCriteria.add(newStudentLanguages);
                newStudentCriteria.add(newStudentPreviousCourses);
                newStudentCriteria.add(newStudentElectives);
                newStudentCriteria.add(newStudentHobbies);

                ScheduleCriteria sc = new ScheduleCriteria(schedule, critSched);
                newStudentCriteria.add(sc);

                /**
                 * The last argument should NOT be null; it is there to keep Gradle happy :P
                 */
                newStudent = new Student(newStudentID[0], newStudentID[1], newStudentID[2], newStudentID[3], newStudentCriteria);

                newCourse.addStudent(newStudent);

                createAccount(newStudentID[2], newStudentID[3]);
                Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                mainIntent.putExtra("loggedIn", "0");
                mainIntent.putExtra("identity", newStudentID);
                mainIntent.putExtra("programmingLanguages", programmingLanguages);
                mainIntent.putExtra("schedule", schedule);
                startActivity(mainIntent);

//                handler = new Handler();
//                final ProgressDialog progressDialog = new ProgressDialog(RatingActivity.this);
//                progressDialog.setMessage("Please wait....");
//                progressDialog.setTitle("Creating User");
//                progressDialog.show();
//                final Student newStudentCopy = newStudent;
//                final Course newCourseCopy = newCourse;
//                final View vCopy = v;
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//
////                        mAuth = FirebaseAuth.getInstance();
//                        createAccount(newStudentID[2], newStudentID[3]);
//
////                        DbAdapter.addStudent(newStudentCopy);
//                        try{
////                            DbAdapter.addCourse(newCourseCopy);
//                        }catch(Exception e){
//
//                        }
////                        DbAdapter.enrolStudentInCourse(newStudentID[2], newStudentID[4]);
//                        handler.post(new Runnable() {
//                            @Override
//                            public void run() {
//                                progressDialog.dismiss();
//                                Intent mainIntent = new Intent(vCopy.getContext(), MainActivity.class);
//                                mainIntent.putExtra("identity", newStudentID);
//                                mainIntent.putExtra("currentCourse", newStudentID[4]);
//                                startActivity(mainIntent);
//                            }
//                        });
//                    }
//                }).start();



            }

        });

    }

    public void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Toast.makeText(RatingActivity.this, "Authentication Failed",

                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
