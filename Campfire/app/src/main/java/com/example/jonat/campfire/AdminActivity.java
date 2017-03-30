package com.example.jonat.campfire;

import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import backend.algorithms.Course;
import backend.algorithms.Student;
import backend.database.DbAdapter;

import static com.example.jonat.campfire.R.id.all;
import static com.example.jonat.campfire.R.id.coursesListView;

public class AdminActivity extends AppCompatActivity {

    private List<Student> allStudents;
    private List<Course> allCourses;
    private String[] names;
    private String[] description;
    private Integer[] images;
    private Integer accountImage = R.drawable.ic_account_circle_white_48dp;
    private Integer accountImageDark = R.drawable.ic_account_circle_black_48dp;
    private Integer courseImage = R.drawable.ic_class_white_48dp;
    private Integer courseImageDark = R.drawable.ic_class_black_48dp;
    private Handler handler;
    private ListView mainListView;
    private View view;

    private List<Student> lockedStudents;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mainListView.setVisibility(View.INVISIBLE);
                    return true;
                case R.id.navigation_all_users:
                    mainListView.setVisibility(View.VISIBLE);
                    displayAllUsers(allStudents);
                    return true;
                case R.id.navigation_all_courses:
                    mainListView.setVisibility(View.VISIBLE);
                    displayAllCourses(allCourses);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        mainListView = (ListView) findViewById(R.id.listOfOptions);
        view = (View) findViewById(R.id.container);

        handler = new Handler();
        final ProgressDialog progressDialog = new ProgressDialog(AdminActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setTitle("Retrieving Data");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                allStudents = DbAdapter.getAllStudents();
                allCourses = DbAdapter.getAllCourses();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                    }
                });
            }
        }).start();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //////////////  Notification Example (To be used later) ///////////
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.ic_account_circle_black_48dp)
//                        .setContentTitle("My notification")
//                        .setContentText("Hello World!");
//        int mNotificationId = 001;
//
//        NotificationManager mNotifyMgr =
//                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    //-------------------------------- USER MANAGEMENT ---------------------------------------
    public void displayAllUsers(List<Student> allStudents){
        int listSize = allStudents.size();
        names = new String[listSize];
        description = new String[listSize];
        images = new Integer[listSize];

        for (int i = 0; i < allStudents.size(); i++){
            names[i] = allStudents.get(i).getFname() + " " + allStudents.get(i).getLname();
            description[i] = allStudents.get(i).getEmail();
            images[i] = accountImage;
        }
        MyCoursesListAdapter mainList = new MyCoursesListAdapter(AdminActivity.this, names, description, images);
        mainListView.setAdapter(mainList);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                new android.app.AlertDialog.Builder(AdminActivity.this)
                        .setTitle(names[i])
                        .setMessage(description[i])
                        .setIcon(accountImageDark)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNeutralButton("Delete User", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                confirmDeleteUser(names[i], description[i]);
                            }
                        })
                        .show();
            }
        });
    }

    public void confirmDeleteUser(String name, String email){
        final String e = email;
        new android.app.AlertDialog.Builder(AdminActivity.this)
                .setTitle("Warning")
                .setMessage("Are you sure you want to delete " + name + "?")
                .setPositiveButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .setNeutralButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        deleteUser(e);
                    }
                })
                .show();
    }

    public void deleteUser(String email){
        final String e = email;
        handler = new Handler();
        final ProgressDialog progressDialog = new ProgressDialog(AdminActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setTitle("Deleting User");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DbAdapter.deleteStudent(e);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Snackbar snackbar = Snackbar
                                .make(view, "Successfully deleted " + e, Snackbar.LENGTH_LONG);
                        snackbar.show();
                        refreshUserList(e);
                    }
                });
            }
        }).start();
    }

    public void refreshUserList(String email){
        ArrayList<Student> newStudentList = new ArrayList<Student>();
        for (Student s: allStudents){
            if (!s.getEmail().equals(email)){
                newStudentList.add(s);
            }
        }
        displayAllUsers(newStudentList);
    }
    //-------------------------------- USER MANAGEMENT ---------------------------------------


    //-------------------------------- COURSE MANAGEMENT ---------------------------------------
    public void displayAllCourses(List<Course> allCourses){
        int listSize = allCourses.size();
        names = new String[listSize];
        description = new String[listSize];
        images = new Integer[listSize];

        for (int i = 0; i < allCourses.size(); i++){
            names[i] = allCourses.get(i).getCourseCode();
            description[i] = allCourses.get(i).getName() + "\n\nInstructor: " + allCourses.get(i).getInstructor();
            images[i] = courseImage;
        }
        MyCoursesListAdapter mainList = new MyCoursesListAdapter(AdminActivity.this, names, description, images);
        mainListView.setAdapter(mainList);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                new android.app.AlertDialog.Builder(AdminActivity.this)
                        .setTitle(names[i])
                        .setMessage(description[i])
                        .setIcon(courseImageDark)
                        .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .setNeutralButton("Enroll a Student", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .show();
            }
        });
    }
    //-------------------------------- COURSE MANAGEMENT ---------------------------------------

}
