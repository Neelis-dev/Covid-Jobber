package com.example.covid_jobber.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.covid_jobber.R;
import com.example.covid_jobber.databinding.ActivityMainBinding;
import com.example.covid_jobber.fragments.NavbarFragment;
import com.example.covid_jobber.fragments.ProfileFragment;
import com.example.covid_jobber.fragments.SwipeFragment;


public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding mainBinding;

    private final  SwipeFragment swipeFragment = new SwipeFragment();
    private final ProfileFragment profileFragment = new ProfileFragment();
    private final NavbarFragment navbarFragment = new NavbarFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);


        // check if content frame exists
        if(findViewById(R.id.content_frame) != null){
            // replace container with fragment object: swipeFragment
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.content_frame,swipeFragment).commitAllowingStateLoss();
        }

        changeToSwipe();

    }

    public void changeToSwipe(){
        // check if content frame exists
        if(findViewById(R.id.content_frame) != null){
            // replace container with fragment object
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.content_frame,swipeFragment).commitAllowingStateLoss();
        }
    }

    public void changeToProfile(){
        // check if content frame exists
        if(findViewById(R.id.content_frame) != null){
            // replace container with fragment object
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.content_frame,profileFragment).commitAllowingStateLoss();
        }
    }


}