package com.example.covid_jobber.classes;

import com.example.covid_jobber.activities.MainActivity;
import com.example.covid_jobber.classes.services.ApiCall;
import com.example.covid_jobber.classes.services.ApiHandler;

import org.json.JSONArray;
import org.json.JSONException;

public class Test {


    public static void main(String[] args) {
        ApiHandler handler = new ApiHandler();

        handler.makeApiCall(new ApiCall() {
            @Override
            public void callback(JSONArray results) {
                foo(results);
            }
        }, new MainActivity());
    }

    public static void foo(JSONArray res){
        System.out.println(res.length());
    }
}
