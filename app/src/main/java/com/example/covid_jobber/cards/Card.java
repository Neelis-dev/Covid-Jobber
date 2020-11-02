package com.example.covid_jobber.cards;

import java.util.UUID;

public class Card {
    String id;
    JobOffer jobOffer;

    public Card(JobOffer wp){
        this.id = UUID.randomUUID().toString();
        this.jobOffer = wp;
    }

}
