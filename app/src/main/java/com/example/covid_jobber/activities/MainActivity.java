package com.example.covid_jobber.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;

import com.example.covid_jobber.R;
import com.example.covid_jobber.classes.Job;
import com.example.covid_jobber.classes.services.ApiHandler;
import com.example.covid_jobber.databinding.ActivityMainBinding;
import com.example.covid_jobber.fragments.FavoritesFragment;
import com.example.covid_jobber.fragments.FiltersFragment;
import com.example.covid_jobber.fragments.NavbarFragment;
import com.example.covid_jobber.fragments.ProfileFragment;
import com.example.covid_jobber.fragments.SwipeFragment;

import java.sql.SQLOutput;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;


public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding mainBinding;
    private ApiHandler handler = new ApiHandler();
    private final  SwipeFragment swipeFragment = new SwipeFragment();
    private final FavoritesFragment favoritesFragment = new FavoritesFragment();
    private final  FiltersFragment filtersFragment = new FiltersFragment();
    private final ProfileFragment profileFragment = new ProfileFragment();
    private final NavbarFragment navbarFragment = new NavbarFragment();


    private final MainActivity instance = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);

        try {
            handler.makeApiCall(this);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        At first Swipe Fragment in Content Frame
        replaceFrame(R.id.content_frame, swipeFragment);

//        Put Navbar Fragment in Navbar Frame
        replaceFrame(R.id.navbar_frame, navbarFragment);

    }

    public void changeToSwipe(){
        replaceFrame(R.id.content_frame, swipeFragment);
    }
    public void changeToFavorites(){
        replaceFrame(R.id.content_frame, favoritesFragment);
    }
    public void changeToFilters(){
        replaceFrame(R.id.content_frame, filtersFragment);
    }
    public void changeToProfile(){
        replaceFrame(R.id.content_frame, profileFragment);
    }

//    Replaces Frame with frameID with Fragment
    public void replaceFrame(int frameID, Fragment fragment){
        // check if content frame exists
        if(findViewById(frameID) != null){
//            Check if this Fragment is already in Frame
            if(this.getSupportFragmentManager().findFragmentById(frameID) != fragment){
                // replace container with fragment object
                getSupportFragmentManager().beginTransaction().replace(frameID,fragment).commitAllowingStateLoss();
                System.out.println("Changed Fragment");
            }
        }

    }

    public void resultsToJobs(JSONArray results) throws JSONException {
        List<Job> jobs = new ArrayList<>();
        for(int i = 0; i< results.length(); i++){
            jobs.add(new Job((JSONObject) results.get(i)));
        }

        // TODO: make Jobs into cards
        List<String> jobtitles = new ArrayList<>();
        for (Job j:jobs) {
            jobtitles.add(j.getTitle());
        }
        swipeFragment.addToList(jobtitles);



    }

//    public FilterOptions getFilterOptions(){
//        return filterOptions;
//    }


}