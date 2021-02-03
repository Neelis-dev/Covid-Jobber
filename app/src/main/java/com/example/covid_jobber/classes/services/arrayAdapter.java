package com.example.covid_jobber.classes.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.covid_jobber.R;
import com.example.covid_jobber.activities.MainActivity;
import com.example.covid_jobber.classes.Job;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<Job> {

    Context context;
    MainActivity mainActivity;

    public arrayAdapter(Context context, int resourceId, List<Job> items, MainActivity mainActivity) {
        super(context, resourceId, items);
        this.mainActivity = mainActivity;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Job job_item  = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.txt_item_title);
        TextView location = convertView.findViewById(R.id.txt_item_location);
        TextView employer = convertView.findViewById(R.id.txt_item_company);
        TextView workingperiod = convertView.findViewById(R.id.txt_item_contracttime);
        TextView salary = convertView.findViewById(R.id.txt_item_salary);

        boolean locationActive = mainActivity.getPrefs().getBoolean("locationActive", false);
        double latlocation = mainActivity.getPrefs().getFloat("latitude", 0);
        double lonlocation = mainActivity.getPrefs().getFloat("longitude",0 );

        double latitude = job_item.getLatitude();
        double longitude = job_item.getLongitude();

        name.setText(job_item.getTitle());

        if (locationActive) {
            //Berechnung des Abstandes vom persönlichen Standort zur Arbeitsstätte (Luftlinie)
            double dx, dy, lat, distance;
            lat = (latitude + latlocation) / (2 * 0.01745);
            dx = 111.3 * Math.cos(lat) * (longitude - lonlocation);
            dy = 111.3 * (latitude - latlocation);
            distance = Math.sqrt(dx * dx + dy * dy);
            distance = Math.round(distance *100.0) /100.0;
            String dist = " (" + distance + " Km)";
            String locationString = job_item.getcity() + dist;
            location.setText(locationString);
        } else {
            location.setText(job_item.getcity());
        }

        employer.setText(job_item.getCompany());

        workingperiod.setText(job_item.getContractTime().getTranslation(mainActivity.language));

        if(job_item.getSalary() < 0){
            String text = "";
            switch (mainActivity.language){
                case GERMAN:
                    text = "Unbekannt"; break;
                case ENGLISH:
                    text = "Unknown"; break;
            }
            salary.setText(text);
        }else {
            String salaryString = job_item.getSalary() + " €";
            salary.setText(salaryString);
        }

        return convertView;
    }

}