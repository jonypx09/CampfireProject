package com.example.jonat.campfire;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import backend.algorithms.CSCCoursesCriteria;
import backend.algorithms.Comparable;
import backend.algorithms.Course;
import backend.algorithms.ElectivesCriteria;
import backend.algorithms.HobbiesCriteria;
import backend.algorithms.ProgrammingLanguagesCriteria;
import backend.algorithms.Student;
import backend.database.DatabaseAdapter;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener{

    private String[] newStudentID;
    private String[] programmingLanguages;
    DatabaseAdapter db;

    private ArrayList<Comparable> newStudentCriteria = new ArrayList<Comparable>();
    private ProgrammingLanguagesCriteria newStudentLanguages;
    private CSCCoursesCriteria newStudentPreviousCourses;
    private ElectivesCriteria newStudentElectives;
    private HobbiesCriteria newStudentHobbies;

    Button previousButton, nextButton, submitButton;
    Button twelveam, oneam, twoam, threeam, fouram, fiveam, sixam, sevenam, eightam, nineam, tenam,
    elevenam, twelvepm, onepm, twopm, threepm, fourpm, fivepm, sixpm, sevenpm, eightpm, ninepm,
            tenpm, elevenpm;

    ScrollView scheduleScroll;
    private TextView dayOfWeek;

    private int daynum = 0;
    private String uEmail;

    private String[] days = {"Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"};
    String[][] schedule = new String[7][24];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_schedule);
        setTitle("Set Availability");

        //Connect to the database
        db = new DatabaseAdapter(this);

        Intent intent = getIntent();
        newStudentID = intent.getExtras().getStringArray("identity");
        programmingLanguages = intent.getExtras().getStringArray("programmingLanguages");

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

        scheduleScroll = (ScrollView) findViewById(R.id.scrollView1);

        previousButton = (Button) findViewById(R.id.previous);
        previousButton.setOnClickListener(this);
        nextButton = (Button) findViewById(R.id.next);
        nextButton.setOnClickListener(this);
        submitButton = (Button) findViewById(R.id.submit);
        submitButton.setOnClickListener(this);
//        submitButton.setEnabled(false);
        twelveam = (Button) findViewById(R.id.twelveam);
        twelveam.setOnClickListener(this);
        oneam = (Button) findViewById(R.id.oneam);
        oneam.setOnClickListener(this);
        twoam = (Button) findViewById(R.id.twoam);
        twoam.setOnClickListener(this);
        threeam = (Button) findViewById(R.id.threeam);
        threeam.setOnClickListener(this);
        fouram = (Button) findViewById(R.id.fouram);
        fouram.setOnClickListener(this);
        fiveam = (Button) findViewById(R.id.fiveam);
        fiveam.setOnClickListener(this);
        sixam = (Button) findViewById(R.id.sixam);
        sixam.setOnClickListener(this);
        sevenam = (Button) findViewById(R.id.sevenam);
        sevenam.setOnClickListener(this);
        eightam = (Button) findViewById(R.id.eightam);
        eightam.setOnClickListener(this);
        nineam = (Button) findViewById(R.id.nineam);
        nineam.setOnClickListener(this);
        tenam = (Button) findViewById(R.id.tenam);
        tenam.setOnClickListener(this);
        elevenam = (Button) findViewById(R.id.elevenam);
        elevenam.setOnClickListener(this);
        twelvepm = (Button) findViewById(R.id.twelvepm);
        twelvepm.setOnClickListener(this);
        onepm = (Button) findViewById(R.id.onepm);
        onepm.setOnClickListener(this);
        twopm = (Button) findViewById(R.id.twopm);
        twopm.setOnClickListener(this);
        threepm = (Button) findViewById(R.id.threepm);
        threepm.setOnClickListener(this);
        fourpm = (Button) findViewById(R.id.fourpm);
        fourpm.setOnClickListener(this);
        fivepm = (Button) findViewById(R.id.fivepm);
        fivepm.setOnClickListener(this);
        sixpm = (Button) findViewById(R.id.sixpm);
        sixpm.setOnClickListener(this);
        sevenpm = (Button) findViewById(R.id.sevenpm);
        sevenpm.setOnClickListener(this);
        eightpm = (Button) findViewById(R.id.eightpm);
        eightpm.setOnClickListener(this);
        ninepm = (Button) findViewById(R.id.ninepm);
        ninepm.setOnClickListener(this);
        tenpm = (Button) findViewById(R.id.tenpm);
        tenpm.setOnClickListener(this);
        elevenpm = (Button) findViewById(R.id.elevenpm);
        elevenpm.setOnClickListener(this);

        dayOfWeek = (TextView) findViewById(R.id.dayofweek);
        // default start at monday
        dayOfWeek.setText(days[daynum]);

        //event for button previous
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.submit:

                /**
                 * 1. Create the comparables associated with the student's preferences (Refer to
                 *    the comment above to determine which index of newStudentID to use.
                 * 2. Create a new Student Object as well as Course Object
                 * 2. Import into Database
                 * 3. Perform matching in the MainActivity, not here
                 */
                Course newCourse = new Course(newStudentID[3], "Some Course Name", "Some Instructor");
                ArrayList<String> programmingLang = new ArrayList(Arrays.asList(programmingLanguages));
                newStudentLanguages = new ProgrammingLanguagesCriteria(programmingLang);

                ArrayList<String> previousCourses = new ArrayList<String>();
                previousCourses.add(0, newStudentID[5]);
                newStudentPreviousCourses = new CSCCoursesCriteria(previousCourses);

                ArrayList<String> electiveCourses = new ArrayList<String>();
                electiveCourses.add(0, newStudentID[6]);
                newStudentElectives = new ElectivesCriteria(electiveCourses);

                ArrayList<String> pastime = new ArrayList<String>();
                pastime.add(0, newStudentID[7]);
                newStudentHobbies = new HobbiesCriteria(pastime);

                newStudentCriteria.add(newStudentLanguages);
                newStudentCriteria.add(newStudentPreviousCourses);
                newStudentCriteria.add(newStudentElectives);
                newStudentCriteria.add(newStudentHobbies);


                /**
                 * The last argument should NOT be null; it is there to keep Gradle happy :P
                 */
                Student newStudent = new Student(newStudentID[0], newStudentID[1], newStudentID[2], newStudentID[3], newStudentCriteria);
                db.addStudent(newStudent);
                db.addCourse(newCourse);

                Intent mainIntent = new Intent(this, MainActivity.class);
                mainIntent.putExtra("identity", newStudentID);
                startActivity(mainIntent);
            case R.id.next:
                if (daynum != 6) {
                    daynum++;
                } else {
                    daynum = 0;
                }
                dayOfWeek.setText(days[daynum]);
                fillSchedule();
                scheduleScroll.fullScroll(ScrollView.FOCUS_UP);
                break;

            case R.id.previous:
                if (daynum != 0) {
                    daynum--;
                } else {
                    daynum = 6;
                }
                dayOfWeek.setText(days[daynum]);
                fillSchedule();
                scheduleScroll.fullScroll(ScrollView.FOCUS_UP);
                break;

            case R.id.twelveam:
                changeScheduleButton(0, twelveam);
                break;

            case R.id.oneam:
                changeScheduleButton(1, oneam);
                break;

            case R.id.twoam:
                changeScheduleButton(2, twoam);
                break;

            case R.id.threeam:
                changeScheduleButton(3, threeam);
                break;
            case R.id.fouram:
                changeScheduleButton(4, fouram);
                break;

            case R.id.fiveam:
                changeScheduleButton(5, fiveam);
                break;

            case R.id.sixam:
                changeScheduleButton(6, sixam);
                break;

            case R.id.sevenam:
                changeScheduleButton(7, sevenam);
                break;

            case R.id.eightam:
                changeScheduleButton(8, eightam);
                break;

            case R.id.nineam:
                changeScheduleButton(9, nineam);
                break;

            case R.id.tenam:
                changeScheduleButton(10, tenam);
                break;

            case R.id.elevenam:
                changeScheduleButton(11, elevenam);
                break;

            case R.id.twelvepm:
                changeScheduleButton(12, twelvepm);
                break;

            case R.id.onepm:
                changeScheduleButton(13, onepm);
                break;

            case R.id.twopm:
                changeScheduleButton(14, twopm);
                break;

            case R.id.threepm:
                changeScheduleButton(15, threepm);
                break;

            case R.id.fourpm:
                changeScheduleButton(16, fourpm);
                break;

            case R.id.fivepm:
                changeScheduleButton(17, fivepm);

            case R.id.sixpm:
                changeScheduleButton(18, sixpm);
                break;

            case R.id.sevenpm:
                changeScheduleButton(19, sevenpm);
                break;

            case R.id.eightpm:
                changeScheduleButton(20, eightpm);
                break;

            case R.id.ninepm:
                changeScheduleButton(21, ninepm);
                break;

            case R.id.tenpm:
                changeScheduleButton(22, tenpm);
                break;

            case R.id.elevenpm:
                changeScheduleButton(23, elevenpm);
                break;
        }
    }

    public void fillSchedule(){
        int i = 0;
        for (String s: schedule[daynum]) {
            switch(i) {
                case 0:
                    setScheduleButton(i, twelveam);
                case 1:
                    setScheduleButton(i, oneam);
                case 2:
                    setScheduleButton(i, twoam);
                case 3:
                    setScheduleButton(i, threeam);
                case 4:
                    setScheduleButton(i, fouram);
                case 5:
                    setScheduleButton(i, fiveam);
                case 6:
                    setScheduleButton(i, sixam);
                case 7:
                    setScheduleButton(i, sevenam);
                case 8:
                    setScheduleButton(i, eightam);
                case 9:
                    setScheduleButton(i, nineam);
                case 10:
                    setScheduleButton(i, tenam);
                case 11:
                    setScheduleButton(i, elevenam);
                case 12:
                    setScheduleButton(i, twelvepm);
                case 13:
                    setScheduleButton(i, onepm);
                case 14:
                    setScheduleButton(i, twopm);
                case 15:
                    setScheduleButton(i, threepm);
                case 16:
                    setScheduleButton(i, fourpm);
                case 17:
                    setScheduleButton(i, fivepm);
                case 18:
                    setScheduleButton(i, sixpm);
                case 19:
                    setScheduleButton(i, sevenpm);
                case 20:
                    setScheduleButton(i, eightpm);
                case 21:
                    setScheduleButton(i, ninepm);
                case 22:
                    setScheduleButton(i, tenpm);
                case 23:
                    setScheduleButton(i, elevenpm);
            }
            i++;
        }

    }

    public void setScheduleButton(int value, Button toChange) {

        if (schedule[daynum][value] == null) {
            toChange.setBackgroundColor(Color.LTGRAY);
        } else if (schedule[daynum][value].equals("0")) {
            toChange.setBackgroundColor(Color.RED);
        } else if (schedule[daynum][value].equals("1")) {
            toChange.setBackgroundColor(Color.GREEN);
        }
    }

    public void changeScheduleButton(int value, Button toChange) {

        if (schedule[daynum][value] == null || schedule[daynum][value].equals("0")) {
            toChange.setBackgroundColor(Color.GREEN);
            schedule[daynum][value] = "1";
            validate();

        } else if (schedule[daynum][value].equals("1")) {
            toChange.setBackgroundColor(Color.RED);
            schedule[daynum][value] = "0";
            validate();
        }
    }

    public boolean validate() {
        for (int i = 0; i < 7; i++) {
            for (String s: schedule[i]) {
                if (s == null) {
                    return false;
                }
            }
        }
        submitButton.setEnabled(true);
        submitButton.setBackgroundColor(Color.GREEN);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            Intent personalIntent = new Intent(this, PersonalizeActivity.class);
            personalIntent.putExtra("identity", newStudentID);
            startActivity(personalIntent);
            return true;
        }
        return false;
    }
}
