package com.example.Docmore;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
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
 * Activities that contain this fragment must implement the
 * interface
 * to handle interaction events.
 * Use the {} factory method to
 * create an instance of this fragment.
 */
public class user_doctor extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    DatabaseReference databaseReference;
    FirebaseAuth auth;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ExampleItem> mExampleList;
    String name,spec,number,time,key;
    FloatingActionButton fob;


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // private OnFragmentInteractionListener mListener;

    public user_doctor() {
        auth = FirebaseAuth.getInstance();
        Log.d("apptest","const");
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_user_doctor, container, false);;
        db();
        createExampleList();
        Log.d("apptest","flat");
        // Inflate the layout for this fragment
        mRecyclerView = view.findViewById(R.id.recyclerview_doc);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getContext());
        mExampleList = new ArrayList<>();
        LinkedHashMap<Integer,String> lk=new LinkedHashMap<>();
        mAdapter = new ExampleAdapter(mExampleList,getContext(),lk);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        return view;

    }


    /* class Doc extends AppCompatActivity {
        private ArrayList<ExampleItem> mExampleList;

        private RecyclerView mRecyclerView;
        private RecyclerView.Adapter mAdapter;
        private RecyclerView.LayoutManager mLayoutManager;
        FloatingActionButton fob;
        private Button buttonInsert;
        private Button buttonRemove;
        private EditText editTextInsert;
        private EditText editTextRemove;
        DatabaseReference databaseReference;
        FirebaseAuth auth;

        Doc() {

            Log.d("Firebasedb", "Db class called");
            auth = FirebaseAuth.getInstance();

            createExampleList();
            //buildRecyclerView();
            db();





        }*/





        public void insertItem(String name, String spec, String time, String mobile,String key_uid) {
            mExampleList.add(new ExampleItem(name,spec,time,mobile,key_uid));
            mAdapter.notifyItemInserted(1);

        }

        public void createExampleList() {
            mExampleList = new ArrayList<>();
            mExampleList.add(new ExampleItem("test1","test2","test3","test4","test5"));



        }

       /* public void buildRecyclerView() {
           // mRecyclerView = findViewById(R.id.recyclerview_doc);
            //mRecyclerView.setHasFixedSize(true);
            //mLayoutManager = new LinearLayoutManager(this);
            mAdapter = new ExampleAdapter(mExampleList,getContext());

            mRecyclerView.setLayoutManager(mLayoutManager);
            mRecyclerView.setAdapter(mAdapter);
        }*/

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
                        insertItem("Dr. "+name,"Speciality"+spec,"Time: "+time,"Number:"+number,key);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }

   /* public String randomname()
    {
        String[] names={"lion","tiger","wolf","teemo","yasuo","zoe","braum","cho'gat"};
        Random rand=new Random();
        int i=rand.nextInt(7);
        return names[i];
    }
    public String randomnum()
    {
        Random rand=new Random();
        return String.valueOf(rand.nextInt(100));
    }*/


    }
//}
