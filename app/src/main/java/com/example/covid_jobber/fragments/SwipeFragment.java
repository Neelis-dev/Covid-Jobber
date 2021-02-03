package com.example.covid_jobber.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.example.covid_jobber.R;
import com.example.covid_jobber.activities.MainActivity;
import com.example.covid_jobber.classes.Job;
import com.example.covid_jobber.classes.services.ApiCall;
import com.example.covid_jobber.classes.services.arrayAdapter;
import com.example.covid_jobber.databinding.FragmentSwipeBinding;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SwipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwipeFragment extends Fragment {

    private MainActivity mainActivity;
    private FragmentSwipeBinding binding;
    private arrayAdapter arrayAdapter;

    private int pageNumber = 1;

    ListView listView;
    List<Job> jobitems;


    public SwipeFragment() {
        // Required empty public constructor
    }

    public static SwipeFragment newInstance() {
        return new SwipeFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentSwipeBinding.inflate(inflater, container, false);
        mainActivity = (MainActivity) getActivity();

        checkInternet();

        jobitems = new ArrayList<>();
        jobitems.add(new Job());

        arrayAdapter = new arrayAdapter(getActivity(), R.layout.item, jobitems, mainActivity);

        SwipeFlingAdapterView flingContainer = binding.frame;

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                jobitems.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
            }

            @Override
            public void onRightCardExit(Object dataObject) {
                Job favorite = (Job) dataObject;
                if(favorite != null && favorite.getId() != -1){
                    if(!mainActivity.findFavoriteJob(favorite.getId())){
                        mainActivity.addFavoriteJob(favorite);
                    }
                }
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                if(itemsInAdapter>=3){
                    return;
                }


                Log.d("TAG", "CARDS ABOUT TO RUN OUT");
                mainActivity.getHandler().makeApiCall(new ApiCall(mainActivity.getFilterFragment().getFilter(),getPageNumberAndIncrease()) {
                    @Override
                    public void callback(JSONArray results) {
                        try {
                            mainActivity.resultsToJobs(results);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, mainActivity);


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



        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void addJob(List<Job> newJobs) {
        jobitems.addAll(newJobs);
    }

    public void setPageNumber(int n){
        pageNumber = n;
    }

    public int getPageNumberAndIncrease(){
        return pageNumber++;
    }

    public String getCurrentUrl(){
        return jobitems.get(0).getUrl();
    }

    public void updateArrayAdapter(){
        arrayAdapter.notifyDataSetChanged();
    }

    private void checkInternet(){
        ConnectivityManager cm = (ConnectivityManager) mainActivity.getSystemService(Context.CONNECTIVITY_SERVICE);
        boolean connected = cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();

        if(!connected){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            String message = "";
            switch (mainActivity.language){
                case GERMAN:
                    message = "Bitte überprüfe deine Internetverbindung! Ohne Internetzugang können leider keine Job-Angebote geladen werden und die App funktioniert nicht ordnungsgemäß."; break;
                case ENGLISH:
                    message = "Please check your internet connection! Without internet access no job offers can be loaded and the app does not function properly."; break;
            }
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Ok", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

}