package com.example.quiz;

import static android.content.ContentValues.TAG;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class TestActivity extends AppCompatActivity {

    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quiz-d45c8-default-rtdb.firebaseio.com/");

    private AppCompatButton OptionA, OptionB, OptionC, OptionD;
    private AppCompatButton NextBtn, submit_btn;
    private TextView QNo;
    private TextView ChoosenTopic;
    private TextView Timer_text;
    private TextView Questions;
    private ImageView backBtn;
    private Vibrator vibrate = null;

    private Timer QuizTimer;
    private int TotalTimeInMins = 1;
    private int second = 0;

    private ArrayList<QuestionModel> SList;
    private int CurrentQuestoionPosition = 0;

    private String Answer = null, Email, SelectedTopic, UserAnswer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        Timer_text=(TextView)findViewById(R.id.Timer_Text);
        ChoosenTopic = (TextView)findViewById(R.id.QuizTopic);
        QNo = (TextView)findViewById(R.id.Question_count);
        Questions = (TextView)findViewById(R.id.Question);
        backBtn = (ImageView) findViewById(R.id.backBtn);
        NextBtn =(AppCompatButton)findViewById(R.id.NextBtn);
        OptionA = (AppCompatButton) findViewById(R.id.FirstOption);
        OptionB = (AppCompatButton) findViewById(R.id.SecondOption);
        OptionC = (AppCompatButton) findViewById(R.id.ThirdOption);
        OptionD = (AppCompatButton) findViewById(R.id.ForthOption);
        submit_btn = (AppCompatButton) findViewById(R.id.Submit_Btn);
        submit_btn.setVisibility(View.INVISIBLE);

        SelectedTopic = getIntent().getStringExtra("selectedTopic");
        ChoosenTopic.setText(SelectedTopic);
        Email = getIntent().getStringExtra("Email");

        LoadQuestion();

        startTimer(Timer_text);

        OptionA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserAnswer.isEmpty())
                {
                    UserAnswer=OptionA.getText().toString();
                    Log.e(TAG, "onClick: OptionA"+ UserAnswer );
                    OptionA.setBackgroundResource(R.drawable.red_background);
                    OptionA.setTextColor(Color.WHITE);
                    revealAns();
                }
            }
        });

        OptionB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserAnswer.isEmpty())
                {
                    UserAnswer=OptionB.getText().toString();
                    Log.e(TAG, "onClick: OptionB"+ UserAnswer );
                    OptionB.setBackgroundResource(R.drawable.red_background);
                    OptionB.setTextColor(Color.WHITE);
                    revealAns();
                }
            }
        });

        OptionC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserAnswer.isEmpty())
                {
                    UserAnswer=OptionC.getText().toString();
                    Log.e(TAG, "onClick: OptionC"+ UserAnswer );
                    OptionC.setBackgroundResource(R.drawable.red_background);
                    OptionC.setTextColor(Color.WHITE);
                    revealAns();
                }
            }
        });

        OptionD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (UserAnswer.isEmpty())
                {
                    UserAnswer=OptionD.getText().toString();
                    Log.e(TAG, "onClick: OptionD"+ UserAnswer );
                    OptionD.setBackgroundResource(R.drawable.red_background);
                    OptionD.setTextColor(Color.WHITE);
                    revealAns();
                }
            }
        });
        NextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(UserAnswer.isEmpty())
                {
                    Toast.makeText(TestActivity.this, "Please Enter Ans", Toast.LENGTH_SHORT).show();
                }
                else{
                    ChangeQuestion();
//                    getCorrectAns();
//                    getIncorrectAns();
//                    Log.e(TAG, "getCorrectAns in Nextbtn: "+CorrectAns);
//                    Log.e(TAG, "getInCorrectAns in Nextbtn: "+InCorrectAns);
                }
            }
        });

        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TestActivity.this, QuizResult.class);
                i.putExtra("correct",getCorrectAns());
                i.putExtra("incorrecet",getIncorrectAns());
                startActivity(i);
                finish();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(TestActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        });

    }

    void LoadQuestion()
    {
        db.child("user").child("Question").child(Email).child(SelectedTopic).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SList = new ArrayList<>();
                for (DataSnapshot ss : snapshot.getChildren())
                {
                    QuestionModel QM = ss.getValue(QuestionModel.class);
                    SList.add(QM);
                }
                if(SList.size() > 0)
                {
                    ShowQoneByone(CurrentQuestoionPosition);
                }
                else{
                    Toast.makeText(TestActivity.this, "No Question Found ", Toast.LENGTH_SHORT).show();
                finish();
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void ShowQoneByone( int i) {
    QuestionModel QM = SList.get(i);

    QNo.setText((CurrentQuestoionPosition+1)+"/"+SList.size());

    Answer   = QM.getAnswer();
    Questions.setText(QM.getQuestion());
    OptionA.setText(QM.getOptionA());
    OptionB.setText(QM.getOptionB());
    OptionC.setText(QM.getOptionC());
    OptionD.setText(QM.getOptionD());
    }

    private void revealAns( )
    {
        if(OptionA.getText().toString().equals(Answer))
        {
            OptionA.setBackgroundResource(R.drawable.green_background);
            OptionA.setTextColor(Color.WHITE);
        }
        else if( OptionB.getText().toString().equals(Answer))
        {
            OptionB.setBackgroundResource(R.drawable.green_background);
            OptionB.setTextColor(Color.WHITE);
        }
        else if( OptionC.getText().toString().equals(Answer))
        {
            OptionC.setBackgroundResource(R.drawable.green_background);
            OptionC.setTextColor(Color.WHITE);
        }
        else if( OptionD.getText().toString().equals(Answer))
        {
            OptionD.setBackgroundResource(R.drawable.green_background);
            OptionD.setTextColor(Color.WHITE);
        }
    }


    private void startTimer(TextView TimerTextView)
    {
        QuizTimer = new Timer();
        QuizTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if(second==0)
                {
                    TotalTimeInMins--;
                    second=59;
                }
                else if(second==00 && TotalTimeInMins==00)
                {
                    QuizTimer.purge();
                    QuizTimer.cancel();
                    Toast.makeText(TestActivity.this, "Time is over", Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(TestActivity.this, QuizResult.class);
                    i.putExtra("correct",getCorrectAns());
                    i.putExtra("incorrecet",getIncorrectAns());
                    startActivity(i);
                    finish();
                }

                else{
                    second--;
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        String  finalMin = String.valueOf(TotalTimeInMins);
                        String finalSec = String.valueOf(second);
                        if(finalMin.length()==1)
                        {
                            finalMin ="0"+finalMin;
                        }
                        if(finalSec.length()==1)
                        {
                            finalSec = "0"+finalSec;
                        }
                        TimerTextView.setText(finalMin+":"+finalSec);
                    }
                });
            }
        },1000,1000);
    }

    private int getCorrectAns()
    {
        int CorrectAns = 0;
        for(int i = 0; i<SList.size();i++)
        {
           final String getAns = SList.get(i).getAnswer();
            if(UserAnswer.equals(getAns))
            {
                CorrectAns++;
                Log.e(TAG, "getCorrectAns in Function: "+CorrectAns);
            }
        }
//        if(CurrentQuestoionPosition < SList.size())
//        {
//            if(Answer.equals(UserAnswer))
//            {
//                CorrectAns++;
//                Log.e(TAG, "getCorrectAns in Function: "+CorrectAns);
//            }
//        }
        return CorrectAns;
    }

    private int getIncorrectAns()
    { int InCorrectAns = 0;
        for(int i = 0; i<SList.size();i++)
        {
          final String getAns = SList.get(i).getAnswer();
            if(!UserAnswer.equals(getAns))
            {
                vibration();
                InCorrectAns++;
                Log.e(TAG, "getCorrectAns in function: "+InCorrectAns);
            }
        }
//        if(CurrentQuestoionPosition < SList.size())
//        {
//
//            if(!Answer.equals(UserAnswer))
//            {
//
//                InCorrectAns++;
//                Log.e(TAG, "getCorrectAns in function: "+InCorrectAns);
//            }
//        }
        return InCorrectAns;
    }

    private void ChangeQuestion(){
        CurrentQuestoionPosition++;
        if((CurrentQuestoionPosition) == SList.size())
        {
            NextBtn.setVisibility(View.GONE);
            submit_btn.setVisibility(View.VISIBLE);
        }
        else if((CurrentQuestoionPosition < SList.size()))
        {
            UserAnswer="";
            Answer="";

            OptionA.setBackgroundResource(R.drawable.option_btn);
            OptionA.setTextColor(Color.BLACK);
            OptionB.setBackgroundResource(R.drawable.option_btn);
            OptionB.setTextColor(Color.BLACK);
            OptionC.setBackgroundResource(R.drawable.option_btn);
            OptionC.setTextColor(Color.BLACK);
            OptionD.setBackgroundResource(R.drawable.option_btn);
            OptionD.setTextColor(Color.BLACK);

        QNo.setText((CurrentQuestoionPosition+1)+"/"+SList.size());
        Answer = SList.get(CurrentQuestoionPosition).getAnswer();
            Log.e(TAG, "ChangeQuestion: "+Answer + "   " + CurrentQuestoionPosition);
        Questions.setText(SList.get(CurrentQuestoionPosition).getQuestion());
        OptionA.setText(SList.get(CurrentQuestoionPosition).getOptionA());
        OptionB.setText(SList.get(CurrentQuestoionPosition).getOptionB());
        OptionC.setText(SList.get(CurrentQuestoionPosition).getOptionC());
        OptionD.setText(SList.get(CurrentQuestoionPosition).getOptionD());

        }
    }


    @Override
    public void onBackPressed()
    {
        QuizTimer.purge();
        QuizTimer.cancel();
        startActivity(new Intent(TestActivity.this,MainActivity.class));
        finish();
    }

   public  void vibration()
   {
       vibrate = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
       if(!Answer.equals(UserAnswer))
       {
           if(Build.VERSION.SDK_INT >=26)
           {
               vibrate.vibrate(VibrationEffect.createOneShot(1000,VibrationEffect.DEFAULT_AMPLITUDE));
           }
           else{
               vibrate.vibrate(1000);
       }

       }
   }

}
