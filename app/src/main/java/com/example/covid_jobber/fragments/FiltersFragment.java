package com.example.covid_jobber.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.covid_jobber.R;
import com.example.covid_jobber.databinding.FragmentFiltersBinding;
import com.example.covid_jobber.databinding.FragmentSettingsBinding;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FiltersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FiltersFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private FragmentFiltersBinding binding;

    // Variables
    private boolean filtersActive;
    private String category;

    private Map<String, String> categoryMap = new HashMap<>();
    private List<String> keyList;

    public FiltersFragment() {
        // Required empty public constructor
        filtersActive = false;
    }

    public static FiltersFragment newInstance() {
        return new FiltersFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFiltersBinding.inflate(inflater, container, false);

        binding.switchProfileToggle.setOnClickListener(this);
        binding.spinnerProfileCategory.setOnItemSelectedListener(this);

        keyList = new ArrayList<>(categoryMap.keySet());
        binding.spinnerProfileCategory.setAdapter(new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line, keyList));

        if (category != null) {
            binding.spinnerProfileCategory.setSelection(keyList.indexOf(category));
        }
        binding.switchProfileToggle.setChecked(filtersActive);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        System.out.println("click");

        if (v.getId() == R.id.switch_profile_toggle) {
            filtersActive = binding.switchProfileToggle.isChecked();
            System.out.println(filtersActive);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        category = categoryMap.get(binding.spinnerProfileCategory.getSelectedItem().toString());
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        category = categoryMap.get(binding.spinnerProfileCategory.getSelectedItem().toString());
    }

    public void setCategories(Map<String, String> categoryMap) {
        this.categoryMap = categoryMap;
    }

    public String getCategoryFilter() {
        return category;
    }

    public boolean getFiltersActive() {
        return filtersActive;
    }

}


