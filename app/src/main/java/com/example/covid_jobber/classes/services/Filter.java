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

    private String whatString;
    private String whereString;

    public Filter(){
        whatString = "&what=";
        whereString= "&where=";
    }

    public void addFilter(FilterType type, String filtervalue){
        if(filtervalue.equals("-")){
            return;
        }

        if(whatString.contains(type.getTypeURL())){
            if(whatString.endsWith("=")){
                whatString = whatString + filtervalue;
            }else{
                whatString = whatString +","+filtervalue;
            }
        }if(whereString.contains(type.getTypeURL())){
            if(whereString.endsWith("=")){
                whereString = whereString + filtervalue;
            }else{
                whereString = whereString +","+filtervalue;
            }
        }


    }

    public String getFilterString(){
        return whatString+whereString;

    }
}

