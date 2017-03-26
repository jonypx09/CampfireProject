package com.example.jonat.campfire;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.afollestad.materialdialogs.MaterialDialog;

import backend.database.DatabaseAdapter;

public class PromoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.fadein, R.anim.fadeout);
        setContentView(R.layout.activity_promo);
        setTitle("");

        //Checks for internet connection
//        Context context = getApplicationContext();
//        ConnectivityManager cm =
//                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
//
//        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
//        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
//
//        if (!isConnected){
//            new MaterialDialog.Builder(this)
//                    .title("No Internet Connection")
//                    .content("This app requires an internet connection!")
//                    .positiveText("Close App")
//                    .show();
//        }
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
