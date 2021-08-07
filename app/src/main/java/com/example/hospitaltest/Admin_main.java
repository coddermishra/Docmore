package com.example.hospitaltest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

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

public class Admin_main extends AppCompatActivity {
    CardView profile,patient,doc,hospital,signout;
    FirebaseAuth auth;
    TextView name;
    CircleImageView profile_img;
    DatabaseReference databaseReference;
    FirebaseStorage storage;
    StorageReference storageReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_main);
        ActionBar bar=getSupportActionBar();
        bar.setTitle("Admin Panel");
        auth=FirebaseAuth.getInstance();
        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();
        button_listener();
        button_listener_methods();
        setprofileImage();
    }


    private void button_listener() {
        profile=findViewById(R.id.Admin_profile);
        doc=findViewById(R.id.Admin_doctor);
        patient=findViewById(R.id.Admin_user);
        hospital=findViewById(R.id.Admin_hospital);
        signout=findViewById(R.id.Admin_signout);
        name=findViewById(R.id.textView31);
        profile_img=findViewById(R.id.Admin_profile_pic);
    }

    private void button_listener_methods() {
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin_main.this,Admin_profile.class));
                //Toast.makeText(Admin_main.this, "Hello", Toast.LENGTH_SHORT).show();
            }
        });
        doc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin_main.this,admin_doc.class));
            }
        });
        patient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin_main.this,Admin_user.class));
                //Toast.makeText(Admin_main.this, "Hello", Toast.LENGTH_SHORT).show();
            }
        });
        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Admin_main.this,admin_hospital.class));
                //Toast.makeText(Admin_main.this, "Hello", Toast.LENGTH_SHORT).show();
            }
        });signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signout();
            }
        });


    }
    private void signout() {
        auth.signOut();
        startActivity(new Intent(Admin_main.this,MainActivity.class));
        finish();
    }




    private void setprofileImage() {

        String uid = auth.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Admin");
        DatabaseReference uid_ref = databaseReference.child(uid);
        uid_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(!dataSnapshot.child("Name").getValue().toString().isEmpty()) {
                    name.setText(dataSnapshot.child("Name").getValue().toString());
                }
                else
                    name.setText(" ");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        try {
            storageReference.child("images/" + auth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    if (!Uri.EMPTY.equals(uri)) {
                        Log.d("Uri", "sadas:  " + uri.toString());
                        Glide.with(Admin_main.this).load(uri).into(profile_img);
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
        }
        catch (NullPointerException n)
        {
            Log.d("LOG",n.getMessage());
        }


    }

}
