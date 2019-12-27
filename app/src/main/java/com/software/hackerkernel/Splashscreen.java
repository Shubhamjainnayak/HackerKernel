package com.software.hackerkernel;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;

import com.software.hackerkernel.Activities.Home;
import com.software.hackerkernel.Activities.LoginActivity;

public class Splashscreen extends AppCompatActivity {
    public static final String SHARED_PREF_NAME = "user_info";
    public static final String LOGGEDIN_SHARED_PREF = "loggedin";
    boolean loggedIn;
    SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new CountDownTimer(2000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {

            }

            @Override
            public void onFinish() {

                sharedPreferences = getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
                loggedIn = sharedPreferences.getBoolean(LOGGEDIN_SHARED_PREF, false);
                if(loggedIn)
                {
                   Intent intent = new Intent(Splashscreen.this, Home.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();


                }
                else
                {
                    Intent intent = new Intent(Splashscreen.this, LoginActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
            }
        }.start();
    }
}
