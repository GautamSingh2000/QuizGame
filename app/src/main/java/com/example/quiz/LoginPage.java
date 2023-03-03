package com.example.quiz;

import static android.content.ContentValues.TAG;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
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
    private LottieAnimationView Peep_animation;
    private boolean password_touch = false;
    private ImageView google,facebook;

    private ProgressDialog  mPregressDialog;

    FirebaseAuth mAuth;

    FirebaseUser mUser;

    GoogleSignInClient mGoogleSignInClint;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_sign_up);

        Admin_TV = (TextView)findViewById(R.id.Admin_tv);
        Player_TV=(TextView)findViewById(R.id.Player_tv);
        SignUP_TV=(TextView)findViewById(R.id.signup_tv);

        Email_EV = (EditText)findViewById(R.id.email);
        password_EV = (EditText)findViewById(R.id.password);

        Peep_animation = findViewById(R.id.peep_anim);
        
        Login_btn =(AppCompatButton)findViewById(R.id.login_btn);

        google = findViewById(R.id.google);
        facebook = findViewById(R.id.facebook);
        
        Admin_TV.setOnClickListener(this);
        Player_TV.setOnClickListener(this);
        Login_btn.setOnClickListener(this);

        mPregressDialog = new ProgressDialog(this);

        password_EV.setOnTouchListener(mTouchListener);

        mAuth = FirebaseAuth.getInstance();

        mUser =mAuth.getCurrentUser();

        SignUP_TV.setOnClickListener(new View.OnClickListener(){
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
                Peep_animation.setVisibility(View.INVISIBLE);
                final String Email_String = Email_EV.getText().toString();
                final String Password_String = password_EV.getText().toString();

                if (Email_String.isEmpty() || Password_String.isEmpty()) {
                    Toast.makeText(LoginPage.this, "Enter all details!", Toast.LENGTH_SHORT).show();
                }
                else {
                    mPregressDialog.setMessage("Checking for Login");
                    mPregressDialog.setTitle("Login");
                    mPregressDialog.setCanceledOnTouchOutside(false);
                    mPregressDialog.show();
                    mAuth.signInWithEmailAndPassword(Email_String,Password_String).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            mPregressDialog.dismiss();
                            if(task.isSuccessful())
                            {
                                Toast.makeText(LoginPage.this, "Login Succesfull", Toast.LENGTH_SHORT).show();
                                Senduser();
                            }
                            else{
                                mPregressDialog.dismiss();
                                Log.e(TAG, "onComplete: "+task.getException() );
                                Toast.makeText(LoginPage.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });

        google.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoogleSignInOptions GSO = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken(getString(R.string.default_web_client_id))
                        .requestEmail()
                        .build();

                mGoogleSignInClint = GoogleSignIn.getClient(LoginPage.this,GSO);

                Intent i = mGoogleSignInClint.getSignInIntent();
                startActivityForResult(i,1000);
            }
        });

        facebook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    private void firebaseAutWithGoogle(String idtoken)
    {
        AuthCredential credential = GoogleAuthProvider.getCredential(idtoken,null);
        mAuth.signInWithCredential(credential).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {
                    mPregressDialog.dismiss();
                    Senduser();
                }
                else{
                    mPregressDialog.dismiss();
                    Toast.makeText(LoginPage.this, "Problem occures!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==1000)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try{
                GoogleSignInAccount acc = task.getResult(ApiException.class);
                firebaseAutWithGoogle(acc.getIdToken());
                task.addOnSuccessListener(new OnSuccessListener<GoogleSignInAccount>() {
                    @Override
                    public void onSuccess(GoogleSignInAccount googleSignInAccount) {
                            if (acc.getDisplayName() != null) {
                                DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quiz-d45c8-default-rtdb.firebaseio.com/");
                                db.child("user").child(User).child(acc.getDisplayName()).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        db.child("user").child(User).child(acc.getDisplayName()).child("FullName").setValue(acc.getDisplayName());
                                        db.child("user").child(User).child(acc.getDisplayName()).child("Gender").setValue("male");
                                        db.child("user").child(User).child(acc.getDisplayName()).child("User").setValue(User);
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                });
            }
            catch(ApiException e)
            {
                Toast.makeText(this, "Google sinin failed", Toast.LENGTH_SHORT).show();
                Log.e(TAG , String.valueOf(e));
                finish();
            }
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

    private void Senduser() {
        Intent i = new Intent(LoginPage.this, MainActivity.class);
        i.putExtra("User", User);
//        if(mUser != null) {
//            if (mUser.getDisplayName() != null) {
//                DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quiz-d45c8-default-rtdb.firebaseio.com/");
//                db.child("user").child(User).child(mUser.getDisplayName()).addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot snapshot) {
//                        db.child("user").child(User).child(mUser.getDisplayName()).child("Gender").setValue("male");
//                    }
//
//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
//            }
//        }
            startActivity(i);
            finish();
        }
    @Override
    protected void onStart() {
        super.onStart();
        if(mAuth.getCurrentUser()!=null)
        {
            Intent i = new Intent(LoginPage.this, MainActivity.class);
            i.putExtra("User",User);
            startActivity(i);
            finish();
        }
    }
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("User",User);
    }
}