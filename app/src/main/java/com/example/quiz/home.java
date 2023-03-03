package com.example.quiz;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class home extends AppCompatActivity {
    TextView Username,Gmail;

    Button sinout_btn;
    GoogleSignInClient GSC;

    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser user = auth.getCurrentUser();



    @Override
    protected void onStart() {
        super.onStart();
        if(user==null)
        {
            startActivity(new Intent(home.this,LoginPage.class));
            finish();
        }
    }

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        Gmail= (TextView) findViewById(R.id.Gmail_tv);
        Username =(TextView) findViewById(R.id.Username_tv);
        sinout_btn = findViewById(R.id.sinout_Btn);

        String email , name;

        if(user!=null)
        {
            Username.setText(user.getDisplayName());
            Gmail.setText(user.getEmail());
        }
        GoogleSignInOptions GSO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GSC = GoogleSignIn.getClient(home.this,GSO);

        GoogleSignInAccount acc = GoogleSignIn.getLastSignedInAccount(this);
        if(acc!=null)
        {
             email = acc.getEmail();
             name = acc.getDisplayName();
            if(email != null) {
                Gmail.setText(email);
            }
            if(name!=null)
            {
            Username.setText(name);
            }
        }
        sinout_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sinout();
            }
        });

    }


    public void sinout()
    {
        auth.signOut();
        GSC.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                finish();
                startActivity(new Intent(home.this, LoginPage.class));
            }
        });
        finish();
    }

}