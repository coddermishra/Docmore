package com.example.hospitaltest;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;

import androidx.recyclerview.widget.RecyclerView;

public class ExampleAdapter_appoint extends RecyclerView.Adapter<ExampleAdapter_appoint.ExampleViewHolder_hos> {
    private ArrayList<item_pre> mExampleList;
    LinkedHashMap<Integer,String> lhashmap;
    int position;
    //private ExampleItem currentitemglobal;
    private FloatingActionButton fob;
    FirebaseAuth auth;
    String uid,Doc_uid;
    DatabaseReference databaseReference;
    public class ExampleViewHolder_hos extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2,mTextView3,mTextView4,mTextView5;
        public Button cancel_appointment;


        public ExampleViewHolder_hos(View itemView) {
            super(itemView);
            //mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.appoint_doc_name);
            mTextView2 = itemView.findViewById(R.id.appoint_time);
            cancel_appointment=itemView.findViewById(R.id.appoint_cancelbt);
            /*mTextView3=itemView.findViewById(R.id.textView17);
            mTextView4=itemView.findViewById(R.id.textView18);
            mTextView5=itemView.findViewById(R.id.textView21);*/
            cancel_appointment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    position=getAdapterPosition();
                    cancelAppoint();
                }
            });
        }

        private void cancelAppoint() {
            Doc_uid=lhashmap.get(1000-position);
            auth=FirebaseAuth.getInstance();
            uid=auth.getUid();
            databaseReference= FirebaseDatabase.getInstance().getReference();
            DatabaseReference doc=databaseReference.child("Doctor");
            DatabaseReference patient=databaseReference.child("Patient");
            doc.child(Doc_uid).child("Appointment").child(uid).child("Status").setValue("Cancelled");
            //doc.child(Doc_uid).child("Appointment").child(uid).child("patient_uid").setValue(uid);

            //patient.child(uid).child("Appointment").child(Doc_uid).setValue(mygettime());
            patient.child(uid).child("Appointment").child(Doc_uid).child("Date").removeValue();
            patient.child(uid).child("Appointment").child(Doc_uid).child("Time").removeValue();
        }
    }

    public ExampleAdapter_appoint(ArrayList<item_pre> exampleList, Context context, LinkedHashMap<Integer,String> myhashMap) {
        mExampleList =exampleList;
        lhashmap=myhashMap;
    }

    @Override
    public ExampleViewHolder_hos onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_user_appoint, parent, false);

        ExampleViewHolder_hos evh = new ExampleViewHolder_hos(v);
        return evh;
    }



    @Override
    public void onBindViewHolder(ExampleViewHolder_hos holder, int position) {
        item_pre currentItem = mExampleList.get(position);
        //currentitemglobal=currentItem;
        holder.mTextView1.setText(currentItem.getDoctor_name());
        holder.mTextView2.setText(currentItem.getTime());


    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public void appoint()
    {
        Log.d("appoint","successful");
    }
}