package com.example.covid_jobber.classes.services;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.covid_jobber.R;
import com.example.covid_jobber.classes.Job;

import java.util.List;

public class arrayAdapter extends ArrayAdapter<Job> {

    Context context;

    public arrayAdapter(Context context, int resourceId, List<Job> items) {
        super(context, resourceId, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Job job_item  = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item, parent, false);
        }

        TextView name = (TextView) convertView.findViewById(R.id.txt_item_title);
        TextView location = (TextView) convertView.findViewById(R.id.location);
        TextView employer = (TextView) convertView.findViewById(R.id.employer);
        TextView workingperiod = (TextView) convertView.findViewById(R.id.workingPeriod);
        TextView salary = (TextView) convertView.findViewById(R.id.salary);
        ImageView image = (ImageView) convertView.findViewById(R.id.imageView);
        image.setImageResource(R.drawable.ic_launcher_background);

        //TODO: Koordinaten aus FilterFragment holen

        double latlocation= 49.53;
        double lonlocation= 8.16;

        double latitude = job_item.getLatitude();
        double longitude = job_item.getLongitude();

        //Berechnung Abstand des Jobs vom Heimatstandort
        double dx, dy, lat, distance;
        lat = (latitude + latlocation) / 2 * 0.01745;
        dx = 111.3 * Math.cos(lat) * (longitude - lonlocation);
        dy = 111.3 * (latitude - latlocation);
        distance = Math.sqrt(dx * dx + dy * dy);
        distance = Math.round(distance *100.0) /100.0;
        String dist = String.valueOf(distance);

        name.setText(job_item.getTitle());
        location.setText(job_item.getcity() + " (" + dist + " Km)");
        employer.setText(job_item.getCompany());
        workingperiod.setText(job_item.getContractTime().toString());
        if(job_item.getSalary() < 0){
            salary.setText("Unbekannt");
        }else {
            salary.setText(job_item.getSalary() + " €");
        }

        return convertView;
    }
}