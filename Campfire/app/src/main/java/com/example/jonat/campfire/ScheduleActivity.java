package com.example.jonat.campfire;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ScheduleActivity extends AppCompatActivity implements View.OnClickListener{

    Button previousButton, nextButton, submitButtom;
    Button twelveam, oneam, twoam, threeam, fouram, fiveam, sixam, sevenam, eightam, nineam, tenam,
    elevenam, twelvepm, onepm, twopm, threepm, fourpm, fivepm, sixpm, sevenpm, eightpm, ninepm,
            tenpm, elevenpm;

    private TextView dayOfWeek;

    private int daynum = 0;

    private String[] days = {"Monday", "Tuesday", "Wednesday",
            "Thursday", "Friday", "Saturday", "Sunday"};
    String[][] schedule = new String[7][24];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule);

        previousButton = (Button) findViewById(R.id.previous);
        nextButton = (Button) findViewById(R.id.next);
        nextButton.setOnClickListener(this);
        // submit button
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
            case R.id.next:
                if (daynum != 6) {
                    daynum++;
                } else {
                    daynum = 0;
                }
                dayOfWeek.setText(days[daynum]);
                fillSchedule();

            case R.id.previous:
                if (daynum != 0) {
                    daynum--;
                } else {
                    daynum = 6;
                }
                dayOfWeek.setText(days[daynum]);
                fillSchedule();

            case R.id.twelveam:
                if (schedule[daynum][0] == null || schedule[daynum][0].equals("0")) {
                    twelveam.setBackgroundColor(Color.GREEN);
                    schedule[daynum][0] = "1";

                } else if (schedule[daynum][0].equals("1")) {
                    twelveam.setBackgroundColor(Color.RED);
                    schedule[daynum][0] = "0";
                }

                break;

            case R.id.oneam:
                if (schedule[daynum][1] == null || schedule[daynum][1].equals("0")) {
                    oneam.setBackgroundColor(Color.GREEN);
                    schedule[daynum][1] = "1";

                } else if (schedule[daynum][0].equals("1")) {
                    oneam.setBackgroundColor(Color.RED);
                    schedule[daynum][1] = "0";
                }
                break;

            case R.id.twoam:
                if (schedule[daynum][2] == null || schedule[daynum][2].equals("0")) {
                    twoam.setBackgroundColor(Color.GREEN);
                    schedule[daynum][2] = "1";

                } else if (schedule[daynum][0].equals("1")) {
                    twoam.setBackgroundColor(Color.RED);
                    schedule[daynum][2] = "0";
                }
                break;

            case R.id.threeam:
                if (schedule[daynum][3] == null || schedule[daynum][3].equals("0")) {
                    threeam.setBackgroundColor(Color.GREEN);
                    schedule[daynum][3] = "1";

                } else if (schedule[daynum][0].equals("1")) {
                    threeam.setBackgroundColor(Color.RED);
                    schedule[daynum][3] = "0";
                }

                break;
            case R.id.fouram:
                if (schedule[daynum][4] == null || schedule[daynum][4].equals("0")) {
                    fouram.setBackgroundColor(Color.GREEN);
                    schedule[daynum][4] = "1";

                } else if (schedule[daynum][4].equals("1")) {
                    fouram.setBackgroundColor(Color.RED);
                    schedule[daynum][4] = "0";
                }
                break;

            case R.id.fiveam:
                if (schedule[daynum][5] == null || schedule[daynum][5].equals("0")) {
                    fiveam.setBackgroundColor(Color.GREEN);
                    schedule[daynum][5] = "1";

                } else if (schedule[daynum][0].equals("1")) {
                    fiveam.setBackgroundColor(Color.RED);
                    schedule[daynum][5] = "0";
                }
                break;

            case R.id.sixam:
                if (schedule[daynum][6] == null || schedule[daynum][6].equals("0")) {
                    sixam.setBackgroundColor(Color.GREEN);
                    schedule[daynum][6] = "1";

                } else if (schedule[daynum][0].equals("1")) {
                    sixam.setBackgroundColor(Color.RED);
                    schedule[daynum][6] = "0";
                }
                break;

            case R.id.sevenam:
                if (schedule[daynum][7] == null || schedule[daynum][7].equals("0")) {
                    sevenam.setBackgroundColor(Color.GREEN);
                    schedule[daynum][7] = "1";

                } else if (schedule[daynum][0].equals("1")) {
                    sevenam.setBackgroundColor(Color.RED);
                    schedule[daynum][7] = "0";
                }
                break;

            case R.id.eightam:
                if (schedule[daynum][8] == null || schedule[daynum][8].equals("0")) {
                    eightam.setBackgroundColor(Color.GREEN);
                    schedule[daynum][8] = "1";

                } else if (schedule[daynum][0].equals("1")) {
                    eightam.setBackgroundColor(Color.RED);
                    schedule[daynum][8] = "0";
                }
                break;

            case R.id.nineam:
                if (schedule[daynum][9] == null || schedule[daynum][9].equals("0")) {
                    nineam.setBackgroundColor(Color.GREEN);
                    schedule[daynum][9] = "1";

                } else if (schedule[daynum][0].equals("1")) {
                    nineam.setBackgroundColor(Color.RED);
                    schedule[daynum][9] = "0";
                }
                break;

            case R.id.tenam:
                if (schedule[daynum][10] == null || schedule[daynum][10].equals("0")) {
                    tenam.setBackgroundColor(Color.GREEN);
                    schedule[daynum][10] = "1";

                } else if (schedule[daynum][0].equals("1")) {
                    tenam.setBackgroundColor(Color.RED);
                    schedule[daynum][10] = "0";
                }
                break;

            case R.id.elevenam:
                if (schedule[daynum][11] == null || schedule[daynum][11].equals("0")) {
                    elevenam.setBackgroundColor(Color.GREEN);
                    schedule[daynum][11] = "1";

                } else if (schedule[daynum][0].equals("1")) {
                    elevenam.setBackgroundColor(Color.RED);
                    schedule[daynum][11] = "0";
                }
                break;

            case R.id.twelvepm:
                if (schedule[daynum][12] == null || schedule[daynum][12].equals("0")) {
                    twelvepm.setBackgroundColor(Color.GREEN);
                    schedule[daynum][12] = "1";

                } else if (schedule[daynum][0].equals("1")) {
                    twelvepm.setBackgroundColor(Color.RED);
                    schedule[daynum][12] = "0";
                }

                break;

            case R.id.onepm:
                if (schedule[daynum][13] == null || schedule[daynum][13].equals("0")) {
                    onepm.setBackgroundColor(Color.GREEN);
                    schedule[daynum][13] = "1";

                } else if (schedule[daynum][0].equals("1")) {
                    onepm.setBackgroundColor(Color.RED);
                    schedule[daynum][13] = "0";
                }

                break;

            case R.id.twopm:
                if (schedule[daynum][14] == null || schedule[daynum][14].equals("0")) {
                    twopm.setBackgroundColor(Color.GREEN);
                    schedule[daynum][14] = "1";

                } else if (schedule[daynum][0].equals("1")) {
                    twopm.setBackgroundColor(Color.RED);
                    schedule[daynum][14] = "0";
                }
                break;

            case R.id.threepm:
                if (schedule[daynum][15] == null || schedule[daynum][15].equals("0")) {
                    threepm.setBackgroundColor(Color.GREEN);
                    schedule[daynum][15] = "1";

                } else if (schedule[daynum][15].equals("1")) {
                    threepm.setBackgroundColor(Color.RED);
                    schedule[daynum][15] = "0";
                }
                break;

            case R.id.fourpm:
                if (schedule[daynum][16] == null || schedule[daynum][16].equals("0")) {
                    fourpm.setBackgroundColor(Color.GREEN);
                    schedule[daynum][16] = "1";

                } else if (schedule[daynum][16].equals("1")) {
                    fourpm.setBackgroundColor(Color.RED);
                    schedule[daynum][16] = "0";
                }
                break;

            case R.id.fivepm:
                if (schedule[daynum][17] == null || schedule[daynum][17].equals("0")) {
                    fivepm.setBackgroundColor(Color.GREEN);
                    schedule[daynum][17] = "1";

                } else if (schedule[daynum][15].equals("1")) {
                    fivepm.setBackgroundColor(Color.RED);
                    schedule[daynum][17] = "0";
                }
                break;

            case R.id.sixpm:
                if (schedule[daynum][18] == null || schedule[daynum][18].equals("0")) {
                    sixpm.setBackgroundColor(Color.GREEN);
                    schedule[daynum][18] = "1";

                } else if (schedule[daynum][18].equals("1")) {
                    sixpm.setBackgroundColor(Color.RED);
                    schedule[daynum][18] = "0";
                }
                break;

            case R.id.sevenpm:
                if (schedule[daynum][19] == null || schedule[daynum][19].equals("0")) {
                    sevenpm.setBackgroundColor(Color.GREEN);
                    schedule[daynum][19] = "1";

                } else if (schedule[daynum][19].equals("1")) {
                    sevenpm.setBackgroundColor(Color.RED);
                    schedule[daynum][19] = "0";
                }
                break;

            case R.id.eightpm:
                if (schedule[daynum][20] == null || schedule[daynum][20].equals("0")) {
                    eightpm.setBackgroundColor(Color.GREEN);
                    schedule[daynum][20] = "1";

                } else if (schedule[daynum][20].equals("1")) {
                    eightpm.setBackgroundColor(Color.RED);
                    schedule[daynum][20] = "0";
                }
                break;

            case R.id.ninepm:
                if (schedule[daynum][21] == null || schedule[daynum][21].equals("0")) {
                    ninepm.setBackgroundColor(Color.GREEN);
                    schedule[daynum][21] = "1";

                } else if (schedule[daynum][21].equals("1")) {
                    ninepm.setBackgroundColor(Color.RED);
                    schedule[daynum][21] = "0";
                }
                break;

            case R.id.tenpm:
                if (schedule[daynum][22] == null || schedule[daynum][22].equals("0")) {
                    tenpm.setBackgroundColor(Color.GREEN);
                    schedule[daynum][22] = "1";

                } else if (schedule[daynum][22].equals("1")) {
                    tenpm.setBackgroundColor(Color.RED);
                    schedule[daynum][22] = "0";
                }
                break;

            case R.id.elevenpm:
                if (schedule[daynum][23] == null || schedule[daynum][23].equals("0")) {
                    elevenpm.setBackgroundColor(Color.GREEN);
                    schedule[daynum][23] = "1";

                } else if (schedule[daynum][23].equals("1")) {
                    elevenpm.setBackgroundColor(Color.RED);
                    schedule[daynum][23] = "0";
                }
                break;
        }
    }



    public void fillSchedule(){

    }
}
