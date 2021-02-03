package com.example.covid_jobber.fragments;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.covid_jobber.R;
import com.example.covid_jobber.activities.MainActivity;
import com.example.covid_jobber.classes.Job;
import com.example.covid_jobber.databinding.FragmentFavoritesBinding;
import com.example.covid_jobber.enums.Language;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FavoritesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FavoritesFragment extends Fragment implements View.OnClickListener {

    //    variables
    private final List<Job> favoriteJobs = new ArrayList<>();
    private final List<FrameLayout> frames = new ArrayList<>();
    private final List<FavoriteJobFragment> fragments = new ArrayList<>();

    private FragmentFavoritesBinding binding;
    private MainActivity mainActivity;

    //    Deleting Jobs
    private boolean deleting = false;
    private final List<FavoriteJobFragment> jobsToDelete = new ArrayList<>();

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
        mainActivity = (MainActivity) getActivity();

        deleting = false;
        binding.btnFavoritesEdit.setOnClickListener(this);

        frames.clear();
        fragments.clear();
        jobsToDelete.clear();

        for (int i = 0; i < favoriteJobs.size(); i++) {
            Job j = favoriteJobs.get(i);
            addJobToLayout(j);
        }

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        if(v == binding.btnFavoritesEdit){
            if(!deleting){
                deleting = true;
                binding.btnFavoritesEdit.setBackgroundTintList(ContextCompat.getColorStateList(this.getContext(), R.color.primary_dark));
                for (FavoriteJobFragment fragment : fragments) {
                    fragment.setDeletable(true);
                }
            } else {
                deleting = false;
                binding.btnFavoritesEdit.setBackgroundTintList(ContextCompat.getColorStateList(this.getContext(), R.color.primary));

//                Delete jobsToDelete
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                String message = "Möchtest du die ausgewählten Einträge wirklich löschen?";
                String yes = "Ja";
                String cancel = "Abbrechen";
                if (mainActivity.language == Language.ENGLISH){
                    message = "Are you sure you want to delete the selected entries?";
                    yes = "Yes";
                    cancel = "Cancel";
                }
                builder.setMessage(message)
                        .setCancelable(false)
                        //                    If Deleting
                        .setPositiveButton(yes, (dialog, id) -> {
                            for (FavoriteJobFragment fragment : jobsToDelete) {
                                deleteFavorite(fragment);
                            }
                            jobsToDelete.clear();
                        })
                        //                    If Cancelled
                        .setNegativeButton(cancel, (dialog, id) -> dialog.cancel());

                AlertDialog alert = builder.create();
                alert.show();

//                End Deleting mode
                for (FavoriteJobFragment fragment : fragments) {
                    fragment.setDeletable(false);
                }

            }
        }
    }

    public void addFavorite(Job job, SharedPreferences prefs){
        favoriteJobs.add(job);
        setJobsToPrefs(prefs);
    }

    public boolean findFavorite(int id){
        boolean found = false;
        for (Job job:favoriteJobs) {
            if(id == job.getId()){
                found = true;
            }
        }
        return found;
    }

    public void addJobToDelete(FavoriteJobFragment fragment){
        jobsToDelete.add(fragment);
    }

    public void removeJobToDelete(FavoriteJobFragment fragment){
        jobsToDelete.remove(fragment);
    }

    private void deleteFavorite(FavoriteJobFragment fragment){
        favoriteJobs.remove(fragment.getJob());
        int index = fragments.indexOf(fragment);
        FrameLayout frame = frames.get(index);
        binding.layoutFavoritesJobs.removeView(frame);
        fragments.remove(fragment);
        frames.remove(frame);
        setJobsToPrefs(mainActivity.getPrefs());
    }

    public void getJobsFromPrefs(SharedPreferences prefs){
        Gson gson = new Gson();
        Set<String> jsons = prefs.getStringSet("favoriteJobs", new HashSet<>());
        favoriteJobs.clear();
        for (String json:jsons) {
            favoriteJobs.add(gson.fromJson(json, Job.class));
        }
    }

    public void setJobsToPrefs(SharedPreferences prefs){
        SharedPreferences.Editor prefsEditor = prefs.edit();
        Gson gson = new Gson();
        Set<String> jsons = new HashSet<>();
        for (Job job:favoriteJobs) {
            jsons.add(gson.toJson(job));
        }
        prefsEditor.putStringSet("favoriteJobs", jsons);
        prefsEditor.apply();
    }

    private void addJobToLayout(Job j){
//            generate FrameLayout
        FrameLayout newFrame = new FrameLayout(this.getContext());
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.WRAP_CONTENT);
        newFrame.setLayoutParams(params);
        newFrame.setId(View.generateViewId());
        frames.add(newFrame);

//            add FrameLayout to LayoutFavoriteJobs
        binding.layoutFavoritesJobs.addView(newFrame, 0);

//            generate FavoriteJobFragment
        FavoriteJobFragment newJobFragment = new FavoriteJobFragment(j);
        fragments.add(newJobFragment);

//            Place FavoriteJobFragment in the new frame
        getActivity().getSupportFragmentManager().beginTransaction().replace(newFrame.getId(),newJobFragment).commitAllowingStateLoss();
    }

}