package com.example.jonat.campfire;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import backend.algorithms.Comparable;
import backend.algorithms.Student;
import backend.database.DatabaseAdapter;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String[] newStudentID;
    DatabaseAdapter db;

    static final String STATE_EMAIL = "email";
    private String uEmail;
    private String uName;
    private Student uStudent;

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

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View headerView = navigationView.getHeaderView(0);
        TextView emailHeader = (TextView) headerView.findViewById(R.id.emailHeader);
        TextView nameHeader = (TextView) headerView.findViewById(R.id.nameHeader);
        emailHeader.setText(uEmail);
        nameHeader.setText(uName);

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
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {

            //Pressing the back button on the Android device will log the user off
            Toast.makeText(getApplicationContext(), "You have logged out!", Toast.LENGTH_SHORT).show();
            Intent promoIntent = new Intent(this, PromoActivity.class);
            startActivity(promoIntent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent promoIntent = new Intent(this, PromoActivity.class);
            startActivity(promoIntent);
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
//        bundle.putString("userEmail", uEmail);
        bundle.putStringArray("identity", newStudentID);

        //creating fragment object
        Fragment fragment = null;
        Intent miscIntent;

        //initializing the fragment object which is selected
        switch (itemId) {
            case R.id.nav_messages:
                fragment = new MessagesFragment();
                fragment.setArguments(bundle);
                break;
            case R.id.nav_my_campfire:
                fragment = new MyCampfireFragment();
                break;
            case R.id.nav_discover:
                fragment = new DiscoverFragment();
                fragment.setArguments(bundle);
                break;
            case R.id.nav_home:
                fragment = new HomeFragment();
                fragment.setArguments(bundle);
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
}
