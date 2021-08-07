package com.example.hospitaltest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class signup extends AppCompatActivity {
    TextInputEditText tiet_email,tiet_pass,tiet_Cpass,tiet_name;
    TextView signin_tv;
    TextInputLayout til_email,til_pass,til_Cpass;
    Button signup_bt;
    String email_str,pass_str,Cpasss_str,type,name,uid;
    ProgressDialog progressDialog;
    private FirebaseAuth auth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        /*ActionBar bar=getSupportActionBar();
        bar.setTitle("Sign Up");*/
        auth= FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        Intent intent=getIntent();
        type=intent.getStringExtra("account");
        buttonListener();
    }

    private void buttonListener() {
        signup_bt=findViewById(R.id.bt_signup);
        signin_tv=findViewById(R.id.Tv_signin);
        signin_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(signup.this,Signin.class);
                intent.putExtra("type","Patient");
                startActivity(intent);
            }
        });
        signup_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();
            }
        });
    }

    private void validation() {
        ///tiet_name=findViewById(R.id.et);
        til_email=findViewById(R.id.til_uname_su);
        til_pass=findViewById(R.id.til_passowrd_su);
        til_Cpass=findViewById(R.id.til_cpass_su);
        tiet_email=findViewById(R.id.et_uname_su);
        tiet_pass=findViewById(R.id.et_pass_su);
        tiet_Cpass=findViewById(R.id.et_cpass_su);
        email_str=tiet_email.getText().toString().trim();
        pass_str=tiet_pass.getText().toString().trim();
        Cpasss_str=tiet_Cpass.getText().toString().trim();
       // name=tiet_name.getText().toString().trim();


    if(Patterns.EMAIL_ADDRESS.matcher(email_str).matches())
    {
        if(pass_str.length()>7)
        {
            til_pass.setError(null);
            if (pass_rules(pass_str))
            {
                til_pass.setError(null);
                if (pass_str.equals(Cpasss_str))
                {
                    progressDialog.setCancelable(false);
                    progressDialog.setMessage("Creating Account");
                    progressDialog.show();
                    signup_method();
                } else
                {
                    pass_validation();

                }
            } else {
                til_pass.setError("Password must atleast contain one uppercase,number and special character");
            }
        }
        else
        {
            til_pass.setError("Password must be more than 8 character");
        }
    }
        else
    {
        tiet_email.setError("Enter a valid Email Address");
    }
    }

    private boolean pass_rules(String password) {
        //TODO:change the Regex...... It's just overkill;
        Log.d("ErrorSolve","password "+password);

        Pattern pattern;
        Matcher matcher;
        final String password_pattern="^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern= Pattern.compile(password_pattern);
        matcher=pattern.matcher(password);
        Log.d("ErrorSolve",String.valueOf(matcher.matches()));
        return matcher.matches();
    }

    private void pass_validation() {
        til_Cpass.setError("Password does not match");
        tiet_Cpass.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                // Toast.makeText(signup.this, ""+tiet_pass.getText(), Toast.LENGTH_LONG).show();
                if(tiet_pass.getText().toString().equals(tiet_Cpass.getText().toString()))
                {
                    til_Cpass.setError(null);
                }
                else {
                    til_Cpass.setError("Password does not match");
                }

            }
        });
    }


    private void signup_method() {
        auth.createUserWithEmailAndPassword(email_str,pass_str).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if(task.isSuccessful())
                {
                    /*if(task.equals("user")) {
                        startActivity(new Intent(signup.this, user.class));
                    }
                    else if(task.equals("admin"))
                    {
                        //startActivity(new Intent(signup.this, admin.class));
                        //TODO:remove toast  ans start activity;
                        Toast.makeText(signup.this, "admin called", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(signup.this, "Please retry", Toast.LENGTH_SHORT).show();
                    }*/

                    send_email_verification();
                    create_db();

                }
                else
                {
                    //TODO:Throw alert instead of Toast;
                    //TODO: Clear all the toast message!!
                    try {
                        throw task.getException();
                    }
                    catch(NullPointerException nullpoint)
                    {
                        Toast.makeText(signup.this, "Nullpoint exception: "+nullpoint, Toast.LENGTH_SHORT).show();
                    }

                    catch (FirebaseAuthUserCollisionException email)
                    {
                        Toast.makeText(signup.this,"User already exist with this email",Toast.LENGTH_LONG).show();
                    }
                    //if this error is thrown either leave the coding or firebase is drunk!!
                    catch (FirebaseAuthWeakPasswordException weakpass)
                    {
                        Toast.makeText(signup.this, "LOL ye toh possible hi nahi hai!! "+weakpass, Toast.LENGTH_SHORT).show();
                    }

                    catch (FirebaseAuthInvalidCredentialsException malformedemail)
                    {
                        Toast.makeText(signup.this, "Malformed Email "+malformedemail, Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception e)
                    {
                        Toast.makeText(signup.this, "Exception: "+e, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private void create_db() {
        databaseReference=FirebaseDatabase.getInstance().getReference();
        databaseReference.child("Patient").child(auth.getUid()).child("junk").setValue("true");
    }


    private void send_email_verification()
    {
        FirebaseUser user = auth.getCurrentUser();
        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d("Email", "Email sent.");
                            Toast.makeText(signup.this,"Account created, Please verify your Email ID",Toast.LENGTH_LONG).show();
                            Intent intent=new Intent(signup.this,Signin.class);
                            intent.putExtra("type","Patient");
                            startActivity(intent);
                        }
                    }
                });
    }
}
