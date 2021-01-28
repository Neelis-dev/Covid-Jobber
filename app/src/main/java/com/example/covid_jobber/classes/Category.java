package com.example.covid_jobber.classes;

import androidx.annotation.NonNull;

import com.example.covid_jobber.enums.DarkMode;
import com.example.covid_jobber.enums.Language;

import java.util.List;

public class Category {
    private final String tag;
    private final String german; // In Api called "label"
    private final String english;

    public Category(String tag, String german){
        this.tag = tag;
        this.german = german;

        String englishName = "";
        String[] parts = tag.split("-");
        for (String part:parts) {
            englishName = englishName.concat(part.substring(0, 1).toUpperCase() + part.substring(1) + " ");
        }
        englishName = englishName.substring(0,englishName.length()-1);
        this.english = englishName;
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
