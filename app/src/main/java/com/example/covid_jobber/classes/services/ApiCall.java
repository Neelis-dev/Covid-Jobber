package com.example.covid_jobber.classes.services;

import com.google.android.gms.common.api.Api;

import org.json.JSONArray;

import java.util.Random;

import okhttp3.Request;

public abstract class ApiCall {
    final String appId = "64fa1822";
    final String appKey = "d41a9537116b72a1c2a890a27376d552";
    final String foundationURL="https://api.adzuna.com/v1/api/jobs/de/search/";

    private Request request;



    public ApiCall(){
        this(1);
    }
    public ApiCall(int pageNumber){
        this.request = new Request.Builder()
                                 .url(foundationURL+pageNumber+"?app_id="+appId+"&app_key="+appKey+"&results_per_page=50")
                                 .build();
    }
    public ApiCall(Filter filter){
        request = new Request.Builder()
                .url(foundationURL+1+"?app_id="+appId+"&app_key="+appKey+"&results_per_page=50")
                .build();




        this.request = new Request.Builder()
                .url(request.url().toString()+filter.getFilterString())
                .build();
        System.out.println(request.url().toString());


    }
    public ApiCall(Request request){
        this.request = request;
    }


    public void filterByCategory(String category){

        this.request = new Request.Builder()
                .url(request.url().toString()+"&category="+category)
                .build();


    }


    public Request getRequest() {
        return request;
    }

    public abstract void callback(JSONArray results);




}
