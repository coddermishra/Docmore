package com.example.hospitaltest;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;

import androidx.recyclerview.widget.RecyclerView;

public class ExampleAdapter_Hos extends RecyclerView.Adapter<ExampleAdapter_Hos.ExampleViewHolder_hos> {
    private ArrayList<item_Hospital> mExampleList;
    //private ExampleItem currentitemglobal;
    private FloatingActionButton fob;
    FirebaseAuth auth;
    String uid,Doc_uid;
    DatabaseReference databaseReference;
    public static class ExampleViewHolder_hos extends RecyclerView.ViewHolder {
        public ImageView mImageView;
        public TextView mTextView1;
        public TextView mTextView2,mTextView3,mTextView4,mTextView5;


        public ExampleViewHolder_hos(View itemView) {
            super(itemView);
            //mImageView = itemView.findViewById(R.id.imageView);
            mTextView1 = itemView.findViewById(R.id.textView15);
            mTextView2 = itemView.findViewById(R.id.textView16);
            mTextView3=itemView.findViewById(R.id.textView17);
            mTextView4=itemView.findViewById(R.id.textView18);
            mTextView5=itemView.findViewById(R.id.textView21);
        }
    }

    public ExampleAdapter_Hos(ArrayList<item_Hospital> exampleList) {
        mExampleList =exampleList;
    }

    @Override
    public ExampleViewHolder_hos onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_hos_card, parent, false);

        ExampleViewHolder_hos evh = new ExampleViewHolder_hos(v);
        return evh;
    }



    @Override
    public void onBindViewHolder(ExampleViewHolder_hos holder, int position) {
        item_Hospital currentItem = mExampleList.get(position);
        //currentitemglobal=currentItem;
        holder.mTextView1.setText(currentItem.getHospital_name());
        holder.mTextView2.setText(currentItem.getHospital_spec());
        holder.mTextView3.setText(currentItem.getHospital_time());
        holder.mTextView4.setText(currentItem.getHospital_contact());
        holder.mTextView5.setText(currentItem.getHospital_address());
        Log.d("adapter data",currentItem.getHospital_name());
        Log.d("adapter data",currentItem.getHospital_spec());
        Log.d("adapter data",currentItem.getHospital_time());
        Log.d("adapter data",currentItem.getHospital_contact());
        Log.d("adapter data",currentItem.getHospital_address());


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