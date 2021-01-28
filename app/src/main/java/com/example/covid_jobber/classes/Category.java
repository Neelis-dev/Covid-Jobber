package com.example.covid_jobber.classes;

import androidx.annotation.NonNull;

import com.example.covid_jobber.enums.DarkMode;
import com.example.covid_jobber.enums.Language;

import java.util.List;

public class Category {
    private final String tag;
    private final String german; // In Api called "label"
    private final String english;

    private static int number = 1;

    public Category(String tag, String german){
        this.tag = tag;
        this.german = german;
//        TODO: Auto-Translate with Library
        this.english = "category "+number;
        number++;
    }

    public String getTag(){
        return tag;
    }

    @NonNull
    @Override
    public String toString() {
        return tag;
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

    public static Category getByTranslation(String translation, List<Category> categories){
        for(Category c: categories) {
            if(c.german.equals(translation)) {
                return c;
            }
            else if(c.english.equals(translation)) {
                return c;
            }
        }
        return null;
    }

    public static Category getByTag(String tag, List<Category> categories){
        for(Category c: categories) {
            if(c.tag.equals(tag)) {
                return c;
            }
        }
        return null;
    }
}
