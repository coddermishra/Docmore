package com.example.Docmore;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class user_doc_Activity extends AppCompatActivity {
    DatabaseReference databaseReference;
    int count=1000;
    int i=0;
    LinkedHashMap<Integer,String> linkedHashMap=new LinkedHashMap<Integer, String>();
    FirebaseAuth auth;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ExampleItem> mExampleList;
    String name,spec,number,time,key;
    FloatingActionButton fob;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_doc_);
        db();
        createExampleList();
        Log.d("apptest","flat");
        // Inflate the layout for this fragment
        mRecyclerView =findViewById(R.id.user_doc_rec);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mExampleList = new ArrayList<ExampleItem>();
        mAdapter = new ExampleAdapter(mExampleList,this,linkedHashMap);


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
    public void insertItem(String name, String spec, String time, String mobile,String key_uid) {
        mExampleList.add(new ExampleItem(name,spec,time,mobile,key_uid));
        mAdapter.notifyItemInserted(1);

    }

    public void createExampleList() {
        mExampleList = new ArrayList<ExampleItem>();
        mExampleList.add(new ExampleItem("test1","test2","test3","test4","test5"));



    }
    public void db() {

        databaseReference = FirebaseDatabase.getInstance().getReference("Doctor");
        Log.d("Firebasedb", "some data" + databaseReference);
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    Log.d("Firebasedb", dataSnapshot1.getKey());

                    key=dataSnapshot1.getKey();
                    DatabaseReference key_ref=databaseReference.child(key);
                    //Log.d("Firebasedb", dataSnapshot1.child("Doctor_name").getValue().toString());
                    name=dataSnapshot1.child("Doctor_name").getValue().toString();
                    spec=dataSnapshot1.child("Speciality").getValue().toString();
                    time=dataSnapshot1.child("Time").getValue().toString();
                    number=dataSnapshot1.child("Mobile_number").getValue().toString();
                    linkedHashMap.put(count-i,key);
                    insertItem("Dr. "+name,spec,"Time: "+time,number,key);
                    i++;
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
