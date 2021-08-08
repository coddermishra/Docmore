package com.example.Docmore;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.TextView;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private ArrayList<ExampleItem> mExampleList;
    private ExampleItem currentitemglobal;
    private LinkedHashMap<Integer,String> lhashmap;
    //private FloatingActionButton fob;
    private Context context;
    FirebaseAuth auth;
    int position;
    String uid,Doc_uid,date_str,time_str,a;
    DatabaseReference databaseReference;
    TextView time,date;
    Button timebt,datebt,appointbt;
    private int mYear, mMonth, mDay, mHour, mMinute;
    //TODO: was static CLASS
    public  class ExampleViewHolder extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2,mTextView3,mTextView4;
        public FloatingActionButton fob;


        public ExampleViewHolder(View itemView) {
            super(itemView);
            //mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.cv_doctor_name);
            mTextView2 = itemView.findViewById(R.id.cv_doc_spec);
            mTextView3=itemView.findViewById(R.id.cv_doc_time);
            mTextView4=itemView.findViewById(R.id.cv_doc_mobile);
            fob=itemView.findViewById(R.id.floatingActionButton2);
            fob.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position=getAdapterPosition();
                    Log.d("position Onclick",String.valueOf(position));
                    fuck();
                }
            });

        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleList, Context context, LinkedHashMap<Integer,String> linkedHashMap) {
        this.context=context;
        mExampleList = exampleList;
        lhashmap=linkedHashMap;
    }

    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);

        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    public void fuck() {
        final Dialog dialog=new Dialog(context);
        dialog.setContentView(R.layout.picker);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        time=dialog.findViewById(R.id.Picker_time_et);
        date=dialog.findViewById(R.id.Picker_date_et);
        timebt=dialog.findViewById(R.id.picker_time_bt);
        datebt=dialog.findViewById(R.id.picker_date_bt);
        appointbt=dialog.findViewById(R.id.Picker_appoint_bt);

        datebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {

                                date.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });
        timebt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(context,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                time.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();
            }
        });
        appointbt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validation())
                {
                    create_db();
                    Toast.makeText(context, "Appointment Scheduled", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else
                {
                    Toast.makeText(context, "Please enter the details", Toast.LENGTH_SHORT).show();
                }
            }
        });
        dialog.show();

















    }

    private boolean validation() {
        date_str=date.getText().toString();
        time_str=time.getText().toString();
        if(date_str.matches("")||time_str.matches(""))
        {
            return false;
        }
        else
        {
            return true;
        }
    }


    private void create_db() {

        //Doc_uid=currentitemglobal.getDoc_uid() ;

        Doc_uid=lhashmap.get(1000-position);
        auth=FirebaseAuth.getInstance();
        uid=auth.getUid();
        databaseReference= FirebaseDatabase.getInstance().getReference();
        DatabaseReference doc=databaseReference.child("Doctor");
        DatabaseReference patient=databaseReference.child("Patient");
        patient.child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                a=dataSnapshot.child("Name").getValue().toString();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        doc.child(Doc_uid).child("Appointment").child(uid).child("Date").setValue(date_str);
        doc.child(Doc_uid).child("Appointment").child(uid).child("Time").setValue(time_str);
        doc.child(Doc_uid).child("Appointment").child(uid).child("Status").setValue("Active");
        doc.child(Doc_uid).child("Appointment").child(uid).child("Patient_name").setValue(a);
        //doc.child(Doc_uid).child("Appointment").child(uid).child("patient_uid").setValue(uid);

        //patient.child(uid).child("Appointment").child(Doc_uid).setValue(mygettime());
        patient.child(uid).child("Appointment").child(Doc_uid).child("Date").setValue(date_str);
        patient.child(uid).child("Appointment").child(Doc_uid).child("Time").setValue(time_str);

    }
    private String mygettime()
    {
        Date date=new Date();
        long time=date.getTime();
        return String.valueOf(time);
    }

    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);
        Log.d("position",String.valueOf(position));
        Log.d("position current otem",currentItem.toString());

        currentitemglobal=currentItem;
        holder.mTextView1.setText(currentItem.getDoc_name());
        holder.mTextView2.setText(currentItem.getDoc_spec());
        holder.mTextView3.setText(currentItem.getDoc_time());
        holder.mTextView4.setText(currentItem.getDoc_num());
    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public void appoint()
    {
        Log.d("appoint","successful");
    }
    public void valuesofclass()
    {}

}