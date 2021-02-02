package com.example.covid_jobber.fragments;

import android.content.Intent;
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
import com.example.covid_jobber.enums.DarkMode;
import com.example.covid_jobber.enums.Language;

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
    private final List<DarkMode> darkModeOptions = new ArrayList<>(Arrays.asList(DarkMode.SYSTEM, DarkMode.ON, DarkMode.OFF));
    private final List<Language> languageOptions = new ArrayList<>(Arrays.asList(Language.ENGLISH, Language.GERMAN));

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

        binding.btnSettingsRecommend.setOnClickListener(this);

//        Dark Mode
        List<String> translatedDarkModes = new ArrayList<>();
        for (DarkMode m:darkModeOptions) {
            translatedDarkModes.add(m.getTranslation(mainActivity.language));
        }
        binding.spinnerSettingsDarkmode.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, translatedDarkModes));
        binding.spinnerSettingsDarkmode.setSelection(translatedDarkModes.indexOf(mainActivity.darkMode.getTranslation(mainActivity.language)));
        binding.spinnerSettingsDarkmode.setOnItemSelectedListener(this);

//        Language
        List<String> translatedLanguages = new ArrayList<>();
        for (Language l:languageOptions) {
            translatedLanguages.add(l.getTranslation(mainActivity.language));
        }
        binding.spinnerSettingsLanguage.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_spinner_dropdown_item, translatedLanguages));
        binding.spinnerSettingsLanguage.setSelection(translatedLanguages.indexOf(mainActivity.language.getTranslation(mainActivity.language)));
        binding.spinnerSettingsLanguage.setOnItemSelectedListener(this);

        return binding.getRoot();
    }




    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent == binding.spinnerSettingsDarkmode){
            mainActivity.darkMode = DarkMode.getByTranslation((String) binding.spinnerSettingsDarkmode.getSelectedItem());
            mainActivity.setMode(mainActivity.getPrefs());
            System.out.println("dark mode: "+mainActivity.darkMode.toString());
        }
        else if(parent == binding.spinnerSettingsLanguage){
            Language prevLanguage = mainActivity.language;
            mainActivity.language = Language.getByTranslation((String) binding.spinnerSettingsLanguage.getSelectedItem());
            mainActivity.setLanguage(mainActivity.getPrefs());
            System.out.println("language: "+mainActivity.language.toString());
            if(!prevLanguage.toString().equals(mainActivity.language.toString())){
                mainActivity.getSupportFragmentManager().beginTransaction().detach(this).attach(this).commit();
            }
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if(parent == binding.spinnerSettingsDarkmode){
            mainActivity.darkMode = DarkMode.getByTranslation((String) binding.spinnerSettingsDarkmode.getSelectedItem());
            mainActivity.setMode(mainActivity.getPrefs());
            System.out.println("dark mode: "+mainActivity.darkMode.toString());
        }
        else if(parent == binding.spinnerSettingsLanguage){
            Language prevLanguage = mainActivity.language;
            mainActivity.language = Language.getByTranslation((String) binding.spinnerSettingsLanguage.getSelectedItem());
            mainActivity.setLanguage(mainActivity.getPrefs());
            System.out.println("language: "+mainActivity.language.toString());
            if(!prevLanguage.toString().equals(mainActivity.language.toString())){
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
