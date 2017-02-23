package com.a02.team.project.csc301.campfire;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;


public class HomeActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = getIntent();
        this.setTitle("Home");

        listView = (ListView) findViewById(R.id.left_drawer);
        listView.setOnItemClickListener(this);


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

        if (((TextView) view).getText().equals("Home")){
            this.setTitle("Home");
            view.setBackgroundColor(Color.parseColor("#FF003C67"));

        }else if (((TextView) view).getText().equals("Messages")){
            this.setTitle("Messages");

        }else if (((TextView) view).getText().equals("My Campfire")){
            this.setTitle("My Campfire");

        }else if (((TextView) view).getText().equals("Discover")){
            this.setTitle("Discover");

        }else if (((TextView) view).getText().equals("FAQ and Help")){
            this.setTitle("FAQ and Help");

        }else{
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_menu, menu);
        return true;
    }
}
