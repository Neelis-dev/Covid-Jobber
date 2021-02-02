package com.example.covid_jobber.classes.services;

import java.util.ArrayList;

import java.util.List;


public class Filter {
    // Elements of Triple are: 1. Type of Filter (category, skills, location...)
    // 2. Value being filtered by
    // 3. String needed in URL for that category

    /**
     *
     * @param type
     * @param filtervalue
     * Entering "-" as the filtervalue will result in nothing to be added to the filter and thus that type of filter ignored
     */

    private List<String> filterList = new ArrayList<>();

    public Filter(){

    }

    public void addFilter(FilterType type, String filtervalue){
        if(filtervalue.equals("-")){
            return;
        }

        filterList.add("&"+type.getTypeURL()+"="+filtervalue);


    }

    /**
     * USE THIS ONLY WHEN YOU KNOW WHAT YOU ARE DOING!! IN CASE OF CONFUSION ASK NEELIS!!
     *
     */
    public void addFilter(String specialFilterString){
        filterList.add("&"+specialFilterString);


    }

    public String getFilterString(){
        String s= "";

        for (String filter:filterList) {
            s = s + filter;
        }
        return s;

    }
}

