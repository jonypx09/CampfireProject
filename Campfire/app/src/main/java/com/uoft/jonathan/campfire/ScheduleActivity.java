package com.uoft.jonathan.campfire;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import backend.algorithms.CSCCoursesCriteria;
import backend.algorithms.Comparable;
import backend.algorithms.ElectivesCriteria;
import backend.algorithms.HobbiesCriteria;
import backend.algorithms.ProgrammingLanguagesCriteria;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener{

    private String[] newStudentID;
    private String[] programmingLanguages;

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

    private String[] days = {"Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"};

    HashMap<String, ArrayList<String>> schedule = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
        setContentView(R.layout.activity_schedule);
        setTitle("Set Availability");

        // setting up hash map of schedule, setting availability to null
        for (String day: days) {
            schedule.put(day, new ArrayList<String>());
        }

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
                Intent mainIntent = new Intent(this, RatingActivity.class);
                mainIntent.putExtra("identity", newStudentID);
                mainIntent.putExtra("programmingLanguages", programmingLanguages);
                mainIntent.putExtra("schedule", schedule);
                startActivity(mainIntent);
            case R.id.next:
                if (daynum != 6) {
                    daynum++;
                } else {
                    daynum = 0;
                }
                dayOfWeek.setText(days[daynum]);
                fillSchedule(days[daynum]);
                scheduleScroll.fullScroll(ScrollView.FOCUS_UP);
                break;
            case R.id.previous:
                if (daynum != 0) {
                    daynum--;
                } else {
                    daynum = 6;
                }
                dayOfWeek.setText(days[daynum]);
                fillSchedule(days[daynum]);
                scheduleScroll.fullScroll(ScrollView.FOCUS_UP);
                break;

            case R.id.twelveam:
                changeScheduleButton(days[daynum], "00:00 - 00:59", twelveam);
                break;

            case R.id.oneam:
                changeScheduleButton(days[daynum], "01:00 - 01:59", oneam);
                break;

            case R.id.twoam:
                changeScheduleButton(days[daynum], "02:00 - 02:59", twoam);
                break;

            case R.id.threeam:
                changeScheduleButton(days[daynum], "03:00 - 03:59", threeam);
                break;
            case R.id.fouram:
                changeScheduleButton(days[daynum], "04:00 - 04:59", fouram);
                break;

            case R.id.fiveam:
                changeScheduleButton(days[daynum], "05:00 - 05:59", fiveam);
                break;

            case R.id.sixam:
                changeScheduleButton(days[daynum], "06:00 - 06:59", sixam);
                break;

            case R.id.sevenam:
                changeScheduleButton(days[daynum], "07:00 - 07:59", sevenam);
                break;

            case R.id.eightam:
                changeScheduleButton(days[daynum], "08:00 - 08:59", eightam);
                break;

            case R.id.nineam:
                changeScheduleButton(days[daynum], "09:00 - 09:59", nineam);
                break;

            case R.id.tenam:
                changeScheduleButton(days[daynum], "10:00 - 10:59", tenam);
                break;

            case R.id.elevenam:
                changeScheduleButton(days[daynum], "11:00 - 11:59", elevenam);
                break;

            case R.id.twelvepm:
                changeScheduleButton(days[daynum], "12:00 - 12:59", twelvepm);
                break;

            case R.id.onepm:
                changeScheduleButton(days[daynum], "13:00 - 13:59", onepm);
                break;

            case R.id.twopm:
                changeScheduleButton(days[daynum], "14:00 - 14:59", twopm);
                break;

            case R.id.threepm:
                changeScheduleButton(days[daynum], "15:00 - 15:59", threepm);
                break;

            case R.id.fourpm:
                changeScheduleButton(days[daynum], "16:00 - 16:59", fourpm);
                break;

            case R.id.fivepm:
                changeScheduleButton(days[daynum], "17:00 - 17:59", fivepm);
                break;

            case R.id.sixpm:
                changeScheduleButton(days[daynum], "18:00 - 18:59", sixpm);
                break;

            case R.id.sevenpm:
                changeScheduleButton(days[daynum], "19:00 - 19:59", sevenpm);
                break;

            case R.id.eightpm:
                changeScheduleButton(days[daynum], "20:00 - 20:59", eightpm);
                break;

            case R.id.ninepm:
                changeScheduleButton(days[daynum], "21:00 - 21:59", ninepm);
                break;

            case R.id.tenpm:
                changeScheduleButton(days[daynum], "22:00 - 22:59", tenpm);
                break;

            case R.id.elevenpm:
                changeScheduleButton(days[daynum], "23:00 - 23:59", elevenpm);
                break;
        }
    }

    public void fillSchedule(String day){
        ArrayList<String> temp = schedule.get(day);
        ArrayList<Button> greenButtons = new ArrayList<Button>();

        for (String t: temp) {
                if (t.equals("00:00 - 00:59")) {
                    greenButtons.add(twelveam);
                }
                else if (t.equals("01:00 - 01:59")) {
                    greenButtons.add(oneam);
                }
                else if (t.equals("02:00 - 02:59")) {
                     greenButtons.add(twoam);
                }
                else if (t.equals("03:00 - 03:59")) {
                    greenButtons.add(threeam);
                }
                else if (t.equals("04:00 - 04:59")) {
                    greenButtons.add(fouram);
                }
                else if (t.equals("05:00 - 05:59")) {
                     greenButtons.add(fiveam);
                }
                else if (t.equals("06:00 - 06:59")) {
                    greenButtons.add(sixam);
                }
                else if (t.equals("07:00 - 07:59")) {
                    greenButtons.add(sevenam);
                }
                else if (t.equals("08:00 - 08:59")) {
                    greenButtons.add(eightam);
                }
                else if (t.equals("09:00 - 09:59")) {
                    greenButtons.add(nineam);
                }
                else if (t.equals("10:00 - 10:59")) {
                    greenButtons.add(tenam);
                }
                else if (t.equals("11:00 - 11:59")) {
                    greenButtons.add(elevenam);
                }
                else if (t.equals("12:00 - 12:59")) {
                    greenButtons.add(twelvepm);
                }
                else if (t.equals("13:00 - 13:59")) {
                    greenButtons.add(onepm);
                }
                else if (t.equals("14:00 - 14:59")) {
                    greenButtons.add(twopm);
                }
                else if (t.equals("15:00 - 15:59")) {
                    greenButtons.add(threepm);
                }
                else if (t.equals("16:00 - 16:59")) {
                    greenButtons.add(fourpm);
                }
                else if (t.equals("17:00 - 17:59")) {
                    greenButtons.add(fivepm);
                }
                else if (t.equals("18:00 - 18:59")) {
                    greenButtons.add(sixpm);
                }
                else if (t.equals("19:00 - 19:59")) {
                    greenButtons.add(sevenpm);
                }
                else if (t.equals("20:00 - 20:59")) {
                    greenButtons.add(eightpm);
                }
                else if (t.equals("21:00 - 21:59")) {
                    greenButtons.add(ninepm);
                }
                else if (t.equals("22:00 - 22:59")) {
                    greenButtons.add(tenpm);
                }
                else if (t.equals("23:00 - 23:59")) {
                    greenButtons.add(elevenpm);
                }
        }

        setScheduleButton(greenButtons);
    }

    public void setScheduleButton(ArrayList<Button> savedButtons) {

        ArrayList<Button> buttons = new ArrayList<>(Arrays.asList(twelveam, oneam, twoam,
                threeam, fouram, fiveam, sixam, sevenam, eightam, nineam, tenam, elevenam, twelvepm,
                onepm, twopm, threepm, fourpm, fivepm, sixpm, sevenpm, eightpm, ninepm, tenpm,
                elevenpm));

        for (Button b: savedButtons) {
            b.setBackgroundColor(Color.GREEN);
        }


        buttons.removeAll(savedButtons);

        for (Button b: buttons) {
            b.setBackgroundResource(android.R.drawable.btn_default);
        }
    }

    public void changeScheduleButton(String day, String time, Button toChange) {

        ArrayList<String> temp = schedule.get(day);

        if (temp == null || !temp.contains(time)) { // toggle to available
            toChange.setBackgroundColor(Color.GREEN);
            // update ArrayList for this day (adding available time)

            temp.add(time);
            schedule.put(day, temp);

        } else if (temp.contains(time)){ // set to not available for this time and day
            toChange.setBackgroundResource(android.R.drawable.btn_default);
            // update ArrayList for this day (removing previously available time)
            temp.remove(time);

            schedule.put(day, temp);
        }
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
