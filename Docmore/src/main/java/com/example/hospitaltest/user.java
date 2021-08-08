package com.example.hospitaltest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class user extends AppCompatActivity {
    FirebaseAuth auth;
    TextView name;
    CircleImageView profileimg;
    Button Doctor,Appointment,Hospital,Prescription;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        auth=FirebaseAuth.getInstance();
        storage=FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        button_listener();
        setprofileImage();
    }

    private void setprofileImage() {
        try {
            String uid = auth.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Patient");
            DatabaseReference uid_ref = databaseReference.child(uid);
            uid_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        name.setText(dataSnapshot.child("Name").getValue().toString());
                    }
                    catch (NullPointerException e)
                    {
                        Log.d("Error",e.getMessage());
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            storageReference = storage.getReference();

            storageReference.child("images/" + auth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    if(uri!=null) {
                        Glide.with(user.this).load(uri).into(profileimg);
                    }
                    else
                    {
                        profileimg.setBackgroundResource(R.drawable.user_blue);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        catch (NullPointerException ex)
        {

        }

    }

    private void button_listener() {
        name=findViewById(R.id.textView12);
        profileimg=findViewById(R.id.circleImageView);
        Doctor=findViewById(R.id.button3);
        Hospital=findViewById(R.id.button5);
        Appointment=findViewById(R.id.button7);
        Prescription=findViewById(R.id.button6);
        Doctor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user.this,user_doc_Activity.class));
            }
        });
        Hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user.this,user_hoc_Activity.class));
            }
        });
        Appointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user.this,user_appoint_activity.class));
            }
        });
        Prescription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(user.this,user_pre_activity.class));
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.about:
                startActivity(new Intent(user.this,Profile.class));
                return (true);
            case R.id.exit:
                signout();
                return (true);

        }
        return super.onOptionsItemSelected(item);



    }

    private void signout() {
        auth.signOut();
        startActivity(new Intent(user.this,MainActivity.class));
        finish();
    }
}
