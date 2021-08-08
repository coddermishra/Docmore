package com.example.hospitaltest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import de.hdodenhof.circleimageview.CircleImageView;

import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Pattern;

public class Doctor_profile extends AppCompatActivity {
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
CircleImageView docprofileimage;
EditText doc_pro_name,doc_pro_spec,doc_pro_mobile;
private String docname_str,docspec_str,docmobile_str,docstime_str,docetime_str;
TextView doc_start_time,doc_end_time;
Spinner spinner_from,spinner_to;
Button mystarttime,myendtime,saveprofile;
String time,day_from,day_to;
    ArrayAdapter<String> arrayAdapter;
    private static final int PICK_IMAGE_REQUEST =73 ;
    Uri filePath;
int fromhour,tohour,frommin,tomin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        auth=FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        button_listener();
        mysetspinner();
        get_data();

    }

    private void button_listener() {
        doc_pro_name=findViewById(R.id.editText4);
        doc_pro_spec=findViewById(R.id.editText7);
        doc_pro_mobile=findViewById(R.id.editText6);
        doc_start_time=findViewById(R.id.textView23);
        doc_end_time=findViewById(R.id.textView24);
        spinner_to=findViewById(R.id.spinner);
        spinner_from=findViewById(R.id.spinner2);
        mystarttime=findViewById(R.id.button8);
        myendtime=findViewById(R.id.button10);
        saveprofile=findViewById(R.id.button9);
        docprofileimage=findViewById(R.id.circleImageView2);
        docprofileimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Profile picture"),PICK_IMAGE_REQUEST);
            }
        });
        saveprofile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isValidMobile(docmobile_str)) {
                    create_db();
                    upload_image();
                } else {
                    Toast.makeText(Doctor_profile.this, "Enter correct mobile number", Toast.LENGTH_SHORT).show();
                }
            }

        });

        mystarttime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Calendar c = Calendar.getInstance();
                fromhour = c.get(Calendar.HOUR_OF_DAY);
                frommin = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Doctor_profile.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                doc_start_time.setText(hourOfDay + ":" + minute);
                                docstime_str=hourOfDay + ":" + minute;
                            }
                        }, fromhour, frommin, false);
                timePickerDialog.show();



            }
        });
        myendtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar c = Calendar.getInstance();
                fromhour = c.get(Calendar.HOUR_OF_DAY);
                frommin = c.get(Calendar.MINUTE);
                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(Doctor_profile.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                doc_end_time.setText(hourOfDay + ":" + minute);
                                docetime_str=hourOfDay + ":" + minute;
                            }
                        }, fromhour, frommin, false);
                timePickerDialog.show();

            }
        });

    }

    private void mysetspinner() {
        List<String> list=new ArrayList<String>();
        list.add("Monday");
        list.add("Tuesday");
        list.add("Wednesday");
        list.add("Thursday");list.add("Friday");list.add("Saturday");list.add("Sunday");
        arrayAdapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,list);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_from.setAdapter(arrayAdapter);
        spinner_to.setAdapter(arrayAdapter);

        spinner_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day_to=parent.getItemAtPosition(position).toString();
                //Toast.makeText(Doctor_profile.this, "Selected day 2"+day_to, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                day_from=parent.getItemAtPosition(position).toString();
                //Toast.makeText(Doctor_profile.this, "Selected day"+day_from, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
    private void validation()
    {
        try {
            docname_str = doc_pro_name.getText().toString();
            docspec_str = doc_pro_spec.getText().toString();
            docmobile_str = doc_pro_mobile.getText().toString();
            if (!docname_str.isEmpty() && !docspec_str.isEmpty() && isValidMobile(docmobile_str)) {
                if (!docstime_str.isEmpty() && !docetime_str.isEmpty() && !day_from.isEmpty() && !day_to.isEmpty()) {
                    create_db();
                    upload_image();
                }
                Toast.makeText(this, "Please enter the Details", Toast.LENGTH_SHORT).show();
            }
            Toast.makeText(this, "Please enter the Details.", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {

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
                docprofileimage.setImageBitmap(bitmap);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }



    }


    private void create_db() {
        String uid=auth.getUid();
        DatabaseReference databaseReference1= FirebaseDatabase.getInstance().getReference();
        DatabaseReference Uid_ref=databaseReference1.child("Doctor").child(uid);
        Uid_ref.child("Doctor_name").setValue(docname_str);
        Uid_ref.child("Speciality").setValue(docspec_str);
        Uid_ref.child("Mobile_number").setValue(docmobile_str);
        Uid_ref.child("Time").setValue(docstime_str+" - "+docetime_str);
        Uid_ref.child("Day").setValue(day_to+" - "+day_from);
        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();

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
                            Toast.makeText(Doctor_profile.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(Doctor_profile.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
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
    private boolean isValidMobile(String phone) {
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            return phone.length() > 6 && phone.length() <= 13;
        }
        Toast.makeText(this, "Please check the mobile number", Toast.LENGTH_SHORT).show();
        return false;

    }
    private void get_data() {

        String uid=auth.getUid();
        databaseReference=FirebaseDatabase.getInstance().getReference("Doctor");
        DatabaseReference uid_ref=databaseReference.child(uid);
        storageReference=storage.getReference();

        storageReference.child("images/"+auth.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Glide.with(Doctor_profile.this).load(uri).into(docprofileimage);
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
                    doc_pro_name.setText(dataSnapshot.child("Doctor_name").getValue().toString());
                    doc_pro_mobile.setText(dataSnapshot.child("Mobile_number").getValue().toString());
                    doc_pro_spec.setText(dataSnapshot.child("Speciality").getValue().toString());
                    String time = dataSnapshot.child("Time").getValue().toString();
                    String[] time2 = time.split(" - ", 5);
                    doc_start_time.setText(time2[0]);
                    doc_end_time.setText(time2[1]);
                    String day = dataSnapshot.child("Day").getValue().toString();
                    String[] day2 = day.split(" - ", 3);
                    spinner_from.setSelection(arrayAdapter.getPosition(day2[0]));
                    spinner_to.setSelection(arrayAdapter.getPosition(day2[1]));
                }
                catch (NullPointerException e)
                {

                }
                catch (Exception ex)
                {

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
