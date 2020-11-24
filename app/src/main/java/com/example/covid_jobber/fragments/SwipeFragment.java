package com.example.covid_jobber.fragments;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.example.covid_jobber.classes.services.ApiHandler;
import com.example.covid_jobber.databinding.FragmentSwipeBinding;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SwipeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SwipeFragment extends Fragment {
    private FragmentSwipeBinding binding;

    private final ApiHandler apiHandler = new ApiHandler();

    public SwipeFragment() {
        // Required empty public constructor
        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("hello");
            }
        };


    }

    public static SwipeFragment newInstance() {
        return new SwipeFragment();
    }

//    In fragments use OnCreateView() instead of OnCreate() for binding
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSwipeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

//    OnStart() is after OnCreateView --> binding can be used
    @Override
    public void onStart() {
        super.onStart();

//        Todo: some real apiHandler action in beginning


//        Just some example to test that binding is working
        String exampleText = "example";
        binding.tvJobName.setText(exampleText);
    }

//    Resets binding, if view is destroyed
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}