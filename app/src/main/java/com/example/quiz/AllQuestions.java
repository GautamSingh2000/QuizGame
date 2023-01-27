package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

public class AllQuestions extends AppCompatActivity {

    private String Email , topic;
    private RecyclerView recyclerView;
    private TextView Topic;
    private ArrayList<QuestionModel> List;
    CustomAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_questions);

        FloatingActionButton AddQuestion =(FloatingActionButton)findViewById(R.id.floating_btn);
        recyclerView = findViewById(R.id.Quesion_recycle);
        ImageView BackBtn = (ImageView)findViewById(R.id.back_button);
        Topic = (TextView)findViewById(R.id.QuizTopic);
        Email = getIntent().getStringExtra("Email");
        topic = getIntent().getStringExtra("Topic");
        Topic.setText(topic);

        DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quiz-d45c8-default-rtdb.firebaseio.com/");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List = new ArrayList<>();
        adapter = new CustomAdapter(this,List);
        recyclerView.setAdapter(adapter);

        db.child("user").child("Question").child(Email).child(topic).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    QuestionModel module = dataSnapshot.getValue(QuestionModel.class);
                    List.add(module);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        AddQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(AllQuestions.this , CreateQuestion.class);
                i.putExtra("Email",Email);
                i.putExtra("Topic",topic);
                startActivity(i);
                finish();
            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                here we are using onBackPressed() to go previous activity..
               onBackPressed();
                finish();
            }
        });
    }
}