package com.example.covid_jobber.classes.services;

public enum FilterType
{
    CATEGORY("category"),LOCATION("where"),KEYWORDS("what"),SALARY("salary_min");

    String typeURL;
    FilterType(String typeURL){
        this.typeURL=typeURL;
    }

    public String getTypeURL() {
        return typeURL;
    }
}
