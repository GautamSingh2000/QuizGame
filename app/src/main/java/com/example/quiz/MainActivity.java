package com.example.quiz;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    public String User="", Email="";
    public LinearLayout firstTopic ;
    public LinearLayout secondTopic ;
    public LinearLayout thirdTopic ;
    public LinearLayout forthTopic ;
    public Button Start_btn;
    public String selectTopicName ="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        firstTopic=(LinearLayout)findViewById(R.id.FirstTopic);
        secondTopic =(LinearLayout) findViewById(R.id.SecondTopic);
        thirdTopic = (LinearLayout)findViewById(R.id.ThirdTopic);
        forthTopic = (LinearLayout)findViewById(R.id.ForthTopic);
        Start_btn = findViewById(R.id.Start_btn);

        User = getIntent().getStringExtra("User");
        Email = getIntent().getStringExtra("Email");

        firstTopic.setOnClickListener(this);
        secondTopic.setOnClickListener(this);
        thirdTopic.setOnClickListener(this);
        forthTopic.setOnClickListener(this);

        Start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User != null) {
                    if(User.equals("Player")) {
                        Intent i = new Intent(MainActivity.this, TestActivity.class);
                        i.putExtra("selectedTopic", selectTopicName);
                        i.putExtra("Email", Email);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(MainActivity.this, AllQuestions.class);
                        i.putExtra("Topic", selectTopicName);
                        i.putExtra("Email", Email);
                        startActivity(i);
                    }
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.FirstTopic:
                selectTopicName="java";
                firstTopic.setBackgroundResource(R.drawable.select_rounded_itm);
                secondTopic.setBackgroundResource(R.drawable.rounded_white_rec);
                thirdTopic.setBackgroundResource(R.drawable.rounded_white_rec);
                forthTopic.setBackgroundResource(R.drawable.rounded_white_rec);
                break;

            case R.id.SecondTopic:
                selectTopicName="php";
                secondTopic.setBackgroundResource(R.drawable.select_rounded_itm);
                firstTopic.setBackgroundResource(R.drawable.rounded_white_rec);
                thirdTopic.setBackgroundResource(R.drawable.rounded_white_rec);
                forthTopic.setBackgroundResource(R.drawable.rounded_white_rec);
                break;

            case R.id.ThirdTopic:
                selectTopicName="c++";
                thirdTopic.setBackgroundResource(R.drawable.select_rounded_itm);
                secondTopic.setBackgroundResource(R.drawable.rounded_white_rec);
                firstTopic.setBackgroundResource(R.drawable.rounded_white_rec);
                forthTopic.setBackgroundResource(R.drawable.rounded_white_rec);
                break;

            case R.id.ForthTopic:
                selectTopicName="python";
                forthTopic.setBackgroundResource(R.drawable.select_rounded_itm);
                secondTopic.setBackgroundResource(R.drawable.rounded_white_rec);
                firstTopic.setBackgroundResource(R.drawable.rounded_white_rec);
                thirdTopic.setBackgroundResource(R.drawable.rounded_white_rec);
                break;
        }
    }

    @Override
    public void onBackPressed()
    {
        startActivity(new Intent(MainActivity.this, LoginPage.class));
        finish();
    }

}