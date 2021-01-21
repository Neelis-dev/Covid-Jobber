package com.example.covid_jobber.fragments;

import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.covid_jobber.R;
import com.example.covid_jobber.databinding.FragmentSettingsBinding;
import com.example.covid_jobber.enums.DarkMode;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentSettingsBinding binding;

//    variables
//    chosen options
    private DarkMode darkMode;

//    available options
    private final List<DarkMode> darkModeOptions = new ArrayList<>(Arrays.asList(DarkMode.SYSTEM, DarkMode.OFF, DarkMode.ON));

    public SettingsFragment() {
        // Required empty public constructor
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentSettingsBinding.inflate(inflater, container, false);

//        Dark Mode
        binding.spinnerSettingsDarkmode.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, darkModeOptions));

        int initialSelectedPosition = binding.spinnerSettingsDarkmode.getSelectedItemPosition();
        binding.spinnerSettingsDarkmode.setSelection(initialSelectedPosition, true);
        detectDarkMode();
        if (darkMode != null) {
            binding.spinnerSettingsDarkmode.setSelection(darkModeOptions.indexOf(darkMode));
        }
        binding.spinnerSettingsDarkmode.setOnItemSelectedListener(this);

        return binding.getRoot();
    }


//    Checks Default App Dark Mode Settings and sets nightMode enum that way
    private void detectDarkMode(){
        int selectedMode = AppCompatDelegate.getDefaultNightMode();
        switch (selectedMode){
            case AppCompatDelegate.MODE_NIGHT_YES:
                darkMode = DarkMode.ON; break;
            case AppCompatDelegate.MODE_NIGHT_NO:
                darkMode = DarkMode.OFF; break;
            case AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM:
                darkMode = DarkMode.SYSTEM; break;
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent == binding.spinnerSettingsDarkmode){
            darkMode = (DarkMode) binding.spinnerSettingsDarkmode.getSelectedItem();
            darkMode.setMode();
            System.out.println("dark mode: "+darkMode.toString());
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if(parent == binding.spinnerSettingsDarkmode){
            darkMode = (DarkMode) binding.spinnerSettingsDarkmode.getSelectedItem();
            darkMode.setMode();
            System.out.println("dark mode: "+darkMode.toString());
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
