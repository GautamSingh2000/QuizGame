package com.example.quiz;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class LoginPage extends AppCompatActivity implements View.OnClickListener {
    public String User="Admin";

    private TextView Admin_TV ,Player_TV, SignUP_TV;
    private EditText Email_EV , password_EV;
    private AppCompatButton Login_btn;

    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quiz-d45c8-default-rtdb.firebaseio.com/");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        Admin_TV = (TextView)findViewById(R.id.Admin_tv);
        Player_TV=(TextView)findViewById(R.id.Player_tv);
        SignUP_TV=(TextView)findViewById(R.id.signup_tv);

        Email_EV = (EditText)findViewById(R.id.email);
        password_EV = (EditText)findViewById(R.id.password);
        
        Login_btn =(AppCompatButton)findViewById(R.id.login_btn);


        Admin_TV.setOnClickListener(this);
        Player_TV.setOnClickListener(this);
        Login_btn.setOnClickListener(this);

        SignUP_TV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginPage.this,RegisterActivity.class);
                i.putExtra("User",User);
                startActivity(i);
                finish();
            }
        });

        Login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String Email_String = Email_EV.getText().toString();
                final String Password_String = password_EV.getText().toString();

                if (Email_String.isEmpty() || Password_String.isEmpty()) {
                    Toast.makeText(LoginPage.this, "Enter all details!", Toast.LENGTH_SHORT).show();
                }
                else {
                    db.child("user").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if(snapshot.hasChild(Email_String))
                            {
                                String GetPass = snapshot.child(Email_String).child("Password").getValue(String.class);
//                                boolean result= GetPass.equals(Password_String);
                                if(GetPass!=null && GetPass.equalsIgnoreCase(Password_String))
                               {
                                   Toast.makeText(LoginPage.this, "Login Succesfull", Toast.LENGTH_SHORT).show();
                                   Intent i = new Intent(LoginPage.this,MainActivity.class);
                                   i.putExtra("User",User);
                                   i.putExtra("Email", Email_String);
                                   startActivity(i);
                               }
                               else{
                                   Toast.makeText(LoginPage.this, "Enter Correct Password", Toast.LENGTH_SHORT).show();
                               }
                            }
                            else{
                                Toast.makeText(LoginPage.this, "No user Found", Toast.LENGTH_SHORT).show();
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