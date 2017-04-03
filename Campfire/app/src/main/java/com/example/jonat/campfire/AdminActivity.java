package com.example.jonat.campfire;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import org.w3c.dom.Text;

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
    private Integer warningImage = R.drawable.ic_warning_white_48dp;
    private Handler handler;
    private ListView mainListView;
    private View view;
    private LinearLayout homeLayout;
    private TextView userCount;
    private TextView courseCount;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mainListView.setVisibility(View.INVISIBLE);
                    homeLayout.setVisibility(View.VISIBLE);
                    setTitle("Home");
                    return true;
                case R.id.navigation_all_users:
                    mainListView.setVisibility(View.VISIBLE);
                    homeLayout.setVisibility(View.INVISIBLE);
                    displayAllUsers(allStudents);
                    setTitle("Manage Users");
                    return true;
                case R.id.navigation_all_courses:
                    mainListView.setVisibility(View.VISIBLE);
                    homeLayout.setVisibility(View.INVISIBLE);
                    displayAllCourses(allCourses);
                    setTitle("Manage Courses");
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
        homeLayout = (LinearLayout) findViewById(R.id.AdminHomeLayout);
        userCount = (TextView) findViewById(R.id.userCount);
        courseCount = (TextView) findViewById(R.id.courseCount);
        setTitle("Home");

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
                        //Any code in this area is executed upon completion of the above Db calls
                        userCount.setText("Current User Count: " + allStudents.size());
                        courseCount.setText("Current Course Count: " + allCourses.size());
                        progressDialog.dismiss();

                        boolean existsUsersThatCannotLogin = false;
                        for (Student s: allStudents){
                            if (s.getPass().endsWith("*")){
                                existsUsersThatCannotLogin = true;
                            }
                        }
                        if (existsUsersThatCannotLogin){
                            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(AdminActivity.this);
                            mBuilder.setSmallIcon(R.drawable.ic_warning_black_48dp)
                                    .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_account_circle_black_48dp))
                                    .setContentTitle("Attention")
                                    .setContentText("There are users that are unable to login to Campfire")
                                    .setDefaults(Notification.DEFAULT_ALL)
                                    .setPriority(Notification.PRIORITY_HIGH);
                            int mNotificationId = 001;

                            NotificationManager mNotifyMgr =
                                    (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                            mNotifyMgr.notify(mNotificationId, mBuilder.build());
                        }
                    }
                });
            }
        }).start();
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    @Override
    public void onBackPressed() {
        logout();
    }

    public void logout(){
        //Pressing the back button on the Android device will log the user off
        Toast.makeText(getApplicationContext(), "You have logged out!", Toast.LENGTH_SHORT).show();
        Intent promoIntent = new Intent(this, PromoActivity.class);
        startActivity(promoIntent);
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
            if (allStudents.get(i).getPass().endsWith("*")){
                images[i] = warningImage;
            }else{
                images[i] = accountImage;
            }
        }
        MyCoursesListAdapter mainList = new MyCoursesListAdapter(AdminActivity.this, names, description, images);
        mainListView.setAdapter(mainList);

        mainListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {
                if (images[i] == warningImage){
                    new android.app.AlertDialog.Builder(AdminActivity.this)
                            .setTitle(names[i])
                            .setMessage(description[i])
                            .setIcon(accountImageDark)
                            .setPositiveButton("Close", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .setNeutralButton("Change Password", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    openChangePasswordDialog(description[i]);
                                }
                            })
                            .show();
                }else{
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
                            .setNegativeButton("Assign to Group", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
            }
        });
    }

    public void openChangePasswordDialog(String email){
        final String emailCopy = email;
        new MaterialDialog.Builder(this)
                .title("Password Change")
                .content("Enter a new password:")
                .inputRange(8, 50)
                .inputType(InputType.TYPE_TEXT_VARIATION_PASSWORD)
                .input("", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        String userInput = input.toString();
                        changePassword(userInput, emailCopy);
                    }
                }).show();
    }

    public void changePassword(String newPassword, String email){
        final String emailCopy = email;
        final ProgressDialog progressDialog = new ProgressDialog(AdminActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setTitle("Changing Password");
        progressDialog.show();
        handler = new Handler();
        final String newPassCopy = newPassword;
        new Thread(new Runnable() {
            @Override
            public void run() {
                final Student currentStudent = DbAdapter.getStudent(emailCopy);
                currentStudent.setPass(newPassCopy);
                DbAdapter.updateStudent(currentStudent);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(AdminActivity.this, "Password Changed", Toast.LENGTH_LONG).show();
                        for (Student s: allStudents){
                            if (s.getEmail().equals(currentStudent.getEmail())){
                                s.setPass(currentStudent.getPass());
                            }
                        }
                        displayAllUsers(allStudents);
                    }
                });
            }
        }).start();
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
            names[i] = allCourses.get(i).getName();
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
                                displayStudentList(names[i]);
                            }
                        })
                        .show();
            }
        });
    }

    public void displayStudentList(final String code){
        Course currentCourse = DbAdapter.getCourse(code);
        ArrayList<Student> studentsInCourse = currentCourse.getStudents();
        final ArrayList<Student> newAllStudents = new ArrayList<Student>();
        for (Student s: allStudents){
            Boolean exists = false;
            for (Student t: studentsInCourse){
                if (s.getEmail().equals(t.getEmail())){
                    exists = true;
                }
            }
            if (!exists){
                newAllStudents.add(s);
            }
        }
        String[] studentNames = new String[newAllStudents.size()];
        for (int i = 0; i < studentNames.length; i++){
            studentNames[i] = newAllStudents.get(i).getFname() + " " + newAllStudents.get(i).getLname();
        }
        new LovelyChoiceDialog(AdminActivity.this)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setTitle("Choose a Student")
                .setMessage("")
                .setItems(studentNames, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        enrollStudent(newAllStudents.get(position).getEmail(), code);
                    }
                })
                .show();
    }

    public void enrollStudent(final String email, final String code){
        handler = new Handler();
        final ProgressDialog progressDialog = new ProgressDialog(AdminActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setTitle("Enrolling Student");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                DbAdapter.enrolStudentInCourse(email, code);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Snackbar snackbar = Snackbar
                                .make(view, "Success", Snackbar.LENGTH_SHORT);
                        snackbar.show();
                    }
                });
            }
        }).start();
    }
    //-------------------------------- COURSE MANAGEMENT ---------------------------------------

}
