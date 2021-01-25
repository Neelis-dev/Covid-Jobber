package com.example.covid_jobber.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.example.covid_jobber.R;
import com.example.covid_jobber.activities.MainActivity;
import com.example.covid_jobber.classes.Applicant;
import com.example.covid_jobber.classes.services.ApiCall;
import com.example.covid_jobber.classes.services.ApiHandler;
import com.example.covid_jobber.classes.services.Filter;
import com.example.covid_jobber.classes.services.FilterType;
import com.example.covid_jobber.databinding.FragmentFiltersBinding;
import com.example.covid_jobber.databinding.FragmentSettingsBinding;
import com.example.covid_jobber.enums.ContractTime;
import com.google.android.gms.common.api.Api;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

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
    private int surrounding;
    private double latitude;
    private double longitude;

//    available options
    private final List<ContractTime> contractTimes = new ArrayList<>(Arrays.asList(ContractTime.EITHER, ContractTime.FULL_TIME, ContractTime.PART_TIME));
    private final List<Integer> surroundingList = new ArrayList<>(Arrays.asList(5, 10, 25, 75, 150));
    private final Map<String,String> contractTimeMap = new HashMap<>();
    private final Map<String, String> categoryMap = new HashMap<>();
    private List<View> editOptions;

    private boolean editing = false;

//    debug variables
    private boolean filtersActive = false;
    private boolean permissionActive = false;

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

//        Permission toggle
        binding.btnFilterPermission.setOnClickListener(this);
        binding.btnFilterPermission.setChecked(permissionActive);

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
        contractTimeMap.put("Vollzeit","full_time");
        contractTimeMap.put("Teilzeit","part_time");
        contractTimeMap.put("Beliebig","-");
        if (contractTime != null) {
            binding.spinnerFilterContractTime.setSelection(contractTimes.indexOf(contractTime));
        }

//        Surrounding Spinner
        binding.spinnerFilterSurrounding.setOnItemSelectedListener(this);

        binding.spinnerFilterSurrounding.setAdapter(new ArrayAdapter<>(getActivity().getApplicationContext(), android.R.layout.simple_dropdown_item_1line, surroundingList));

//        All options set to disabled if not in edit mode
        editOptions = new ArrayList<>(Arrays.asList(binding.inputFilterSalary, binding.spinnerFilterCategory, binding.spinnerFilterContractTime, binding.btnFilterPermission,  binding.spinnerFilterSurrounding));
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
            System.out.println("filters: " + filtersActive);
        } else if (v == binding.btnFilterEdit) {
            startEditing();
        } else if (v == binding.btnFilterSave) {
            endEditing();
        } else if (v == binding.btnFilterPermission) {
            permissionActive = binding.btnFilterPermission.isChecked();
            System.out.println("permission: " + permissionActive);
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                updateLocation();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            updateLocation();
        }
        //evtl. ein else um zu erklären, dass nicht auf die Örtlichkeit gefiltert werden kann
    }

    @SuppressLint("MissingPermission")
    public void updateLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this::onLocationReceived);
    }

    public void onLocationReceived(Location location){
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        System.out.println(latitude + " " + longitude);
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

        MainActivity mainActivity = (MainActivity) getActivity();
        //get new cards based on new filter
        mainActivity.getHandler().makeApiCall(new ApiCall(mainActivity.getFilterFragment().getFilter()) {
            @Override
            public void callback(JSONArray results) {
                try {
                    mainActivity.resultsToJobs(results);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

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
        filter.addFilter(FilterType.CONTENT,contractTimeMap.get(contractTime.toString()));
        filter.addFilter(FilterType.CONTENT,category);

        return filter;
    }


//      Berechnung zur Bestimmung ob ein Ort aus der API innerhalb des ausgewählten Umkreises des Users liegt
//      lat1 und lon 1 sind die Koordinaten des Jobs, lat2 und lon2 sind die Koordinaten des Users
    public boolean checkDistance(int surrounding, double latlocation, double lonlocation){
        double dx, dy, lat, distance=0;

//      lat = (latitude + latlocation) / 2 * 0.01745;
//      dx = 111.3 * cos(lat) * (longitude - lonlocation);
//      dy = 111.3 * (latitude - latlocation);
//      distance = sqrt(dx * dx + dy * dy);

        return distance <= surrounding;
    }

}


