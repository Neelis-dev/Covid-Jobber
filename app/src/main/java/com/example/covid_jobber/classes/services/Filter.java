package com.example.covid_jobber.classes.services;

import android.util.Log;

import com.example.covid_jobber.classes.Triple;

import java.util.ArrayList;

import java.util.List;


public class Filter {
    // Elements of Triple are: 1. Type of Filter (category, skills, location...)
    // 2. Value being filtered by
    // 3. String needed in URL for that category
    private final List<String> filterList = new ArrayList<>();


    public void addFilter(FilterType type, String filtervalue){
        filterList.add("&"+type.getTypeURL()+"="+filtervalue);
    }

    public List<String> getFilterList() {
        return filterList;
    }
}

