package com.example.covid_jobber.fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.covid_jobber.activities.MainActivity;
import com.example.covid_jobber.classes.Job;
import com.example.covid_jobber.databinding.FragmentFavoriteJobBinding;
import com.example.covid_jobber.enums.ContractTime;
import com.example.covid_jobber.enums.Language;

import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteJobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteJobFragment extends Fragment implements View.OnClickListener{

    private final Job job;
    private FragmentFavoriteJobBinding binding;

    private boolean extended = false;
    private MainActivity mainActivity;

    public FavoriteJobFragment(Job job) {
        this.job = job;
    }

    public FavoriteJobFragment() {
        // Required empty public constructor
        this.job = null;
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

        mainActivity = (MainActivity) getActivity();

        binding.btnJobMore.setOnClickListener(this);
        binding.btnJobLess.setOnClickListener(this);
        binding.btnJobWebsite.setOnClickListener(this);
        binding.btnJobDelete.setOnClickListener(this);

        binding.btnJobDelete.setVisibility(View.GONE);

//        set texts
        if(job != null){
            binding.txtJobTitle.setText(job.getTitle());
            binding.txtJobCompany.setText(job.getCompany());

            String contractTimeText = "Arbeitszeit: ";
            if(mainActivity.language == Language.ENGLISH){
                contractTimeText = "Contract Time: ";
            }

            if(job.getContractTime() != ContractTime.EITHER){
                contractTimeText = contractTimeText+job.getContractTime().getTranslation(mainActivity.language);
            }
            else {
                contractTimeText = contractTimeText+ContractTime.UNKNOWN.getTranslation(mainActivity.language);
            }

            binding.txtJobContractTime.setText(contractTimeText);

            binding.txtJobDescription.setText(job.getDescription());
        }


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
        else if(v == binding.btnJobDelete){
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            String message = "Möchtest du den Eintrag wirklich löschen?";
            String yes = "Ja";
            String cancel = "Abbrechen";
            if (mainActivity.language == Language.ENGLISH){
                message = "Are you sure you want to delete this entry?";
                yes = "Yes";
                cancel = "Cancel";
            }
            builder.setMessage(message)
                    .setCancelable(false)
                    .setPositiveButton(yes, (dialog, id) -> deleteHelper())
                    .setNegativeButton(cancel, (dialog, id) -> dialog.cancel());
            AlertDialog alert = builder.create();
            alert.show();
        }
    }

    public void deleteHelper(){
        mainActivity.getFavoritesFragment().deleteFavorite(this);
    }

    public void setDeletable(boolean deletable){
        if(deletable){
            binding.btnJobDelete.setVisibility(View.VISIBLE);
        } else {
            binding.btnJobDelete.setVisibility(View.GONE);
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

    public Job getJob(){
        return job;
    }
}