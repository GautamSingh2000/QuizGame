package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class QuizResult extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_result);

       final  TextView WrongAns =(TextView)findViewById(R.id.Wrong_scr);
       final  TextView CorrectAns =(TextView)findViewById(R.id.Correct_scr);
        final AppCompatButton restartBtn = (AppCompatButton)findViewById(R.id.new_quiz);

        final int getCorrectAns = getIntent().getIntExtra("correct",0);
        final int getIncorrectAns = getIntent().getIntExtra("incorrect",0);

        WrongAns.setText(String.valueOf("Wrong Answers: "+getIncorrectAns));
        CorrectAns.setText(String.valueOf("Correct Answers: "+getCorrectAns));


        restartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(QuizResult.this,MainActivity.class);
                finish();
            }
        });

    }
}