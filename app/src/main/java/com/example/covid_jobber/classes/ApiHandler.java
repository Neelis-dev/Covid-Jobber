package com.example.covid_jobber.classes;

import android.widget.TextView;

import com.example.covid_jobber.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

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



    public void forUI(){
        Request request = new Request.Builder()
                .url("https://api.adzuna.com/v1/api/jobs/gb/search/"+pageNumber+"?app_id="+appId+"&app_key="+appKey)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                System.out.println("response recieved");
                ResponseBody responseBody = response.body();
                JSONObject jsonObject;
                try {
                    jsonObject= new JSONObject(responseBody.string());
                    String title = jsonObject.getJSONArray("results").getJSONObject(0).get("title").toString();
                    System.out.println("Job Desc should be:" + title);
//                    https://stackoverflow.com/questions/50139888/how-to-get-async-call-to-return-response-to-main-thread-using-okhttp
//                    Todo: Return Value
                } catch (JSONException e) {
                    e.printStackTrace();
                    System.out.println("fail while creating JSON");
                }

            }
        });
    }
}
