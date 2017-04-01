package com.example.jonat.campfire;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;

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

import static com.example.jonat.campfire.R.id.programmingLanguages;

public class RatingActivity extends AppCompatActivity {

    private String[] newStudentID;
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);
        setTitle("Select Matching Criteria");

        Intent intent = getIntent();
        newStudentID = intent.getExtras().getStringArray("identity");
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
                Course newCourse = new Course(newStudentID[3], "Some Course Name", "Some Instructor");
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
                Student newStudent = new Student(newStudentID[0], newStudentID[1], newStudentID[2], newStudentID[3], newStudentCriteria);
                newCourse.addStudent(newStudent);
//                db.addStudent(newStudent);
//                db.addCourse(newCourse);
//                db.addToTaking(newStudentID[4], newStudentID[2]);
                DbAdapter.addStudent(newStudent);
                try{
                    DbAdapter.addCourse(newCourse);
                }catch(Exception e){

                }
                DbAdapter.enrolStudentInCourse(newStudentID[2], newStudentID[4]);

                Intent mainIntent = new Intent(v.getContext(), MainActivity.class);
                mainIntent.putExtra("identity", newStudentID);
                startActivity(mainIntent);
            }

        });

    }
}
