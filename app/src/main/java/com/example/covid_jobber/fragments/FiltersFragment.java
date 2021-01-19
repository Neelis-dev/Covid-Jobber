package com.example.covid_jobber.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.covid_jobber.R;
import com.example.covid_jobber.classes.services.ApiCall;
import com.example.covid_jobber.classes.services.ApiHandler;
import com.example.covid_jobber.classes.services.Filter;
import com.example.covid_jobber.classes.services.FilterType;
import com.example.covid_jobber.databinding.FragmentFiltersBinding;
import com.example.covid_jobber.databinding.FragmentSettingsBinding;
import com.google.android.gms.common.api.Api;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FiltersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FiltersFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private FragmentFiltersBinding binding;

    // Variables
    private boolean filtersActive=false;
    private String category;

    private Map<String, String> categoryMap = new HashMap<>();
    private List<String> keyList;

    public FiltersFragment() {
        // Required empty public constructor
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

    public void fillCategorySpinner(){
        ApiHandler handler= new ApiHandler();
        handler.makeApiCall(new ApiCall(new Request.Builder().url("https://api.adzuna.com/v1/api/jobs/de/categories?app_id=64fa1822&app_key=d41a9537116b72a1c2a890a27376d552").build()) {
            @Override
            public void callback(JSONArray results) {
                for(int i=0; i<results.length();i++){
                    try {
                        categoryMap.put(results.getJSONObject(i).getString("label"), results.getJSONObject(i).getString("tag"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    public Filter getFilter(){
        if(!filtersActive){
            return new Filter();
        }

        Filter filter = new Filter();
        filter.addFilter(FilterType.CATEGORY,category);
        return filter;
    }

}


