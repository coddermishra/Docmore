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
import java.util.LinkedHashMap;

public class user_appoint_activity extends AppCompatActivity {
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<item_pre> mExampleList;
    String key, name, spec, time, date, address;
    int count=1000;
    int i=0;
    LinkedHashMap<Integer,String> linkedHashMap=new LinkedHashMap<Integer, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_appoint_activity);
        auth=FirebaseAuth.getInstance();
        db();
        createExampleList();
        buildRecyclerView();
    }

    public void insertItem(String name, String time, String address) {
        //TODO: some changes
        mExampleList.add(new item_pre(name, time, address));
        mAdapter.notifyItemInserted(1);
    }


    public void createExampleList() {
        mExampleList = new ArrayList<>();

        //mExampleList.add(new item_pre("test1","test2","test3"));


    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.user_appoint_rec);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter_appoint(mExampleList,this,linkedHashMap);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    public void db() {

        databaseReference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference patient_uid=databaseReference.child("Patient").child(auth.getUid()).child("Appointment");
        patient_uid.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (final DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                    key=dataSnapshot1.getKey();
                    DatabaseReference doc_ref=databaseReference.child("Doctor").child(key).child("Doctor_name");
                    doc_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                          /*  String name123=dataSnapshot.getValue().toString();
                            name=name123;*/
                            db_test(dataSnapshot1,dataSnapshot);
                            //Toast.makeText(user_appoint_activity.this, "Name"+name, Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });


                    /*date=dataSnapshot1.child("Date").getValue().toString();
                    time=dataSnapshot1.child("Time").getValue().toString();
                    Log.d("appoint",date);
                    Log.d("appoint",key);
                    Log.d("appoint",time);
                    //Log.d("appoint",name);
                    linkedHashMap.put(count-i,key);
                    insertItem(name,date+"\t"+time,"");
                    i++;*/
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });



    }
    private void db_test(DataSnapshot dataSnapshot1,DataSnapshot dataSnapshot)
    {
        name=dataSnapshot.getValue().toString();
        date=dataSnapshot1.child("Date").getValue().toString();
        time=dataSnapshot1.child("Time").getValue().toString();
        Log.d("appoint",date);
        Log.d("appoint",key);
        Log.d("appoint",time);
        //Log.d("appoint",name);
        linkedHashMap.put(count-i,key);
        insertItem(name,date+"\t"+time,"");
        i++;
    }
}
