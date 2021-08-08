package com.example.Docmore;

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

public class user_pre_activity extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<item_pre> mExampleList;
    String key, name, spec, time, number, address;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_pre_activity);
        auth=FirebaseAuth.getInstance();
        db();
        createExampleList();
        buildRecyclerView();
    }
    public void insertItem(String name,String time, String address) {
        //TODO: some changes
        mExampleList.add(new item_pre(name,time ,address));
        mAdapter.notifyItemInserted(1);
    }


    public void createExampleList() {
        mExampleList = new ArrayList<>();

        //mExampleList.add(new item_pre("test1","test2","test3"));


    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.user_pre_rec);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter_pre(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void db() {
        String uid=auth.getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference("Patient");
        DatabaseReference Uid_ref=databaseReference.child(uid);
        DatabaseReference prescrip=Uid_ref.child("Prescription");
        try {
            prescrip.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        //name = dataSnapshot1.child("Doctor_name").getValue().toString();
                        name="Doctor name";
                        number = dataSnapshot1.child("Time").getValue().toString();
                        address = dataSnapshot1.child("Prescription").getValue().toString();

                        Log.d("dbprescription", "naem" + name);
                        Log.d("dbprescription", number);
                        Log.d("dbprescription", address);
                        insertItem(name, number, address);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch(NullPointerException e)
        {
            Log.d("Log",e.getMessage());
        }


    }
}
