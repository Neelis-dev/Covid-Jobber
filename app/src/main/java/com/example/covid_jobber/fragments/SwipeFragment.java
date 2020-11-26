package com.example.covid_jobber.fragments;


import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;


import com.example.covid_jobber.R;

import com.example.covid_jobber.databinding.FragmentProfileBinding;
import com.example.covid_jobber.databinding.FragmentSwipeBinding;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SwipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwipeFragment extends Fragment {
    private FragmentSwipeBinding fragmentSwipeBinding;

    private boolean wannasave = false;
    private ArrayList<String> saved;
    private ArrayList<String> al;
    private FragmentProfileBinding fragmentProfileBinding;

    public SwipeFragment() {
        // Required empty public constructor
    }

    public static SwipeFragment newInstance() {
        return new SwipeFragment();
    }

//    In fragments use OnCreateView() instead of OnCreate() for binding
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayAdapter<String> arrayAdapter;
        View v = inflater.inflate(R.layout.fragment_swipe, container, false);

        al = new ArrayList<>();
        al.add("Bäcker");
        al.add("Zahnarzt helfer/in");
        al.add("Data Scientist");
        al.add("Bizness Analyst");
        al.add("Dualer Student");
        al.add("Fahrer");
        al.add("Abteilungsleiter");
        al.add("Manager");

        arrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.item, R.id.helloText, al );

        SwipeFlingAdapterView flingContainer = (SwipeFlingAdapterView) v.findViewById(R.id.frame);

        flingContainer.setAdapter(arrayAdapter);
        flingContainer.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                // this is the simplest way to delete an object from the Adapter (/AdapterView)
                Log.d("LIST", "removed object!");
                String job = al.remove(0);
                arrayAdapter.notifyDataSetChanged();
            }

            @Override
            public void onLeftCardExit(Object dataObject) {
            }

            @Override
            public void onRightCardExit(Object dataObject) {
            }

            @Override
            public void onAdapterAboutToEmpty(int itemsInAdapter) {
                // Ask for more data here
                al.add("Momentan nicht mehr Jobs verfügbar");
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
        fragmentProfileBinding = null;
        fragmentSwipeBinding = null;
    }
}