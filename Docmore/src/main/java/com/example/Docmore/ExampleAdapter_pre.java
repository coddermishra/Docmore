package com.example.Docmore;


import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

import androidx.recyclerview.widget.RecyclerView;

public class ExampleAdapter_pre extends RecyclerView.Adapter<ExampleAdapter_pre.ExampleViewHolder_hos> {
    private ArrayList<item_pre> mExampleList;
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
            mTextView1 = itemView.findViewById(R.id.textView19);
            mTextView2 = itemView.findViewById(R.id.textView20);
            mTextView3=itemView.findViewById(R.id.textView22);

        }
    }

    public ExampleAdapter_pre(ArrayList<item_pre> exampleList) {
        mExampleList =exampleList;
    }

    @Override
    public ExampleViewHolder_hos onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_pre_card, parent, false);

        ExampleViewHolder_hos evh = new ExampleViewHolder_hos(v);
        return evh;
    }



    @Override
    public void onBindViewHolder(ExampleViewHolder_hos holder, int position) {
        item_pre currentItem = mExampleList.get(position);
        //currentitemglobal=currentItem;
        holder.mTextView1.setText(currentItem.getDoctor_name());
        holder.mTextView2.setText(currentItem.getTime());
        holder.mTextView3.setText(currentItem.getPrescription());


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