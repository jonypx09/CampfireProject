package com.a02.team.project.csc301.campfire;

import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        this.setTitle(" ");

        final Intent promoIntent = new Intent(this, PromoActivity.class);

        new CountDownTimer(3000, 1000) {
            public void onFinish() {
                startActivity(promoIntent);
            }

            public void onTick(long millisUntilFinished) {
                // millisUntilFinished    The amount of time until finished.
            }
        }.start();
    }
}
