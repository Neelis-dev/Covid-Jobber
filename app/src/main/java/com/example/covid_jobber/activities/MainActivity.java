package com.example.covid_jobber.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.covid_jobber.R;
import com.example.covid_jobber.databinding.ActivityMainBinding;
import com.example.covid_jobber.fragments.SwipeFragment;


public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding mainBinding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);


        // check if card display container exists
        if(findViewById(R.id.content_fragment) != null){
            // replace container with fragment object
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.content_fragment,new SwipeFragment()).commitAllowingStateLoss();
        }


    }


}