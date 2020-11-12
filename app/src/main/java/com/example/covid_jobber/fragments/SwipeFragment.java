package com.example.covid_jobber.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.covid_jobber.R;
import com.example.covid_jobber.classes.ApiHandler;
import com.example.covid_jobber.databinding.FragmentSwipeBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SwipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwipeFragment extends Fragment {

    private ApiHandler apiHandler = new ApiHandler();

    public FragmentSwipeBinding fragmentSwipeBinding;

    public SwipeFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static SwipeFragment newInstance() {
        SwipeFragment fragment = new SwipeFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        apiHandler.forUI();

        this.fragmentSwipeBinding = FragmentSwipeBinding.inflate(getLayoutInflater());
        this.fragmentSwipeBinding.tvJobDescription.setText("Blabla");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_swipe, container, false);
    }
}