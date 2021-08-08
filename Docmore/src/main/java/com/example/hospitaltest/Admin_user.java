package com.example.hospitaltest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Admin_user extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    LinkedHashMap<Integer,String> linkedHashMap=new LinkedHashMap<Integer, String>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<item_Hospital> mExampleList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_user);
        ActionBar bar=getSupportActionBar();
        ColorDrawable colorDrawable=new ColorDrawable(Color.parseColor("#077cff"));
        bar.setBackgroundDrawable(colorDrawable);
        bar.setTitle("User List");
        db();
        createExampleList();

        mRecyclerView =findViewById(R.id.admin_user_rec);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mExampleList = new ArrayList<item_Hospital>();
        mAdapter = new Adapter_admin_user(mExampleList,this,linkedHashMap);
        //TODO:changes


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void insertItem(String name, String time, String address, String spec,String cont) {
        mExampleList.add(new item_Hospital(name,time,address,spec,cont));
        mAdapter.notifyItemInserted(1);

    }

    public void createExampleList() {
        mExampleList = new ArrayList<>();
        /*mExampleList.add(new item_Hospital("test1", "test2", "", "test4", "test5"));
        mExampleList.add(new item_Hospital("test1", "test2", "", "test4", "test5"));
        mExampleList.add(new item_Hospital("test1", "test2", "", "test4", "test5"));
        mExampleList.add(new item_Hospital("test1", "test2", "", "test4", "test5"));

    */}
    private void db() {
        databaseReference= FirebaseDatabase.getInstance().getReference();
        DatabaseReference docs=databaseReference.child("Patient");
        DatabaseReference doctor=databaseReference.child("Doctor");
        docs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {

                    Log.d("LOG",ds.getValue().toString());
                    insertItem(ds.child("Name").getValue().toString(),"",
                            "User", "", ds.child("Mobile_number").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        doctor.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {

                    insertItem(ds.child("Doctor_name").getValue().toString(),"",
                            "Doctor", "", ds.child("Mobile_number").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
