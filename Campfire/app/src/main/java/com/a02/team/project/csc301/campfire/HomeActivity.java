package com.a02.team.project.csc301.campfire;

import android.app.FragmentManager;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;
    private String[] mPlanetTitles;
    private DrawerLayout mDrawerLayout;
    private ListView mDrawerList;
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        this.setTitle("Home");

        listView = (ListView) findViewById(R.id.left_drawer);
        listView.setOnItemClickListener(this);

//        mPlanetTitles = getResources().getStringArray(R.array.planets_array);
//        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
//        mPlanetTitles = new String[]{ "A", "B", "C" };
//        mDrawerList = (ListView) findViewById(R.id.left_drawer);

//        // Set the adapter for the list view
//        mDrawerList.setAdapter(new ArrayAdapter<String>(this,
//                R.layout.activity_home, mPlanetTitles));
//        // Set the list's click listener
//        mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

    }

    /*
     * Parameters:
        adapter - The AdapterView where the click happened.
        view - The view within the AdapterView that was clicked
        position - The position of the view in the adapter.
        id - The row id of the item that was clicked.
     */
    @Override
    public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
        Toast.makeText(getApplicationContext(), ((TextView) view).getText(),
                Toast.LENGTH_SHORT).show();
        System.out.println(((TextView) view).getText());
        if (((TextView) view).getText().equals("Settings")){
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
    }

//    private class DrawerItemClickListener implements ListView.OnItemClickListener {
//        @Override
//        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//            selectItem(position);
//        }
//    }
//
//    /** Swaps fragments in the main content view */
//    private void selectItem(int position) {
////        // Create a new fragment and specify the planet to show based on position
////        Fragment fragment = new PlanetFragment();
////        Bundle args = new Bundle();
////        args.putInt(PlanetFragment.ARG_PLANET_NUMBER, position);
////        fragment.setArguments(args);
////
////        // Insert the fragment by replacing any existing fragment
////        FragmentManager fragmentManager = getFragmentManager();
////        fragmentManager.beginTransaction()
////                .replace(R.id.content_frame, fragment)
////                .commit();
////
////        // Highlight the selected item, update the title, and close the drawer
////        mDrawerList.setItemChecked(position, true);
////        setTitle(mPlanetTitles[position]);
////        mDrawerLayout.closeDrawer(mDrawerList);
//    }
//
//    @Override
//    public void setTitle(CharSequence title) {
//        mTitle = title;
//        getActionBar().setTitle(mTitle);
//    }
}
