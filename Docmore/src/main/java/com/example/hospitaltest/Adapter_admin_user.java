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

public class Adapter_admin_user extends RecyclerView.Adapter<Adapter_admin_user.ExampleViewHolder_hos> {
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
        public TextView mTextView1name, mTextView2email, mTextView3gender, mTextView4number, mTextView5number;
        public Button add_prescription;


        public ExampleViewHolder_hos(View itemView) {
            super(itemView);
            //mImageView = itemView.findViewById(R.id.imageView);
            mTextView1name= itemView.findViewById(R.id.textView47);
           // mTextView2email = itemView.findViewById(R.id.textView48);
            mTextView3gender = itemView.findViewById(R.id.textView50);
            mTextView4number= itemView.findViewById(R.id.textView49);
            //mTextView5number= itemView.findViewById(R.id.textView42);

        }

    }

    public Adapter_admin_user(ArrayList<item_Hospital> exampleList, Context context, LinkedHashMap<Integer,String> myhashMap) {
        mExampleList =exampleList;
        lhashmap=myhashMap;
        this.context=context;
    }

    @Override
    public ExampleViewHolder_hos onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.admin_user_card, parent, false);

        ExampleViewHolder_hos evh = new ExampleViewHolder_hos(v);
        return evh;
    }



    @Override
    public void onBindViewHolder(ExampleViewHolder_hos holder, int position) {
        item_Hospital currentItem = mExampleList.get(position);
        //currentitemglobal=currentItem;
        //holder.mTextView1.setText("test is this");
        holder.mTextView1name.setText("Name:" +currentItem.getHospital_name());
        //holder.mTextView2email.setText("Specialist: "+currentItem.getHospital_spec());
        holder.mTextView3gender.setText("Privilage"+currentItem.getHospital_address());
        //holder.mTextView4time.setText("Time: "+currentItem.getHospital_time());
        holder.mTextView4number.setText("Contact: "+currentItem.getHospital_contact());



    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

}