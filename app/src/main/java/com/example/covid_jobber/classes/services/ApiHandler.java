package com.example.covid_jobber.classes.services;





import com.example.covid_jobber.activities.MainActivity;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;
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
    final private String appId = "64fa1822";
    final private String appKey = "d41a9537116b72a1c2a890a27376d552";
    final private String pageNumber = "1";

    private String title = "";
    private OkHttpClient client = new OkHttpClient();





    public void forUI(MainActivity mainActivity) throws JSONException {
        Request request = new Request.Builder()
                .url("https://api.adzuna.com/v1/api/jobs/gb/search/"+pageNumber+"?app_id="+appId+"&app_key="+appKey)
                .build();

        Runnable apiCallThread = new Runnable() {
            @Override
            public void run() {
                Future<JSONArray> f = makeApiCall(request);
                try {
                    mainActivity.callmeback(f.get());
                } catch (ExecutionException | InterruptedException | JSONException e) {
                    e.printStackTrace();
                }
            }
        };
        new Thread(apiCallThread).start();



    }


    private Future<JSONArray> makeApiCall(Request request){
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
            }
        });

        return f;
    }
}
