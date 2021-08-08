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
import java.util.LinkedHashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class user_prescription extends Fragment {
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ExampleItem> mExampleList;
    String key, name, spec, time, number, address;


    public user_prescription() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_prescription, container, false);
        mRecyclerView = view.findViewById(R.id.recyclerview_pres);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mExampleList = new ArrayList<>();
        LinkedHashMap<Integer,String> lk=new LinkedHashMap<>();
        mAdapter = new ExampleAdapter(mExampleList,getContext(),lk);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
        new prescription();
        return view;

    }


    class prescription extends AppCompatActivity {
        private ArrayList<ExampleItem> mExampleList;

        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;

        private Button buttonInsert;
        private Button buttonRemove;
        private EditText editTextInsert;
        private EditText editTextRemove;
        DatabaseReference databaseReference;
        FirebaseAuth auth;

        prescription() {

            Log.d("Firebasedb", "Db class called");
            auth = FirebaseAuth.getInstance();
            createExampleList();

            db();
        }


        public void insertItem(String name, String spec, String time, String mobile, String address) {
            mExampleList.add(new ExampleItem(name, spec, time, null, null));
        }

        public void createExampleList() {
            mExampleList = new ArrayList<>();


        }

        public void buildRecyclerView() {
            mRecyclerView = findViewById(R.id.recyclerview_doc);
            mRecyclerView.setHasFixedSize(true);
            mLayoutManager = new LinearLayoutManager(this);
            LinkedHashMap<Integer,String> lk=new LinkedHashMap<>();
            mAdapter = new ExampleAdapter(mExampleList,getContext(),lk);

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
        }

        public void db() {
            key = auth.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Patient");
            Log.d("Firebasedb", "some data" + databaseReference);
            databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        Log.d("Firebasedb", dataSnapshot1.getKey());

                        //insertItem(dataSnapshot1.child());

                        name = dataSnapshot1.child(key).child("time").child("Doctor_name").getValue().toString();
                        spec = dataSnapshot1.child(key).child("time").child("Prescription").getValue().toString();
                        time = dataSnapshot1.child(key).child("Time").getKey();
                        insertItem(name, spec, time, number, address);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

    }
}
