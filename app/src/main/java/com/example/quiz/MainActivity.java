package com.example.quiz;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;


public class MainActivity extends AppCompatActivity implements View.OnClickListener ,PopupMenu.OnMenuItemClickListener {
    private String User="",selectTopicName ="",Gender="";
    private LinearLayout firstTopic ,forthTopic , thirdTopic, secondTopic;
    private Button Start_btn;
    private ImageView Setting, iv_profile;
    private TextView tv_name, tv_user , tv_logout, tv_about, tv_update_profile;

    private View menu_layout;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quiz-d45c8-default-rtdb.firebaseio.com/user");
    boolean i ;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(savedInstanceState != null)
        {
            User = savedInstanceState.getString("User");
        }

        firstTopic=(LinearLayout)findViewById(R.id.FirstTopic);
        secondTopic =(LinearLayout) findViewById(R.id.SecondTopic);
        thirdTopic = (LinearLayout)findViewById(R.id.ThirdTopic);
        forthTopic = (LinearLayout)findViewById(R.id.ForthTopic);
        Start_btn = findViewById(R.id.Start_btn);
        Setting = (ImageView)findViewById(R.id.Setting_btn);

        tv_name = findViewById(R.id.player_name);
        tv_user = findViewById(R.id.player_user);
        tv_about = findViewById(R.id.tv_about_me);
        tv_update_profile = findViewById(R.id.tv_profile_update);
        tv_logout = findViewById(R.id.tv_logout);

        menu_layout = (View)findViewById(R.id.menu_layout);

        firstTopic.setOnClickListener(this);
        secondTopic.setOnClickListener(this);
        thirdTopic.setOnClickListener(this);
        forthTopic.setOnClickListener(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        User = getIntent().getStringExtra("User");

        iv_profile = (ImageView)findViewById(R.id.iv_profile);

//        menu_layout.setVisibility(View.INVISIBLE);
        i = false;

        // this methos id for update the profile;
        UpdateProfile();

        Start_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (User != null) {
                    Log.e("tag", "in AllQuestion: "+User+" "+selectTopicName);
                    if(User.matches("Player")) {
                        Intent i = new Intent(MainActivity.this, TestActivity.class);
                        i.putExtra("selectedTopic", selectTopicName);
                        Log.e("tag", "in MainActivity: "+selectTopicName);
                        startActivity(i);
                    } else {
                        Intent i = new Intent(MainActivity.this, AllQuestions.class);
                        i.putExtra("Topic", selectTopicName);
                        i.putExtra("User",User);
                        Log.e("tag", "in MainActivity: "+selectTopicName);
                        startActivity(i);
                    }
                }
                else{
                    Log.e(TAG,"no User found");
                }
            }
        });

        Setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSlideViewButtonClick(menu_layout);

            }
        });

        tv_logout.setOnClickListener(this);
        tv_about.setOnClickListener(this);
        tv_update_profile.setOnClickListener(this);

    }

    public void slideleft(View view){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_left);
        Setting.setAnimation(animation);
        view.setVisibility(View.VISIBLE);
        TranslateAnimation animate = new TranslateAnimation(
                view.getWidth(),                 // fromXDelta
                0,                 // toXDelta
                0,  // fromYDelta
                0);                // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    // slide the view from its current position to below itself
    public void slideright(View view){
        Animation animation = AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_right);
        Setting.setAnimation(animation);
        TranslateAnimation animate = new TranslateAnimation(
               0,                 // fromXDelta
                 view.getWidth(),                 // toXDelta
                0,                 // fromYDelta
                0); // toYDelta
        animate.setDuration(500);
        animate.setFillAfter(true);
        view.startAnimation(animate);
    }

    public void onSlideViewButtonClick(View view) {
        if (i) {
            slideright(view);
        } else {
            slideleft(view);
        }
        i = !i;
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

            case R.id.tv_logout:
                sinout();
                break;
            case R.id.tv_profile_update:

                Intent i = new Intent(MainActivity.this , Update.class);
                startActivity(i);
                break;
            case R.id.tv_about_me:
                Toast.makeText(this, "Not now baby girl!!", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if(id==R.id.sinout_Action)
        {
            sinout();

        }
        return true;
    }

    public void sinout() {
        mAuth.signOut();
        startActivity(new Intent(MainActivity.this, LoginPage.class));
        finish();
    }

    private void UpdateProfile() {
            try {
                if (User != null) {
                    tv_name.setText(mUser.getDisplayName());
                    tv_user.setText(User);
                    String name = tv_name.getText().toString();
                    db.child(User).child(name).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.exists())
                            {
                                Map map = (Map) snapshot.getValue();
                                if(map.get("Gender") != null) {
                                    Gender = map.get("Gender").toString();
                                }
                                Log.e(TAG," in onDataChange "+Gender);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                    if(Gender == "male")
                    {
                        Log.e(TAG,"male true "+Gender);
                        Picasso.get().load(mUser.getPhotoUrl()).placeholder(R.drawable.male_img).into(iv_profile);
                    }
                    else{
                        Log.e(TAG,"female true" +Gender);
                        Picasso.get().load(mUser.getPhotoUrl()).placeholder(R.drawable.female_img).into(iv_profile);
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, "UpdateProfile: " + e);
            } finally {
                           }
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("User",User);
    }
}