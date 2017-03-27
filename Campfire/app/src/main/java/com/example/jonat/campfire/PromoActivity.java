package com.example.jonat.campfire;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

public class PromoActivity extends AppCompatActivity {

    private Button loginButton;
    private Button signupButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_promo);
        setTitle("");

        //Check for internet connection
        ConnectivityManager connection =
                (ConnectivityManager)getSystemService(getBaseContext().CONNECTIVITY_SERVICE);

        //Check for network connections
        if (connection.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTED ||
                connection.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connection.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTING ||
                connection.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.CONNECTED ) {
            Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
        } else if (
                connection.getNetworkInfo(0).getState() == android.net.NetworkInfo.State.DISCONNECTED ||
                        connection.getNetworkInfo(1).getState() == android.net.NetworkInfo.State.DISCONNECTED  ) {
            loginButton = (Button) findViewById(R.id.loginButton);
            signupButton = (Button) findViewById(R.id.signupButton);
            loginButton.setEnabled(false);
            signupButton.setEnabled(false);
            new MaterialDialog.Builder(this)
                    .title("No Internet Connection")
                    .content("You must be connected to the internet to use this app!")
                    .positiveText("Ok")
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
}
