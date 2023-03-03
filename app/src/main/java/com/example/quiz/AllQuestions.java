package com.example.quiz;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import javax.security.auth.login.LoginException;

public class AllQuestions extends AppCompatActivity {

    private String full_name , topic,User;
    private RecyclerView recyclerView;
    private TextView Topic;
    private ArrayList<QuestionModel> List;
    CustomAdapter adapter;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_questions);

        if(savedInstanceState != null)
        {
            full_name =  savedInstanceState.getString("mfull_name");
            topic =  savedInstanceState.getString("mtopic");
            User = savedInstanceState.getString("mUser");
            Log.e(TAG, "in AllQuestion of saveInstancesState: "+full_name+" "+topic);
        }

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        FloatingActionButton AddQuestion =(FloatingActionButton)findViewById(R.id.floating_btn);
        recyclerView = findViewById(R.id.Quesion_recycle);
        ImageView BackBtn = (ImageView)findViewById(R.id.back_button);
        Topic = (TextView)findViewById(R.id.QuizTopic);

        User = getIntent().getStringExtra("User");
        full_name = mUser.getDisplayName();
        topic = getIntent().getStringExtra("Topic");
        Log.e("tag", "in AllQuestion: "+User+" "+full_name+" "+topic);

        Topic.setText(topic);

        DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quiz-d45c8-default-rtdb.firebaseio.com/user");

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        List = new ArrayList<>();
        adapter = new CustomAdapter(this,List);
        recyclerView.setAdapter(adapter);

        db.child("Admin").child(full_name).child("Questions").child(topic).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                    List.clear();
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
                i.putExtra("User",User);
                i.putExtra("Topic",topic);
               startActivity(i);
//                startActivityForResult(i,1);
            }
        });

        BackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                here we are using onBackPressed() to go previous activity..
//               onBackPressed();
                NavUtils.navigateUpFromSameTask(AllQuestions.this);
//                finish();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@Nullable Bundle OutState) {
        super.onSaveInstanceState(OutState);
        OutState.putString("mEmail",full_name);
        OutState.putString("mtopic",topic);
        OutState.putString("mUser",User);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e("tag", "in AllQuestion of onResume    : "+full_name+" "+topic);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause of Allquestion: " );
    }

//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//    }
}