package com.uoft.jonathan.campfire;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.MenuInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import backend.algorithms.CampfireGroup;
import backend.algorithms.Course;
import backend.algorithms.Student;
import backend.database.Chat;
import backend.database.DbAdapter;

import static backend.database.DbAdapter.getAllChatsForUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, DiscoverFragment.usersLoadedListener{

    private String[] newStudentID;
    NavigationView navigationView;

    static final String STATE_EMAIL = "email";
    private String uEmail;
    private String uName;
    private Student uStudent;
    private String[] currentStudentID = new String[4];
    private String[] currentStringCriteria;

    //Transferable data between fragments
    private String[] classmatesNames;
    private String[] classmatesEmails;
    private String[] courseCodes;
    private String[] courseNames;
    private String[] courseInstructors;

    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText editSearch;
    private boolean searchInProgress = false;
    private boolean myCoursesIsOpen = false;
    private boolean myCampfireIsOpen = false;

    private ArrayList<Student> studentsInCourse;
    private List<Student> allStudents;
    private Course currentCourse;
    private List<String> enrolledCourses;
    private List<Course> allCourses;

    private List<CampfireGroup> currentGroups;
    private List<Chat> chats;

    private Handler handler;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private FirebaseUser currentUser;
    private String[] programmingLanguages;
    private HashMap<String, ArrayList<String>> schedule = new HashMap<>();
    private ArrayList<String> coursesTaking = new ArrayList<String>();
    private String currentUserID;

    private String loggedIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main);
        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Campfire");


//        fetchAllStudents();

//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                allCourses = DbAdapter.getAllCourses();
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                    }
//                });
//            }
//        }).start();

        /**
         * 1. Connect to the Database
         * 2. Begin matching and display results in the form of cards
         */
        Intent intent = getIntent();
        loggedIn = intent.getExtras().getString("loggedIn");
        String userEmail = intent.getExtras().getString("email");
        String userPassword = intent.getExtras().getString("password");

        mAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        mAuth.signOut();

        if (loggedIn.equals("0")){
            newStudentID = intent.getExtras().getStringArray("identity");
            programmingLanguages = intent.getExtras().getStringArray("programmingLanguages");
            schedule = (HashMap<String,ArrayList<String>>) intent.getSerializableExtra("schedule");
            uEmail = newStudentID[2];
            final String currentCourse = newStudentID[4];

            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser newUser = firebaseAuth.getCurrentUser();
                    if (newUser != null) {
                        // User is signed in

                        String name = newUser.getDisplayName();
                        String email = newUser.getEmail();
                        currentUserID = newUser.getUid();

                        DatabaseReference myRef = database.getReference("Users/" + currentUserID + "/Email");
                        myRef.setValue(email);

                        myRef = database.getReference("Users/" + currentUserID + "/Name");
                        myRef.setValue(newStudentID[0] + " " + newStudentID[1]);

                        myRef = database.getReference("Users/" + currentUserID + "/Previous CS Courses");
                        ArrayList<String> csCourses = new ArrayList<String>();
                        csCourses.add(newStudentID[5]);
                        myRef.setValue(csCourses);

                        myRef = database.getReference("Users/" + currentUserID + "/Previous Elective Courses");
                        ArrayList<String> electives = new ArrayList<String>();
                        electives.add(newStudentID[6]);
                        myRef.setValue(electives);

                        myRef = database.getReference("Users/" + currentUserID + "/Hobbies");
                        ArrayList<String> hobbies = new ArrayList<String>();
                        hobbies.add(newStudentID[7]);
                        myRef.setValue(hobbies);

                        myRef = database.getReference("Users/" + currentUserID + "/Programming Languages");
                        ArrayList<String> programmingLang = new ArrayList(Arrays.asList(programmingLanguages));
                        myRef.setValue(programmingLang);

                        for (String day: schedule.keySet()){
                            if (schedule.get(day).size() > 0){
                                myRef = database.getReference("Users/" + currentUserID + "/Schedule/" + day);
                                myRef.setValue(schedule.get(day));
                            }
                        }


                        myRef = database.getReference("Taking/" + currentUserID);
                        ArrayList<String> taking = new ArrayList<String>();
                        taking.add(currentCourse);
                        myRef.setValue(taking);

                        myRef = database.getReference("Courses/" + currentCourse + "/Users/" + currentUserID);
                        myRef.setValue(uEmail);

                    } else {
                        // User is signed out
                    }
                    // ...
                }
            };
        }else{
            newStudentID = new String[3];
            mAuthListener = new FirebaseAuth.AuthStateListener() {
                @Override
                public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                    FirebaseUser newUser = firebaseAuth.getCurrentUser();
                    if (newUser != null) {
                        // User is signed in
                        currentUserID = newUser.getUid();
                        DatabaseReference userRef = database.getReference("Users/" + currentUserID);
                        DatabaseReference takingRef = database.getReference("Taking/" + currentUserID);
                        userRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                int i = 0;
                                ArrayList<String> hobbies;
                                ArrayList<String> prevCSCourses;
                                ArrayList<String> prevElectives;
                                ArrayList<String> programmingLanguages;

                                for (DataSnapshot child: dataSnapshot.getChildren()){
                                    if (i == 0) {

                                    }else if (i == 1){

                                    }else if (i == 2){
                                        uEmail = child.getValue(String.class);
                                        newStudentID[2] = uEmail;
                                    }else if (i == 3){

                                    }else if (i == 4){

                                    }else if (i == 5){
                                        uName = child.getValue(String.class);
                                        newStudentID[0] = uName.substring(0, uName.indexOf(" "));
                                        newStudentID[1] = uName.substring(uName.indexOf(" ") + 1, uName.length());
                                    }else if (i == 6){
//                                        prevCSCourses = child.getValue(ArrayList.class);
                                    }else if (i == 7){
//                                        prevElectives = child.getValue(ArrayList.class);
                                    }else if (i == 8){
//                                        programmingLanguages = child.getValue(ArrayList.class);
                                    }else if (i == 9){

                                    }
                                    i++;
                                }
                                System.out.println("Number of Children: " + Long.toString(dataSnapshot.getChildrenCount()));
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        takingRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                for (DataSnapshot child: dataSnapshot.getChildren()){
                                    coursesTaking.add(child.getValue(String.class));
                                }
                                renderData(toolbar);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                    } else {
                        // User is signed out
                    }
                    // ...
                }
            };
            signIn(userEmail, userPassword);
        }

//        signIn(newStudentID[2], newStudentID[3]);
//        currentUser = mAuth.getCurrentUser();

//        String uid = mAuth.getCurrentUser().getUid();
//        System.out.println("\n\n\n\nXXXXXXXXXXXXXXXXX " + uid + "\n\n\n\n");
//        FirebaseDatabase database = FirebaseDatabase.getInstance();
//        DatabaseReference myRef = database.getReference("Taking/" + uid);
//        myRef.setValue(newStudentID[4]);
//        myRef.push();

//        if (currentUser != null) {
//            // Name, email address
//            String name = currentUser.getDisplayName();
//            String email = currentUser.getEmail();
//
//            // The user's ID, unique to the Firebase project. Do NOT use this value to
//            // authenticate with your backend server, if you have one. Use
//            // FirebaseUser.getToken() instead.
//            String uid = currentUser.getUid();
//
//            System.out.println("\n\n\n\nXXXXXXXXXXXXXXXXX " + uid + "\n\n\n\n");
//
//            FirebaseDatabase database = FirebaseDatabase.getInstance();
//            DatabaseReference myRef = database.getReference("Taking/" + uid);
//            myRef.setValue(newStudentID[4]);
//            myRef.push();
//        }
//        handler = new Handler();
//        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
//        progressDialog.setMessage("Please wait....");
//        progressDialog.setTitle("Retrieving User Data");
//        progressDialog.show();
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                uStudent = DbAdapter.getStudentLite(uEmail);
//                uName = uStudent.getFname() + " " + uStudent.getLname();
//                List<String> enrolledCourses = DbAdapter.allStudentsCourses(uEmail);
//                currentCourse = DbAdapter.getCourse(enrolledCourses.get(0));
//
//                currentStudentID[0] = uStudent.getFname();
//                currentStudentID[1] = uStudent.getLname();
//                currentStudentID[2] = uStudent.getEmail();
//                currentStudentID[3] = uStudent.getPass();
//
//                //Defaults to first course
//                studentsInCourse = uStudent.getallOtherCourseStudents(currentCourse);
//                ArrayList<String> classmatesNamesList = new ArrayList<String>();
//                ArrayList<String> classmatesEmailsList = new ArrayList<String>();
//                for (Student s: studentsInCourse){
//                    classmatesNamesList.add(s.getFname() + " " + s.getLname());
//                    classmatesEmailsList.add(s.getEmail());
//                }
//                classmatesNames = new String[classmatesNamesList.size()];
//                classmatesNames = classmatesNamesList.toArray(classmatesNames);
//                classmatesEmails = new String[classmatesEmailsList.size()];
//                classmatesEmails = classmatesEmailsList.toArray(classmatesEmails);
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        renderData(toolbar);
//                        progressDialog.dismiss();
////                        matchesDialog.show();
//                    }
//                });
//            }
//        }).start();
//        getGroups(uEmail);
//        processChats(uEmail);
    }


    public void renderData(Toolbar toolbar){
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView emailHeader = (TextView) headerView.findViewById(R.id.emailHeader);
        TextView nameHeader = (TextView) headerView.findViewById(R.id.nameHeader);
        TextView courseHeader = (TextView) headerView.findViewById(R.id.courseHeader);
        emailHeader.setText(uEmail);
        nameHeader.setText(uName);

//        courseHeader.setText("Current Course: " + currentCourse.getName());
        courseHeader.setText("Current Course: " + coursesTaking.get(0));

//        displaySelectedScreen(R.id.nav_home);

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myProfileIntent;
                myProfileIntent = new Intent(MainActivity.this, MyProfileActivity.class);
                myProfileIntent.putExtra("userEmail", uEmail);
                myProfileIntent.putExtra("currentUserID", currentUserID);
                startActivity(myProfileIntent);
            }
        });
        navigationView.getMenu().getItem(0).setChecked(true);

//        Menu menuNav = navigationView.getMenu();
//        final MenuItem nav_courses = menuNav.findItem(R.id.nav_my_courses);
//        nav_courses.setEnabled(false);
//
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                List<String> enrolledCourses = DbAdapter.allStudentsCourses(uEmail);
//                currentCourse = DbAdapter.getCourse(enrolledCourses.get(0));
//                ArrayList<String> courseCodesList = new ArrayList<String>();
//                ArrayList<String> courseNamesList = new ArrayList<String>();
//                ArrayList<String> courseInstructorList = new ArrayList<String>();
//                for (String code: enrolledCourses){
//                    Course current = DbAdapter.getCourse(code);
//                    courseCodesList.add(code);
//                    courseNamesList.add(current.getDescription());
//                    courseInstructorList.add(current.getInstructor());
//                }
//                courseCodes = new String[courseCodesList.size()];
//                courseCodes = courseCodesList.toArray(courseCodes);
//                courseNames = new String[courseNamesList.size()];
//                courseNames = courseNamesList.toArray(courseNames);
//                courseInstructors = new String[courseInstructorList.size()];
//                courseInstructors = courseInstructorList.toArray(courseInstructors);
//                handler.post(new Runnable() {
//                    @Override
//                    public void run() {
//                        nav_courses.setEnabled(true);
//                    }
//                });
//            }
//        }).start();
    }

    public void getGroups(final String email){
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentGroups = DbAdapter.getAllStudentsGroups(email);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        }).start();
    }

    public void processChats(final String email){
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    chats = getAllChatsForUser(uEmail);
                }catch (Exception e){
                    chats = new ArrayList<Chat>();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        }).start();
    }

    public void fetchAllStudents(){
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                allStudents = DbAdapter.getAllStudents();
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                    }
                });
            }
        }).start();
    }

    public List<Chat> getChats(){
        return this.chats;
    }

    public List<CampfireGroup> getCurrentGroups(){
        return this.currentGroups;
    }

    public ArrayList<Student> getStudentsInCourse(){
        return this.studentsInCourse;
    }

    public String[] getClassmatesNames(){
        return this.classmatesNames;
    }

    public List<String> getCurrentCourses(){
        return this.enrolledCourses;
    }

    public List<Student> getAllStudents(){
        return this.allStudents;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        savedInstanceState.putString(STATE_EMAIL, uEmail);
        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (isSearchOpened){
            handleMenuSearch();
            return;
        }else{
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
            } else {

                AlertDialog terminateDialog = new AlertDialog.Builder(MainActivity.this).create();
                terminateDialog.setMessage("Would you like to Logout or Close the App?");
                terminateDialog.setButton(AlertDialog.BUTTON_POSITIVE, "Close App",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                moveTaskToBack(true);
                                android.os.Process.killProcess(android.os.Process.myPid());
                                System.exit(1);
                            }
                        });
                terminateDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "Logout",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                logout();
                            }
                        });
                terminateDialog.show();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_settings:
                Intent promoIntent = new Intent(this, PromoActivity.class);
                startActivity(promoIntent);
                return true;
            case R.id.action_search:
                handleMenuSearch();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        //calling the method displayselectedscreen and passing the id of selected menu
        displaySelectedScreen(item.getItemId());
        //make this method blank
        return true;
    }

    private void displaySelectedScreen(int itemId) {

        Bundle bundle = new Bundle();
        bundle.putStringArray("identity", newStudentID);

        //creating fragment object
        Fragment fragment = null;
        Intent miscIntent;

        if (isSearchOpened) {
            handleMenuSearch();
            return;
        }
        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_messages:
                fragment = new MessagesFragment();
                fragment.setArguments(bundle);
                mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_white_48dp));
                myCoursesIsOpen = false;
                myCampfireIsOpen = false;
                break;
            case R.id.nav_my_campfire:
                fragment = new MyCampfireFragment();
                bundle.putString("currentCourse", currentCourse.getName());
                fragment.setArguments(bundle);
                mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_add_box_white_48dp));
                myCoursesIsOpen = false;
                myCampfireIsOpen = true;
                break;
            case R.id.nav_discover:
                fragment = new DiscoverFragment();
                bundle.putStringArray("classmatesNames", classmatesNames);
                bundle.putStringArray("classmatesEmails", classmatesEmails);
                fragment.setArguments(bundle);
                mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_white_48dp));
                myCoursesIsOpen = false;
                break;
            case R.id.nav_home:
                setTitle("Home");
                fragment = new HomeFragment();
                //bundle.putString("currentCourse", currentCourse.getName());
                //fragment.setArguments(bundle);
                myCampfireIsOpen = false;
                if (myCoursesIsOpen){
                    mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_white_48dp));
                    myCoursesIsOpen = false;
                }
                break;
            case R.id.nav_my_courses:
                fragment = new MyCoursesFragment();
                bundle.putStringArray("courseCodes", courseCodes);
                bundle.putStringArray("courseNames", courseNames);
                bundle.putStringArray("courseInstructors", courseInstructors);
                bundle.putString("currentCourseCode", currentCourse.getName());
                fragment.setArguments(bundle);
                mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_add_circle_white_48dp));
                myCoursesIsOpen = true;
                myCampfireIsOpen = false;
                break;
            case R.id.nav_help:
                miscIntent = new Intent(this, HelpActivity.class);
                miscIntent.putExtra("identity", newStudentID);
                startActivity(miscIntent);
                break;
            case R.id.nav_settings:
                miscIntent = new Intent(this, SettingsActivity.class);
                miscIntent.putExtra("identity", newStudentID);
                startActivity(miscIntent);
                break;
            case R.id.nav_logout:
                Intent promoIntent = new Intent(this, PromoActivity.class);
                startActivity(promoIntent);
                break;
            case R.id.nav_change_password:
                miscIntent = new Intent(this, ChangePasswordActivity.class);
                miscIntent.putExtra("identity", newStudentID);
                startActivity(miscIntent);
                break;
        }

        //replacing the fragment
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.commit();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    public void myProfile(){
        Intent miscIntent;
        miscIntent = new Intent(this, SettingsActivity.class);
        miscIntent.putExtra("userEmail", uEmail);
        startActivity(miscIntent);
    }

    public Student getCurrentStudent() {
        return this.uStudent;
    }

    public Course getCurrentCourse(){
        return this.currentCourse;
    }

    public void logout(){

        FirebaseAuth.getInstance().signOut();

        //Pressing the back button on the Android device will log the user off
        Toast.makeText(getApplicationContext(), "You have logged out!", Toast.LENGTH_SHORT).show();
        Intent promoIntent = new Intent(this, PromoActivity.class);
        startActivity(promoIntent);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        mSearchAction = menu.findItem(R.id.action_search);
        return super.onPrepareOptionsMenu(menu);
    }

    protected void handleMenuSearch(){
        ActionBar action = getSupportActionBar(); //get the actionbar

        if(isSearchOpened){ //test if the search is open

            action.setDisplayShowCustomEnabled(false); //disable a custom view inside the actionbar
            action.setDisplayShowTitleEnabled(true); //show the title in the action bar

            //hides the keyboard
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

            //add the search icon in the action bar
            mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_white_48dp));
            isSearchOpened = false;

            //Refresh user list
            if (searchInProgress){
                Bundle bundle = new Bundle();
                bundle.putStringArray("identity", newStudentID);
                bundle.putStringArray("search", null);
                bundle.putStringArray("classmatesNames", classmatesNames);
                bundle.putStringArray("classmatesEmails", classmatesEmails);
                Fragment fragment = null;
                fragment = new DiscoverFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                searchInProgress = false;
            }

        } else { //open the search entry or course adder

            if (myCoursesIsOpen) {
                addCourse();
            }else if (myCampfireIsOpen){
                addGroup();
            }else{
                action.setDisplayShowCustomEnabled(true); //enable it to display a
                // custom view in the action bar.
                action.setCustomView(R.layout.search_bar);//add the custom view
                action.setDisplayShowTitleEnabled(false); //hide the title

                editSearch = (EditText)action.getCustomView().findViewById(R.id.editSearch); //the text editor

                //this is a listener to do a search when the user clicks on search button
                editSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
                    @Override
                    public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                        if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                            editSearch.clearFocus();
                            performSearch(editSearch.getText().toString());
                            closeKeyboard();
                            return true;
                        }
                        return false;
                    }
                });
                editSearch.requestFocus();

                //open the keyboard focused in the edtSearch
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(editSearch, InputMethodManager.SHOW_IMPLICIT);

                //add the close icon
                mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_clear_white_24dp));
                isSearchOpened = true;
            }
        }
    }

    public void performSearch(String query){
        Snackbar.make(findViewById(R.id.content_main), "Search results for: " + query, Snackbar.LENGTH_LONG)
                .setAction("Action", null).show();

        ArrayList<String> searchResults = new ArrayList<String>();

        for (Student s : studentsInCourse) {
            if ((!s.getEmail().equals(uEmail)) && (s.getFname().contains(query))){
                searchResults.add(s.getEmail());
            }
        }
        String[] searchResultsArray = new String[searchResults.size()];
        searchResultsArray = searchResults.toArray(searchResultsArray);

        Bundle bundle = new Bundle();
        bundle.putStringArray("identity", newStudentID);
        bundle.putStringArray("search", searchResultsArray);
        Fragment fragment = null;
        fragment = new DiscoverFragment();
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();

        closeKeyboard();
        navigationView.getMenu().getItem(4).setChecked(true);
        searchInProgress = true;
        isSearchOpened = true;
    }

    public void closeKeyboard(){
        Context context = getApplicationContext();
        View view = getCurrentFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public void addCourse(){
        new MaterialDialog.Builder(this)
                .title("Add Course")
                .content("Enter a course code:")
                .inputType(InputType.TYPE_TEXT_VARIATION_NORMAL)
                .inputRange(8, 8)
                .input("CSC108H1", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        String userInput = input.toString();
                        Course newCourse = new Course(userInput, "", "");
                        try{
                            DbAdapter.addCourse(newCourse);
                        }catch(Exception e){

                        }
                        DbAdapter.enrolStudentInCourse(uEmail, userInput);
                        refreshCourseList();
                    }
                }).show();
    }

    public void refreshCourseList(){
        handler = new Handler();
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setTitle("Adding Course");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                List<String> enrolledCourses = DbAdapter.allStudentsCourses(uEmail);
                ArrayList<String> courseCodesList = new ArrayList<String>();
                ArrayList<String> courseNamesList = new ArrayList<String>();
                ArrayList<String> courseInstructorList = new ArrayList<String>();
                for (String code: enrolledCourses){
                    Course current = DbAdapter.getCourse(code);
                    courseCodesList.add(code);
                    courseNamesList.add(current.getDescription());
                    courseInstructorList.add(current.getInstructor());
                }
                courseCodes = new String[courseCodesList.size()];
                courseCodes = courseCodesList.toArray(courseCodes);
                courseNames = new String[courseNamesList.size()];
                courseNames = courseNamesList.toArray(courseNames);
                courseInstructors = new String[courseInstructorList.size()];
                courseInstructors = courseInstructorList.toArray(courseInstructors);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Bundle bundle = new Bundle();
                        bundle.putStringArray("identity", newStudentID);
                        bundle.putStringArray("courseCodes", courseCodes);
                        bundle.putStringArray("courseNames", courseNames);
                        bundle.putStringArray("courseInstructors", courseInstructors);
                        bundle.putString("currentCourseCode", currentCourse.getName());
                        Fragment fragment = null;
                        fragment = new MyCoursesFragment();
                        fragment.setArguments(bundle);
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.content_frame, fragment);
                        ft.commit();
                    }
                });
            }
        }).start();
    }

    public void loadUsers(ArrayList<Student> users){
        this.studentsInCourse = users;
    }

    public void addGroup(){
        new MaterialDialog.Builder(this)
                .title("Add Group")
                .content("Enter a group name:")
                .inputType(InputType.TYPE_TEXT_VARIATION_NORMAL)
                .inputRange(2, 50)
                .input("Assignment 1", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        String userInput = input.toString();
                        addGroupSize(userInput);
                    }
                }).show();
    }

    public void addGroupSize(String name){
        final String groupName = name;
        new MaterialDialog.Builder(this)
                .title("Specify Size")
                .content("Enter the size of this group:")
                .inputType(InputType.TYPE_CLASS_NUMBER)
                .inputRange(1, 1)
                .input("", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        String userInput = input.toString();
                        int size = Integer.parseInt(userInput);
                        ArrayList<Student> members = new ArrayList<Student>();
                        members.add(uStudent);
                        if (size != 0){
                            // TODO: Should generate actual distinct ID, instead of using rand.
//                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                                uStudent.createGroup(currentCourse, groupName, size, ThreadLocalRandom.current().nextInt(0, 1000000000));
//                            }
//                            else {
//                                Random rand = new Random();
//                                uStudent.createGroup(currentCourse, groupName, size, rand.nextInt(1000000000));
//                            }
                            createGroup(groupName, members, size);
                        }else{
                            Toast.makeText(MainActivity.this, "Invalid Number", Toast.LENGTH_SHORT).show();
                        }
                    }
                }).show();
    }

    public void createGroup(final String groupName, final ArrayList<Student> members, final int size){
        handler = new Handler();
        final ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait....");
        progressDialog.setTitle("Creating Group");
        progressDialog.show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                CampfireGroup newGroup = new CampfireGroup(groupName, members, size, DbAdapter.getUniqueGroupKey());
                DbAdapter.addGroup(newGroup);
                currentGroups = DbAdapter.getAllStudentsGroups(uStudent.getEmail());
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        refreshGroupList();
                    }
                });
            }
        }).start();
    }

    public void refreshGroupList(){
        Bundle bundle = new Bundle();
        bundle.putStringArray("identity", newStudentID);
        bundle.putString("currentCourse", currentCourse.getName());
        Fragment fragment = null;
        fragment = new MyCampfireFragment();
        fragment.setArguments(bundle);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    public void refreshGroupListMemberCount(final String email){
        handler = new Handler();
        new Thread(new Runnable() {
            @Override
            public void run() {
                currentGroups = DbAdapter.getAllStudentsGroups(email);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        refreshGroupList();
                    }
                });
            }
        }).start();
    }

    public void updateCourse(String courseCode){
        final String newCourseCode = courseCode;
        final Course newCourse = DbAdapter.getCourse(newCourseCode);
        currentCourse = newCourse;
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);
        TextView courseHeader = (TextView) headerView.findViewById(R.id.courseHeader);
        courseHeader.setText("Current Course: " + currentCourse.getName());
        studentsInCourse = uStudent.getallOtherCourseStudents(currentCourse);
        ArrayList<String> classmatesNamesList = new ArrayList<String>();
        ArrayList<String> classmatesEmailsList = new ArrayList<String>();
        for (Student s: studentsInCourse){
            classmatesNamesList.add(s.getFname() + " " + s.getLname());
            classmatesEmailsList.add(s.getEmail());
        }
        classmatesNames = new String[classmatesNamesList.size()];
        classmatesNames = classmatesNamesList.toArray(classmatesNames);
        classmatesEmails = new String[classmatesEmailsList.size()];
        classmatesEmails = classmatesEmailsList.toArray(classmatesEmails);
    }







    @Override
    public void onStart(){
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null){
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    public void signIn(String email, String password){
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
//                        Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
//                            Log.w(TAG, "signInWithEmail:failed", task.getException());
                            Toast.makeText(MainActivity.this, "Authentication Failed",
                                    Toast.LENGTH_SHORT).show();
                        }

                        // ...
                    }
                });
    }
}
