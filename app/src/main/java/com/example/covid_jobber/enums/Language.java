package com.example.covid_jobber.enums;

import androidx.annotation.NonNull;

public enum Language {
    ENGLISH("en","English","Englisch"),
    GERMAN("de","German","Deutsch");

    private final String code;
    private final String english;
    private final String german;

    Language(String code, String english, String german) {
        this.code = code;
        this.english = english;
        this.german = german;
    }

    @NonNull
    @Override
    public String toString() {
        return code;
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

    public static Language getByCode(String code){
        for(Language l: Language.values()) {
            if(l.code.equals(code)) {
                return l;
            }
        }
        return null;
    }

    public static Language getByTranslation(String translation) {
        for(Language c: Language.values()) {
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
