package com.example.hospitaltest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class verification extends AppCompatActivity {
FirebaseAuth auth;
    FirebaseUser user;
    Button send_verification;
    TextView display,time;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification);
        Button_listener();
    }
    private void Button_listener() {
        auth= FirebaseAuth.getInstance();
        user=auth.getCurrentUser();
        String email=user.getEmail();
        send_verification=findViewById(R.id.verify);
        display=findViewById(R.id.textView2);
        time=findViewById(R.id.textView3);
        time.setVisibility(View.INVISIBLE);
        /*String text="Your email id: "+email+" is not verified\n Please, Click \"Send Verification\" Button to get Verification Email";
        display.setText(text);*/
        display.setText(Html.fromHtml("Your email id: <font color='red'>"+email+"</font> is not verified. <br>\n\nPlease, Click \"Send Verification\" Button to get Verification Email"));
        send_verification.setBackgroundColor(Color.parseColor("#54ac68"));
        send_verification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                send_Email_verification();

            }
        });
    }

    private void send_Email_verification() {
        send_verification.setClickable(false);
//TODO: Correct this function;
       user.sendEmailVerification().addOnCompleteListener(this, new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful())
                {
        Toast.makeText(verification.this, "Email link was send", Toast.LENGTH_SHORT).show();
        send_verification.setBackgroundColor(Color.parseColor("#ac5468"));
        time.setVisibility(View.VISIBLE);
        new CountDownTimer(60000,1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                int sec=(int) millisUntilFinished/1000;
                time.setText("Resend Email link in "+sec);
            }

            @Override
            public void onFinish() {
                send_verification.setClickable(true);
                send_verification.setBackgroundColor(Color.parseColor("#54ac68"));
                time.setVisibility(View.INVISIBLE);
            }
        }.start();
                }
                else
                {
                    Toast.makeText(verification.this, "some issue", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}
