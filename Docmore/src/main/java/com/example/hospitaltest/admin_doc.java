package com.example.hospitaltest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;

public class admin_doc extends AppCompatActivity {

    String key,name_str,date_str,time_str,spec_str;
    String name_string,pass_string,email_string,spec_string,number_string,startdat_string,enddat_string,starttme_string,endtime_string;
    Spinner spinner_to,spinner_from;
    EditText nameet,email,password,spec,number;
    Button mystarttime,myendtime,Submit;
    TextView doc_start_time,doc_end_time;
    int count=1000;
    int i=0;
    Button adddoc;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    LinkedHashMap<Integer,String> linkedHashMap=new LinkedHashMap<Integer, String>();
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<item_Hospital> mExampleList;
    int fromhour,tohour,frommin,tomin;
    ArrayAdapter<String> arrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_doc);
        auth=FirebaseAuth.getInstance();
        db();
        createExampleList();
        button_listener();

        mRecyclerView =findViewById(R.id.admin_doc_rec);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mExampleList = new ArrayList<item_Hospital>();
        mAdapter = new Adapter_admin_doc(mExampleList,this,linkedHashMap);
        //TODO:changes


        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void button_listener() {
        adddoc=findViewById(R.id.button13);
        adddoc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(admin_doc.this);
                dialog.setContentView(R.layout.add_doctor);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                nameet=dialog.findViewById(R.id.editText10);
                spec=dialog.findViewById(R.id.editText14);
                email=dialog.findViewById(R.id.editText11);
                password=dialog.findViewById(R.id.editText12);
                number=dialog.findViewById(R.id.editText13);
                spinner_to=dialog.findViewById(R.id.spinner3);
                spinner_from=dialog.findViewById(R.id.spinner4);
                doc_start_time=dialog.findViewById(R.id.textView43);
                doc_end_time=dialog.findViewById(R.id.textView44);
                mystarttime=dialog.findViewById(R.id.button17);
                myendtime=dialog.findViewById(R.id.button16);


                mystarttime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        final Calendar c = Calendar.getInstance();
                        fromhour = c.get(Calendar.HOUR_OF_DAY);
                        frommin = c.get(Calendar.MINUTE);
                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(admin_doc.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        doc_start_time.setText(hourOfDay + ":" + minute);
                                        starttme_string=hourOfDay + ":" + minute;
                                    }
                                }, fromhour, frommin, false);
                        timePickerDialog.show();



                    }
                });
                myendtime.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final Calendar c = Calendar.getInstance();
                        fromhour = c.get(Calendar.HOUR_OF_DAY);
                        frommin = c.get(Calendar.MINUTE);
                        // Launch Time Picker Dialog
                        TimePickerDialog timePickerDialog = new TimePickerDialog(admin_doc.this,
                                new TimePickerDialog.OnTimeSetListener() {

                                    @Override
                                    public void onTimeSet(TimePicker view, int hourOfDay,
                                                          int minute) {

                                        doc_end_time.setText(hourOfDay + ":" + minute);
                                        endtime_string=hourOfDay + ":" + minute;
                                    }
                                }, fromhour, frommin, false);
                        timePickerDialog.show();

                    }
                });
                mysetspinner();




                Submit=dialog.findViewById(R.id.button15);
                Submit.setOnClickListener(new View.OnClickListener() {
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


    void validation()
    {
        name_string=nameet.getText().toString();
        pass_string=password.getText().toString();
        email_string=email.getText().toString();
        spec_string=spec.getText().toString();
        number_string=number.getText().toString();
        if(Patterns.EMAIL_ADDRESS.matcher(email_string).matches())
        {
            if(pass_string.length()>7)
            {
                create_acc(email_string,pass_string);
            }
        }
        else
        {
            Toast.makeText(this, "Please provide valid email and password", Toast.LENGTH_SHORT).show();
        }

    }
    void create_acc(String em,String pass)
    {
        auth.createUserWithEmailAndPassword(em,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(task.isSuccessful())
                {
                    /*if(task.equals("user")) {
                        startActivity(new Intent(signup.this, user.class));
                    }
                    else if(task.equals("admin"))
                    {
                        //startActivity(new Intent(signup.this, admin.class));
                        //TODO:remove toast  ans start activity;
                        Toast.makeText(signup.this, "admin called", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(signup.this, "Please retry", Toast.LENGTH_SHORT).show();
                    }*/
                    if(!name_string.isEmpty()&&!spec_string.isEmpty()&&!number_string.isEmpty())
                    {
                        create_db(task.getResult().getUser().getUid());
                    }
                    else
                    {
                        DatabaseReference db=FirebaseDatabase.getInstance().getReference();
                        db.child("Doctor").child(task.getResult().getUser().toString()).child("junk").setValue("value");
                    }


                }
                else
                {
                    //TODO:Throw alert instead of Toast;
                    //TODO: Clear all the toast message!!
                    try {
                        throw task.getException();
                    }
                    catch(NullPointerException nullpoint)
                    {
                        Toast.makeText(admin_doc.this, "Nullpoint exception: "+nullpoint, Toast.LENGTH_SHORT).show();
                    }

                    catch (FirebaseAuthUserCollisionException email)
                    {
                        Toast.makeText(admin_doc.this,"User already exist with this email",Toast.LENGTH_LONG).show();
                    }
                    //if this error is thrown either leave the coding or firebase is drunk!!
                    catch (FirebaseAuthWeakPasswordException weakpass)
                    {
                        Toast.makeText(admin_doc.this, "LOL ye toh possible hi nahi hai!! "+weakpass, Toast.LENGTH_SHORT).show();
                    }

                    catch (FirebaseAuthInvalidCredentialsException malformedemail)
                    {
                        Toast.makeText(admin_doc.this, "Malformed Email "+malformedemail, Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(admin_doc.this, "Exception: "+e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

    }

    private void create_db(String uid) {
        DatabaseReference db=FirebaseDatabase.getInstance().getReference();
        db.child("Doctor").child(uid).child("Doctor_name").setValue(name_string);
        db.child("Doctor").child(uid).child("Mobile_number").setValue(number_string);
        db.child("Doctor").child(uid).child("Speciality").setValue(spec_string);
        db.child("Doctor").child(uid).child("Day").setValue(startdat_string+" - "+enddat_string);
        db.child("Doctor").child(uid).child("Time").setValue(starttme_string+" - "+endtime_string);

    }


    private void mysetspinner() {
        List<String> list=new ArrayList<String>();
        list.add("Monday");
        list.add("Tuesday");
        list.add("Wednesday");
        list.add("Thursday");list.add("Friday");list.add("Saturday");list.add("Sunday");
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_from.setAdapter(arrayAdapter);
        spinner_to.setAdapter(arrayAdapter);

        spinner_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                startdat_string=parent.getItemAtPosition(position).toString();
                //Toast.makeText(Doctor_profile.this, "Selected day 2"+day_to, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                enddat_string=parent.getItemAtPosition(position).toString();
                //Toast.makeText(Doctor_profile.this, "Selected day"+day_from, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
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
        DatabaseReference docs=databaseReference.child("Doctor");
        docs.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds:dataSnapshot.getChildren())
                {
                    Log.d("LOG",ds.getValue().toString());
                    insertItem(ds.child("Doctor_name").getValue().toString(),
                            ds.child("Time").getValue().toString(),
                            ds.child("Day").getValue().toString(),
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
