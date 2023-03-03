package com.example.quiz;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    public String User="Admin";
    private TextView Admin_TV ,Player_TV, Login_TV;

    private String EmailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+", Gender ;
    private static final String FEMALE = "female";
    private static final String MALE = "male";
    private EditText Email_EV , password_EV, confirmPass_EV, FullName_EV;
    private AppCompatButton  Signup_btn;
    private LottieAnimationView Peep_animation;
//    private RadioButton RB_Male ,RB_Female;
    private RadioGroup Gender_radio_group;
    String Email_String, FullName_String , Password_String , ConPass_String;
    private boolean password_touch = false;
    ProgressDialog mPD;
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quiz-d45c8-default-rtdb.firebaseio.com/");

        mAuth = FirebaseAuth.getInstance();
        mUser =mAuth.getCurrentUser();

        Admin_TV = (TextView)findViewById(R.id.Admin_tv);
        Player_TV=(TextView)findViewById(R.id.Player_tv);
        Login_TV=(TextView)findViewById(R.id.login_tv);

        confirmPass_EV = (EditText)findViewById(R.id.Confirm_password);
        FullName_EV = (EditText)findViewById(R.id.Full_name);
        Email_EV = (EditText)findViewById(R.id.email);
        password_EV = (EditText)findViewById(R.id.password);

//        RB_Male = findViewById(R.id.male);
//        RB_Female = findViewById(R.id.female);
        Gender_radio_group = findViewById(R.id.gender_radio_group);

        mPD = new ProgressDialog(this);

        Peep_animation = findViewById(R.id.peep_animation);

        Signup_btn =(AppCompatButton)findViewById(R.id.signup_btn);

        Admin_TV.setOnClickListener(this);
        Player_TV.setOnClickListener(this);
        Signup_btn.setOnClickListener(this);

        password_EV.setOnTouchListener(mTouchListener);

        User = getIntent().getStringExtra("User");

        Login_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginPage.class);
               startActivity(i);
               finish();
            }
        });

        Signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 Email_String = Email_EV.getText().toString().trim();
                 FullName_String = FullName_EV.getText().toString().trim();
                Password_String = password_EV.getText().toString().trim();
                 ConPass_String = confirmPass_EV.getText().toString().trim();
                int checkedRB = Gender_radio_group.getCheckedRadioButtonId();
                switch (checkedRB)
                {
                    case R.id.male:
                        Gender = MALE;
                        break;
                    case R.id.female:
                        Gender = FEMALE;
                        break;
                }

                Log.e("values","Values are: "+Email_String +" "+ FullName_String+" "+Password_String+" "+ConPass_String+" "+Gender+" "+User);

                if(Email_String.isEmpty()||FullName_String.isEmpty()||Password_String.isEmpty()||ConPass_String.isEmpty()|| Gender==null)
                {
                    Toast.makeText(RegisterActivity.this, "Please enter all details", Toast.LENGTH_LONG).show();
                }
                else if(!Email_String.matches(EmailPattern))
                {
                    Toast.makeText(RegisterActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                }
                else if(!Password_String.equals(ConPass_String))
                {
                    Toast.makeText(RegisterActivity.this, "Passwords are not same!", Toast.LENGTH_LONG).show();
                }
                else{

                    mPD.setTitle("Registration..");
                    mPD.setMessage("Registration is processing.");
                    mPD.show();
                    mAuth.createUserWithEmailAndPassword(Email_String,Password_String).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mPD.dismiss();
                            if(task.isSuccessful())
                            {

                                Toast.makeText(RegisterActivity.this, "Registration Succesfull", Toast.LENGTH_SHORT).show();
                                db.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        db.child("user").child(User).child(FullName_String).child("FullName").setValue(FullName_String);
                                        db.child("user").child(User).child(FullName_String).child("Password").setValue(Password_String);
                                        db.child("user").child(User).child(FullName_String).child("User").setValue(User);
                                        db.child("user").child(User).child(FullName_String).child("Gender").setValue(Gender);
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                    }
                                });
                                SendUser();
                            }
                            else{
                                Log.e(TAG, "onComplete: "+task.getException() );
                                Toast.makeText(RegisterActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

    }

    private void SendUser()
    {
        try {
            UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                    .setDisplayName(FullName_String).build();
            Log.e(TAG, "Senduser: " + FullName_String);
            mAuth.getCurrentUser().updateProfile(changeRequest);
        }catch (Exception e)
        {
            Toast.makeText(RegisterActivity.this,"Fail to save Name",Toast.LENGTH_SHORT).show();
        }
        finally {
            mAuth.signOut();
            startActivity(new Intent(RegisterActivity.this, LoginPage.class));
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.Admin_tv:
                Player_TV.setTextColor(Color.GRAY);
                Admin_TV.setTextColor(getResources().getColor(R.color.dark_purple));
                Admin_TV.setTextSize(TypedValue.COMPLEX_UNIT_SP,19);
                Player_TV.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                User="Admin";
                Log.e("tag", "onCreate in LoginActivity: "+User);
                break;
            case R.id.Player_tv:
                Player_TV.setTextColor(getResources().getColor(R.color.dark_purple));
                Admin_TV.setTextColor(Color.GRAY);
                Admin_TV.setTextSize(TypedValue.COMPLEX_UNIT_SP,15);
                Player_TV.setTextSize(TypedValue.COMPLEX_UNIT_SP,19);
                User="Player";
                Log.e("tag", "onCreate in LoginActivity: "+User);
                break;
        }
    }

    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            Peep_animation.setVisibility(View.VISIBLE);
            password_touch = true;
            return false;
        }
    };

}