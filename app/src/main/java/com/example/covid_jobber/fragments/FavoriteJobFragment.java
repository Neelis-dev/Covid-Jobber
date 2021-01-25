package com.example.covid_jobber.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.covid_jobber.R;
import com.example.covid_jobber.classes.Job;
import com.example.covid_jobber.databinding.FragmentFavoriteJobBinding;
import com.example.covid_jobber.databinding.FragmentFavoritesBinding;
import com.example.covid_jobber.enums.ContractTime;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteJobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteJobFragment extends Fragment implements View.OnClickListener{

    private final Job job;
    private FragmentFavoriteJobBinding binding;

    private boolean extended = false;

    public FavoriteJobFragment(Job job) {
        // Required empty public constructor
        this.job = job;
    }

    public static FavoriteJobFragment newInstance(Job job) {
        return new FavoriteJobFragment(job);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoriteJobBinding.inflate(inflater, container, false);

        binding.btnJobMore.setOnClickListener(this);
        binding.btnJobLess.setOnClickListener(this);
        binding.btnJobWebsite.setOnClickListener(this);

//        set texts
        binding.txtJobTitle.setText(job.getTitle());
        binding.txtJobCompany.setText(job.getCompany());

        String contractTimeText = "Arbeitszeit: Unbekannt";
        if(job.getContractTime() != ContractTime.EITHER){
            contractTimeText = "Arbeitszeit: "+job.getContractTime().toString();
        }
        binding.txtJobContractTime.setText(contractTimeText);

        binding.txtJobDescription.setText(job.getDescription());

//        hide lower part
        minimize();

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if(v == binding.btnJobMore){
            extend();
        }
        else if(v == binding.btnJobLess){
            minimize();
        }
        else if(v == binding.btnJobWebsite){
            openUrl(job.getUrl());
        }
    }

    private void extend(){
        extended = true;
        binding.layoutJobsExtend.setVisibility(View.VISIBLE);
        binding.btnJobMore.setVisibility(View.GONE);
    }

    private void minimize(){
        extended = false;
        binding.layoutJobsExtend.setVisibility(View.GONE);
        binding.btnJobMore.setVisibility(View.VISIBLE);
    }

    private void openUrl(String url){
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(url));
        startActivity(i);
    }
}