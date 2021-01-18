package com.example.covid_jobber.classes;

public class Address {
    String displayName;
    String country;
    String federalState;
    String city;

    double latitude;
    double longitude;

    public Address(){
        //TODO
    }

    public Address(String displayName, String country, String federalState, String city, double latitude, double longitude) {
        this.displayName = displayName;
        this.country = country;
        this.federalState = federalState;
        this.city = city;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public boolean checkLocation(String location){

        // TODO: talk about this; just here because of class diagram

        return location.equals(displayName);
    }

    public String getGeolocation(){

        // TODO: talk about this; just here because of class diagram


        return displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getFederalState() {
        return federalState;
    }

    public void setFederalState(String federalState) {
        this.federalState = federalState;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
