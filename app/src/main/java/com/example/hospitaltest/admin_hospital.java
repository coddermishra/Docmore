package com.example.hospitaltest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.LinkedHashMap;

public class admin_hospital extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    LinkedHashMap<Integer,String> linkedHashMap=new LinkedHashMap<Integer, String>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<item_Hospital> mExampleList;
    int fromhour,tohour,frommin,tomin;
    ArrayAdapter<String> arrayAdapter;
    Button AddHos,save;
    EditText Hospital_name,Hospital_spec,Hospital_number,Hospital_address;
    String name_string,spec_string,number_string,time_string,address_string;


    RadioGroup radioGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_hospital);
        ActionBar bar=getSupportActionBar();
        bar.setTitle("Admin Panel Hospital");
        db();
        createExampleList();
        button_listener();

        mRecyclerView =findViewById(R.id.admin_hos_rec);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mExampleList = new ArrayList<item_Hospital>();
        mAdapter = new Adapter_admin_hos(mExampleList,this,linkedHashMap);
        //TODO:changes


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
    void button_listener()
    {
        AddHos=findViewById(R.id.button14);
        AddHos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(admin_hospital.this);
                dialog.setContentView(R.layout.add_hos);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                radioGroup=dialog.findViewById(R.id.hospital_time);
                Hospital_name=dialog.findViewById(R.id.editText16);
                Hospital_address=dialog.findViewById(R.id.editText18);
                Hospital_number=dialog.findViewById(R.id.editText19);
                Hospital_spec=dialog.findViewById(R.id.editText17);
                save=dialog.findViewById(R.id.button18);
                radioGroup.clearCheck();
                radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton rb = (RadioButton) group.findViewById(checkedId);
                        if (null != rb) {
                            time_string=rb.getText().toString();
                        }
                    }
                });
                save.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        validation();

                        dialog.dismiss();

                    }
                });
                dialog.show();
            }
        });

    }

    private void validation() {
        name_string=Hospital_name.getText().toString();
        address_string=Hospital_address.getText().toString();

        spec_string=Hospital_spec.getText().toString();
        number_string=Hospital_number.getText().toString();
        if(!name_string.isEmpty()&&
                !address_string.isEmpty()&&
                !spec_string.isEmpty()&&
                !number_string.isEmpty()&&
                !time_string.isEmpty())
        {
            create_db();
        }
        else

        {
            Toast.makeText(this, "Please provide all the details", Toast.LENGTH_SHORT).show();
        }
    }

    private void create_db() {

        DatabaseReference db1=FirebaseDatabase.getInstance().getReference("Hospital");
        DatabaseReference push=db1.push();
        push.child("Hospital_name").setValue(name_string);
        push.child("Address").setValue(address_string);
        push.child("Time").setValue(time_string);
        push.child("Speciality").setValue(spec_string);
        push.child("Mobile_number").setValue(number_string);
        Toast.makeText(this, "Hospital Added", Toast.LENGTH_SHORT).show();
        finish();
        startActivity(getIntent());


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
        DatabaseReference docs=databaseReference.child("Hospital");
        docs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Log.d("LOG",ds.getValue().toString());
                    insertItem(ds.child("Hospital_name").getValue().toString(),
                            ds.child("Time").getValue().toString(),
                            ds.child("Address").getValue().toString(),
                            ds.child("Speciality").getValue().toString(),
                            ds.child("Mobile_number").getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
