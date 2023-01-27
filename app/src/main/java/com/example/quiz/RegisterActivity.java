package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {
    public String User="Admin";
    private TextView Admin_TV ,Player_TV, Login_TV;
    private EditText Email_EV , password_EV, confirmPass_EV, FullName_EV;
    private AppCompatButton  Signup_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quiz-d45c8-default-rtdb.firebaseio.com/");

        Admin_TV = (TextView)findViewById(R.id.Admin_tv);
        Player_TV=(TextView)findViewById(R.id.Player_tv);
        Login_TV=(TextView)findViewById(R.id.login_tv);

        confirmPass_EV = (EditText)findViewById(R.id.Confirm_password);
        FullName_EV = (EditText)findViewById(R.id.Full_name);
        Email_EV = (EditText)findViewById(R.id.email);
        password_EV = (EditText)findViewById(R.id.password);

        Signup_btn =(AppCompatButton)findViewById(R.id.signup_btn);

        Admin_TV.setOnClickListener(this);
        Player_TV.setOnClickListener(this);
        Signup_btn.setOnClickListener(this);

        User = getIntent().getStringExtra("User");

        Login_TV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisterActivity.this, LoginPage.class);
               startActivity(i);
            }
        });

        Signup_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String Email_String = Email_EV.getText().toString();
                final String FullName_String = FullName_EV.getText().toString();
                final String Password_String = password_EV.getText().toString();
                final String ConPass_String = confirmPass_EV.getText().toString();

                Log.e("values","Values are: "+Email_String + FullName_String+" "+Password_String+" "+ConPass_String+" "+User);
                if(Email_String.isEmpty()||FullName_String.isEmpty()||Password_String.isEmpty()||ConPass_String.isEmpty())
                {

                    Toast.makeText(RegisterActivity.this, "Please enter all details", Toast.LENGTH_LONG).show();
                }
                else if(!Password_String.equals(ConPass_String))
                {
                    Toast.makeText(RegisterActivity.this, "Passwords are not same!", Toast.LENGTH_LONG).show();
                }
                else{

                    //Sending data to the Firebase

                    db.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            //checking whether the inserted Email is already registered or not..
                            if(snapshot.hasChild(Email_String))
                            {
                                Toast.makeText(RegisterActivity.this, "Email is already registered", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                //if Email is not..
                                db.child("user").child(Email_String).child("FullName").setValue(FullName_String);
                                db.child("user").child(Email_String).child("Password").setValue(Password_String);
                                Toast.makeText(RegisterActivity.this, "Registration Succesfull!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.Admin_tv:
                Player_TV.setTextColor(Color.GRAY);
                Admin_TV.setTextColor(getResources().getColor(R.color.dark_purple));
                User="Admin";
                Log.e("tag", "onCreate in LoginActivity: "+User);
                break;
            case R.id.Player_tv:
                Player_TV.setTextColor(getResources().getColor(R.color.dark_purple));
                Admin_TV.setTextColor(Color.GRAY);
                User="Player";
                Log.e("tag", "onCreate in LoginActivity: "+User);
                break;
        }
    }

}