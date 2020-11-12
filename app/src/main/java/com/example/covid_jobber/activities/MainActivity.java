package com.example.covid_jobber.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.covid_jobber.R;
import com.example.covid_jobber.databinding.ActivityMainBinding;
import com.example.covid_jobber.fragments.NavbarFragment;
import com.example.covid_jobber.fragments.SwipeFragment;


public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding mainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);


        // check if content frame exists
        if(findViewById(R.id.content_frame) != null){
            // replace container with fragment object
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.content_frame,new SwipeFragment()).commitAllowingStateLoss();
        }

        // check if navbar frame exists
        if(findViewById(R.id.navbar_frame) != null){
            // replace container with fragment object
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.navbar_frame,new NavbarFragment()).commitAllowingStateLoss();
        }


    }


}