package com.example.covid_jobber.enums;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;

public enum DarkMode {
    SYSTEM("System","System"),
    ON("An","On"),
    OFF("Aus","Off");

    private final String german;
    private final String english;

    DarkMode(String german, String english) {
        this.german = german;
        this.english = english;
    }

    @NonNull
    @Override
    public String toString() {
        return english;
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

    public static DarkMode getByName(String name){
        for(DarkMode c: DarkMode.values()) {
            if(c.english.equals(name)) {
                return c;
            }
        }
        return null;
    }

    public static DarkMode getByTranslation(String translation) {
        for(DarkMode c: DarkMode.values()) {
            if(c.german.equals(translation)) {
                return c;
            }
            else if(c.english.equals(translation)) {
                return c;
            }
        }
        return null;
    }

    public static void setMode(DarkMode mode){
        switch (mode){
            case ON: AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES); break;
            case OFF: AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO); break;
            case SYSTEM: AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM); break;
        }
    }

}
