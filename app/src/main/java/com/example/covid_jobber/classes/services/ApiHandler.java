package com.example.covid_jobber.classes.services;


import android.util.Log;

import com.example.covid_jobber.activities.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class ApiHandler {
    private OkHttpClient client = new OkHttpClient();

    public void makeApiCall(ApiCall apiCall, MainActivity mainActivity){

        // make Runnable to create seperate Thread and not use Main Thread
        Runnable apiCallThread = () -> {
            Future<JSONArray> f = makeCall(apiCall.getRequest());

            try {
                apiCall.callback(f.get());
                mainActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        mainActivity.getSwipeFragment().updateArrayAdapter();
                    }
                });

            } catch ( ExecutionException | InterruptedException e) {
                e.printStackTrace();
            }

        };
       new Thread(apiCallThread).start();

    }


    private Future<JSONArray> makeCall(Request request){
        CompletableFuture<JSONArray> f = new CompletableFuture<>();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                f.completeExceptionally(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                System.out.println("response recieved");
                ResponseBody responseBody = response.body();

                try{
                    // Create jsonObject out of responsebody
                    JSONObject jsonObject = new JSONObject(responseBody.string());
                    // Return JSONArray results
                    f.complete(jsonObject.getJSONArray("results"));
                }catch(JSONException j){

                    j.printStackTrace();
                }
                response.close();
            }
        });

        return f;
    }
}
