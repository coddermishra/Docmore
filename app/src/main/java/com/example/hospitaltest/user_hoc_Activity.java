package com.example.hospitaltest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class user_hoc_Activity extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<item_Hospital> mExampleList;
    String key, name, spec, time, number, address;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_hoc_);
        db();
        createExampleList();
        buildRecyclerView();
    }

    public void insertItem(String name, String spec, String time, String mobile, String address) {

        mExampleList.add(new item_Hospital(name, spec, time, mobile, address));
        mAdapter.notifyItemInserted(1);
    }


    public void createExampleList() {
        mExampleList = new ArrayList<>();

        mExampleList.add(new item_Hospital("test1","test2","test3","test4","test5"));



    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.user_hoc_rec);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter_Hos(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void db() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Hospital");
        Log.d("Firebasedb", "some data" + databaseReference);
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.d("Firebasedb", dataSnapshot1.getKey());
                    //insertItem(dataSnapshot1.child());

                    key = dataSnapshot1.getKey();
                    Log.d("FirebasedbHospital",dataSnapshot1.child("Hospital_name").getValue().toString());
                    name = dataSnapshot1.child("Hospital_name").getValue().toString();
                    spec = dataSnapshot1.child("Speciality").getValue().toString();
                    time = dataSnapshot1.child("Time").getValue().toString();
                    number = dataSnapshot1.child("Mobile_number").getValue().toString();
                    address = dataSnapshot1.child("Address").getValue().toString();
                    Log.d("data","name "+name);
                    Log.d("data","spec "+spec);
                    Log.d("data","time "+time);
                    Log.d("data","number "+number);
                    Log.d("data","address "+address);

                    insertItem(name, spec, time, number, address);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
