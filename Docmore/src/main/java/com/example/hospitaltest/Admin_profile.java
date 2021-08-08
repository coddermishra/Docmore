package com.example.hospitaltest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.regex.Pattern;

public class Admin_profile extends AppCompatActivity {
    FirebaseAuth auth;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    Button save;
    RadioGroup radioGroup;
    EditText name,mobile;
    String name_string,mobile_string,gender_string,name_str,Gender_str,mobile_str;
    CircleImageView profile_pic;
    Uri filePath;
    private static final int PICK_IMAGE_REQUEST =73 ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_profile);
        ActionBar bar=getSupportActionBar();
        ColorDrawable colorDrawable=new ColorDrawable(Color.parseColor("#077cff"));
        bar.setBackgroundDrawable(colorDrawable);
        bar.setTitle("Edit Profile");
        auth= FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        button_listener();
        get_data();
    }

    private void button_listener() {
        save=findViewById(R.id.admin_save_button);
        radioGroup=findViewById(R.id.admin_rg);
        name=findViewById(R.id.editText9);
        mobile=findViewById(R.id.editText8);
        profile_pic=findViewById(R.id.circleImageView3);
        radioGroup.clearCheck();
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton rb = (RadioButton) group.findViewById(checkedId);
                if (null != rb) {
                    gender_string=rb.getText().toString();
                }
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validation();

            }
        });
        profile_pic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Profile picture"),PICK_IMAGE_REQUEST);
            }
        });

    }
    private void validation() {
        name_string=name.getText().toString();
        mobile_string=mobile.getText().toString();
        if(isValidMobile(mobile_string)&&null!=name_string)
        {
            create_db();
            upload_image();
        }

    }
    private boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        return false;

    }
    private void create_db() {
        //TODO:cahnge this;
        String uid=auth.getUid();
        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference();
        DatabaseReference Uid_ref=databaseReference1.child("Admin").child(uid);
        Uid_ref.child("Name").setValue(name_string);
        Uid_ref.child("Gender").setValue(gender_string);
        Uid_ref.child("Mobile_number").setValue(mobile_string);
        Toast.makeText(this, "Profile updated", Toast.LENGTH_SHORT).show();

    }
    private void upload_image() {
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ auth.getUid().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Toast.makeText(Admin_profile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Admin_profile.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            //Compressor compressor=new Compressor(this).compressToFile()
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                profile_pic.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
    private void get_data() {
        try {
            String uid = auth.getUid();
            databaseReference = FirebaseDatabase.getInstance().getReference("Admin");
            DatabaseReference uid_ref = databaseReference.child(uid);
            storageReference = storage.getReference();

            storageReference.child("images/" + auth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Glide.with(Admin_profile.this).load(uri).into(profile_pic);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                    // Handle any errors
                }
            });
            uid_ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    try {
                        Log.d("datasnapshot", dataSnapshot.toString());
                        Log.d("datasnapshot", dataSnapshot.child("Name").getValue().toString());
                        name.setText(dataSnapshot.child("Name").getValue().toString());
                        mobile.setText(dataSnapshot.child("Mobile_number").getValue().toString());
                        if (!dataSnapshot.child("Gender").getValue().toString().isEmpty() && dataSnapshot.child("Gender").getValue().toString().matches("Male")) {
                            radioGroup.check(R.id.radiobuttonmale);
                        }
                        else if(!dataSnapshot.child("Gender").getValue().toString().isEmpty() && dataSnapshot.child("Gender").getValue().toString().matches("Female"))
                        {
                            radioGroup.check(R.id.radiobuttonfemale);
                        }
                    }
                    catch (NullPointerException ec)
                    {
                        Log.d("Error",ec.getMessage());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
        catch (NullPointerException e)
        {
            Log.d("Error",e.getMessage());
        }


    }
}

