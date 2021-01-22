package com.example.covid_jobber.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.renderscript.ScriptGroup;
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
import com.example.covid_jobber.enums.ContractTime;
import com.google.android.gms.common.api.Api;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
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
//    chosen options
    private double expSalary = 1000;
    private ContractTime contractTime;
    private String category;

//    available options
    private final List<ContractTime> contractTimes = new ArrayList<>(Arrays.asList(ContractTime.EITHER, ContractTime.FULL_TIME, ContractTime.PART_TIME));
    private final Map<String, String> categoryMap = new HashMap<>();
    private List<View> editOptions;

    private boolean editing = false;

//    debug variables
    private boolean filtersActive = false;

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

//        Filter toggle
        binding.switchFilterToggle.setOnClickListener(this);
        binding.switchFilterToggle.setChecked(filtersActive);

//        Edit Button
        binding.btnFilterEdit.setOnClickListener(this);

//        Save Button
        binding.btnFilterSave.setOnClickListener(this);
        binding.btnFilterSave.setVisibility(View.INVISIBLE);

//        Salary Input
        binding.inputFilterSalary.setText(String.valueOf(expSalary));

//        Category Spinner
        binding.spinnerFilterCategory.setOnItemSelectedListener(this);

        List<String> keyList = new ArrayList<>(categoryMap.keySet());
        binding.spinnerFilterCategory.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_dropdown_item_1line, keyList));

        if (category != null) {
            binding.spinnerFilterCategory.setSelection(keyList.indexOf(category));
        }

//        Contract Time Spinner
        binding.spinnerFilterContractTime.setOnItemSelectedListener(this);

        binding.spinnerFilterContractTime.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_dropdown_item_1line, contractTimes));

        if (contractTime != null) {
            binding.spinnerFilterContractTime.setSelection(contractTimes.indexOf(contractTime));
        }

//        All options set to disabled if not in edit mode
        editOptions = new ArrayList<>(Arrays.asList(binding.inputFilterSalary, binding.spinnerFilterCategory, binding.spinnerFilterContractTime));
        for (View option:editOptions) {
            option.setEnabled(false);
        }

        return binding.getRoot();
    }

    @Override
    public void onStart() {
        if(editing){
            endEditing();
        }
        super.onStart();
    }

    @Override
    public void onDestroyView() {
//        binding = null;
        super.onDestroyView();
    }

//    Currently only used for toggle button
    @Override
    public void onClick(View v) {
        if (v == binding.switchFilterToggle) {
            filtersActive = binding.switchFilterToggle.isChecked();
            System.out.println("filters: "+filtersActive);
        }
        else if (v == binding.btnFilterEdit){
            startEditing();
        }
        else if (v == binding.btnFilterSave){
            endEditing();
        }
    }

//    enables edit mode -> activated by edit button
    private void startEditing(){
        editing = true;
        binding.btnFilterSave.setVisibility(View.VISIBLE);
        binding.btnFilterEdit.setEnabled(false);

//        Enable all edit options
        for (View option:editOptions) {
            option.setEnabled(true);
        }
    }

//    disables edit mode -> activated by save button
    private void endEditing(){
        editing = false;
        binding.btnFilterSave.setVisibility(View.INVISIBLE);
        binding.btnFilterEdit.setEnabled(true);

//        Disble all edit options
        for (View option:editOptions) {
            option.setEnabled(false);
        }

//        Save salary input value
        try{
            expSalary = Double.parseDouble(binding.inputFilterSalary.getText().toString());
        }
        catch (NumberFormatException e){
            System.out.println("Not a number was entered");
        }
        binding.inputFilterSalary.setText(String.valueOf(expSalary));

    }

//    currently only used for category spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent == binding.spinnerFilterCategory){
            category = categoryMap.get(binding.spinnerFilterCategory.getSelectedItem().toString());
            System.out.println("category chosen: "+category);
        }
        else if(parent == binding.spinnerFilterContractTime){
            contractTime = (ContractTime) binding.spinnerFilterContractTime.getSelectedItem();
            System.out.println("contract time chosen: "+contractTime.toString());
        }

    }

//    currently only used for category spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        category = categoryMap.get(binding.spinnerFilterCategory.getSelectedItem().toString());
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


