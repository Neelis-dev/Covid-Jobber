package com.example.covid_jobber.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.covid_jobber.R;
import com.example.covid_jobber.classes.Job;
import com.example.covid_jobber.databinding.FragmentFavoriteJobBinding;
import com.example.covid_jobber.databinding.FragmentFavoritesBinding;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoriteJobFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoriteJobFragment extends Fragment {

    private final Job job;
    private FragmentFavoriteJobBinding binding;

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

        binding.txtJobTitle.setText(job.getTitle());
        binding.txtJobCompany.setText(job.getCompany());

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}