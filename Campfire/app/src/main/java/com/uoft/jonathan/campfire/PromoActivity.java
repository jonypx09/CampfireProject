package com.uoft.jonathan.campfire;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PromoActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_promo);
        setTitle("");

        //Check for internet connection and persist
        ConnectivityManager connection =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        //Check for network connections
        if (connection.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connection.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connection.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connection.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
        } else if (
                connection.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connection.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            loginButton = (Button) findViewById(R.id.loginButton);
            signupButton = (Button) findViewById(R.id.signupButton);
            loginButton.setEnabled(false);
            loginButton.setBackgroundColor(Color.argb(255, 0, 60, 103));
            signupButton.setEnabled(false);
            signupButton.setBackgroundColor(Color.argb(255, 0, 60, 103));
            new AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("You must be connected to the internet to use this app!")
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            checkConnection();
                        }
                    })
                    .show();
        }
    }

    public void toLoginScreen(View view){
        Intent signInIntent = new Intent(this, LoginActivity.class);
        startActivity(signInIntent);
    }

    public void toSignUpScreen(View view){
        Intent signInIntent = new Intent(this, SignUpActivity.class);
        startActivity(signInIntent);
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(1);
    }

    public void checkConnection(){
        //Check for internet connection and persist
        ConnectivityManager connection =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        //Check for network connections
        if (connection.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connection.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connection.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connection.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            loginButton = (Button) findViewById(R.id.loginButton);
            signupButton = (Button) findViewById(R.id.signupButton);
            loginButton.setEnabled(true);
            loginButton.setBackgroundColor(Color.argb(255, 0, 70, 121));
            signupButton.setEnabled(true);
            signupButton.setBackgroundColor(Color.argb(255, 0, 70, 121));
            Toast.makeText(this, "Connected!", Toast.LENGTH_LONG).show();
        } else if (
                connection.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connection.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            loginButton = (Button) findViewById(R.id.loginButton);
            signupButton = (Button) findViewById(R.id.signupButton);
            loginButton.setEnabled(false);
            loginButton.setBackgroundColor(Color.argb(255, 0, 60, 103));
            signupButton.setEnabled(false);
            signupButton.setBackgroundColor(Color.argb(255, 0, 60, 103));
            new AlertDialog.Builder(this)
                    .setTitle("No Internet Connection")
                    .setMessage("You must be connected to the internet to use this app!")
                    .setPositiveButton("Try Again", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            checkConnection();
                        }
                    })
                    .show();
        }
    }
}
