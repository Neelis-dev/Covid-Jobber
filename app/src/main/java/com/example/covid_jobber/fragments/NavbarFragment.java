package com.example.covid_jobber.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

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
public class NavbarFragment extends Fragment implements View.OnClickListener{
    private FragmentNavbarBinding binding;
    private MainActivity mainActivity;

    public NavbarFragment() {
        // Required empty public constructor
    }

    public static NavbarFragment newInstance() {
        return new NavbarFragment();
    }

    private static void setButtonPressed(ImageButton[] buttons,ImageButton pressedButton){
        for (ImageButton button:buttons) {
            button.setSelected(false);
        }
        pressedButton.setSelected(true);
    }

//        In fragments use OnCreateView() instead of OnCreate() for binding
    @Override
    public View onCreateView(@NotNull LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentNavbarBinding.inflate(inflater, container, false);



//        Search Button is selected when app is started
        binding.btnNavbarSearch.setSelected(true);

        mainActivity = (MainActivity) getActivity();

        binding.btnNavbarSearch.setOnClickListener(this);
        binding.btnNavbarFavorites.setOnClickListener(this);
        binding.btnNavbarFilters.setOnClickListener(this);
        binding.btnNavbarSettings.setOnClickListener(this);

        return binding.getRoot();
    }


    @Override
    public void onClick(View v) {
        ImageButton[] buttons = {binding.btnNavbarSearch, binding.btnNavbarFavorites, binding.btnNavbarFilters, binding.btnNavbarSettings};
        if(v == binding.btnNavbarSearch){
            mainActivity.changeToSwipe();
            setButtonPressed(buttons, binding.btnNavbarSearch);
        }
        else if(v == binding.btnNavbarFavorites){
            mainActivity.changeToFavorites();
            setButtonPressed(buttons, binding.btnNavbarFavorites);
        }
        else if(v == binding.btnNavbarFilters){
            mainActivity.changeToFilters();
            setButtonPressed(buttons, binding.btnNavbarFilters);
        }
        else if(v == binding.btnNavbarSettings){
            mainActivity.changeToSettings();
            setButtonPressed(buttons, binding.btnNavbarSettings);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }


    //    Resets binding, if view is destroyed
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}