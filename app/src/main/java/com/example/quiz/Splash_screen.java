package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;

import com.airbnb.lottie.LottieAnimationView;

public class Splash_screen extends AppCompatActivity {
    public LottieAnimationView lottiView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        lottiView = findViewById(R.id.second_starting_animation);

        //Handler class use to work with Threads in handler class here we use .postDelayed(Runnable r , time delay)
        //where this method use to delay the thread
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(getApplicationContext(), LoginPage.class);
                startActivity(i);
            }
        },5000);

    }
}