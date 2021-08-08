package com.example.Docmore;

import android.os.Bundle;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.Random;

public class testactivity extends AppCompatActivity {
    private ArrayList<ExampleItem> mExampleList;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    private Button buttonInsert;
    private Button buttonRemove;
    private EditText editTextInsert;
    private EditText editTextRemove;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_testactivity);
        auth=FirebaseAuth.getInstance()
;        createExampleList();
        buildRecyclerView();
        buttonlistener();
    }

    private void buttonlistener() {
        Button button=findViewById(R.id.magic);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertItem();
                dbtest();}
        });
    }

    private void dbtest() {
        //String uid=auth.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Patient").child("Talib").setValue("ha zinda hai");


    }

    public void insertItem() {
        mExampleList.add(new ExampleItem(randomname() , randomnum(),"","",""));
        mAdapter.notifyItemInserted(1);
    }
    public void createExampleList() {
        mExampleList = new ArrayList<>();
        //mExampleList.add(new ExampleItem(random(), random()));

    }

    public void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
       // mAdapter = new ExampleAdapater2(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
    public String randomname()
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
    }

}
