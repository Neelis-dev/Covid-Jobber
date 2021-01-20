package com.example.covid_jobber.classes.services;

public enum FilterType
{
    CATEGORY("category"),LOCATION("where");

    String typeURL;
    FilterType(String typeURL){
        this.typeURL=typeURL;
    }

    public String getTypeURL() {
        return typeURL;
    }
}
