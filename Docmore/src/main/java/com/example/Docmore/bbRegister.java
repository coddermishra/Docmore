package com.example.Docmore;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

class Register extends AppCompatActivity {
    private EditText nameEt,cityEt,mob_numberEt,bloodGroupEt,passwordEt;
    /**
     *
     */
    private Button submit_Button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_bbregister);
        nameEt=findViewById (R.id.name);
        cityEt=findViewById (R.id.city);
        mob_numberEt=findViewById (R.id.number);
        bloodGroupEt=findViewById (R.id.blood_group);
        passwordEt=findViewById (R.id.password);
        submit_Button=findViewById (R.id.submit_button);
        submit_Button.setOnClickListener (new View.OnClickListener ( ) {
            @Override
            public void onClick(View v) {
                String name,city,blood_group,number,password;
                name=nameEt.getText().toString();
                city=cityEt.getText().toString();
                number=mob_numberEt.getText().toString();
                blood_group=bloodGroupEt.getText().toString();
                password=passwordEt.getText().toString();

                showMessage (name+"\n"+city +"\n"+number+"" + "\n"+blood_group+"\n"+password);

                if (isValid(name,city,blood_group,password,number)){

                }

            }
        });
    }


    private boolean isValid(String name,String city,String blood_group,String password,String number){
        List<String> valid_blood_groups = new ArrayList<>();
        valid_blood_groups.add("A+");
        valid_blood_groups.add("A-");
        valid_blood_groups.add("B+");
        valid_blood_groups.add("B-");
        valid_blood_groups.add("AB+");
        valid_blood_groups.add("AB-");
        valid_blood_groups.add("O+");
        valid_blood_groups.add("O-");
        if(name.isEmpty()){
            showMessage("Name is empty");
            return false;
        }else if (city.isEmpty()){
            showMessage("City Name is empty");
            return false;
        }else if (!valid_blood_groups.contains(blood_group)){
            showMessage("Blood group invalid choose from"+valid_blood_groups);
            return false;
        }else if (number.isEmpty() || number.length()!=10){
            showMessage("Invalid mobile number, number should be of 10 digits");
            return false;
        }
        return true;

    }

    private void showMessage(String msg)
    {
        String name = " ";
        Toast.makeText (Register.this, name +" ",Toast.LENGTH_LONG).show ();
    }
}