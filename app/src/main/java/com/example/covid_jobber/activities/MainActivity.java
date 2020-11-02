package com.example.covid_jobber.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.covid_jobber.R;
import com.example.covid_jobber.fragments.CardDisplayFragment;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.card_display_fragment) != null){
            getSupportFragmentManager().beginTransaction().
                    replace(R.id.card_display_fragment,new CardDisplayFragment()).commitAllowingStateLoss();
        }
    }


}