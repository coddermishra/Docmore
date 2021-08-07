package com.example.hospitaltest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.FirebaseTooManyRequestsException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Signin extends AppCompatActivity {

TextView signup;
TextInputLayout til_uname,til_pass;
TextInputEditText tiet_email,tiet_pass;
Button signin,send;
TextView fpass,time;
private Boolean docvalid,uservalid,adminvalid;
    String email_str,pass_str,uid;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
boolean Flag_forgot_pass=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        ActionBar bar=getSupportActionBar();
        bar.setTitle("Sign In");
        auth=FirebaseAuth.getInstance();
        buttonlistener();
    }

    private void buttonlistener() {
        signin=findViewById(R.id.loginbt);
        signup=findViewById(R.id.signup_tv);
        fpass=findViewById(R.id.textView5);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent privilage=getIntent();
                String type=privilage.getStringExtra("type");
                if(type.matches("Patient")) {
                    startActivity(new Intent(Signin.this, signup.class));
                }
                else
                {
                    Toast.makeText(Signin.this, "Please contact Admin\t Signup page is only for user", Toast.LENGTH_SHORT).show();
                }
            }
        });
        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });
        fpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Flag_forgot_pass) {
                    forgot_password();
                }
                else
                {
                    Toast.makeText(Signin.this, "Please wait before sending another Reset Request", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void validate() {
        tiet_email=findViewById(R.id.tiet_email_si);
        tiet_pass=findViewById(R.id.tiet_pass_si);
        email_str=tiet_email.getText().toString().trim();
        pass_str=tiet_pass.getText().toString().trim();
        if(Patterns.EMAIL_ADDRESS.matcher(email_str).matches())
        {
            if(pass_str.length()>7)
            {
                signin_method();
            }
        }
        else
        {
            Toast.makeText(this, "Please provide valid email and password", Toast.LENGTH_SHORT).show();
        }
    }


    private void signin_method() {


        final ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Logging In.....Please wait");
        progressDialog.setCancelable(true);
        progressDialog.show();
        auth.signInWithEmailAndPassword(email_str,pass_str).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful())
                {

                    final FirebaseUser usermain=FirebaseAuth.getInstance().getCurrentUser();
                    progressDialog.dismiss();
                    Intent privilage=getIntent();
                    String type=privilage.getStringExtra("type");
                    switch (type)
                    {
                        case "Admin":
                        {
                            DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference("Admin");
                            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(usermain.getUid()))
                                    {
                                       startActivity(new Intent(Signin.this,Admin_main.class));
                                    }
                                    else
                                    {
                                        Toast.makeText(Signin.this, "Not authorised", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            break;
                        }
                        case "Doctor":
                        {

                            DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference("Doctor");
                            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(usermain.getUid()))
                                    {
                                        startActivity(new Intent(Signin.this,Doctor.class));
                                    }
                                    else
                                    {
                                        Toast.makeText(Signin.this, "Not authorised", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                            break;
                        }
                        case "Patient":
                        {
                            final FirebaseUser user=auth.getCurrentUser();
                            DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference("Patient");
                            databaseReference1.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    if(dataSnapshot.hasChild(usermain.getUid()))
                                    {
                                        if(user.isEmailVerified()) {

                                            startActivity(new Intent(Signin.this,user.class));


                                            //finish();

                                            // Toast.makeText(signin.this, "Login successful Homepage", Toast.LENGTH_SHORT).show();
                                        }
                                        else
                                        {


                                            startActivity(new Intent(Signin.this,verification.class));
                                            Toast.makeText(Signin.this, "Please verify your account\nVerification link has been sent to your email", Toast.LENGTH_LONG).show();
                                        }
                                    }
                                    else
                                    {
                                        Toast.makeText(Signin.this, "Not authorised", Toast.LENGTH_SHORT).show();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });

                        }
                    }



                }
                else
                {
                    progressDialog.dismiss();
                    try {
                        throw task.getException();
                    }

                    catch(FirebaseAuthInvalidUserException email)
                    {
                        Toast.makeText(Signin.this, "User does not exist", Toast.LENGTH_SHORT).show();
                    }

                    catch(FirebaseTooManyRequestsException toomany)
                    {
                        Toast.makeText(Signin.this, "You have tried to Login Too many times", Toast.LENGTH_SHORT).show();
                    }
                    catch (FirebaseAuthInvalidCredentialsException pass)
                    {
                        Toast.makeText(Signin.this, "Email or pass does not match", Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        //TODO: dont catch this one;
                        Toast.makeText(Signin.this, "some error: "+e, Toast.LENGTH_LONG).show();
                    }
                    Toast.makeText(Signin.this,"Login failed",Toast.LENGTH_LONG).show();
                }
            }
        });
    }













    private void forgot_password() {
        final Dialog dialog=new Dialog(this);
        dialog.setContentView(R.layout.email);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        final EditText editText=dialog.findViewById(R.id.editText);
        send=dialog.findViewById(R.id.button2);
        time=dialog.findViewById(R.id.textView10);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email_st=editText.getText().toString();
                if(Patterns.EMAIL_ADDRESS.matcher(email_st).matches()) {
                    auth.sendPasswordResetEmail(email_st).addOnCompleteListener(Signin.this, new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful())
                            {
                                Toast.makeText(Signin.this,"Reset email has been sent to your email address ",Toast.LENGTH_LONG).show();
                                send_Email_reset();
                                dialog.dismiss();
                            }
                            else
                            {
                                Toast.makeText(Signin.this, "Failed to send Email", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                else
                {
                    Toast.makeText(Signin.this,"Please enter valid Email ID",Toast.LENGTH_LONG).show();
                }
            }
        });

        dialog.show();
    }
    private void send_Email_reset() {
        Flag_forgot_pass=false;
        send.setClickable(false);
        send.setTextColor(Color.parseColor("#ac5468"));
        time.setVisibility(View.VISIBLE);
        new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int sec=(int) millisUntilFinished/1000;
                time.setText("Resend Email link in "+sec);
            }

            @Override
            public void onFinish() {
                Flag_forgot_pass=true;
                send.setClickable(true);
                send.setTextColor(Color.parseColor("#54ac68"));
                time.setVisibility(View.INVISIBLE);
            }
        }.start();
    }
}
