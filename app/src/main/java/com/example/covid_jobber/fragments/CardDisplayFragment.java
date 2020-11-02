package com.example.covid_jobber.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.covid_jobber.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CardDisplayFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CardDisplayFragment extends Fragment {


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
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_card_display, container, false);
    }
}