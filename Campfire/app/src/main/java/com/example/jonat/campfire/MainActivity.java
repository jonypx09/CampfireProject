package com.example.jonat.campfire;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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
import com.yarolegovich.lovelydialog.LovelyChoiceDialog;

import java.util.ArrayList;

import backend.algorithms.Student;
import backend.database.DatabaseAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String[] newStudentID;
    DatabaseAdapter db;
    NavigationView navigationView;

    static final String STATE_EMAIL = "email";
    private String uEmail;
    private String uName;
    private Student uStudent;

    private MenuItem mSearchAction;
    private boolean isSearchOpened = false;
    private EditText editSearch;
    private boolean searchInProgress = false;
    private boolean myCoursesIsOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /**
         * 1. Connect to the Database
         * 2. Begin matching and display results in the form of cards
         */
        db = new DatabaseAdapter(this);

        Intent intent = getIntent();
        newStudentID = intent.getExtras().getStringArray("identity");
        uEmail = newStudentID[2];
        uStudent = db.getStudent(uEmail);
        uName = uStudent.getFname() + " " + uStudent.getLname();

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
        ArrayList<String> enrolledCourses = db.enrolledIn(uEmail);
        courseHeader.setText("Current Course: " + enrolledCourses.get(1));

        displaySelectedScreen(R.id.nav_home);

        headerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myProfileIntent;
                myProfileIntent = new Intent(MainActivity.this, MyProfileActivity.class);
                myProfileIntent.putExtra("userEmail", uEmail);
                startActivity(myProfileIntent);
            }
        });
        navigationView.getMenu().getItem(0).setChecked(true);
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
                break;
            case R.id.nav_my_campfire:
                fragment = new MyCampfireFragment();
                fragment.setArguments(bundle);
                mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_white_48dp));
                break;
            case R.id.nav_discover:
                fragment = new DiscoverFragment();
                fragment.setArguments(bundle);
                mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_white_48dp));
                break;
            case R.id.nav_home:
                fragment = new HomeFragment();
                fragment.setArguments(bundle);
                if (myCoursesIsOpen){
                    mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_search_white_48dp));
                    myCoursesIsOpen = false;
                }
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
            case R.id.nav_my_courses:
                fragment = new MyCoursesFragment();
                fragment.setArguments(bundle);
                mSearchAction.setIcon(getResources().getDrawable(R.drawable.ic_add_circle_white_48dp));
                myCoursesIsOpen = true;
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

    public void logout(){
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
                Fragment fragment = null;
                fragment = new DiscoverFragment();
                fragment.setArguments(bundle);
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.content_frame, fragment);
                ft.commit();
                searchInProgress = false;
            }

        } else { //open the search entry or course adder

            if (myCoursesIsOpen){
                addCourse();
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
        ArrayList<String> enrolledCourses = db.enrolledIn(uEmail);
        ArrayList<Student> classmates = db.getStudentsInCourse(enrolledCourses.get(0));
        for (Student s : classmates) {
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
        navigationView.getMenu().getItem(3).setChecked(true);
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
                .input("CSC108H1", "", new MaterialDialog.InputCallback() {
                    @Override
                    public void onInput(MaterialDialog dialog, CharSequence input) {
                        // Do something
                    }
                }).show();
    }
}
