package com.example.covid_jobber.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.covid_jobber.R;
import com.example.covid_jobber.databinding.FragmentFiltersBinding;
import com.example.covid_jobber.databinding.FragmentProfileBinding;

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
public class FiltersFragment extends Fragment implements View.OnClickListener {

    private FragmentFiltersBinding binding;

    // Variables
    private boolean filtersActive;
    private Map<String,String> categoryMap = new HashMap<>();

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

        binding.switchToggleFilters.setOnClickListener(this);
        binding.spinnerCategory.setAdapter(new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line, new ArrayList<>(categoryMap.keySet())));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        System.out.println(categoryMap.get(binding.spinnerCategory.getSelectedItem()));
        if (v.getId() == R.id.switch_toggleFilters) {
            filtersActive = binding.switchToggleFilters.isChecked();
            System.out.println(filtersActive);
        }
    }

    public void setCategories(Map<String,String> categoryMap) {
        this.categoryMap = categoryMap;
    }

    public String getCategoryFilter(){
        return categoryMap.get(binding.spinnerCategory.getSelectedItem());
    }
    public boolean getFiltersActive(){
        return filtersActive;
    }
}