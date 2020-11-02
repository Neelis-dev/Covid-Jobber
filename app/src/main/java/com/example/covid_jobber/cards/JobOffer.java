package com.example.covid_jobber.cards;

import java.util.UUID;

public class JobOffer {
    String id;
    String companyName;
    String jobName;
    String requiredKnowledge;
    String description;
    String address;
    //always in euro
    double wage;

    public JobOffer(){
        this.id = UUID.randomUUID().toString();
        companyName = "Stadt Mannheim";
        jobName = "Straßenplaner";
        wage = 10000.0;
        requiredKnowledge = "Nichts";
        description = "Wir suchen inkompetente Straßenplaner für unsere Stadt";
        address = "E 5, 68159 Mannheim";
    }

    public JobOffer(String companyName, String jobName, String requiredKnowledge, String description, String address,double wage){
        this.id = UUID.randomUUID().toString();
        this.companyName = companyName;
        this.jobName = jobName;
        this.requiredKnowledge = requiredKnowledge;
        this.description = description;
        this.address = address;
        this.wage = wage;
    }

}
