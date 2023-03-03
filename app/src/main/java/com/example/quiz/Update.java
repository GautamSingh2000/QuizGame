package com.example.quiz;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class Update extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private ImageView Btn_Back, Btn_Done , IV_profile;
    private EditText Et_Email, Et_Fullname;
    private Spinner SGender;
    private String Updated_Email, Update_Fullname, Gender, hint_Email, hint_Fullname, hint_Gender;
    private TextView TV_Edit;
    DatabaseReference db = FirebaseDatabase.getInstance().getReferenceFromUrl("https://quiz-d45c8-default-rtdb.firebaseio.com/user");
    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Btn_Back = findViewById(R.id.btn_back);
        Btn_Done = findViewById(R.id.btn_done);
        Et_Email = findViewById(R.id.et_email);
        Et_Fullname = findViewById(R.id.et_fullname);
        SGender = findViewById(R.id.spinner_gender);
        IV_profile = findViewById(R.id.iv_profile);
        TV_Edit = findViewById(R.id.tv_edit);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        db.child("Admin").child(mUser.getDisplayName()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists())
                {
                    Map map = (Map) snapshot.getValue();
                    if(map.get("Gender") != null) {
                        Gender = map.get("Gender").toString();
                        Log.e(TAG," in Update "+Gender);
                        if(Gender == "male")
                        {
                            Log.e(TAG,"male true "+Gender);
                            Picasso.get().load(mUser.getPhotoUrl()).placeholder(R.drawable.male_img).into(IV_profile);
                        }
                        else{
                            Log.e(TAG,"female true" +Gender);
                            Picasso.get().load(mUser.getPhotoUrl()).placeholder(R.drawable.female_img).into(IV_profile);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
//       db.child("Admin").child(mUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
//           @Override
//           public void onComplete(@NonNull Task<DataSnapshot> task) {
//               if (task.isSuccessful()) {
//                   if (task.getResult().exists()) {
//                       DataSnapshot dataSnapshot = task.getResult();
//                       Gender = String.valueOf(dataSnapshot.child("Gender").getValue());
//                       if(Gender.equals("female"))
//                       {
//                           IV_profile.setImageResource(R.drawable.female_img);
//                       }
//                   }
//               }
//           }
//       });


        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,R.array.Gender_spinner, android.R.layout.simple_dropdown_item_1line);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        SGender.setAdapter(adapter);

        SGender.setOnItemSelectedListener(this);

        if(mUser != null )
        {
            UpdateHints();
        }

        Btn_Done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    UpdateValue();
            }
        });

        Btn_Back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Toast.makeText(Update.this, "profle updated!!", Toast.LENGTH_SHORT).show();
            }
        });

        TV_Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_PICK);
                i.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i,1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == RESULT_OK)
        {
            if(requestCode==1000)
            {
                IV_profile.setImageURI(data.getData());
            }
        }
    }

    public void UpdateHints()
    {
      hint_Email = mUser.getEmail();
      hint_Fullname = mUser.getDisplayName();
      Et_Email.setText(hint_Email);
      Et_Email.setSelection(hint_Email.length());
      Et_Fullname.setText(hint_Fullname);
      Et_Fullname.setSelection(hint_Fullname.length());
    }

    public void UpdateValue() {
        Update_Fullname = Et_Fullname.getText().toString();
//        Updated_Email = Et_Email.getText().toString();

        HashMap<String, Object> user = new HashMap<String, Object>();
        user.put("FullName",Update_Fullname);

           db.child("Admin").child(mUser.getDisplayName()).updateChildren(user).addOnCompleteListener(new OnCompleteListener() {
               @Override
               public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {

                        Et_Fullname.setText(Update_Fullname);
                        try{
                        UserProfileChangeRequest changeRequest = new UserProfileChangeRequest.Builder()
                                .setDisplayName(Update_Fullname).build();
                            mAuth.getCurrentUser().updateProfile(changeRequest);
                        }catch (Exception e)
                        {
                            Log.e(TAG,"exception"+e);
                        }
                        Toast.makeText(Update.this, "Update Successfull!!", Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(Update.this, "Update Unsuccessfull!!", Toast.LENGTH_SHORT).show();
                    }
               }
           });
        }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Toast.makeText(this, "profile Update!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
       String choosenText = parent.getItemAtPosition(position).toString();
        Toast.makeText(this, choosenText+" is selected", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}