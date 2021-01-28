package com.example.covid_jobber.fragments;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.fragment.app.Fragment;

import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.covid_jobber.R;
import com.example.covid_jobber.activities.MainActivity;
import com.example.covid_jobber.databinding.FragmentSettingsBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.prefs.Preferences;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SettingsFragment extends Fragment implements AdapterView.OnItemSelectedListener {

    private FragmentSettingsBinding binding;
    private MainActivity mainActivity;

//    variables
//    chosen options in main activity because they effect the whole activity -> needed from start of app

//    available options
    private final List<String> darkModeOptions = new ArrayList<>(Arrays.asList("System", "Off", "On"));
    private final List<String> languageOptions = new ArrayList<>(Arrays.asList("English","Deutsch"));

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
        mainActivity = (MainActivity) getActivity();

//        Dark Mode
        binding.spinnerSettingsDarkmode.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, darkModeOptions));
        binding.spinnerSettingsDarkmode.setSelection(darkModeOptions.indexOf(mainActivity.darkMode));
        binding.spinnerSettingsDarkmode.setOnItemSelectedListener(this);

//        Language
        binding.spinnerSettingsLanguage.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, languageOptions));
        binding.spinnerSettingsLanguage.setSelection(languageOptions.indexOf(mainActivity.language));
        binding.spinnerSettingsLanguage.setOnItemSelectedListener(this);

        return binding.getRoot();
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent == binding.spinnerSettingsDarkmode){
            mainActivity.darkMode = (String) binding.spinnerSettingsDarkmode.getSelectedItem();
            mainActivity.setMode();
            System.out.println("dark mode: "+mainActivity.darkMode);
        }
        else if(parent == binding.spinnerSettingsLanguage){
            String prevLanguage = mainActivity.language;
            mainActivity.language = (String) binding.spinnerSettingsLanguage.getSelectedItem();
            mainActivity.setLanguage();
            System.out.println("language: "+mainActivity.language);
            if(!prevLanguage.equals(mainActivity.language)){
                mainActivity.getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if(parent == binding.spinnerSettingsDarkmode){
            mainActivity.darkMode = (String) binding.spinnerSettingsDarkmode.getSelectedItem();
            mainActivity.setMode();
            System.out.println("dark mode: "+mainActivity.darkMode);
        }
        else if(parent == binding.spinnerSettingsLanguage){
            String prevLanguage = mainActivity.language;
            mainActivity.language = (String) binding.spinnerSettingsLanguage.getSelectedItem();
            mainActivity.setLanguage();
            System.out.println("language: "+mainActivity.language);
            if(!prevLanguage.equals(mainActivity.language)){
                mainActivity.getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();
            }
        }
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
