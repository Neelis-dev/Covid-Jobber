package com.example.covid_jobber.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.covid_jobber.R;
import com.example.covid_jobber.databinding.FragmentCardDisplayBinding;

import org.json.JSONObject;

import java.io.IOException;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.X509TrustManager;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardDisplayFragment extends Fragment {

    public FragmentCardDisplayBinding fragmentCardDisplayBinding;
    public CardDisplayFragment() {
        // Required empty public constructor

    }


    public static CardDisplayFragment newInstance(String param1, String param2) {
        CardDisplayFragment fragment = new CardDisplayFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentCardDisplayBinding = FragmentCardDisplayBinding.inflate(getLayoutInflater());

        final String appId = "64fa1822";
        final String appKey = "d41a9537116b72a1c2a890a27376d552";

        Request request = new Request.Builder()
                .url("https://api.adzuna.com/v1/api/jobs/gb/search/1?app_id="+appId+"&app_key="+appKey)
                .build();

        OkHttpClient client = new OkHttpClient();
        client.newCall(request).enqueue(new Callback() {
            @Override public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override public void onResponse(Call call, Response response) throws IOException {
                ResponseBody responseBody = response.body();
                JSONObject jsonObject=null;
                System.out.println("it worked");
                System.out.println(responseBody.string());
                fragmentCardDisplayBinding.tvJobDescription.setText("If you can see this the API responded successfully! no Error code!");
            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_display, container, false);
    }

}