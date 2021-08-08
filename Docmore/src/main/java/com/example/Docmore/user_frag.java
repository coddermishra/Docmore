package com.example.Docmore;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;

public class user_frag extends AppCompatActivity {
    FirebaseAuth auth;
    TabLayout tabLayout;
    FrameLayout frameLayout;
    Fragment fragment=null;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FloatingActionButton fob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_frag);
        auth=FirebaseAuth.getInstance();
        tabLayout=findViewById(R.id.tabLayout);
        fragment=new user_doctor();
        fragmentManager=getSupportFragmentManager();
        fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout,fragment);
        fragmentTransaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        fragmentTransaction.commit();

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                FragmentManager fm = getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                switch (tab.getPosition()) {
                    case 0:

                        fragment = new user_doctor();
                        break;
                    case 1:

                        fragment = new user_hospital();
                        break;
                    case 2:

                        fragment = new user_prescription();
                        break;
                }

                ft.replace(R.id.frameLayout, fragment);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                fragment=null;

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });




    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.about:
                startActivity(new Intent(user_frag.this,Profile.class));
                return (true);
            case R.id.exit:
                signout();
                return (true);

        }
        return super.onOptionsItemSelected(item);



    }

    private void signout() {
        auth.signOut();
        startActivity(new Intent(user_frag.this,MainActivity.class));
        finish();
    }
    }