package com.example.covid_jobber.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.View;

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

//        Content Frame at first Swipe View
        changeToSwipe();

//        Put Navbar Fragment in Navbar Frame
        replaceFrame(R.id.navbar_frame, navbarFragment);

    }

    public void changeToSwipe(){
        replaceFrame(R.id.content_frame, swipeFragment);
    }

    public void changeToProfile(){
        replaceFrame(R.id.content_frame, profileFragment);
    }

//    Replaces Frame with frameID with Fragment
    public void replaceFrame(int frameID, Fragment fragment){
        // check if content frame exists
        if(findViewById(frameID) != null){
            // replace container with fragment object
            getSupportFragmentManager().beginTransaction().
                    replace(frameID,fragment).commitAllowingStateLoss();
        }
    }


}