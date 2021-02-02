package com.example.covid_jobber.enums;

import androidx.annotation.NonNull;

public enum ContractTime {
    FULL_TIME("full_time","Vollzeit","Full Time"),
    PART_TIME("part_time","Teilzeit","Part Time"),
    EITHER("-","Beliebig","Either"),
    UNKNOWN("xxx", "Unbekannt", "Unknown");

    private final String name;
    private final String german;
    private final String english;

    ContractTime(String name, String german, String english) {
        this.name = name;
        this.german = german;
        this.english = english;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }

    public String getTranslation(Language language){
        switch (language){
            case ENGLISH:
                return english;
            case GERMAN:
                return german;
        }
        return null;
    }

    public static ContractTime getByName(String name){
        for(ContractTime c: ContractTime.values()) {
            if(c.name.equals(name)) {
                return c;
            }
        }
        return null;
    }

    public static ContractTime getByTranslation(String translation) {
        for(ContractTime c: ContractTime.values()) {
            if(c.german.equals(translation)) {
                return c;
            }
            else if(c.english.equals(translation)) {
                return c;
            }
        }
        return null;
    }

}
