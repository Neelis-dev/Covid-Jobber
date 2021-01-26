package com.example.covid_jobber.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.example.covid_jobber.R;
import com.example.covid_jobber.activities.MainActivity;
import com.example.covid_jobber.classes.Job;
import com.example.covid_jobber.classes.services.ApiCall;
import com.example.covid_jobber.classes.services.Filter;
import com.example.covid_jobber.classes.services.FilterType;
import com.example.covid_jobber.databinding.FragmentFavoriteJobBinding;
import com.example.covid_jobber.databinding.FragmentNavbarBinding;

import com.google.android.gms.common.api.Api;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SwipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwipeFragment extends Fragment {

    private MainActivity mainActivity;

    private final boolean wannasave = false;
    private List<String> saved;

    private List<Job> jobs;
    private List<String> titles; // Renamed al in titles, because currently only titles used for card creation TODO: later on directly from jobs Array?

    public SwipeFragment() {
        // Required empty public constructor
    }

    public static SwipeFragment newInstance() {
        return new SwipeFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayAdapter<String> arrayAdapter;
        View v = inflater.inflate(R.layout.fragment_swipe, container, false);

        mainActivity = (MainActivity) getActivity();

        jobs = new ArrayList<>();
        titles = new ArrayList<>();
        titles.add("Start swiping!");

        arrayAdapter = new ArrayAdapter(getActivity(), R.layout.item, R.id.helloText, titles);

        SwipeFlingAdapterView flingContainer = v.findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                String title = titles.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                String title = (String) dataObject;
                Job favorite = getJobByTitle(title);
                if(favorite != null){
                    mainActivity.addFavoriteJob(favorite);
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {

                if(itemsInAdapter!=3){
                   return;
                }

                Log.d("TAG", "CARDS ABOUT TO RUN OUT");

                mainActivity.getHandler().makeApiCall(new ApiCall(mainActivity.getFilterFragment().getFilter()) {
                    @Override
                    public void callback(JSONArray results) {
                        try {
                            mainActivity.resultsToJobs(results);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });


                arrayAdapter.notifyDataSetChanged();
                Log.d("LIST", "notified");
            }

            //Durch die Farbänderung stürzt die App bisher manchmal ab
            @Override
            public void onScroll(float scrollProgressPercent) {
                View view = flingContainer.getSelectedView();
                if(view == null){
                    return;
                }
                view.findViewById(R.id.item_swipe_right_indicator).setAlpha(scrollProgressPercent < 0 ? -scrollProgressPercent : 0);
                view.findViewById(R.id.item_swipe_left_indicator).setAlpha(scrollProgressPercent > 0 ? scrollProgressPercent : 0);
            }
        });


        // Optionally add an OnItemClickListener
        flingContainer.setOnItemClickListener(new SwipeFlingAdapterView.OnItemClickListener() {
            @Override
            public void onItemClicked(int itemPosition, Object dataObject) {
            }
        });

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void addJob(List<Job> newJobs) {
        jobs.addAll(newJobs);
        for (Job j:newJobs) {
            titles.add(j.getTitle());
        }
    }

    private Job getJobByTitle(String title){
        Job job = null;
        for (Job j:jobs) {
            if(j.getTitle().equals(title)){
                job = j;
            }
        }
        return job;
    }

}
