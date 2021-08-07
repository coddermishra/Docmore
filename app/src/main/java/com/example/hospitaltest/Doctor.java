package com.example.hospitaltest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
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

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Doctor extends AppCompatActivity {

    String key,name_str,date_str,time_str,spec_str;
    int count=1000;
    int i=0;
    LinkedHashMap<Integer,String> linkedHashMap=new LinkedHashMap<Integer, String>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<item_Hospital> mExampleList;


    DatabaseReference databaseReference,databaseReference2;
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageReference;
    private TextView name,day,time,spec;
    private CircleImageView Doc_proimg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);
        auth=FirebaseAuth.getInstance();
        storage= FirebaseStorage.getInstance();
        storageReference=storage.getReference();

        db();
        setdata();
        createExampleList();
        Log.d("apptest","flat");
        // Inflate the layout for this fragment
        mRecyclerView =findViewById(R.id.maindoc_recycle_card);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mExampleList = new ArrayList<item_Hospital>();
        mAdapter = new Adapter_Doctor(mExampleList,this,linkedHashMap);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);





        button_listener();


        setprofileImage();
    }
    public void insertItem(String name, String time, String address, String spec,String cont) {
        mExampleList.add(new item_Hospital(name,time,address,spec,cont));
        mAdapter.notifyItemInserted(1);

    }

    public void createExampleList() {
        mExampleList = new ArrayList<>();
        mExampleList.add(new item_Hospital("test1", "test2", "", "test4", "test5"));
        mExampleList.add(new item_Hospital("test1", "test2", "", "test4", "test5"));
        mExampleList.add(new item_Hospital("test1", "test2", "", "test4", "test5"));
        mExampleList.add(new item_Hospital("test1", "test2", "", "test4", "test5"));

    }
    private void db() {
        String uid=auth.getUid();
        DatabaseReference databaseReference1=FirebaseDatabase.getInstance().getReference();
        DatabaseReference Doc=databaseReference1.child("Doctor");
        DatabaseReference patient=databaseReference1.child("Patient");




        DatabaseReference Doc_uid=databaseReference1.child("Doctor").child(auth.getUid()).child("Appointment");
        Doc_uid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    key=dataSnapshot1.getKey();
                    Log.d("appont doc",key);


                    date_str="Date: "+dataSnapshot1.child("Date").getValue().toString();
                    time_str="Time: "+dataSnapshot1.child("Time").getValue().toString();
                    spec_str="Status: "+dataSnapshot1.child("Status").getValue().toString();
                    name_str="Name: "+dataSnapshot1.child("Patient_name").getValue().toString();
                    //Log.d("appoint doc",name_str);

                    linkedHashMap.put(count-i,key);
                    insertItem(name_str,time_str,"name",spec_str,date_str);
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });













    }














    private void setdata() {
        String uid=auth.getUid();
        databaseReference=FirebaseDatabase.getInstance().getReference("Doctor");
        DatabaseReference uid_ref=databaseReference.child(uid);
        uid_ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                name.setText(dataSnapshot.child("Doctor_name").getValue().toString());
                spec.setText(dataSnapshot.child("Speciality").getValue().toString());
                time.setText(dataSnapshot.child("Time").getValue().toString());
                day.setText(dataSnapshot.child("Day").getValue().toString());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }


    private void button_listener() {
        name=findViewById(R.id.Doctor_name_docac);
        day=findViewById(R.id.Doctor_day_docac);
        time=findViewById(R.id.Doctor_time_docac);
        spec=findViewById(R.id.Doctor_spec_docac);
        Doc_proimg=findViewById(R.id.DoctorImg_docac);

    }

    private void setprofileImage() {

            String uid = auth.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Doctor");
            DatabaseReference uid_ref = databaseReference.child(uid);
            uid_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if(!dataSnapshot.child("Doctor_name").getValue().toString().isEmpty()) {
                        name.setText(dataSnapshot.child("Doctor_name").getValue().toString());
                    }
                    else
                        name.setText(" ");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
                storageReference.child("images/" + auth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        if (!Uri.EMPTY.equals(uri)) {
                            Log.d("Uri","sadas:  "+uri.toString());
                            Glide.with(Doctor.this).load(uri).into(Doc_proimg);
                        }
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        // Handle any errors
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
                startActivity(new Intent(Doctor.this,Doctor_profile.class));
                return (true);
            case R.id.exit:
                signout();
                return (true);

        }
        return super.onOptionsItemSelected(item);



    }
    private void signout() {
        auth.signOut();
        startActivity(new Intent(Doctor.this,MainActivity.class));
        finish();
    }
}
