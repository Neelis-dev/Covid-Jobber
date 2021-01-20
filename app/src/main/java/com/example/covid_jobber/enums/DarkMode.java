package com.example.covid_jobber.enums;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

import org.jetbrains.annotations.NotNull;

public enum DarkMode {
    ON("An"),
    OFF("Aus"),
    SYSTEM("Systemeinstellung");

    private final String name;

    DarkMode(String name) {
        this.name = name;
    }

    @NotNull
    @Override
    public String toString() {
        return name;
    }

    public void setMode(){
        switch (this){
            case ON: AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); break;
            case OFF: AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); break;
            case SYSTEM: AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM); break;
        }
    }
}
