package com.example.covid_jobber.classes;

import com.example.covid_jobber.Enums.ContractTime;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class Applicant {
    String name;
    double expSalary;
    ContractTime expContractTime;
    Address location;

    List<String> qualifications;
    List<String> skills;


    public Applicant() {
        this("Peter" + new Random().nextInt(), 1337.69, ContractTime.UNKNOWN, new Address());
    }

    public Applicant(String name, double expSalary, ContractTime expContractTime, Address location) {
        this(name,expSalary,expContractTime,location,new ArrayList<>(), new ArrayList<>());
    }

    public Applicant(String name, double expSalary, ContractTime expContractTime, Address location, List<String> qualifications, List<String> skills) {
        this.name = name;
        this.expSalary = expSalary;
        this.expContractTime = expContractTime;
        this.location = location;
        this.qualifications = qualifications;
        this.skills = skills;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getExpSalary() {
        return expSalary;
    }

    public void setExpSalary(double expSalary) {
        this.expSalary = expSalary;
    }

    public ContractTime getExpContractTime() {
        return expContractTime;
    }

    public void setExpContractTime(ContractTime expContractTime) {
        this.expContractTime = expContractTime;
    }

    public Address getLocation() {
        return location;
    }

    public void setLocation(Address location) {
        this.location = location;
    }

    public List<String> getQualifications() {
        return qualifications;
    }

    public void setQualifications(List<String> qualifications) {
        this.qualifications = qualifications;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }
}
