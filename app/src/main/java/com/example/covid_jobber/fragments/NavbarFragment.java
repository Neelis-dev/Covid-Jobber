package com.example.covid_jobber.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.covid_jobber.R;
import com.example.covid_jobber.activities.MainActivity;
import com.example.covid_jobber.databinding.FragmentNavbarBinding;
import com.example.covid_jobber.databinding.FragmentSwipeBinding;

import org.jetbrains.annotations.NotNull;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NavbarFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NavbarFragment extends Fragment {
    private FragmentNavbarBinding binding;
    private MainActivity mainActivity;

    public NavbarFragment() {
        // Required empty public constructor
    }

    public static NavbarFragment newInstance() {
        return new NavbarFragment();
    }

    //    In fragments use OnCreateView() instead of OnCreate() for binding
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNavbarBinding.inflate(inflater, container, false);

        mainActivity = (MainActivity) getActivity();

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(v == binding.btnNavbarSearch){
                    mainActivity.changeToSwipe();
                }
                else if(v == binding.btnNavbarFavorites){
                    mainActivity.changeToFavorites();
                }
                else if(v == binding.btnNavbarFilters){
                    mainActivity.changeToFilters();
                }
                else if(v == binding.btnNavbarProfile){
                    mainActivity.changeToProfile();
                }
            }
        };

        binding.btnNavbarSearch.setOnClickListener(listener);
        binding.btnNavbarFavorites.setOnClickListener(listener);
        binding.btnNavbarFilters.setOnClickListener(listener);
        binding.btnNavbarProfile.setOnClickListener(listener);

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();

//        Todo: Do something on start?
    }


    //    Resets binding, if view is destroyed
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}