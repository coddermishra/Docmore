package com.example.hospitaltest;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
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

public class Adapter_Doctor extends RecyclerView.Adapter<Adapter_Doctor.ExampleViewHolder_hos> {
    private ArrayList<item_Hospital> mExampleList;
    Context context;
    LinkedHashMap<Integer,String> lhashmap;
    int position;
    //private ExampleItem currentitemglobal;
    private FloatingActionButton fob;
    FirebaseAuth auth;
    String uid,Patient_uid,Doc_name,Pre_data;
    EditText et1;
    Button Submit;
    DatabaseReference databaseReference;
    public class ExampleViewHolder_hos extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1,mTextView2,mTextView3,mTextView4,mTextView5;
        public Button add_prescription;


        public ExampleViewHolder_hos(View itemView) {
            super(itemView);
            //mImageView = itemView.findViewById(R.id.imageView);
            mTextView1=itemView.findViewById(R.id.textView30);
            mTextView2 =itemView.findViewById(R.id.textView25);
            mTextView3=itemView.findViewById(R.id.textView27);
            mTextView4=itemView.findViewById(R.id.textView29);
            add_prescription=itemView.findViewById(R.id.button11);
            /*mTextView3=itemView.findViewById(R.id.textView17);
            mTextView4=itemView.findViewById(R.id.textView18);
            mTextView5=itemView.findViewById(R.id.textView21);*/
            add_prescription.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position=getAdapterPosition();
                    Addpre();
                    //Addprescription();
                }
            });
        }

        private void Addpre()
        {
            final Dialog dialog=new Dialog(context);
            dialog.setContentView(R.layout.add_prescription);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            et1=dialog.findViewById(R.id.editText5);
            Submit=dialog.findViewById(R.id.button12);
            Submit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Pre_data=et1.getText().toString();
                    Toast.makeText(context, "Prescription added", Toast.LENGTH_SHORT).show();
                    Addprescription();
                    dialog.dismiss();
                }
            });
            dialog.show();


        }
        private void Addprescription() {
            Patient_uid=lhashmap.get(1000-position);
            auth=FirebaseAuth.getInstance();
            uid=auth.getUid();

            databaseReference= FirebaseDatabase.getInstance().getReference();
            DatabaseReference doc=databaseReference.child("Doctor");
            DatabaseReference doc_uid_ref=doc.child(uid);
            doc_uid_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    Doc_name=dataSnapshot.child("Doctor_name").getValue().toString();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            DatabaseReference patient=databaseReference.child("Patient");
            patient.child(Patient_uid).child("Prescription").child(uid).child("Doctor_name").setValue(Doc_name);
            patient.child(Patient_uid).child("Prescription").child(uid).child("Time").setValue(getcurrenttime());
            patient.child(Patient_uid).child("Prescription").child(uid).child("Prescription").setValue(Pre_data);

        }
    }

    public Adapter_Doctor(ArrayList<item_Hospital> exampleList, Context context, LinkedHashMap<Integer,String> myhashMap) {
        mExampleList =exampleList;
        lhashmap=myhashMap;
        this.context=context;
    }

    @Override
    public ExampleViewHolder_hos onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_patient_name, parent, false);

        ExampleViewHolder_hos evh = new ExampleViewHolder_hos(v);
        return evh;
    }



    @Override
    public void onBindViewHolder(ExampleViewHolder_hos holder, int position) {
        item_Hospital currentItem = mExampleList.get(position);
        //currentitemglobal=currentItem;
        //holder.mTextView1.setText("test is this");
        holder.mTextView1.setText(currentItem.getHospital_name());
        Log.d("appont currentitem","hello"+currentItem.getHospital_name());
        holder.mTextView2.setText(currentItem.getHospital_contact());
        holder.mTextView3.setText(currentItem.getHospital_time());
        holder.mTextView4.setText(currentItem.getHospital_spec());



    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public void appoint()
    {
        Log.d("appoint","successful");
    }

        String getcurrenttime()
    {
        Date currentTime = Calendar.getInstance().getTime();
        Log.d("Time",currentTime.toString());
        return currentTime.toString();
    }
}