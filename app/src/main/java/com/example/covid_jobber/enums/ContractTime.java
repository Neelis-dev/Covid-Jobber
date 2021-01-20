package com.example.covid_jobber.enums;

import androidx.annotation.NonNull;

public enum ContractTime {
    FULL_TIME("Vollzeit"),
    PART_TIME("Teilzeit"),
    EITHER("Beliebig");

    private final String name;

    ContractTime(String name) {
        this.name = name;
    }


    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
