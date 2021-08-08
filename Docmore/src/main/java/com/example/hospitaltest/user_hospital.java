package com.example.hospitaltest;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class user_hospital extends Fragment {

    DatabaseReference databaseReference;
    FirebaseAuth auth;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<item_Hospital> mExampleList;
    String key, name, spec, time, number, address;

    public user_hospital() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        new hospital();
        View view = inflater.inflate(R.layout.fragment_user_hospital, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview_hospital);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mExampleList = new ArrayList<>();
        mAdapter = new ExampleAdapter_Hos(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        return view;

    }


    class hospital extends AppCompatActivity {
        private ArrayList<item_Hospital> mExampleList;

        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;

        private Button buttonInsert;
        private Button buttonRemove;
        private EditText editTextInsert;
        private EditText editTextRemove;
        DatabaseReference databaseReference;
        FirebaseAuth auth;

        hospital() {

            Log.d("Firebasedb", "Db class called");
            auth = FirebaseAuth.getInstance();
            createExampleList();

            db();
        }


        public void insertItem(String name, String spec, String time, String mobile, String address) {
            mExampleList.add(new item_Hospital(name, spec, time, mobile, address));
        }

        public void createExampleList() {
            mExampleList = new ArrayList<>();


        }

        public void buildRecyclerView() {
            mRecyclerView = findViewById(R.id.recyclerview_doc);
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

                        insertItem(name, "Speciality: "+spec,"Time: "+time, "Contact: "+number,"Address: "+ address);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }
}
