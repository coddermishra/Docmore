package com.example.hospitaltest;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
Button Admin_bt,Doctor_bt,Patient_bt,Emergency_bt;
FirebaseUser user;
    public static final int RESULT_ENABLE=110;
FirebaseAuth auth;
Button call,google,hospital;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar bar=getSupportActionBar();
        bar.setTitle("Hospital Management");
        auth=FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        ButtonListener();

    }

    private void ButtonListener() {

        Admin_bt=findViewById(R.id.Admin);
        Doctor_bt=findViewById(R.id.Doctor);
        Patient_bt=findViewById(R.id.Patient);
        Emergency_bt=findViewById(R.id.Emergency);
        Admin_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Signin.class);
                intent.putExtra("type","Admin");
                    startActivity(intent);

            }
        });
        Patient_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Signin.class);
                intent.putExtra("type","Patient");
                startActivity(intent);
            }
        });
        Doctor_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Signin.class);
                intent.putExtra("type","Doctor");
                startActivity(intent);
            }
        });
        Emergency_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Dialog dialog=new Dialog(MainActivity.this);
                dialog.setContentView(R.layout.emergency);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                call=dialog.findViewById(R.id.button22);
                google=dialog.findViewById(R.id.button23);
                hospital=dialog.findViewById(R.id.button24);
                call.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callNum();
                    }
                });
                google.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        googlemap();
                    }
                });
                hospital.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(MainActivity.this,Emergency_hospital.class));
                    }
                });
                dialog.show();
            }
        });
    }

    private void googlemap() {

        String uri="https://www.google.com/maps/search/?api=1&query=Hospital";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
        startActivity(intent);
    }

    private void callNum() {
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat
                    .requestPermissions(
                            MainActivity.this,
                            new String[] {Manifest.permission.CALL_PHONE  },
                            RESULT_ENABLE
                            );
            //Toast.makeText(this, "Permission is required to make call", Toast.LENGTH_SHORT).show();
        }
        else {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + 102));//change the number
            startActivity(callIntent);
        }
    }
}
