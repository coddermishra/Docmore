package com.example.Docmore;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;
import java.util.LinkedHashMap;

import androidx.recyclerview.widget.RecyclerView;

public class Adapter_admin_hos extends RecyclerView.Adapter<Adapter_admin_hos.ExampleViewHolder_hos> {
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
        public TextView mTextView1name, mTextView2spec, mTextView3day, mTextView4time, mTextView5number;
        public Button add_prescription;


        public ExampleViewHolder_hos(View itemView) {
            super(itemView);
            //mImageView = itemView.findViewById(R.id.imageView);
            mTextView1name= itemView.findViewById(R.id.textView38);
            mTextView2spec = itemView.findViewById(R.id.textView39);
            mTextView3day = itemView.findViewById(R.id.textView41);
            mTextView4time= itemView.findViewById(R.id.textView40);
            mTextView5number= itemView.findViewById(R.id.textView42);

        }

    }

    public Adapter_admin_hos(ArrayList<item_Hospital> exampleList, Context context, LinkedHashMap<Integer,String> myhashMap) {
        mExampleList =exampleList;
        lhashmap=myhashMap;
        this.context=context;
    }

    @Override
    public ExampleViewHolder_hos onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_admin_doc, parent, false);

        ExampleViewHolder_hos evh = new ExampleViewHolder_hos(v);
        return evh;
    }



    @Override
    public void onBindViewHolder(ExampleViewHolder_hos holder, int position) {
        item_Hospital currentItem = mExampleList.get(position);
        //currentitemglobal=currentItem;
        //holder.mTextView1.setText("test is this");
        holder.mTextView1name.setText("Name:" +currentItem.getHospital_name());
        holder.mTextView2spec.setText("Specialist: "+currentItem.getHospital_spec());
        holder.mTextView3day.setText("Address: "+currentItem.getHospital_address());
        holder.mTextView4time.setText("Time: "+currentItem.getHospital_time());
        holder.mTextView5number.setText("Contact: "+currentItem.getHospital_contact());



    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }

}