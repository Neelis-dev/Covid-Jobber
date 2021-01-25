package com.example.covid_jobber.fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.covid_jobber.R;
import com.example.covid_jobber.classes.Job;
import com.example.covid_jobber.databinding.FragmentFavoritesBinding;
import com.example.covid_jobber.databinding.FragmentFiltersBinding;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment {

//    variables
    private final List<Job> favoriteJobs = new ArrayList<>();

    private FragmentFavoritesBinding binding;

    public FavoritesFragment() {
        // Required empty public constructor
    }

    public static FavoritesFragment newInstance() {
        return new FavoritesFragment();
    }

    @Override
    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentFavoritesBinding.inflate(inflater, container, false);

        for (int i = 0; i < favoriteJobs.size(); i++) {

            Job j = favoriteJobs.get(i);

//            generate FrameLayout
            FrameLayout newFrame = new FrameLayout(this.getContext());
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
            newFrame.setLayoutParams(params);
            newFrame.setId(View.generateViewId());

//            add FrameLayout to LayoutFavoriteJobs
            binding.layoutFavoritesJobs.addView(newFrame, 0);

//            generate FavoriteJobFragment
            FavoriteJobFragment newJobFragment = new FavoriteJobFragment(j);

//            Place FavoriteJobFragment in the new frame
            getActivity().getSupportFragmentManager().beginTransaction().replace(newFrame.getId(),newJobFragment).commitAllowingStateLoss();
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    public void addFavorite(Job job){
        favoriteJobs.add(job);
    }
}