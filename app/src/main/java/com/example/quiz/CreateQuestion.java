package com.example.quiz;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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

import java.security.cert.PKIXRevocationChecker;
import java.util.UUID;

public class CreateQuestion extends AppCompatActivity {

    private String choosenTopic ,Email;
    private EditText Question,OptionA,OptionB,OptionC,OptionD;
    private RadioButton ansA,ansB,ansC,ansD,selectedradio_btn;
    private TextView Topic;
    private int radio_id;
    private RadioGroup radiogroup;
    private AppCompatButton Add_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_question);

        Email = getIntent().getStringExtra("Email");
        choosenTopic = getIntent().getStringExtra("Topic");
        ImageView Back_Btn =(ImageView)findViewById(R.id.back_btn);

        Question = (EditText) findViewById(R.id.Admin_Ques);
        OptionA = (EditText) findViewById(R.id.AdminFirstOption);
       OptionB = (EditText) findViewById(R.id.AdminSecondOption);
        OptionC = (EditText) findViewById(R.id.AdminThirdOption);
        OptionD =(EditText) findViewById(R.id.AdminForthOption);

        Topic = (TextView)findViewById(R.id.QuizTopic);
        Topic.setText(choosenTopic);

        ansA=(RadioButton)findViewById(R.id.answer_a);
        ansB=(RadioButton)findViewById(R.id.AnsB);
        ansC = (RadioButton)findViewById(R.id.AnsC);
        ansD = (RadioButton)findViewById(R.id.AnsD);


        radiogroup = (RadioGroup)findViewById(R.id.radio_group);

        Add_btn =(AppCompatButton)findViewById(R.id.q_add);

        DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quiz-d45c8-default-rtdb.firebaseio.com/user");

        Add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String answer = "";
                String QuestionId = UUID.randomUUID().toString();
                final String question_String = Question.getText().toString();
                final String optionA_String = OptionA.getText().toString();
                final String optionB_String = OptionB.getText().toString();
                final String optionC_String = OptionC.getText().toString();
                final String optionD_String = OptionD.getText().toString();
                radio_id = radiogroup.getCheckedRadioButtonId();
                selectedradio_btn = (RadioButton)findViewById(radio_id);
                String Chosse_ans = selectedradio_btn.getText().toString();
                if(question_String.isEmpty() || optionA_String.isEmpty() ||
                        optionB_String.isEmpty() || optionC_String.isEmpty() ||
                        optionD_String.isEmpty() || Chosse_ans.isEmpty())
                {
                    Toast.makeText(CreateQuestion.this, "Please enter all detail", Toast.LENGTH_SHORT).show();
                }
                else{
                    if(Chosse_ans.equals("A"))
                    {
                     answer=optionA_String;
                    }
                    if(Chosse_ans.equals("B"))
                    {
                        answer=optionB_String;
                    }
                    if(Chosse_ans.equals("C"))
                    {
                        answer=optionC_String;
                    }
                    if(Chosse_ans.equals("D"))
                    {
                        answer=optionD_String;
                    }

                    String finalAnswer = answer;
                    db.child("Questions").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            db.child("Question").child(Email).child(choosenTopic).child(QuestionId).child("Question").setValue(question_String);
                            db.child("Question").child(Email).child(choosenTopic).child(QuestionId).child("OptionA").setValue(optionA_String);
                            db.child("Question").child(Email).child(choosenTopic).child(QuestionId).child("OptionB").setValue(optionB_String);
                            db.child("Question").child(Email).child(choosenTopic).child(QuestionId).child("OptionC").setValue(optionC_String);
                            db.child("Question").child(Email).child(choosenTopic).child(QuestionId).child("OptionD").setValue(optionD_String);
                            db.child("Question").child(Email).child(choosenTopic).child(QuestionId).child("Answer").setValue(finalAnswer);
                            Toast.makeText(CreateQuestion.this, "Saved!", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(CreateQuestion.this,AllQuestions.class);
                            startActivity(i);
//                            finish();
                        }
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }

            }
        });
        Back_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(CreateQuestion.this,AllQuestions.class);
                startActivity(i);
                finish();
            }
        });
    }
}