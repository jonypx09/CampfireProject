package com.example.jonat.campfire;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.yarolegovich.lovelydialog.LovelyChoiceDialog;
import com.yarolegovich.lovelydialog.LovelyStandardDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private String[] newStudentID;
    private String uEmail;

    private TextView settingsTextview;
    private TextView miscTextview;
    private ListView settingsListview;
    private ListView miscListview;
    private RelativeLayout mainLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        setTitle("Settings");

        Intent intent = getIntent();
        newStudentID = intent.getExtras().getStringArray("identity");
        uEmail = newStudentID[2];

        settingsListview = (ListView) findViewById(R.id.settingsListview);
        miscListview = (ListView) findViewById(R.id.miscListview);
        settingsTextview = (TextView) findViewById(R.id.settingsTextview);
        miscTextview = (TextView) findViewById(R.id.miscTextview);
        mainLayout = (RelativeLayout) findViewById(R.id.activity_settings);
        String[] settingsOptions = new String[] {
                "Personalize",
                "Notifications"
        };
        String[] miscOptions = new String[] {
                "Version\n1.0 (Stable)",
                "What's new",
                "Rate this app",
                "View help",
                "Credits"
        };
        List<String> options_list = new ArrayList<String>(Arrays.asList(settingsOptions));
        List<String> misc_list = new ArrayList<String>(Arrays.asList(miscOptions));

        ArrayAdapter<String> settingsArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, options_list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.WHITE);

                // Generate ListView Item using TextView
                return view;
            }
        };
        ArrayAdapter<String> miscArrayAdapter = new ArrayAdapter<String>
                (this, android.R.layout.simple_list_item_1, misc_list){
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                // Get the Item from ListView
                View view = super.getView(position, convertView, parent);

                // Initialize a TextView for ListView each Item
                TextView tv = (TextView) view.findViewById(android.R.id.text1);

                // Set the text color of TextView (ListView Item)
                tv.setTextColor(Color.WHITE);

                // Generate ListView Item using TextView
                return view;
            }
        };

        // DataBind ListView with items from ArrayAdapter
        settingsListview.setAdapter(settingsArrayAdapter);
        miscListview.setAdapter(miscArrayAdapter);

        String htmlStringSettings="<u>App Settings</u>";
        settingsTextview.setText(Html.fromHtml(htmlStringSettings));

        String htmlStringMisc="<u>About</u>";
        miscTextview.setText(Html.fromHtml(htmlStringMisc));


        //Add actions to each list view
        addActions(settingsListview);
        addActions(miscListview);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId() == android.R.id.home){
            Intent mainIntent = new Intent(this, MainActivity.class);
            mainIntent.putExtra("identity", newStudentID);
            startActivity(mainIntent);
            return true;
        }
        return false;
    }

    public void addActions(final ListView listOfOptions){
        listOfOptions.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                // ListView Clicked item index
                int itemPosition = position;
                parseSelection(listOfOptions, itemPosition);
            }
        });
    }

    public void parseSelection(ListView menuOptions, int index){
        if (menuOptions.equals(settingsListview)){
            if (index == 0){
                Snackbar snackbar = Snackbar.make(mainLayout, "Personalize Disabled", Snackbar.LENGTH_LONG);
                snackbar.show();
            }else{
                Snackbar snackbar = Snackbar.make(mainLayout, "Notifications Disabled", Snackbar.LENGTH_LONG);
                snackbar.show();
            }
        }else{
            if (index == 1){
                displayChangelog();
            }else if (index == 2){
                Snackbar snackbar = Snackbar.make(mainLayout, "Open Google Play Store", Snackbar.LENGTH_LONG);
                snackbar.show();
            }else if (index == 3){
                Intent miscIntent = new Intent(this, HelpActivity.class);
                miscIntent.putExtra("identity", newStudentID);
                startActivity(miscIntent);
            }else if (index == 4){
                displayCredits();
            }
        }
    }

    public void displayCredits(){
        String[] devs = new String[] {
                "Adam Capparelli - Backend Developer\n",
                "Vlad Chapurny - Backend Developer\n",
                "Quinn Daneyko - Frontend Developer\n",
                "Andrew Goupil - Frontend Developer\n",
                "Rod Ali Mazloomi - Database Developer\n",
                "Jonathan Pelastine - Frontend Developer\n",
                "Fullchee Zhang - Database Developer\n"
        };
        new LovelyChoiceDialog(this)
                .setTopColorRes(R.color.colorPrimaryDark)
                .setTitle("Credits")
                .setIcon(R.drawable.uoft_cs_logo)
                .setMessage("Special thanks to the following group of talented individuals:")
                .setItems(devs, new LovelyChoiceDialog.OnItemSelectedListener<String>() {
                    @Override
                    public void onItemSelected(int position, String item) {
                        Intent moreInfo = new Intent(Intent.ACTION_VIEW);
                        String url = null;
                        if (position == 0){
                            url = "https://github.com/acapparelli";
                        }else if (position == 1){
                            url = "https://github.com/vladchapurny";
                        }else if (position == 2){
                            url = "https://github.com/quinndaneyko";
                        }else if (position == 3){
                            url = "https://github.com/AndrewGoupil";
                        }else if (position == 4){
                            url = "https://github.com/RodAli";
                        }else if (position == 5){
                            url = "https://github.com/jonypx19";
                        }else{
                            url = "https://github.com/Fullchee";
                        }
                        moreInfo.setData(Uri.parse(url));
                        startActivity(moreInfo);
                    }
                })
                .show();
    }

    public void displayChangelog(){
        new LovelyStandardDialog(this)
                .setTopColorRes(R.color.colorPrimary)
                .setButtonsColorRes(R.color.colorPrimaryDark)
                .setIcon(R.drawable.ic_developer_board_white_48dp)
                .setTitle("Changelog (03-04-2017):\n")
                .setMessage("-Added Messenger\n" +
                        "-Bug Fixes\n" +
                        "-Fixed an issue with signup\n" +
                        "-Performance improvements to login\n")
                .setPositiveButton("Close", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                })
                .show();
    }
}
