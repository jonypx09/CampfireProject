package com.example.jonat.campfire;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setTitle(" ");

        final Intent promoIntent = new Intent(this, PromoActivity.class);

        new CountDownTimer(1800, 1000) {
            public void onFinish() {
                startActivity(promoIntent);
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }

    /**
     * Properly send this user to the login screen
     * @param view View object to react on button click
     */
    public void toPromoScreen(View view){
        Intent promoIntent = new Intent(this, PromoActivity.class);
        startActivity(promoIntent);
    }
}
