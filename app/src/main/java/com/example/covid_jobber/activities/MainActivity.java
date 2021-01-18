package com.example.covid_jobber.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;

import com.example.covid_jobber.R;
import com.example.covid_jobber.classes.Job;
import com.example.covid_jobber.classes.services.ApiCall;
import com.example.covid_jobber.classes.services.ApiHandler;
import com.example.covid_jobber.databinding.ActivityMainBinding;
import com.example.covid_jobber.fragments.FavoritesFragment;
import com.example.covid_jobber.fragments.FiltersFragment;
import com.example.covid_jobber.fragments.NavbarFragment;
import com.example.covid_jobber.fragments.SettingsFragment;
import com.example.covid_jobber.fragments.SwipeFragment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;


public class MainActivity extends AppCompatActivity {
    public ActivityMainBinding mainBinding;
    private final ApiHandler handler = new ApiHandler();
    private final  SwipeFragment swipeFragment = new SwipeFragment();
    private final FavoritesFragment favoritesFragment = new FavoritesFragment();
    private final  FiltersFragment filtersFragment = new FiltersFragment(); // vllt autofilter
    private final SettingsFragment settingsFragment = new SettingsFragment();
    private final NavbarFragment navbarFragment = new NavbarFragment();


    private final MainActivity instance = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.mainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);
        refreshCategories();
        handler.makeApiCall(new ApiCall() {
            @Override
            public void callback(JSONArray results) {
                try {
                    resultsToJobs(results);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

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
    public void changeToSettings(){
        replaceFrame(R.id.content_frame, settingsFragment);
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

        //  make Jobs into cards
        List<String> jobtitles = new ArrayList<>();
        for (Job j:jobs) {
            jobtitles.add(j.getTitle());
        }
        swipeFragment.addToList(jobtitles);
    }

    public void refreshCategories() {
        Map<String,String> categoryMap = new HashMap<>();
        handler.makeApiCall(new ApiCall(new Request.Builder().url("https://api.adzuna.com/v1/api/jobs/de/categories?app_id=64fa1822&app_key=d41a9537116b72a1c2a890a27376d552").build()) {
            @Override
            public void callback(JSONArray results) {
                for(int i=0; i<results.length();i++){
                    try {
                        categoryMap.put(results.getJSONObject(i).getString("label"), results.getJSONObject(i).getString("tag"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                filtersFragment.setCategories(categoryMap);
            }
        });

    }

    public String getCategoryFilter(){
        return filtersFragment.getCategoryFilter();
    }

    public ApiHandler getHandler(){
        return handler;
    }

//    public FilterOptions getFilterOptions(){
//        return filterOptions;
//    }


}