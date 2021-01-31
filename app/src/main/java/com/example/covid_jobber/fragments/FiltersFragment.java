package com.example.covid_jobber.fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.example.covid_jobber.activities.MainActivity;
import com.example.covid_jobber.classes.Category;
import com.example.covid_jobber.classes.services.ApiCall;
import com.example.covid_jobber.classes.services.ApiHandler;
import com.example.covid_jobber.classes.services.Filter;
import com.example.covid_jobber.classes.services.FilterType;
import com.example.covid_jobber.databinding.FragmentFiltersBinding;
import com.example.covid_jobber.enums.ContractTime;
import com.example.covid_jobber.enums.Language;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FiltersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FiltersFragment extends Fragment implements View.OnClickListener, AdapterView.OnItemSelectedListener {

    private FragmentFiltersBinding binding;
    private MainActivity mainActivity;

    // Persistence
    SharedPreferences prefs;

    // Variables
//    chosen options
    private double expSalary = 1000;
    private ContractTime contractTime;
    private Category category;
    private List<String> keywords = new ArrayList<>();
    private int surrounding;
    private double latitude;
    private double longitude;

//    available options
    private final List<ContractTime> contractTimes = new ArrayList<>(Arrays.asList(ContractTime.EITHER, ContractTime.FULL_TIME, ContractTime.PART_TIME));
    private final List<Integer> surroundingList = new ArrayList<>(Arrays.asList(5, 25, 75, 150, 250));
    private List<View> editOptions;

    private final List<Category> categories = new ArrayList<>();

//    debug variables
    private boolean editing = false;
    private boolean filtersActive = false;
    private boolean locationActive = false;


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

        mainActivity = (MainActivity) getActivity();
        prefs = mainActivity.getPrefs();

        // Assign variables from SharedPreferences
        getPreferences();

//        Inputs ------------------------------------------------

        //        Salary Input
        binding.inputFilterSalary.setText(String.valueOf(expSalary));
        //        Keyword Inputs
        for (int i = 0; i < keywords.size(); i++) {
            addKeywordView(keywords.get(i));
            EditText view = (EditText) binding.layoutFilterKeywords.getChildAt(i);
            view.setEnabled(false);
        }

//        Buttons ----------------------------------------------

        //        Edit Button
        binding.btnFilterEdit.setOnClickListener(this);
        //        Save Button
        binding.btnFilterSave.setOnClickListener(this);
        binding.btnFilterSave.setVisibility(View.INVISIBLE);
        //        Add Keyword Button
        binding.btnFilterAddKeyword.setOnClickListener(this);
        //        Delete Keyword Button
        binding.btnFilterDeleteKeyword.setOnClickListener(this);

//        Toggles ----------------------------------------------

        //        Filter toggle
        binding.switchFilterToggle.setOnClickListener(this);
        binding.switchFilterToggle.setChecked(filtersActive);
        //        Location Permission toggle
        binding.btnLocationPermission.setOnClickListener(this);
        binding.btnLocationPermission.setChecked(locationActive);
        //        Surrounding Option visible/not visible -> depends on locationActive
        if(locationActive){
            binding.txtFilterSurrounding.setVisibility(View.VISIBLE);
            binding.spinnerFilterSurrounding.setVisibility(View.VISIBLE);
        }
        else{
            binding.txtFilterSurrounding.setVisibility(View.GONE);
            binding.spinnerFilterSurrounding.setVisibility(View.GONE);
        }

//        Spinner -----------------------------------------------------

        //        Category Spinner
        binding.spinnerFilterCategory.setOnItemSelectedListener(this);
        List<String> translatedCategories = new ArrayList<>();
        for (Category c:categories) {
            translatedCategories.add(c.getTranslation(mainActivity.language));
        }
//        Collections.sort(translatedCategories);
        binding.spinnerFilterCategory.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_dropdown_item_1line, translatedCategories));
        binding.spinnerFilterCategory.setSelection(translatedCategories.indexOf(category.getTranslation(mainActivity.language)));

        //        Contract Time Spinner
        binding.spinnerFilterContractTime.setOnItemSelectedListener(this);
        List<String> translatedContractTimes = new ArrayList<>();
        for (ContractTime c:contractTimes) {
            translatedContractTimes.add(c.getTranslation(mainActivity.language));
        }
        binding.spinnerFilterContractTime.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_dropdown_item_1line, translatedContractTimes));
        if (contractTime != null) {
            binding.spinnerFilterContractTime.setSelection(translatedContractTimes.indexOf(contractTime.getTranslation(mainActivity.language)));
        }

        //        Surrounding Spinner
        binding.spinnerFilterSurrounding.setOnItemSelectedListener(this);
        binding.spinnerFilterSurrounding.setAdapter(new ArrayAdapter<>(this.getContext(), android.R.layout.simple_dropdown_item_1line, surroundingList));
        binding.spinnerFilterSurrounding.setSelection(surroundingList.indexOf(surrounding));

//        --------------------------------------------------------------
//        All options set to disabled if not in edit mode
        editOptions = new ArrayList<>(Arrays.asList(binding.inputFilterSalary, binding.spinnerFilterCategory, binding.spinnerFilterContractTime,
                binding.btnLocationPermission,  binding.spinnerFilterSurrounding, binding.btnFilterAddKeyword, binding.btnFilterDeleteKeyword));
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
        binding = null;
        super.onDestroyView();
    }

    //    Currently only used for toggle button
    @Override
    public void onClick(View v) {
//        Toggle Button to set filters to active
        if (v == binding.switchFilterToggle) {
            filtersActive = binding.switchFilterToggle.isChecked();
            System.out.println("filters: " + filtersActive);

        }
//        Button to start Editing
        else if (v == binding.btnFilterEdit) {
            startEditing();

        }
//        Button to end edting
        else if (v == binding.btnFilterSave) {
            endEditing();

        }
//        Add Keyword Button
        else if(v == binding.btnFilterAddKeyword){
            addKeywordView("");
            keywords.add("");
        }
//        Delete Keyword Button
        else if(v == binding.btnFilterDeleteKeyword){
            int index = keywords.size()-1;
            if(index >= 0){
                binding.layoutFilterKeywords.removeViewAt(index);
                keywords.remove(index);
            }

        }
//        Location Button
        else if (v == binding.btnLocationPermission) {
            locationActive = binding.btnLocationPermission.isChecked();
            if(locationActive){
                binding.spinnerFilterSurrounding.setVisibility(View.VISIBLE);
                binding.txtFilterSurrounding.setVisibility(View.VISIBLE);
            } else if(!locationActive){
                binding.spinnerFilterSurrounding.setVisibility(View.GONE);
                binding.txtFilterSurrounding.setVisibility(View.GONE);
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                String message = "";
                switch (mainActivity.language){
                    case GERMAN:
                        message = "Wenn du den Standort deaktivierst, kann deine Suche nicht geografisch eingegrenzt werden. Du erhältst Vorschläge aus ganz Deutschland."; break;
                    case ENGLISH:
                        message = "If you deactivate your location, your search cannot be filtered geographically. You will see job offers from all over Germany."; break;
                }
                builder.setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton("Ok", (dialog, id) -> dialog.cancel());
                AlertDialog alert = builder.create();
                alert.show();
            }
            if (ContextCompat.checkSelfPermission(this.getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                updateLocation();
            } else {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 0);
            }
        }
    }

    //    currently only used for category spinner
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(parent == binding.spinnerFilterCategory){
            category = Category.getByTranslation(binding.spinnerFilterCategory.getSelectedItem().toString(), categories);
            System.out.println("category chosen: "+category);
        }
        else if(parent == binding.spinnerFilterContractTime){
            contractTime = ContractTime.getByTranslation((String) binding.spinnerFilterContractTime.getSelectedItem());
            System.out.println("contract time chosen: "+contractTime.toString());
        }
        else if(parent == binding.spinnerFilterSurrounding){
            surrounding = (int) binding.spinnerFilterSurrounding.getSelectedItem();
            System.out.println("surrounding chosen: "+surrounding);
        }
    }

    //    currently only used for category spinner
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        if(parent == binding.spinnerFilterCategory){
            category = Category.getByTranslation(binding.spinnerFilterCategory.getSelectedItem().toString(), categories);
            System.out.println("category chosen: "+category);
        }
        else if(parent == binding.spinnerFilterContractTime){
            contractTime = ContractTime.getByTranslation((String) binding.spinnerFilterContractTime.getSelectedItem());
            System.out.println("contract time chosen: "+contractTime.toString());
        }
        else if(parent == binding.spinnerFilterSurrounding){
            surrounding = (int) binding.spinnerFilterSurrounding.getSelectedItem();
            System.out.println("surrounding chosen: "+surrounding);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            updateLocation();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setMessage("Wenn du den Standort nicht freigeben möchtest, kann deine Suche nicht geografisch eingegrenzt werden. Du erhältst Vorschläge aus ganz Deutschland.")
                    .setCancelable(false)
                    .setPositiveButton("Ok", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    @SuppressLint("MissingPermission")
    public void updateLocation() {
        FusedLocationProviderClient fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this.getContext());
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this::onLocationReceived);
    }


    public void onLocationReceived(Location location){
        if(location != null){
            latitude = location.getLatitude();
            longitude = location.getLongitude();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            String message = "";
            switch (mainActivity.language){
                case GERMAN:
                    message = "Entschuldige, dein Standort konnte nicht bestimmt werden."; break;
                case ENGLISH:
                    message = "Sorry, your location could not be determined."; break;
            }
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton("Ok", (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    //      Berechnung zur Bestimmung ob ein Ort aus der API innerhalb des ausgewählten Umkreises des Users liegt
    public boolean checkDistance(int surrounding, double latlocation, double lonlocation){
        double dx, dy, lat, distance;

        lat = (latitude + latlocation) / 2 * 0.01745;
        dx = 111.3 * Math.cos(lat) * (longitude - lonlocation);
        dy = 111.3 * (latitude - latlocation);
        distance = Math.sqrt(dx * dx + dy * dy);

        return distance <= surrounding;
    }


    public void fillCategorySpinner(){
        ApiHandler handler= new ApiHandler();
        handler.makeApiCall(new ApiCall(new Request.Builder().url("https://api.adzuna.com/v1/api/jobs/de/categories?app_id=64fa1822&app_key=d41a9537116b72a1c2a890a27376d552").build()) {
            @Override
            public void callback(JSONArray results) {

                for(int i=0; i<results.length();i++){
                    try {
                        categories.add(new Category(results.getJSONObject(i).getString("tag"), results.getJSONObject(i).getString("label")));
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
        filter.addFilter(FilterType.CATEGORY,category.getTag());
        filter.addFilter(FilterType.SALARY,String.valueOf((int) Math.floor(expSalary)));
        if(!(contractTime.toString()).equals("-")){
            filter.addFilter(contractTime.toString()+"=1");
        }
        String keywordString="";
        for(int i = 0;i<keywords.size();i++){
            if(i == keywords.size()-1){
                keywordString = keywordString + keywords.get(i);
            }else{
                keywordString = keywordString + keywords.get(i) + ",";
            }
        }
        filter.addFilter(FilterType.KEYWORDS,keywordString);

        return filter;
    }


//    Private Functions -----------------------------------------------

    //    enables edit mode -> activated by edit button
    private void startEditing(){
        editing = true;
        binding.btnFilterSave.setVisibility(View.VISIBLE);
        binding.btnFilterEdit.setEnabled(false);

//        Enable all edit options
        for (View option:editOptions) {
            option.setEnabled(true);
        }

        for (int i = 0; i < keywords.size(); i++) {
            EditText view = (EditText) binding.layoutFilterKeywords.getChildAt(i);
            view.setEnabled(true);
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

//        Save keywords inputs
        for (int i = 0; i < keywords.size(); i++) {
            EditText view = (EditText) binding.layoutFilterKeywords.getChildAt(i);
            keywords.set(i, view.getText().toString());
            view.setEnabled(false);
        }

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

        setPreferences();
    }

    // Assign variables from SharedPreferences
    public void getPreferences(){
        mainActivity = (MainActivity) getActivity();
        prefs = mainActivity.getPrefs();

        expSalary = prefs.getFloat("expSalary",1000);
        category = Category.getByTag(prefs.getString("category","it-jobs"), categories);
        contractTime = ContractTime.getByName(prefs.getString("contractTime",ContractTime.EITHER.toString()));
        Set<String> keywordSet = prefs.getStringSet("keywords", new HashSet<String>());
        keywords = new ArrayList<>(keywordSet);
        surrounding = prefs.getInt("surrounding",5);
        latitude = prefs.getFloat("latitude",0);
        longitude = prefs.getFloat("longitude",0);
        filtersActive = prefs.getBoolean("filtersActive",false);
        locationActive = prefs.getBoolean("locationActive",false);

        Log.d("TAG","SP DATA"+"\n"+
                "expSalary: "+expSalary+"\n"+
                "category: "+category+"\n"+
                "surrounding: "+surrounding+"\n"+
                "filtersActive: "+filtersActive+"\n"+
                "locationActive: "+locationActive+"\n"
        );
    }

    // set variables from SharedPreferences
    private void setPreferences(){
        SharedPreferences.Editor editor = prefs.edit();
        System.out.println("editor should write");
        editor.putFloat("expSalary",(float) expSalary);
        editor.putString("contractTime",contractTime.toString());
        editor.putString("category",category.getTag());
        Set<String> keywordSet = new HashSet<>(keywords);
        editor.putStringSet("keywords",keywordSet);
        editor.putInt("surrounding",surrounding);
        editor.putFloat("latitude",(float) latitude);
        editor.putFloat("longitude",(float) longitude);
        editor.putBoolean("filtersActive",filtersActive);
        editor.putBoolean("locationActive",locationActive);
        editor.apply();
    }

    //    Adds a EditText to the Keywords Layout with the given text
    private void addKeywordView(String text){
        EditText newKeyword = new EditText(this.getContext());
        newKeyword.setId(View.generateViewId());
        newKeyword.setText(text);
        newKeyword.setSingleLine();
        binding.layoutFilterKeywords.addView(newKeyword);
    }


}


